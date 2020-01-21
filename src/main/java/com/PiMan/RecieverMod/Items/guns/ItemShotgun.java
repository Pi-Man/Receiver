package com.PiMan.RecieverMod.Items.guns;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageEject;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.FlashHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShotgun extends ItemGun {
	
	private static final IItemPropertyGetter SLIDE_FRAME_GETTER = new IItemPropertyGetter()
    {
        @Override
		@SideOnly(Side.CLIENT)
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
        {
        	if (worldIn == null) {
        		worldIn = Minecraft.getMinecraft().world;
        	}
        	
        	if (worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
        		return 0.0F;
        	}
        	NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
			NBTTagCompound oldnbt = (NBTTagCompound) nbt.getTag("prev");
			
			if (oldnbt == null) {
				return 0.0F;
			}
			
			float pt = RenderPartialTickHandler.renderPartialTick;
			
			int oldval = oldnbt.getInteger("SlideFrame") == 5 ? 2 : oldnbt.getInteger("SlideFrame");
			
			int newval = nbt.getInteger("SlideFrame") == 5 ? 2 : nbt.getInteger("SlideFrame");
			
            float j = oldval * (1 - pt) + newval * pt;
                        
            return j / 10.0F;
        }
    };	
    
    private static final IItemPropertyGetter CHECK_CHAMBER_GETTER = new IItemPropertyGetter()
    {
        @Override
		@SideOnly(Side.CLIENT)
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
        {
        	if (entityIn == null || worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
        		return 0.0F;
        	}
        	NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
        	NBTTagCompound oldnbt = nbt.getCompoundTag("prev");
        	
        	float oldval = oldnbt.getInteger("SlideFrame") == 5 ? 0.3F : oldnbt.getInteger("SlideFrame") < 3 ? oldnbt.getInteger("SlideFrame") / 10.0F : 0.0F;
        	
        	float newval = nbt.getInteger("SlideFrame") == 5 ? 0.3F : nbt.getInteger("SlideFrame") < 3 ? nbt.getInteger("SlideFrame") / 10.0F : 0.0F;
        	
        	float pt = RenderPartialTickHandler.renderPartialTick;
        	
        	if (!KeyInputHandler.isKeyDown(KeyPresses.SlideLock)) {
        		newval = 0F;
        	}
        	if (!(KeyInputHandler.isKeyDown(KeyPresses.SlideLock) || KeyInputHandler.isKeyUnpressed(KeyPresses.SlideLock) && !KeyInputHandler.isKeyPressed(KeyPresses.SlideLock))) {
        		oldval = 0F;
        	}
            return (1 - pt) * oldval + pt * newval;
        }
    };
    
    private static final IItemPropertyGetter ADD_BULLET_GETTER = new IItemPropertyGetter() {
		
		@Override
		public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {

        	if (entityIn == null || worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
        		return 0.0F;
        	}
        	NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
        	NBTTagCompound oldnbt = nbt.getCompoundTag("prev");
        	
        	float oldval = oldnbt.getBoolean("AddBullets") ? 1.0F : 0.0F;
        	
        	float newval = nbt.getBoolean("AddBullets") ? 1.0F : 0.0F;
        	
        	float pt = RenderPartialTickHandler.renderPartialTick;
        	
            return (1 - pt) * oldval + pt * newval; 
		}
	};
	

	public ItemShotgun(String name) {
		super(name);
		
		this.drift = 30;
		this.spreadX = 0.1;
		this.spreadY = 1;
		
        this.addPropertyOverride(new ResourceLocation("slide"), SLIDE_FRAME_GETTER);
        this.addPropertyOverride(new ResourceLocation("check"), CHECK_CHAMBER_GETTER);
        this.addPropertyOverride(new ResourceLocation("add_bullet"), ADD_BULLET_GETTER);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		if (worldIn.isRemote) {
			
			if (entityIn instanceof EntityPlayer) {
				
				EntityPlayer player = (EntityPlayer)entityIn;
			
				NBTTagCompound tag = this.getNBTTag(stack);
				
				NBTTagCompound baseTag = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
				
				NBTTagCompound nbt = baseTag.getCompoundTag(tag.getString("UUID"));
				
				NBTTagCompound oldnbt = nbt.copy();
				oldnbt.removeTag("prev");
				nbt.setTag("prev", oldnbt);
				if (!nbt.hasKey("Bullets", 9)) {
					nbt.setTag("Bullets", new NBTTagList());
				}
				
				
				if (player.getHeldItemMainhand().equals(stack)) {
					
					boolean flag;
					
					nbt.setBoolean("ADS", KeyInputHandler.isKeyDown(KeyPresses.RightClick));
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.LeftClick)) {						
						flag = Shoot(nbt, player, ModConfig.remingtondamage, nbt.getBoolean("ADS") ? 0 : 10F, 5F, ModConfig.remingtonpelletcount, 0, nbt.getInteger("SlideFrame") == 0 && !nbt.getBoolean("AddBullets"));
						if (flag) {
							FlashHandler.CreateFlash(new BlockPos(player.posX, player.posY + 1, player.posZ), player.dimension, 2);
							nbt.setBoolean("EmptyBulletChambered", true);
						}
					}
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveClip)) {
					
						nbt.setBoolean("AddBullets", !nbt.getBoolean("AddBullets"));
						
					}
					
					if (nbt.getBoolean("AddBullets")) {
						
						if (KeyInputHandler.isKeyPressed(KeyPresses.AddBullet) && nbt.getTagList("Bullets", 8).tagCount() < 6) {
							
							int slot = findAmmo(player);
							
							if (slot != -1) {
								ItemStack ammo = player.inventory.getStackInSlot(slot);
								NetworkHandler.sendToServer(new MessageAddToInventory(ammo, -1));
								nbt.getTagList("Bullets", 8).appendTag(new NBTTagString(ammo.getItem().getRegistryName().toString()));
								
							}
							
						}
						
					}
						
					if (KeyInputHandler.isKeyDown(KeyPresses.RemoveBullet)) {
					
						if (nbt.getInteger("SlideFrame") < 4) {
							
							nbt.setInteger("SlideFrame", nbt.getInteger("SlideFrame") + 1);
							
						} 
						
						if (nbt.getInteger("SlideFrame") == 5) {
							nbt.setInteger("SlideFrame", 2);
						}
						
						if (nbt.getInteger("SlideFrame") == 2 && KeyInputHandler.isKeyDown(KeyPresses.SlideLock)) {
							nbt.setInteger("SlideFrame", 5);
						}
						
						if (nbt.getInteger("SlideFrame") == 3) {
							
							if (!nbt.getString("BulletChambered").isEmpty()) {
																
								NetworkHandler.sendToServer(new MessageEject(new ItemStack(ModItems.BULLETSHOTGUN)));
								nbt.setString("BulletChambered", "");
								
							}
							
							if (nbt.getBoolean("EmptyBulletChambered")) {
								NetworkHandler.sendToServer(new MessageEject(new ItemStack(ModItems.BULLETSHOTGUNCASING)));
								nbt.setBoolean("EmptyBulletChambered", false);
							}
							
						}
						
					}
					else {
						
						if (nbt.getInteger("SlideFrame") == 5) {
							nbt.setInteger("SlideFrame", 2);
						}
						
						if (nbt.getInteger("SlideFrame") < 5 && nbt.getInteger("SlideFrame") > 0) {

							nbt.setInteger("SlideFrame", nbt.getInteger("SlideFrame") - 1);
							
						}
						
					}
					
					if (nbt.getInteger("SlideFrame") == 4) {
						
						if (nbt.getString("BulletChambered").isEmpty() && nbt.getTagList("Bullets", 8).tagCount() > 0) {
							
							nbt.setString("BulletChambered", nbt.getTagList("Bullets", 8).getStringTagAt(nbt.getTagList("Bullets", 8).tagCount() - 1));
							nbt.getTagList("Bullets", 8).removeTag(nbt.getTagList("Bullets", 8).tagCount() - 1);
							
						}
						
					}
					
				}
				
				NetworkHandler.sendToServer(new MessageUpdateNBT(stack, itemSlot, nbt));
				
			}
		}
	}
	
	@Override
	public SoundEvent getShootSound() {
		return SoundsHandler.getSoundEvent(Sounds.RIFLE_SHOOT);
	}


	@Override
	public float getDefaultZoomFactor(ItemStack stack) {
		return 0.9F;
	}
}
