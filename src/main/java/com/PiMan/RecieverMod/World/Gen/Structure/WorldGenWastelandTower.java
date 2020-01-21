package com.PiMan.RecieverMod.World.Gen.Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.World.Gen.ChunkGeneratorReceiver;
import com.PiMan.RecieverMod.init.BiomeInit;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenWastelandTower implements IWorldGenerator {
	
	public static final WorldGenStructure TOWER = new WorldGenStructure("wasteland", new PlacementSettings().setBoundingBox(new StructureBoundingBox(0, -255, 0, 14, 255, 14)).setIgnoreEntities(false));

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		
		switch(world.provider.getDimension()) {
		case -1:
			break;
		case 0:
			
			generateStructure(TOWER, chunkGenerator, world, random, 8, -1, 8, chunkX, chunkZ, 500, Biomes.DESERT.getClass());
			
			break;
		case 1:
			break;
		case 2:		
			break;
		}
	}

	
	private void generateStructure(WorldGenStructure generator, IChunkGenerator chunkGenerator, World world, Random rand, int offsetX, int offsetY, int offsetZ, int chunkX, int chunkZ, int chance, Class<?>...classes) {
		ArrayList<Class<?>> classesList = new ArrayList<Class<?>>(Arrays.asList(classes));
		
		int x = (chunkX * 16) + offsetX;
		
		int z = (chunkZ * 16) + offsetZ;
		
		BlockPos pos = new BlockPos(x, 0, z);
		
		pos = world.getTopSolidOrLiquidBlock(pos);
		
		pos = pos.add(0, offsetY, 0);
		
		Class<?> biome = world.provider.getBiomeForCoords(pos).getClass();
		
		if (classesList.contains(biome)) {
			if (rand.nextInt(chance) == 0) {			
				//Main.LOGGER.info("Spawning Structure at {}", pos);
				generator.generate(world, rand, pos);
			}
		}
	}
}
