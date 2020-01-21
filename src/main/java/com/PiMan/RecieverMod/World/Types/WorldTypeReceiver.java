package com.PiMan.RecieverMod.World.Types;

import com.PiMan.RecieverMod.World.Biomes.BiomeProviderMaze;
import com.PiMan.RecieverMod.World.Types.Layer.GenLayerMaze;
import com.PiMan.RecieverMod.init.BiomeInit;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;

public class WorldTypeReceiver extends WorldType {

	public WorldTypeReceiver(String name) {
		super(name);
	}

	@Override
	public boolean canBeCreated() {
		return false;
	}	
	
	@Override
	public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer, ChunkGeneratorSettings chunkSettings) {
		return new GenLayerMaze(worldSeed, parentLayer, this, chunkSettings);
	}
	
	@Override
	public BiomeProvider getBiomeProvider(World world) {
		return new BiomeProviderMaze(world.getWorldInfo(), this);
	}
	
}
