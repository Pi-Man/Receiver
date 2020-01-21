package com.PiMan.RecieverMod.Items.animations;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.Packets.MessageEject;
import com.PiMan.RecieverMod.Packets.MessagePlaySound;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.FlashHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnimationControllerShoot implements IAnimationController {
	
	public final Condition condition;
	
	public AnimationControllerShoot(Condition condition) {
		this.condition = condition;
	}

	@Override
	public List<ItemPropertyWrapper> getProperties() {
		List<ItemPropertyWrapper> list = new ArrayList<>();
		
		list.add(new ItemPropertyWrapper("fired", new IItemPropertyGetter() {
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
				
	            float j = (oldnbt.getBoolean("fired") ? 1.0F : 0.0F) * (1 - pt) + (nbt.getBoolean("fired") ? 1.0F : 0.0F) * pt;
				
				return j;
			}
		}));
		
		return list;
	}

	@Override
	public void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, NBTTagCompound nbt, ItemGun gun) {
		if (entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityIn;
			nbt.setBoolean("fired", false);
			if (KeyInputHandler.isKeyDown(KeyPresses.LeftClick) && nbt.getBoolean("hammer") && (!nbt.getBoolean("held") || nbt.getBoolean("Auto"))) {
				boolean flag = gun.Shoot(nbt, (EntityLivingBase) entityIn, ModConfig.glockdamage, nbt.getBoolean("ADS") ? 0 : 10, 0, 1, condition.apply(nbt));
				if (flag) {
					if (!player.isCreative()) {
						//NetworkHandler.sendToServer(new MessageEject(new ItemStack(ModItems.BULLET9MMCASING)));
					}
					if (nbt.getBoolean("Auto") && !KeyInputHandler.isKeyPressed(KeyPresses.LeftClick)) {
						FlashHandler.CreateFlash(new BlockPos(player.posX, player.posY + 1, player.posZ), player.dimension, 1);
					}
					else {
						FlashHandler.CreateFlash(new BlockPos(player.posX, player.posY + 1, player.posZ), player.dimension, 2);
					}
					nbt.setBoolean("fired", true);
				}
				else {
					NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_DRY));
				}
				nbt.setBoolean("held", true);
			}
			else if (KeyInputHandler.isKeyUnpressed(KeyPresses.LeftClick)) {
				nbt.setBoolean("held", false);
			}
		}
	}

	public static interface Condition {
		public boolean apply(NBTTagCompound nbt);
	}
	
}


