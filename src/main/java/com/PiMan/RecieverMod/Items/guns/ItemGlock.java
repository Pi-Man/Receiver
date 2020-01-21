package com.PiMan.RecieverMod.Items.guns;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.Items.mags.ItemClipGlock;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageEject;
import com.PiMan.RecieverMod.Packets.MessageFlashServer;
import com.PiMan.RecieverMod.Packets.MessagePlaySound;
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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGlock extends ItemGun {
	
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
	
	public ItemGlock(String name) {
		super(name);
		
		this.drift = 10;
		this.spreadX = 0.5;
		this.spreadY = 0.5;
		
        this.addPropertyOverride(new ResourceLocation("slide"), SLIDE_FRAME_GETTER);
        this.addPropertyOverride(new ResourceLocation("check"), CHECK_CHAMBER_GETTER);
        
    }
    
    @Override
	public SoundEvent getShootSound() {
		return SoundsHandler.getSoundEvent(Sounds.GLOCK_SHOT);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		boolean flag = false;

		if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).getHeldItemMainhand().equals(stack)) {
			
			EntityPlayer player = (EntityPlayer)entityIn;
						
			NBTTagCompound tag = this.getNBTTag(stack);
			
			NBTTagCompound baseTag = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
			
			NBTTagCompound nbt = baseTag.getCompoundTag(tag.getString("UUID"));
			
			NBTTagCompound oldnbt = nbt.copy();
			oldnbt.removeTag("prev");
			nbt.setTag("prev", oldnbt);
			
			if (worldIn.isRemote) {
																
				if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveBullet)) {
					NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_SLIDEBACK));
				}
				
				if (KeyInputHandler.isKeyUnpressed(KeyPresses.RemoveBullet)) {
					NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_SLIDEFORWARD));
				}
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.SlideLock) && nbt.getBoolean("AutoSlideLock")) {
					NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_SLIDEFORWARD));
				}
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.Safety)) {
					nbt.setBoolean("Auto", !nbt.getBoolean("Auto"));
				}
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.LeftClick) || (KeyInputHandler.isKeyDown(KeyPresses.LeftClick) && nbt.getBoolean("Auto"))) {
					flag = Shoot(nbt, (EntityLivingBase) entityIn, ModConfig.glockdamage, nbt.getBoolean("ADS") ? 0 : 10, 0, 1, nbt.getInteger("SlideFrame") == 0);
					if (flag) {
						if (!player.isCreative()) {
							NetworkHandler.sendToServer(new MessageEject(new ItemStack(ModItems.BULLET9MMCASING)));
						}
						if (nbt.getBoolean("Auto") && !KeyInputHandler.isKeyPressed(KeyPresses.LeftClick)) {
							FlashHandler.CreateFlash(new BlockPos(player.posX, player.posY + 1, player.posZ), player.dimension, 1);
						}
						else {
							FlashHandler.CreateFlash(new BlockPos(player.posX, player.posY + 1, player.posZ), player.dimension, 2);
						}
					}
					if (KeyInputHandler.isKeyPressed(KeyPresses.LeftClick) && !flag && nbt.getInteger("SlideFrame") == 0) {
						NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_DRY));
					}
				}
				
				nbt.setBoolean("fired", flag);
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.AddBullet) && nbt.getString("Clip ID").isEmpty()) {
					
					System.out.println("Add Clip Pressed");
					
					int clipslot = findClip(player);
										
					if (clipslot != -1) {
						ItemStack clip = player.inventory.getStackInSlot(clipslot);
						//System.out.println("Clip Found: " + clip + clip.getTagCompound());
						NetworkHandler.sendToServer(new MessageAddToInventory(clip, -1, clipslot));
						NBTTagCompound clipTag = baseTag.getCompoundTag(clip.getTagCompound().getString("UUID"));
						nbt.setInteger("Bullets", clipTag.getInteger("Bullets"));
						nbt.setString("Clip ID", clipTag.getString("UUID"));
						NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_MAG_IN));
					}
					
				}
				
				nbt.setBoolean("ADS", KeyInputHandler.isKeyDown(KeyPresses.RightClick));
				
				nbt.setBoolean("SlideLock", KeyInputHandler.isKeyDown(KeyPresses.SlideLock));
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveClip)) {
					
					if (!nbt.getString("Clip ID").isEmpty()) {
						NBTTagCompound clipNBT = new NBTTagCompound();
						
						clipNBT.setInteger("Bullets", nbt.getInteger("Bullets"));
						clipNBT.setString("UUID", nbt.getString("Clip ID"));
						nbt.setInteger("Bullets", 0);
						nbt.setString("Clip ID", "");
						
						ItemStack clip = new ItemStack(ModItems.GLOCKCLIP);
						
						clip.setTagCompound(new NBTTagCompound());
						clip.getTagCompound().setString("UUID", clipNBT.getString("UUID"));
						baseTag.setTag(clipNBT.getString("UUID"), clipNBT);
						
						NetworkHandler.sendToServer(new MessageAddToInventory(clip, 1, player.inventory.getSizeInventory() - 1));
						
						NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_MAG_OUT));
					}
					else if (this.isClip(player.getHeldItemOffhand())) {
						TreeMap<Integer, Pair<ItemStack, Integer>> mags = new TreeMap<Integer, Pair<ItemStack, Integer>>();
						for (int i = 0; i < player.inventory.getSizeInventory() - 1; i++) {
							ItemStack itemstack = player.inventory.getStackInSlot(i);
							if (this.isClip(itemstack)) {
								mags.put(baseTag.getCompoundTag(itemstack.getTagCompound().getString("UUID")).getInteger("Bullets"), Pair.of(itemstack, i));
							}
						}
						if (!mags.isEmpty()) {
							int slot = mags.lastEntry().getValue().getRight();
							ItemStack oldstack = player.getHeldItemOffhand();
							ItemStack newstack = mags.lastEntry().getValue().getLeft();
							NetworkHandler.sendToServer(new MessageAddToInventory(newstack, -1, slot));
							NetworkHandler.sendToServer(new MessageAddToInventory(oldstack, -1, player.inventory.getSizeInventory() - 1));
							NetworkHandler.sendToServer(new MessageAddToInventory(oldstack, 1, slot));
							NetworkHandler.sendToServer(new MessageAddToInventory(newstack, 1, player.inventory.getSizeInventory() - 1));
						}
					}
				}
				
				if (KeyInputHandler.isKeyDown(KeyPresses.RemoveBullet)) {
					if (nbt.getInteger("SlideFrame") < 2) {
						nbt.setInteger("SlideFrame", nbt.getInteger("SlideFrame") + 1);
					}
					else if ((nbt.getInteger("SlideFrame") == 2 || nbt.getInteger("SlideFrame") == 5) && KeyInputHandler.isKeyDown(KeyPresses.SlideLock)) {
						//System.out.println("Half Lock");
						nbt.setInteger("SlideFrame", 5);
					}
					else if (nbt.getInteger("SlideFrame") == 5) {
						nbt.setInteger("SlideFrame", 2);
					}
					else if (nbt.getInteger("SlideFrame") < 4) {
						nbt.setInteger("SlideFrame", nbt.getInteger("SlideFrame") + 1);
					}
					if (nbt.getInteger("SlideFrame") == 3 && nbt.getBoolean("BulletChambered")) {
						
						ItemStack bullet = new ItemStack(ModItems.BULLET9MM);
						NetworkHandler.sendToServer(new MessageEject(bullet));
						nbt.setBoolean("BulletChambered", false);
					}
				}
				if (nbt.getInteger("SlideFrame") > 2 && nbt.getInteger("SlideFrame") < 5 && nbt.getInteger("Bullets") > 0 && !nbt.getBoolean("BulletChambered")) {
					nbt.setInteger("Bullets", nbt.getInteger("Bullets") - 1);
					nbt.setBoolean("BulletChambered", true);
				}
				if (!KeyInputHandler.isKeyDown(KeyPresses.RemoveBullet)) {
					if (nbt.getInteger("SlideFrame") == 5) {
						nbt.setInteger("SlideFrame", 2);
					}
					if (nbt.getInteger("SlideFrame") == 4) {
						if (!KeyInputHandler.isKeyDown(KeyPresses.SlideLock) && !nbt.getBoolean("BulletChambered") && !nbt.getString("Clip ID").isEmpty() && nbt.getInteger("Bullets") == 0) {
							nbt.setBoolean("AutoSlideLock", true);
						}
						else {
							nbt.setBoolean("AutoSlideLock", false);
						}
						nbt.setInteger("SlideFrame", 3);
					}
					if (nbt.getInteger("SlideFrame") == 3 && (KeyInputHandler.isKeyDown(KeyPresses.SlideLock) || nbt.getBoolean("AutoSlideLock")) && !KeyInputHandler.isKeyPressed(KeyPresses.SlideLock)) {
						//System.out.println("Full Lock");
						nbt.setBoolean("AutoSlideLock", true);
					}
					else {
						nbt.setInteger("SlideFrame", 0);
					}
				}
				if (nbt.getInteger("SlideFrame") == 0) {
					nbt.setBoolean("AutoSlideLock", false);
				}

				//System.out.println(nbt);
				
				if (flag) {
					nbt.setInteger("SlideFrame", 4);
				}
						
				NetworkHandler.sendToServer(new MessageUpdateNBT(stack, itemSlot, nbt));
			}
	    }
	}

	@Override
	public float getDefaultZoomFactor(ItemStack stack) {
		return 0.9F;
	}
}
