package com.PiMan.RecieverMod.Items.guns;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.Entity.EntityRPG;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemRPG7 extends ItemGun {
	
	IItemPropertyGetter HAMMER_GETTER = new IItemPropertyGetter() {
		
		@Override
		public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
			if (worldIn == null) {
				worldIn = Minecraft.getMinecraft().world;
			}
			
			if (!worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || worldIn == null || !stack.hasTagCompound()) {
				return 0f;
			}
			
			NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
			NBTTagCompound oldnbt = nbt.getCompoundTag("prev");
			
			float oldVal = oldnbt.getBoolean("hammer") ? 1f : 0f;
			float newVal = nbt.getBoolean("hammer") ? 1f : 0f;
			
			float pt = RenderPartialTickHandler.renderPartialTick;
						
			return (1f - pt) * oldVal + pt * newVal;
			
		}
		
	};

	public ItemRPG7(String name) {
		super(name);
		
		drift = 5;
		spreadX = 1;
		spreadY = 1;
		
		addPropertyOverride(new ResourceLocation("hammer"), HAMMER_GETTER);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).getHeldItemMainhand().equals(stack)) {
			
			EntityPlayer player = (EntityPlayer)entityIn;
			
			NBTTagCompound tag = this.getNBTTag(stack);
			
			NBTTagCompound baseTag = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
			
			NBTTagCompound nbt = baseTag.getCompoundTag(tag.getString("UUID"));
			nbt.setBoolean("admin", tag.getBoolean("admin"));

			NBTTagCompound oldnbt = nbt.copy();
			oldnbt.removeTag("prev");
			nbt.setTag("prev", oldnbt);
			
			if (worldIn.isRemote) {
				
				boolean fired = false;
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.LeftClick) && !KeyInputHandler.isKeyDown(KeyPresses.Safety)) {
					fired = this.Shoot(nbt, player, 100, 0, 0, 1, nbt.getBoolean("hammer"));
					nbt.setBoolean("hammer", false);
				}
				
				if (KeyInputHandler.isKeyUnpressed(KeyPresses.Safety) && KeyInputHandler.isKeyDown(KeyPresses.LeftClick)) {
					nbt.setBoolean("hammer", false);
				}
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.Safety)) {
					nbt.setBoolean("hammer", true);
				}
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.AddBullet) && !nbt.getBoolean("BulletChambered")) {
					int k = this.findAmmo(player);
					if (k != -1) {
						NetworkHandler.sendToServer(new MessageAddToInventory(player.inventory.getStackInSlot(k), -1, k));
						nbt.setBoolean("BulletChambered", true);
					}
				}
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveBullet) && nbt.getBoolean("BulletChambered")) {
					NetworkHandler.sendToServer(new MessageAddToInventory(ModItems.RPG, 1));
					nbt.setBoolean("BulletChambered", false);
				}
				
				NetworkHandler.sendToServer(new MessageUpdateNBT(stack, itemSlot, nbt));
				
			}
		}
		
	}
	
	@Override
	public boolean Shoot(NBTTagCompound nbt, EntityLivingBase entityLiving, double damage, float entityAccuracy, float gunAccuracy, int bullets, int life, boolean flag2) {
		
		if (entityLiving instanceof EntityPlayer) {
			
			EntityPlayer player = ((EntityPlayer)entityLiving);
			World world = player.world;
			boolean flag1 = player.capabilities.isCreativeMode;
			
			if (world.isRemote) {
				NetworkHandler.sendToServer(new MessageShoot(nbt, damage, entityAccuracy, gunAccuracy, life, bullets, flag2));
			}
						
            int i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(player.getHeldItemMainhand(), world, player, 20, (flag1 || nbt.getBoolean("BulletChambered")) && flag2);
            
            if (i < 0) {
            	System.out.println("ERROR ON EVENT REGISTER"); 
            	return false;
            }
            
            if ((flag1 || nbt.getBoolean("BulletChambered")) && flag2) {
            	
            	//System.out.println("Bullet Found");

            	if (!world.isRemote) {
                	
            		EntityRPG rpg = new EntityRPG(world, player);
                    rpg.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 5F, entityAccuracy);
                    if (nbt.getBoolean("admin")) {
                    	Main.LOGGER.info("Admin RPG");
                    	rpg.getEntityData().setBoolean("admin", true);
                    }
                    
                    world.spawnEntity(rpg);
                    
                    //System.out.println("Spawning Bullet");
                    
                    world.playSound(null, player.posX, player.posY, player.posZ, getShootSound(), SoundCategory.PLAYERS, 1, 1);

            	}
            	
            	player.rotationPitch += world.rand.nextGaussian() * this.spreadY - this.drift;
            	player.rotationYaw += world.rand.nextGaussian() * this.spreadX;

            	nbt.setBoolean("BulletChambered", false);
            	                                
                return true;
            }
		}
		return false;
    }
	
	@Override
	public float getDefaultZoomFactor(ItemStack stack) {
		return 0;
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundEvents.ITEM_FIRECHARGE_USE;
	}

}
