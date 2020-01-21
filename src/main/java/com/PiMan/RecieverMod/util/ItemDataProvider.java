package com.PiMan.RecieverMod.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ItemDataProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject (IItemData.class)
	public static final Capability<IItemData> ITEMDATA_CAP = null;
	
	private IItemData instance = ITEMDATA_CAP.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == ITEMDATA_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == ITEMDATA_CAP ? ITEMDATA_CAP.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return ITEMDATA_CAP.getStorage().writeNBT(ITEMDATA_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		ITEMDATA_CAP.getStorage().readNBT(ITEMDATA_CAP, this.instance, null, nbt);
	}

}
