package com.PiMan.RecieverMod.World.Biomes;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.util.handlers.LootTables;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeVoidDecorator;

public class BiomeNew extends BiomeMaze {
	
	public BiomeNew(BiomeProperties properties) {
		super(properties);
	}
	
	public BiomeNew() {
		super(new BiomeProperties("room_3").setBaseHeight(0F).setHeightVariation(0F).setRainDisabled().setTemperature(0.5F));
		
		this.topBlock = Blocks.IRON_BARS.getDefaultState();
		this.fillerBlock = Blocks.IRON_BARS.getDefaultState();
		this.filler_deapth = 1;
		
		this.roofBlocks.add(Blocks.CONCRETE.getStateFromMeta(0));
		
		this.wallBlocks.add(Blocks.CONCRETE.getStateFromMeta(0));
		
		this.floorBlocks.add(Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(0));
		
		this.groundBlocks.add(Blocks.STONE.getDefaultState());

	
		this.decorator = new BiomeVoidDecorator();
		
		lootTable = LootTables.BIOME_NEW;
		
	}

}
