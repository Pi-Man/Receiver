package com.PiMan.RecieverMod.Packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageSendEntityData extends MessageBase<MessageSendEntityData> {

	Entity entity;
	
	public MessageSendEntityData() {}
	
	public MessageSendEntityData(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		entity.writeToNBT(nbt);
		ByteBufUtils.writeTag(buf, nbt);
	}

	@Override
	public void handleClientSide(MessageSendEntityData message, EntityPlayer player) {

	}

	@Override
	public void handleServerSide(MessageSendEntityData message, EntityPlayer player) {

	}

}
