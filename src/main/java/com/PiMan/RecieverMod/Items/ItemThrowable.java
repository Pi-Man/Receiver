package com.PiMan.RecieverMod.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemThrowable extends ItemBase {

	public ItemThrowable(String name, type TYPE) {
		super(name);
		this.setMaxStackSize(16);
		this.TYPE = TYPE;
	}
	
	static type TYPE;
	
	public boolean Explode() {
		return false;
	}
	
	@Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 100;
    }
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
	
	enum type {
		LETHAL,
		NONLETHAL;
	}

}
