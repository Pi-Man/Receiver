package com.PiMan.RecieverMod.World.Gen.Structure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.World.Biomes.BiomeOld;
import com.PiMan.RecieverMod.World.Biomes.BiomeWasteland;
import com.PiMan.RecieverMod.init.BiomeInit;
import com.google.common.collect.Lists;

import net.minecraft.init.Biomes;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class MapGenCustomStructureTest extends MapGenStructure {

    private static final List<Biome> BIOMELIST = Arrays.<Biome>asList(BiomeInit.WASTELAND, BiomeInit.ABANDONED, BiomeInit.OLD, BiomeInit.NEW);
    /** the maximum distance between scattered features */
    private int maxDistanceBetweenScatteredFeatures;
    /** the minimum distance between scattered features */
    private final int minDistanceBetweenScatteredFeatures;

    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer) {
    	super.generate(worldIn, x, z, primer);
    }
    
    public MapGenCustomStructureTest()
    {
        this.maxDistanceBetweenScatteredFeatures = 9;
        this.minDistanceBetweenScatteredFeatures = 0;
    }

    public MapGenCustomStructureTest(Map<String, String> p_i2061_1_)
    {
        this();

        for (Entry<String, String> entry : p_i2061_1_.entrySet())
        {
            if (((String)entry.getKey()).equals("distance"))
            {
                this.maxDistanceBetweenScatteredFeatures = MathHelper.getInt(entry.getValue(), this.maxDistanceBetweenScatteredFeatures, 9);
            }
        }
    }

    public String getStructureName()
    {
        return "Tower";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
    	
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        int k = chunkX / this.maxDistanceBetweenScatteredFeatures;
        int l = chunkZ / this.maxDistanceBetweenScatteredFeatures;
        Random random = this.world.setRandomSeed(k, l, 14357617);
        k = k * this.maxDistanceBetweenScatteredFeatures;
        l = l * this.maxDistanceBetweenScatteredFeatures;
        k = k + random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
        l = l + random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);

        if (i == k && j == l)
        {
            Biome biome = this.world.getBiomeProvider().getBiome(new BlockPos(i * 16 + 8, 0, j * 16 + 8));

            if (biome == null)
            {
                return false;
            }

            for (Biome biome1 : BIOMELIST)
            {
                if (biome == biome1)
                {
                	//this.structureSpawned = true;
                    return true;
                }
            }
        }

        return false;
    }

    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, this.maxDistanceBetweenScatteredFeatures, 8, 14357617, false, 100, findUnexplored);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new Start(this.world, this.rand, chunkX, chunkZ);
    }
    
    public static class Start extends StructureStart
    {
        public Start()
        {
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ)
        {
            this(worldIn, random, chunkX, chunkZ, worldIn.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8)));
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ, Biome biomeIn)
        {        	
            super(chunkX, chunkZ);
            
            TemplateManager templatemanager = worldIn.getSaveHandler().getStructureTemplateManager();
                        
            if (biomeIn instanceof BiomeWasteland) {
            	
            	WorldGenStructureComponentTest.Tower structure = null;
            	
            	int n = random.nextInt(10);
            	switch(n%2) {
            	case 0:
                	structure = new WorldGenStructureComponentTest.Tower(templatemanager, "wasteland_tower", new BlockPos(chunkX * 16 + 8, 57, chunkZ * 16 + 8), Rotation.values()[random.nextInt(Rotation.values().length)], true);
                	break;
            	case 1:
                	structure = new WorldGenStructureComponentTest.Tower(templatemanager, "wasteland_tower2", new BlockPos(chunkX * 16 + 8, 54, chunkZ * 16 + 8), Rotation.values()[random.nextInt(Rotation.values().length)], true);
                	break;
            	}
                        
                this.components.add(structure);

            }
            else if (biomeIn instanceof BiomeOld) {
            	
            	
            	WorldGenStructureComponentTest.Tower structure = null;
            	
            	int n = random.nextInt(10);
            	switch(n%2) {
            	case 0:
                	structure = new WorldGenStructureComponentTest.Tower(templatemanager, "wasteland_tower", new BlockPos(chunkX * 16 + 8, 57, chunkZ * 16 + 8), Rotation.values()[random.nextInt(Rotation.values().length)], true);
                	break;
            	case 1:
                	structure = new WorldGenStructureComponentTest.Tower(templatemanager, "room1", new BlockPos(chunkX * 16 + 8, 57, chunkZ * 16 + 8), Rotation.values()[random.nextInt(Rotation.values().length)], true);
                	break;
            	}
                
                this.components.add(structure);
            }
            else {
                this.components.add(new WorldGenStructureComponentTest.Tower(templatemanager, "wasteland_tower", new BlockPos(chunkX * 16 + 8, 57, chunkZ * 16 + 8), Rotation.values()[random.nextInt(Rotation.values().length)], true));
            }
            
            
            this.updateBoundingBox();
        }
    }

}
