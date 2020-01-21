package com.PiMan.RecieverMod.util.handlers;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class RenderPartialTickHandler {
	
	public static float renderPartialTick;
	
	@SubscribeEvent
	public void OnRenderPartialTick(RenderTickEvent event) {
		renderPartialTick = event.renderTickTime;
	}

}
