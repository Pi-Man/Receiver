package com.PiMan.RecieverMod.Items.animations;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class AnimationControllerADS implements IAnimationController {

	@Override
	public List<ItemPropertyWrapper> getProperties() {
		List<ItemPropertyWrapper> list= new ArrayList<>();
		
		list.add(new ItemPropertyWrapper("ads", new IItemPropertyGetter() {
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
				
	            float j = (oldnbt.getBoolean("ADS") ? 1.0F : 0.0F) * (1 - pt) + (nbt.getBoolean("ADS") ? 1.0F : 0.0F) * pt;
				
				return j;
			}
		}));
		
		return list;
	}

	@Override
	public void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, NBTTagCompound nbt, ItemGun gun) {
		nbt.setBoolean("ADS", KeyInputHandler.isKeyDown(KeyPresses.RightClick));
	}

}
