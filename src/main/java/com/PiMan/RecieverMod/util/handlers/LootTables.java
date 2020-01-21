package com.PiMan.RecieverMod.util.handlers;

import com.PiMan.RecieverMod.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTables {

	public static ResourceLocation BIOME_WASTELAND;
	public static ResourceLocation BIOME_ABANDONED;
	public static ResourceLocation BIOME_OLD;
	public static ResourceLocation BIOME_NEW;
	public static ResourceLocation ENTITIES_TURRET;
	public static ResourceLocation TOWER_MAIN;
	
	public static void register() {
		BIOME_WASTELAND = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "biomes/wasteland"));
		BIOME_ABANDONED = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "biomes/abandoned"));
		BIOME_OLD = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "biomes/old"));
		BIOME_NEW = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "biomes/new"));
		ENTITIES_TURRET = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/turret"));
		TOWER_MAIN = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "chests/tower_main"));
	}
	
}
