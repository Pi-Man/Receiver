package com.PiMan.RecieverMod.Items.animations;

import java.util.List;

import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.guns.ItemGun;

import net.minecraft.entity.Entity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface IAnimationController {
	
	List<ItemPropertyWrapper> getProperties();
	
	void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, NBTTagCompound nbt, ItemGun gun);

}
