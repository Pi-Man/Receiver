package com.PiMan.RecieverMod.World.Biomes;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.Entity.EntityTurret;
import com.PiMan.RecieverMod.util.handlers.LootTables;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeVoidDecorator;

public class BiomeWasteland extends BiomeMaze {
	
	public BiomeWasteland(BiomeProperties properties) {
		super(properties);
	}
	
	public BiomeWasteland() {
		super(new BiomeProperties("room_4").setBaseHeight(-0.415F).setHeightVariation(0F).setRainDisabled().setTemperature(0.5F));
		
		this.spawnableMonsterList.removeIf(entry -> entry.entityClass == EntityTurret.class);
		
		this.topBlock = Blocks.SAND.getDefaultState();
		this.fillerBlock = Blocks.SAND.getStateFromMeta(2);
		this.filler_deapth = 0;
		
		this.roofBlocks.add(Blocks.SAND.getDefaultState());
		
		this.wallBlocks.add(Blocks.SANDSTONE.getDefaultState());
		
		for (int i = 0; i < 10; i ++) {
			this.floorBlocks.add(Blocks.SAND.getDefaultState());
		}
		this.floorBlocks.add(Blocks.COBBLESTONE.getDefaultState());
		
		this.groundBlocks = this.floorBlocks;
	
		this.decorator = new BiomeVoidDecorator();
		
		this.lootTable = LootTables.BIOME_WASTELAND;
		
	}

}
