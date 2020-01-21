package com.PiMan.RecieverMod.Packets;

import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.util.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;

public class MessageSyncConfig extends MessageBase<MessageSyncConfig> {

	private int mazeID, remingtonpelletcount, fragcount;
	private double headShotmul, coltdamage, glockdamage, model10damage, rifledamage, thompsondamage, remingtondamage, fragdamage;
	
	@Override
	public void fromBytes(ByteBuf buf) {
		mazeID = buf.readInt();
		headShotmul = buf.readDouble();
		coltdamage = buf.readDouble();
		glockdamage = buf.readDouble();
		model10damage = buf.readDouble();
		rifledamage = buf.readDouble();
		thompsondamage = buf.readDouble();
		remingtonpelletcount = buf.readInt();
		remingtondamage = buf.readDouble();
		fragcount = buf.readInt();
		fragdamage = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(ModConfig.mazeID);
		buf.writeDouble(ModConfig.headShotMul);
		buf.writeDouble(ModConfig.coltdamage);
		buf.writeDouble(ModConfig.glockdamage);
		buf.writeDouble(ModConfig.model10damage);
		buf.writeDouble(ModConfig.rifledamage);
		buf.writeDouble(ModConfig.thompsondamage);
		buf.writeInt(ModConfig.remingtonpelletcount);
		buf.writeDouble(ModConfig.remingtondamage);
		buf.writeInt(ModConfig.fragcount);
		buf.writeDouble(ModConfig.fragdamage);
	}

	@Override
	public void handleClientSide(MessageSyncConfig message, EntityPlayer player) {
		ModConfig.mazeID = message.mazeID;
		ModConfig.headShotMul = message.headShotmul;
		ModConfig.coltdamage = message.coltdamage;
		ModConfig.glockdamage = message.glockdamage;
		ModConfig.model10damage = message.model10damage;
		ModConfig.rifledamage = message.rifledamage;
		ModConfig.thompsondamage = message.thompsondamage;
		ModConfig.remingtonpelletcount = message.remingtonpelletcount;
		ModConfig.remingtondamage = message.remingtondamage;
		ModConfig.fragcount = message.fragcount;
		ModConfig.fragdamage = message.fragdamage;
		ConfigManager.sync(Reference.MOD_ID, Type.INSTANCE);
	}

	@Override
	public void handleServerSide(MessageSyncConfig message, EntityPlayer player) {

	}

}
