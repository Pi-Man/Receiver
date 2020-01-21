package com.PiMan.RecieverMod.Items.guns;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.Items.accesories.ItemAccessories;
import com.PiMan.RecieverMod.Items.animations.IAnimationController;
import com.PiMan.RecieverMod.Items.bullets.ItemBullet;
import com.PiMan.RecieverMod.Packets.MessagePlaySound;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.MiscEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemGun extends ItemBase {
	
    private static final IItemPropertyGetter ADS_GETTER = new BooleanPropertyGetter("ADS");
    
    private static final IItemPropertyGetter BULLET_CHAMBERED_GETTER = new IItemPropertyGetter()
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
        	float j = !nbt.getString("BulletChambered").isEmpty() ? 1.0F : 0.0F;
            return j;
        }
    };
    
    private static final IItemPropertyGetter FIRED_GETTER = new BooleanPropertyGetter("fired");
		
	protected double spreadY;
	protected double spreadX;
	protected double drift;
	protected List<IAnimationController> animationControllers = new ArrayList<>();
	public Item ammo;
	public Item casing;
	public Item mag;

	public ItemGun(String name) {
		super(name);
	
		maxStackSize = 1;
        this.addPropertyOverride(new ResourceLocation("ads"), ADS_GETTER);
        this.addPropertyOverride(new ResourceLocation("chambered"), BULLET_CHAMBERED_GETTER);
        this.addPropertyOverride(new ResourceLocation("fired"), FIRED_GETTER);
		
	}
	
	public boolean Shoot(NBTTagCompound nbt, EntityLivingBase entityLiving, double damage, float entityAccuracy, float gunAccuracy, int bullets, boolean flag2) {
		return Shoot(nbt, entityLiving, damage, entityAccuracy, gunAccuracy, bullets, 1200, flag2);
	}
	
	public boolean Shoot(NBTTagCompound nbt, EntityLivingBase entityLiving, double damage, float entityAccuracy, float gunAccuracy, int bullets, int life, boolean flag2) {
				
		if (entityLiving instanceof EntityPlayer) {
			
			EntityPlayer player = ((EntityPlayer)entityLiving);
			World world = player.world;
			boolean flag1 = player.capabilities.isCreativeMode;
			
			if (world.isRemote) {
				NetworkHandler.sendToServer(new MessageShoot(nbt, damage, entityAccuracy, gunAccuracy, life, bullets, flag2));
			}
						
            int i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(player.getHeldItemMainhand(), world, player, 20, (flag1 || !nbt.getString("BulletChambered").isEmpty()) && flag2);
            
            if (i < 0) {
            	return false;
            }
            
            if ((flag1 || !nbt.getString("BulletChambered").isEmpty()) && flag2) {
            	
            	ItemBullet item = (ItemBullet) Item.getByNameOrId(nbt.getString("BulletChambered"));
            	
            	if (item != null) {
            		if (!world.isRemote) {
            			item.fire(world, player, entityAccuracy, gunAccuracy, life);
            		}
            		nbt.setString("BulletChambered", item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath() + "casing");
            	}
            	else if (!world.isRemote) {
                	
            		EntityBullet bulletdummy = new EntityBullet(world, player);
                    bulletdummy.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 23F, entityAccuracy);

                    for (int j = 0; j < bullets; j++) {
                    	
                    	EntityBullet bullet = new EntityBullet(world, player, life);
                    	double x = bulletdummy.motionX;
                    	double y = bulletdummy.motionY;
                    	double z = bulletdummy.motionZ;
                    	bullet.shoot(x, y, z, MathHelper.sqrt(x * x + y * y + z * z), gunAccuracy);
                    	
                    	bullet.posY += 0.1D;
                    	bullet.setIsCritical(false);
                    	bullet.setDamage(damage);
                    	                        
                    	player.world.spawnEntity(bullet);
                    	                    	                    	
                    }
                    
                    //System.out.println("Spawning Bullet");
                    
                    world.playSound(null, player.posX, player.posY, player.posZ, getShootSound(), SoundCategory.PLAYERS, 1, 1);

            	}
            	
            	player.rotationPitch += world.rand.nextGaussian() * this.spreadY - this.drift;
            	player.rotationYaw += world.rand.nextGaussian() * this.spreadX;

            	//nbt.setBoolean("BulletChambered", false);
            	
            	                                
                return true;
            }
		}
		return false;
    }

	public abstract SoundEvent getShootSound();
	
	public float getZoomFactor(ItemStack stack) {
		if (this.getNBTTag(stack).hasKey("Accessories", 10)) {
			ItemAccessories item = (ItemAccessories) Item.getByNameOrId(this.getNBTTag(stack).getCompoundTag("Accessories").getString("1"));
			if (item != null) {
				return item.getZoomFactor();
			}
		}
		return this.getDefaultZoomFactor(stack);
	}
	
	public abstract float getDefaultZoomFactor(ItemStack stack);
	
	public boolean hasAccessories(ItemStack stack) {
		return false;
	}
	
	public boolean hasAccessory(ItemStack stack, int type) {
		return stack.getOrCreateSubCompound("Accessories").hasKey(Integer.valueOf(type).toString(), 8) && !stack.getOrCreateSubCompound("Accessories").getString(Integer.valueOf(type).toString()).isEmpty();
	}
	
	public List<ItemStack> getAccesories(ItemStack stack) {
		
		if (this.getNBTTag(stack).hasKey("Accessories", 10)) {
			NBTTagCompound nbt = stack.getOrCreateSubCompound("Accessories");
			
			List<ItemStack> list = new ArrayList<ItemStack>();
			
			for (Integer i = 0; i < 9; i++) {
				ItemStack accessory = new ItemStack(Item.getByNameOrId(nbt.getString(i.toString())));
				if (accessory != null && !accessory.isEmpty()) {
					this.getNBTTag(accessory).setBoolean("model", true);
					this.getNBTTag(accessory).setString("UUID", this.getNBTTag(stack).getString("UUID"));
					this.getNBTTag(accessory).setIntArray("transform", this.getAccessoryTransformInts(((ItemAccessories)accessory.getItem()).getType()));
					list.add(accessory);
				}
			}
			
			return list;
		}
		else {
			return null;
		}
	}
	
	private int [] getAccessoryTransformInts(int type) {
		
		int [] ints = new int[16];
		
		Matrix4f m = this.getAccessoryTransform(type);
		
		for (int i = 0; i < 16; i++) {
			ints[i] = Float.floatToIntBits(m.getElement(i / 4, i % 4));
		}
		
		return ints;
	}
	
	public Matrix4f getAccessoryTransform(int type) {
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		return m;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (worldIn.isRemote) {
			
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityIn;

				if (player.getHeldItemMainhand().equals(stack)) {
					if (KeyInputHandler.isKeyDown(KeyPresses.Shift)) {
						MiscEventHandler.cancleBob();
					}
				}
			}
		}
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        
		boolean flag = false;
		
		if (oldStack.getItem() instanceof ItemGun && newStack.getItem() instanceof ItemGun) {
			ItemGun oldItem = (ItemGun) oldStack.getItem();
			ItemGun newItem = (ItemGun) newStack.getItem();
			
			flag = oldItem.checkNBTTags(oldStack).getString("UUID").equals(newItem.checkNBTTags(newStack).getString("UUID"));
		}
		
		return slotChanged || !flag;
    }
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("ID: " + ((ItemBase)stack.getItem()).getNBTTag(stack).getString("UUID"));
	}
	
	public int findAmmo(EntityPlayer player) {
		for (int i = -1; i < player.inventory.getSizeInventory() - 1; ++i)
        {
			int k = i < 0 ? player.inventory.getSizeInventory() - 1 : i;
						
            ItemStack itemstack = player.inventory.getStackInSlot(k);
            
            if (this.isAmmo(itemstack))
            {
                return k;
            }
        }

        return -1;
	}
	
	public int findClip(EntityPlayer player) {
		for (int i = -1; i < player.inventory.getSizeInventory() - 1; ++i) {
			int k = i < 0 ? player.inventory.getSizeInventory() - 1 : i;
			
            ItemStack itemstack = player.inventory.getStackInSlot(k);
            
            if (this.isClip(itemstack)) {
                return k;
            }
        }

        return -1;
	}
	
	public boolean isClip(ItemStack stack) {
        return stack.getItem() == mag;
    }
	
	private boolean isAmmo(ItemStack stack) {
		return stack.getItem() == ammo;
	}
	
	protected static class BooleanPropertyGetter implements IItemPropertyGetter {
		
		private final String name;
		
		public BooleanPropertyGetter(String name) {
			this.name = name;
		}
		
		@Override
		public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
			if (worldIn == null) {
				worldIn = Minecraft.getMinecraft().world;
			}
			
			if (worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
				return 0.0F;
			}
			
			NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
			NBTTagCompound oldnbt = nbt.getCompoundTag("prev");
			
			float pt = RenderPartialTickHandler.renderPartialTick;
			
            float j = (oldnbt.getBoolean(name) ? 1.0F : 0.0F) * (1 - pt) + (nbt.getBoolean(name) ? 1.0F : 0.0F) * pt;
			
			return j;
		}
		
	}
}
