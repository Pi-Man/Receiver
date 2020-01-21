package com.PiMan.RecieverMod.util.handlers;

import com.PiMan.RecieverMod.client.model.IModelGunBBModel;
import com.PiMan.RecieverMod.util.IModelClip;
import com.PiMan.RecieverMod.util.IModelGun;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class RegistryEventHandler {

	@SubscribeEvent
	public static void onModelRegistryEvent(ModelRegistryEvent event) {
		System.out.println("Registering Model Loaders");
		ModelLoaderRegistry.registerLoader(IModelGun.GunModelLoader.INSTANCE);
		ModelLoaderRegistry.registerLoader(IModelClip.ClipModelLoader.INSTANCE);
		ModelLoaderRegistry.registerLoader(IModelGunBBModel.GunModelLoader.INSTANCE);
	}

}
