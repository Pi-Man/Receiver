package com.PiMan.RecieverMod.World.Biomes;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.Entity.EntityTurret;
import com.PiMan.RecieverMod.util.handlers.LootTables;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeVoidDecorator;

public class BiomeAbandoned extends BiomeMaze {
		
	public BiomeAbandoned(BiomeProperties properties) {
		super(properties);
	}
	
	public BiomeAbandoned() {
		super(new BiomeProperties("room_1").setBaseHeight(0F).setHeightVariation(0F).setRainDisabled().setTemperature(0.5F));

		this.topBlock = Blocks.COBBLESTONE.getDefaultState();
		this.fillerBlock = Blocks.STONEBRICK.getStateFromMeta(2);
		this.filler_deapth = 0;
		
		this.roofBlocks.add(Blocks.COBBLESTONE.getDefaultState());
		this.roofBlocks.add(Blocks.STONEBRICK.getStateFromMeta(1));
				
		this.wallBlocks.add(Blocks.STONEBRICK.getStateFromMeta(0));
		this.wallBlocks.add(Blocks.STONEBRICK.getStateFromMeta(1));
		this.wallBlocks.add(Blocks.STONEBRICK.getStateFromMeta(2));
		
		this.floorBlocks.add(Blocks.GRAVEL.getDefaultState());
		this.floorBlocks.add(Blocks.STONEBRICK.getStateFromMeta(1));
		this.floorBlocks.add(Blocks.STONEBRICK.getStateFromMeta(2));
		
		this.groundBlocks.add(Blocks.STONE.getDefaultState());
	
		this.decorator = new BiomeVoidDecorator();
		
		lootTable = LootTables.BIOME_ABANDONED;
		
	}

}
