package com.PiMan.RecieverMod.util;

import net.minecraft.nbt.NBTTagCompound;

public class ItemData implements IItemData {

	private NBTTagCompound nbt = new NBTTagCompound();
	
	@Override
	public void setItemData(NBTTagCompound nbt) {
		this.nbt = nbt;
	}

	@Override
	public NBTTagCompound getItemData() {
		return this.nbt;
	}

}
