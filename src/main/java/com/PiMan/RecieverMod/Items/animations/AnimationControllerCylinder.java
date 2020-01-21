package com.PiMan.RecieverMod.Items.animations;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.guns.Item44Magnum;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.FlashHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AnimationControllerCylinder implements IAnimationController {
	
	private final double friction;
	
	public AnimationControllerCylinder(double friction) {
		this.friction = friction;
	}

	@Override
	public List<ItemPropertyWrapper> getProperties() {
		
		List<ItemPropertyWrapper> list = new ArrayList<>();
		
		list.add(new ItemPropertyWrapper("spin",new IItemPropertyGetter()
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
	        	
	        	NBTTagCompound oldnbt = nbt.getCompoundTag("prev");
	        	
	        	float nextspin = (float) ((Item44Magnum) stack.getItem()).getSpin(nbt);
	        	
	        	float prevspin = (float) ((Item44Magnum) stack.getItem()).getSpin(oldnbt);

	        	float partialTicks = RenderPartialTickHandler.renderPartialTick;
	        	
	        	float spin = prevspin * (1 - partialTicks) + nextspin * partialTicks;
	        	        	
	        	return spin;
	        }
	    }));
	    
	    list.add(new ItemPropertyWrapper("open", new IItemPropertyGetter() {
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
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
				
	            float j = (oldnbt.getBoolean("open") ? 1.0F : 0.0F) * (1 - pt) + (nbt.getBoolean("open") ? 1.0F : 0.0F) * pt;
	                        
	            return j;
			}
		}));
	    
	    list.add(new ItemPropertyWrapper("eject", new IItemPropertyGetter() {
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
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
				
	            float j = (oldnbt.getBoolean("eject") ? 1.0F : 0.0F) * (1 - pt) + (nbt.getBoolean("eject") ? 1.0F : 0.0F) * pt;
	                        
	            return j;
			}
		}));
	    
		list.add(new ItemPropertyWrapper("bullet1", new IItemPropertyGetter() {
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
	        	
	        	return nbt.getInteger("bullet1");
	        }
	    }));
	    
	    list.add(new ItemPropertyWrapper("bullet2", new IItemPropertyGetter() {
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
	        	
	        	return nbt.getInteger("bullet2");
	        }
	    }));
	    
	    list.add(new ItemPropertyWrapper("bullet3", new IItemPropertyGetter()
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
	        	
	        	return nbt.getInteger("bullet3");
	        }
	    }));
	    
	    list.add(new ItemPropertyWrapper("bullet4", new IItemPropertyGetter()
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
	        	
	        	return nbt.getInteger("bullet4");
	        }
	    }));
	    
	    list.add(new ItemPropertyWrapper("bullet5", new IItemPropertyGetter()
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
	        	
	        	return nbt.getInteger("bullet5");
	        }
	    }));
	    
	    list.add(new ItemPropertyWrapper("bullet6", new IItemPropertyGetter()
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
	        	
	        	return nbt.getInteger("bullet6");
	        }
	    }));
		
		return list;
	}

	@Override
	public void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, NBTTagCompound nbt, ItemGun gun) {
		double theta = nbt.getDouble("theta");
		double dtheta = nbt.getDouble("dtheta");
		double prevtheta = nbt.getCompoundTag("prev").getDouble("theta");
		double prevdtheta = nbt.getCompoundTag("prev").getDouble("dtheta");
					
		if (entityIn instanceof EntityPlayer) {
		
			EntityPlayer player = (EntityPlayer) entityIn;
			
			if (stack.equals(player.getHeldItemMainhand())) {
												
				if (KeyInputHandler.isKeyDown(KeyPresses.Shift)) {
					dtheta += KeyInputHandler.getScroll();
				}
				
				KeyInputHandler.cancleScroll(KeyInputHandler.isKeyDown(KeyPresses.Shift));
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveClip)) {
					nbt.setBoolean("open", !nbt.getBoolean("open"));
				}

				{
					int n = (int) -Math.round(theta) + 2;
					
					while (n < 1) {
						n += 6;
					}
					
					while (n > 6) {
						n -= 6;
					}
					
					if (nbt.getString("BulletChambered").equals(gun.casing.getRegistryName().toString())) {
						setBullet(n, 2, nbt);
					}
					
					if (getBullet(n, nbt) == 1) {
						nbt.setString("BulletChambered", gun.ammo.getRegistryName().toString());
					}
					else {
						nbt.setString("BulletChambered", "");
					}
				}

				if (nbt.getBoolean("hammer") && KeyInputHandler.isKeyDown(KeyPresses.LeftClick)) {
					theta += 1;
				}
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.AddBullet) && nbt.getBoolean("open")) {
					
					int k = gun.findAmmo(player);
					
					if (k != -1) {
					
						int n = (int) -Math.round(theta) + 2;
						
						while (n < 1) {
							n += 6;
						}
						
						while (n > 6) {
							n -= 6;
						}
						
						int i;
						
						for (i = 0; getBullet(n--, nbt) != 0 && i < 6; i++) {
							if (n < 1) {
								n += 6;
							}
						};
						if (i < 6) {
							setBullet(n + 1, 1, nbt);
							ItemStack bullet = player.inventory.getStackInSlot(k);
							NetworkHandler.sendToServer(new MessageAddToInventory(bullet, -1, k));
						}
					}
				}
				
				if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveBullet) && nbt.getBoolean("open")) {
					int n = (int) -Math.round(theta) + 2;
					
					while (n < 1) {
						n += 6;
					}
					
					while (n > 6) {
						n -= 6;
					}
					
					int i;
					
					for (i = 0; getBullet(n--, nbt) == 0 && i < 6; i++) {
						if (n < 1) {
							n += 6;
						}
					};
					
					if (i < 6) {
						if (getBullet(n + 1, nbt) == 1) {
							NetworkHandler.sendToServer(new MessageAddToInventory(gun.ammo,  1));
						}
						else {
							NetworkHandler.sendToServer(new MessageAddToInventory(gun.casing,  1));
						}
						setBullet(n + 1, 0, nbt);
					}
				}
			}
		}
		
		double b = Math.sqrt(Math.abs(2*friction*dtheta));
		
		double velocity = b - friction/2;
		
		if (b/friction < 1) {
			dtheta = 0;
			theta -= prevdtheta;
			theta = Math.round(theta);
			
			if (Math.abs(theta) >=6) {
				prevtheta -= 6*Main.sign(theta);
				theta -= 6*Main.sign(theta);
			}
			
		}
		else {
			dtheta -= velocity * Main.sign(dtheta);
			theta -= velocity * Main.sign(dtheta);
		}
					
		nbt.setDouble("theta", theta);
		nbt.setDouble("dtheta", dtheta);
		nbt.getCompoundTag("prev").setDouble("theta", prevtheta);
		nbt.getCompoundTag("prev").setDouble("dtheta", prevdtheta);
	}
	
	public int getBullet(int n, NBTTagCompound nbt) {
		
		switch(n) {
			case 1:
				return nbt.getInteger("bullet1");
			case 2:
				return nbt.getInteger("bullet2");
			case 3:
				return nbt.getInteger("bullet3");
			case 4:
				return nbt.getInteger("bullet4");
			case 5:
				return nbt.getInteger("bullet5");
			case 6:
				return nbt.getInteger("bullet6");
			default:
				return 0;
		}
			
	}
	
	public void setBullet(int n, int flag, NBTTagCompound nbt) {
		
		switch(n) {
			case 1:
				nbt.setInteger("bullet1", flag);
				break;
			case 2:
				nbt.setInteger("bullet2", flag);
				break;
			case 3:
				nbt.setInteger("bullet3", flag);
				break;
			case 4:
				nbt.setInteger("bullet4", flag);
				break;
			case 5:
				nbt.setInteger("bullet5", flag);
				break;
			case 6:
				nbt.setInteger("bullet6", flag);
				break;
		}
	}

}
