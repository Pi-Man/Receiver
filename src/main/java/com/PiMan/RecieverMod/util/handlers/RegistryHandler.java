package com.PiMan.RecieverMod.util.handlers;

import java.util.List;

import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.Items.IItemInit;
import com.PiMan.RecieverMod.init.EntityInit;
import com.PiMan.RecieverMod.init.ModBlocks;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.IHasModel;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@EventBusSubscriber
public class RegistryHandler {

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0] ));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0] ));
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
	
		for(Item item : ModItems.ITEMS) {
			
			if(item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
			if (item instanceof IItemInit) {
				((IItemInit) item).Init();
			}
		}
		
		for (Block block : ModBlocks.BLOCKS) {
			if (block instanceof IHasModel) {
				((IHasModel)block).registerModels();
			}
		}
	}
}
