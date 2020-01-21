package com.PiMan.RecieverMod.proxy;

import com.PiMan.RecieverMod.KeyBinding.Keybinds;
import com.PiMan.RecieverMod.init.BiomeInit;
import com.PiMan.RecieverMod.init.DimensionInit;
import com.PiMan.RecieverMod.init.EntityInit;
import com.PiMan.RecieverMod.util.CapabilityHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.LootTables;
import com.PiMan.RecieverMod.util.handlers.MiscEventHandler;
import com.PiMan.RecieverMod.util.handlers.PlayerRenderHandler;
import com.PiMan.RecieverMod.util.handlers.RegistryEventHandler;
import com.PiMan.RecieverMod.util.handlers.RenderHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.TextureStitcher;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import paulscode.sound.SoundSystemConfig;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void registerItemRenderer(Item item, int meta, ModelResourceLocation location) {
		ModelLoader.setCustomModelResourceLocation(item, meta, location);
	}
	
	@Override
    public void preInit() {
		SoundSystemConfig.setNumberNormalChannels(1000);
		Keybinds.register();
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerRenderHandler());
		MinecraftForge.EVENT_BUS.register(new MiscEventHandler());
		MinecraftForge.EVENT_BUS.register(new RegistryEventHandler());
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new RenderPartialTickHandler());
		MinecraftForge.EVENT_BUS.register(new TextureStitcher());
    	LootTables.register();
		BiomeInit.registerBiomes();
		DimensionInit.registerDiminsions();
		EntityInit.registerEntities();
		RenderHandler.registerEntityRenders();
	}
	
	@Override
	public void postInit() {
		super.postInit();
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}
	
	@Override
	public World getWorld(int dimension) {
		return Minecraft.getMinecraft().world;
	}
	
	@Override
	public void playLoopingEntitySound(Entity entity, SoundEvent sound, SoundCategory category) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new SoundsHandler.LoopingEntitySound(entity, sound, category));
	}

}
