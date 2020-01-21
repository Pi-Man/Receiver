package com.PiMan.RecieverMod.Items.guns;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.lwjgl.input.Mouse;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.Items.accesories.ItemAccessories;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletSmall;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageEject;
import com.PiMan.RecieverMod.Packets.MessageFlashServer;
import com.PiMan.RecieverMod.Packets.MessagePlaySound;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.IHasModel;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.TransformationBuilder;
import com.PiMan.RecieverMod.util.handlers.FlashHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;

import ibxm.Player;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRifle extends ItemGun{
    
    private static final IItemPropertyGetter BOLT_UP_GETTER = new IItemPropertyGetter()
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
			
			float f = oldnbt.getInteger("BoltUp") * (1 - pt) + nbt.getInteger("BoltUp") * pt;
			
            return f;
        }
    };
    
    private static final IItemPropertyGetter BOLT_BACK_GETTER = new IItemPropertyGetter()
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
			    			    			
			float f = oldnbt.getInteger("BoltBack") * (1 - pt) + nbt.getInteger("BoltBack") * pt;
			    			
            return f;
        }
    };
	
	public ItemRifle(String name) {
		super(name);
		
		this.drift = 1;
		this.spreadX = 0.1;
		this.spreadY = 0.1;

        this.addPropertyOverride(new ResourceLocation("boltup"), BOLT_UP_GETTER);
        this.addPropertyOverride(new ResourceLocation("boltback"), BOLT_BACK_GETTER);
    }
	
	@Override
	public SoundEvent getShootSound() {
		return SoundsHandler.getSoundEvent(Sounds.RIFLE_SHOOT);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		if (entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entityIn;
			
			if (player.getHeldItemMainhand().equals(stack)) {
				//stack.removeSubCompound("Accessories");
				//stack.getOrCreateSubCompound("Accessories").setString("0", "rm:rifle_scope");
				if (worldIn.isRemote) {

					NBTTagCompound tag = this.getNBTTag(stack);
					
					NBTTagCompound baseTag = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
					
					NBTTagCompound nbt = baseTag.getCompoundTag(tag.getString("UUID"));
					
					NBTTagCompound oldnbt = nbt.copy();
					oldnbt.removeTag("prev");
					nbt.setTag("prev", oldnbt);
										
					nbt.setBoolean("ADS", KeyInputHandler.isKeyDown(KeyPresses.RightClick));
										
					boolean flag = false;
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.LeftClick)) {
						flag = Shoot(nbt, player, ModConfig.rifledamage, nbt.getBoolean("ADS") ? 0 : 10, 0, 1, nbt.getInteger("BoltUp") == 0);
						if (flag) {
							nbt.setBoolean("EmptyBulletChambered", true);
							FlashHandler.CreateFlash(new BlockPos(player.posX, player.posY + 1, player.posZ), player.dimension, 2);
						}
					}
					
					nbt.setBoolean("fired", flag);
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveClip) && nbt.getInteger("BoltBack") == 0) {
						NetworkHandler.sendToServer(new MessagePlaySound(nbt.getBoolean("BoltUpToggle") ? Sounds.RIFLE_BOLT_DOWN : Sounds.RIFLE_BOLT_UP));
						nbt.setBoolean("BoltUpToggle", !nbt.getBoolean("BoltUpToggle"));
					}
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveBullet) && nbt.getInteger("BoltUp") == 2) {
						NetworkHandler.sendToServer(new MessagePlaySound(nbt.getBoolean("BoltBackToggle") ? Sounds.RIFLE_BOLT_FORWARD : Sounds.RIFLE_BOLT_BACK));
						nbt.setBoolean("BoltBackToggle", !nbt.getBoolean("BoltBackToggle"));
					}
					
					if (nbt.getBoolean("BoltUpToggle") && nbt.getInteger("BoltUp") < 2) {
						nbt.setInteger("BoltUp", nbt.getInteger("BoltUp") + 1);
					}
					if (nbt.getBoolean("BoltBackToggle") && nbt.getInteger("BoltBack") < 2 && nbt.getInteger("BoltUp") == 2) {
						nbt.setInteger("BoltBack", nbt.getInteger("BoltBack") + 1);
					}
					if (nbt.getInteger("BoltBack") == 1 && nbt.getBoolean("BoltBackToggle")) {
						if (nbt.getBoolean("BulletChambered")) {
							NetworkHandler.sendToServer(new MessageEject(new ItemStack(ModItems.BULLET22)));
							nbt.setBoolean("BulletChambered", false);
						}
						if (nbt.getBoolean("EmptyBulletChambered")) {
							NetworkHandler.sendToServer(new MessageEject(new ItemStack(ModItems.BULLET22CASING)));
							nbt.setBoolean("EmptyBulletChambered", false);
						}
					}
					if (nbt.getInteger("BoltBack") == 2) {
						if (nbt.getInteger("Bullets") < 4 && KeyInputHandler.isKeyPressed(KeyPresses.AddBullet)) {
							int slot = findAmmo(player);
							
							if (slot != -1) {
								ItemStack ammo = player.inventory.getStackInSlot(slot);
								NetworkHandler.sendToServer(new MessageAddToInventory(ammo, -1));
								nbt.setInteger("Bullets", nbt.getInteger("Bullets") + 1);
							}
							
						}
						if (nbt.getInteger("Bullets") > 0 && !nbt.getBoolean("BulletChambered")) {
							nbt.setInteger("Bullets", nbt.getInteger("Bullets") - 1);
							nbt.setBoolean("BulletChambered", true);
						}
					}
					if (!nbt.getBoolean("BoltUpToggle") && nbt.getInteger("BoltUp") > 0 && nbt.getInteger("BoltBack") == 0) {
						nbt.setInteger("BoltUp", nbt.getInteger("BoltUp") - 1);
					}
					if (!nbt.getBoolean("BoltBackToggle") && nbt.getInteger("BoltBack") > 0) {
						nbt.setInteger("BoltBack", nbt.getInteger("BoltBack") - 1);
					}
										
					NetworkHandler.sendToServer(new MessageUpdateNBT(stack, itemSlot, nbt));
				}
			}
		}
	}

	@Override
	public float getDefaultZoomFactor(ItemStack stack) {
		return 0.9F;
	}
	
	@Override
	public Matrix4f getAccessoryTransform(int type) {
		if (type == 1) {
			TransformationBuilder builder = new TransformationBuilder();
			builder.add(new Vector3f(-1.05F / 16F, 0.6F / 16F, 2.25F / 16F), null, null, new Vector3f(0.5F, 0.5F, 0.5F), 0);
			return builder.build().getMatrix();
		}
		else {
			return super.getAccessoryTransform(type);
		}
	}
}