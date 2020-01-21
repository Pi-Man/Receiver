package com.PiMan.RecieverMod.util.handlers;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Packets.MessageFlashClient;
import com.PiMan.RecieverMod.Packets.MessageSyncConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class ServerEventHandler {

	@SubscribeEvent
	public void ServerTick(ServerTickEvent event) {
		if (event.phase == Phase.START) {
			for (int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().worlds.length; i++) {
				World world = FMLCommonHandler.instance().getMinecraftServerInstance().worlds[i];
				NetworkHandler.sendToAll(new MessageFlashClient(true, world.provider.getDimension()));
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public void PlayerJoin(PlayerLoggedInEvent event) {
		NetworkHandler.sendToClient(new MessageSyncConfig(), (EntityPlayerMP) event.player);
	}
}
