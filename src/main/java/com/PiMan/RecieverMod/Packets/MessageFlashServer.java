package com.PiMan.RecieverMod.Packets;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.PiMan.RecieverMod.util.handlers.FlashHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageFlashServer extends MessageBase<MessageFlashServer> implements IMessage {
			
	private int dimension;
	
	private int duration;
	
	private int x, y, z;

	public MessageFlashServer(){}
	
	public MessageFlashServer(BlockPos pos, int dimension, int duration) {
		this.dimension = dimension;
		this.duration = duration;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {

		dimension = buf.readInt();
		duration = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(dimension);
		buf.writeInt(duration);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		
	}

	@Override
	public void handleClientSide(MessageFlashServer message, EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(MessageFlashServer message, EntityPlayer player) {
		
		IMessage outmessage;

		outmessage = new MessageFlashClient(false, new BlockPos(message.x, message.y, message.z), message.dimension, message.duration);
		
		NetworkHandler.sendToAll(outmessage);
		//System.out.println("Sending Flash Message");
	}

}
