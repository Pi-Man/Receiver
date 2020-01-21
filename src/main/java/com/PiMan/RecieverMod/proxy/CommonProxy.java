package com.PiMan.RecieverMod.proxy;
import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.World.Types.WorldTypeReceiver;
import com.PiMan.RecieverMod.init.BiomeInit;
import com.PiMan.RecieverMod.init.DimensionInit;
import com.PiMan.RecieverMod.init.EntityInit;
import com.PiMan.RecieverMod.util.CapabilityHandler;
import com.PiMan.RecieverMod.util.handlers.LootTables;
import com.PiMan.RecieverMod.util.handlers.RegistryEventHandler;
import com.PiMan.RecieverMod.util.handlers.ServerEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.server.FMLServerHandler;

public class CommonProxy {
	
	public static final CommonProxy INSTANCE = new CommonProxy();
	
	public WorldType RECEIVER;
	
    public void preInit() {
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
    	LootTables.register();
		BiomeInit.registerBiomes();
		DimensionInit.registerDiminsions();
		EntityInit.registerEntities();
    }
    
    public void postInit() {
    	RECEIVER = new WorldTypeReceiver("receiver");
    }
	
	public void registerItemRenderer(Item item, int meta, ModelResourceLocation location) {}

	public void registerRenderers(Main ins) {}

	public EntityPlayer getPlayer() {
		return null;
	}

	public World getWorld(int dimension) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
	}
	
	public void playLoopingEntitySound(Entity entity, SoundEvent sound, SoundCategory category) {}
	
}
