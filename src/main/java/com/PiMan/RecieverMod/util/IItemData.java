package com.PiMan.RecieverMod.util;

import net.minecraft.nbt.NBTTagCompound;

public interface IItemData {
	
	public void setItemData(NBTTagCompound nbt);
	
	public NBTTagCompound getItemData();

}
