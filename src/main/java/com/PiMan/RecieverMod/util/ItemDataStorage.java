package com.PiMan.RecieverMod.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ItemDataStorage implements IStorage<IItemData> {

	@Override
	public NBTBase writeNBT(Capability<IItemData> capability, IItemData instance, EnumFacing side) {
		return instance.getItemData();
	}

	@Override
	public void readNBT(Capability<IItemData> capability, IItemData instance, EnumFacing side, NBTBase nbt) {
		if (!nbt.toString().equals("{}")) {
			instance.setItemData((NBTTagCompound) nbt);
		}
	}

}
