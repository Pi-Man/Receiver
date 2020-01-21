package com.PiMan.RecieverMod.init;

import com.PiMan.RecieverMod.World.Biomes.BiomeAbandoned;
import com.PiMan.RecieverMod.World.Biomes.BiomeAbandonedRoof;
import com.PiMan.RecieverMod.World.Biomes.BiomeOld;
import com.PiMan.RecieverMod.World.Biomes.BiomeOldRoof;
import com.PiMan.RecieverMod.World.Biomes.BiomeNew;
import com.PiMan.RecieverMod.World.Biomes.BiomeWasteland;
import com.PiMan.RecieverMod.World.Dimensions.Maze.DimensionMaze;
import com.PiMan.RecieverMod.World.Biomes.BiomeNewRoof;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeInit {
	
	public static final Biome WASTELAND = new BiomeWasteland();
	public static final Biome ABANDONED = new BiomeAbandoned();
	public static final Biome ABANDONEDROOF = new BiomeAbandonedRoof();
	public static final Biome OLD = new BiomeOld();
	public static final Biome OLDROOF = new BiomeOldRoof();
	public static final Biome NEW = new BiomeNew();
	public static final Biome NEWROOF = new BiomeNewRoof();
	
	public static void registerBiomes() {
		initBiome(false, false, WASTELAND, "wasteland", BiomeType.WARM, Type.DEAD, Type.SPOOKY, Type.WASTELAND);
		initBiome(false, true, ABANDONED, "abandoned", BiomeType.WARM, Type.DEAD, Type.SPOOKY, Type.WASTELAND);
		initBiome(false, false, ABANDONEDROOF, "abandonedroof", BiomeType.WARM, Type.DEAD, Type.SPOOKY, Type.WASTELAND);
		initBiome(false, false, OLD, "old", BiomeType.WARM, Type.DEAD, Type.SPOOKY, Type.WASTELAND);
		initBiome(false, false, OLDROOF, "oldroof", BiomeType.WARM, Type.DEAD, Type.SPOOKY, Type.WASTELAND);
		initBiome(false, false, NEW, "new", BiomeType.WARM, Type.DEAD, Type.SPOOKY, Type.WASTELAND);
		initBiome(false, false, NEWROOF, "newroof", BiomeType.WARM, Type.DEAD, Type.SPOOKY, Type.WASTELAND);
	}
	
	private static Biome initBiome(boolean inOverworld, boolean isSpawn, Biome biome, String name, BiomeType biomeType, Type... types) {
		
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
		if (inOverworld) {
			BiomeManager.addBiome(biomeType, new BiomeEntry(biome, 1000));
			if (isSpawn) {
				BiomeManager.addSpawnBiome(biome);
			}
		}
		if (isSpawn) {
			BiomeManager.addSpawnBiome(biome);
		}
		
		return biome;
	}
}
