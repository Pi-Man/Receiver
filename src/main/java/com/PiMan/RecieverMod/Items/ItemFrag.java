package com.PiMan.RecieverMod.Items;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.Entity.EntityGrenade;
import com.PiMan.RecieverMod.Packets.MessageSpawnEntity;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFrag extends ItemThrowable {

	public ItemFrag(String name) {
		super(name, type.LETHAL);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
				
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		NBTTagCompound nbt = getNBTTag(stack);
		
		if (worldIn.isRemote) {
		
			if (!nbt.getBoolean("Active") && KeyInputHandler.isKeyDown(KeyPresses.RightClick)) {
				
				int slot = playerIn.inventory.getSlotFor(stack);
			
				System.out.println("right click");
				
				nbt.setBoolean("Active", true);
		
				if (!nbt.getBoolean("exploded")) {
					
					nbt.setInteger("Time", 100);
											        
					NetworkHandler.sendToServer(new MessageUpdateNBT(stack, slot, nbt));
	
			        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			        
				}
				else {
					
					System.out.println("Stack Just Exploded");
					
					NetworkHandler.sendToServer(new MessageUpdateNBT(stack, slot, nbt));
					
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
					
				}
			}
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			
	        NBTTagCompound nbt = getNBTTag(stack);
	        
			if (!nbt.getBoolean("exploded")) {
							
		        if (!player.capabilities.isCreativeMode)
		        {
		        	stack.shrink(1);
		        }
		        		        
		        worldIn.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
				        
	            EntityGrenade entitygrenade = new EntityGrenade(worldIn, player);
	            entitygrenade.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
	            NBTTagCompound entityNBT = entitygrenade.getEntityData();
	            entityNBT.setInteger("Time", nbt.getInteger("Time"));
	            
	            NetworkHandler.sendToServer(new MessageSpawnEntity(entitygrenade));
	            
			}
			else {
				nbt.setBoolean("exploded", false);
			}
	        
	        nbt.setBoolean("Active", false);
	        	
	        player.addStat(StatList.getObjectUseStats(this));
		}
    }
	
	@Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        return stack;
    }

	@Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		if (entityIn instanceof EntityPlayer) {
						
			EntityPlayer player = (EntityPlayer) entityIn;
			
			NBTTagCompound nbt = getNBTTag(stack);
			
			if (worldIn.isRemote) {
				//System.out.println("Active: " + nbt.getBoolean("Active") + ", Exploded: " + nbt.getBoolean("exploded") + ", Right Click: " + KeyInputHandler.isKeyDown(KeyPresses.RightClick));
				
				if (nbt.getBoolean("Active")) {
					
					if (!isSelected || KeyInputHandler.isKeyUnpressed(KeyPresses.RightClick)) {
						onPlayerStoppedUsing(stack, worldIn, (EntityLivingBase) entityIn, 0);
					}
					
					if (!nbt.getBoolean("exploded")) {
		
						nbt.setInteger("Time", nbt.getInteger("Time") - 1);
						
						if (nbt.getInteger("Time") == 0) {
							if (!player.capabilities.isCreativeMode) {
								stack.shrink(1);
							}
							//System.out.println("BOOM");
							
							nbt.setBoolean("exploded", true);
							
				            EntityGrenade entitygrenade = new EntityGrenade(worldIn, player);
				            entitygrenade.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
				            NBTTagCompound entityNBT = entitygrenade.getEntityData();
				            entityNBT.setInteger("Time", 1);
				            
				            NetworkHandler.sendToServer(new MessageSpawnEntity(entitygrenade));
						}
					}
				}
			}
		}
    }
}
