package com.PiMan.RecieverMod.World.Biomes;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.util.handlers.LootTables;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeVoidDecorator;

public class BiomeOldRoof extends BiomeMaze {
	
	public BiomeOldRoof(BiomeProperties properties) {
		super(properties);
	}
	
	public BiomeOldRoof() {
		super(new BiomeProperties("room_2").setBaseHeight(1F).setHeightVariation(0F).setRainDisabled().setTemperature(0.5F));
		
		this.topBlock = Blocks.STONEBRICK.getDefaultState();
		this.fillerBlock = Blocks.STONEBRICK.getDefaultState();
		this.filler_deapth = 0;
		
		this.roofBlocks.add(Blocks.STONEBRICK.getStateFromMeta(3));
		
		this.wallBlocks.add(Blocks.STONEBRICK.getDefaultState());
		
		this.floorBlocks.add(Blocks.STONEBRICK.getStateFromMeta(3));
		
		this.groundBlocks.add(Blocks.STONE.getDefaultState());

	
		this.decorator = new BiomeVoidDecorator();
		
		lootTable = LootTables.BIOME_OLD;
		
	}

}
