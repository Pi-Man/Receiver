package com.PiMan.RecieverMod.Packets;

import com.PiMan.RecieverMod.util.handlers.NetworkHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageGetPlayerNBT extends MessageBase<MessageGetPlayerNBT> {

	private String name;
	
	public MessageGetPlayerNBT() {}
	
	public MessageGetPlayerNBT(String name) {
		this.name = name;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, name);
	}

	@Override
	public void handleClientSide(MessageGetPlayerNBT message, EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(MessageGetPlayerNBT message, EntityPlayer player) {
		//System.out.println("Message Recieved from Client");
		
		EntityPlayer playerRequest = player.world.getPlayerEntityByName(message.name);
		NBTTagCompound nbt = playerRequest.getEntityData();
		
		//System.out.println("Sending Requested Data for player: " + playerRequest + "  Data: " + nbt);

		//NetworkHandler.sendToClient(new MessageSendEntityData(nbt, message.name), (EntityPlayerMP) player);
	}
}
