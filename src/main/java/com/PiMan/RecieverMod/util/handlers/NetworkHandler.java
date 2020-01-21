package com.PiMan.RecieverMod.util.handlers;

import com.PiMan.RecieverMod.Packets.MessageEject;
import com.PiMan.RecieverMod.Packets.MessageEntityPosVelUpdate;
import com.PiMan.RecieverMod.Packets.MessageFlashClient;
import com.PiMan.RecieverMod.Packets.MessageFlashServer;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageDamageParticles;
import com.PiMan.RecieverMod.Packets.MessageGetPlayerNBT;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.util.Reference;
import com.PiMan.RecieverMod.Packets.MessagePlaySound;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.Packets.MessageSpawnEntity;
import com.PiMan.RecieverMod.Packets.MessageSyncConfig;
import com.PiMan.RecieverMod.Packets.MessageSendEntityData;
import com.PiMan.RecieverMod.Packets.MessageSendItemUUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	
	private static SimpleNetworkWrapper INSTANCE;

	public static void init() {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
		
		int i = 0;
		
		INSTANCE.registerMessage(MessageAddToInventory.class, MessageAddToInventory.class, i++, Side.SERVER);
		INSTANCE.registerMessage(MessageUpdateNBT.class, MessageUpdateNBT.class, i++, Side.SERVER);
		INSTANCE.registerMessage(MessageUpdateNBT.class, MessageUpdateNBT.class, i++, Side.CLIENT);
		INSTANCE.registerMessage(MessagePlaySound.class, MessagePlaySound.class, i++, Side.SERVER);
		INSTANCE.registerMessage(MessageGetPlayerNBT.class, MessageGetPlayerNBT.class, i++, Side.SERVER);
		INSTANCE.registerMessage(MessageSendEntityData.class, MessageSendEntityData.class, i++, Side.CLIENT);
		INSTANCE.registerMessage(MessageEject.class, MessageEject.class, i++, Side.SERVER);
		INSTANCE.registerMessage(MessageShoot.class, MessageShoot.class, i++, Side.SERVER);
		INSTANCE.registerMessage(MessageFlashServer.class, MessageFlashServer.class, i++, Side.SERVER);
		INSTANCE.registerMessage(MessageFlashClient.class, MessageFlashClient.class, i++, Side.CLIENT);
		INSTANCE.registerMessage(MessageEntityPosVelUpdate.class, MessageEntityPosVelUpdate.class, i++, Side.CLIENT);
		INSTANCE.registerMessage(MessageDamageParticles.class, MessageDamageParticles.class, i++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSpawnEntity.class, MessageSpawnEntity.class, i++, Side.SERVER);
		INSTANCE.registerMessage(MessageSendItemUUID.class, MessageSendItemUUID.class, i++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSyncConfig.class, MessageSyncConfig.class, i++, Side.CLIENT);
		
	}
	
	public static void sendToServer(IMessage message) {
		INSTANCE.sendToServer(message);
	}
		
	public static void sendToClient(IMessage message, EntityPlayerMP player) {
		INSTANCE.sendTo(message, player);
	}
	
	public static void sendToAll(IMessage message) {
		INSTANCE.sendToAll(message);
	}
	public static void sendToAllTracking(IMessage message, Entity entity) {
		INSTANCE.sendToAllTracking(message, entity);
	}
}
