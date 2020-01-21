package com.PiMan.RecieverMod.Items.guns;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.FlashHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemModel10 extends ItemGun{
	
	private static final IItemPropertyGetter SPIN_GETTER = new IItemPropertyGetter()
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
        	
        	if (oldnbt == null) {
        		return 0.0F;
        	}
        	
        	float nextspin = (float) ((ItemModel10) stack.getItem()).getSpin(nbt);
        	
        	float prevspin = (float) ((ItemModel10) stack.getItem()).getSpin(oldnbt);

        	float partialTicks = RenderPartialTickHandler.renderPartialTick;
        	
        	float spin = prevspin * (1 - partialTicks) + nextspin * partialTicks;
        	        	
        	return spin;
        }
    };
    
	private static final IItemPropertyGetter OPEN_GETTER = new IItemPropertyGetter()
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
			
            float j = (oldnbt.getBoolean("open") ? 1.0F : 0.0F) * (1 - pt) + (nbt.getBoolean("open") ? 1.0F : 0.0F) * pt;
                        
            return j;
        }
    };
    
    private static final IItemPropertyGetter HAMMER_GETTER = new IItemPropertyGetter()
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
			
            float j = (oldnbt.getBoolean("hammer") ? 1.0F : 0.0F) * (1 - pt) + (nbt.getBoolean("hammer") ? 1.0F : 0.0F) * pt;
                        
            return j;
        }
    };
    
	private static final IItemPropertyGetter BULLET1_GETTER = new IItemPropertyGetter()
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
        	
        	return nbt.getInteger("bullet1");
        }
    };
    
    private static final IItemPropertyGetter BULLET2_GETTER = new IItemPropertyGetter()
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
        	
        	return nbt.getInteger("bullet2");
        }
    };
    
    private static final IItemPropertyGetter BULLET3_GETTER = new IItemPropertyGetter()
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
    };
    
    private static final IItemPropertyGetter BULLET4_GETTER = new IItemPropertyGetter()
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
    };
    
    private static final IItemPropertyGetter BULLET5_GETTER = new IItemPropertyGetter()
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
    };
    
    private static final IItemPropertyGetter BULLET6_GETTER = new IItemPropertyGetter()
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
    };

	public ItemModel10(String name) {
		super(name);
		
		this.drift = 20;
		this.spreadX = 0.5;
		this.spreadY = 0.5;
		this.ammo = ModItems.BULLET38SPECIAL;
		
		addPropertyOverride(new ResourceLocation("spin"), SPIN_GETTER);
		addPropertyOverride(new ResourceLocation("open"), OPEN_GETTER);
		addPropertyOverride(new ResourceLocation("hammer"), HAMMER_GETTER);
		addPropertyOverride(new ResourceLocation("bullet1"), BULLET1_GETTER);
		addPropertyOverride(new ResourceLocation("bullet2"), BULLET2_GETTER);
		addPropertyOverride(new ResourceLocation("bullet3"), BULLET3_GETTER);
		addPropertyOverride(new ResourceLocation("bullet4"), BULLET4_GETTER);
		addPropertyOverride(new ResourceLocation("bullet5"), BULLET5_GETTER);
		addPropertyOverride(new ResourceLocation("bullet6"), BULLET6_GETTER);
		
	}
	
	static final double friction = 0.02;
	
	public double getSpin(NBTTagCompound nbt) {
		return nbt.getDouble("theta") + (nbt.getBoolean("hammer") && !nbt.getBoolean("open") ? 0 : -0.5);
	}
	
	@Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		if (worldIn.isRemote) {
			
			
			NBTTagCompound tag = this.getNBTTag(stack);
			
			NBTTagCompound baseTag = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
			
			NBTTagCompound nbt = baseTag.getCompoundTag(tag.getString("UUID"));
			
			NBTTagCompound oldnbt = nbt.copy();
			oldnbt.removeTag("prev");
			nbt.setTag("prev", oldnbt);
			
			double theta = nbt.getDouble("theta");
			double dtheta = nbt.getDouble("dtheta");
			double prevtheta = nbt.getCompoundTag("prev").getDouble("theta");
			double prevdtheta = nbt.getCompoundTag("prev").getDouble("dtheta");
						
			if (entityIn instanceof EntityPlayer) {
			
				EntityPlayer player = (EntityPlayer) entityIn;
				
				if (stack.equals(player.getHeldItemMainhand())) {
					
					boolean flag = false;
									
					if (KeyInputHandler.isKeyDown(KeyPresses.Shift)) {
						dtheta += KeyInputHandler.getScroll();
					}
					
					KeyInputHandler.cancleScroll(KeyInputHandler.isKeyDown(KeyPresses.Shift));
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveClip)) {
						nbt.setBoolean("open", !nbt.getBoolean("open"));
					}
					
					nbt.setBoolean("ADS", KeyInputHandler.isKeyDown(KeyPresses.RightClick));

					if ((KeyInputHandler.isKeyDown(KeyPresses.LeftClick) || KeyInputHandler.isKeyUnpressed(KeyPresses.LeftClick)) && nbt.getBoolean("hammer") && !KeyInputHandler.isKeyDown(KeyPresses.Safety)) {
																													
						if (!KeyInputHandler.isKeyUnpressed(KeyPresses.Safety)) {
							
							int n = (int) -Math.round(theta) + 2;
							
							while (n < 1) {
								n += 6;
							}
							
							while (n > 6) {
								n -= 6;
							}
							
							if (getBullet(n, nbt) == 1) {
								nbt.setString("BulletChambered", ModItems.BULLET38SPECIAL.getRegistryName().toString());
							}
							
							flag = this.Shoot(nbt, player, ModConfig.model10damage, nbt.getBoolean("ADS") ? 0 : 10, 0, 1, !nbt.getBoolean("open"));
							
							if (flag) {
								FlashHandler.AddFlash(player.getPosition(), player.dimension, 2);
								setBullet(n, 2, nbt);
							}
							flag = true;
							nbt.setBoolean("hammer", false);
							
						}
						if (!nbt.getBoolean("open")) {
							theta += 1;
						}
					}
					
					nbt.setBoolean("hammer", KeyInputHandler.isKeyDown(KeyPresses.Safety) || nbt.getBoolean("hammer") && !KeyInputHandler.isKeyDown(KeyPresses.LeftClick));
					
					if (!flag && KeyInputHandler.isKeyPressed(KeyPresses.LeftClick) && !nbt.getBoolean("hammer")) {
						nbt.setBoolean("hammer", true);
					}
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.AddBullet) && nbt.getBoolean("open")) {
						
						int k = findAmmo(player);
						
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
								NetworkHandler.sendToServer(new MessageAddToInventory(ModItems.BULLET38SPECIAL,  1));
							}
							else {
								NetworkHandler.sendToServer(new MessageAddToInventory(ModItems.BULLET38SPECIALCASING,  1));
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
			
			NetworkHandler.sendToServer(new MessageUpdateNBT(stack, itemSlot, nbt));
	    }
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
	
	@Override
	public SoundEvent getShootSound() {
		return SoundsHandler.getSoundEvent(Sounds.COLT_1911_SHOT);
	}

	@Override
	public float getDefaultZoomFactor(ItemStack stack) {
		return 0.9F;
	}
}
