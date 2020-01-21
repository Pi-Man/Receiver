package com.PiMan.RecieverMod.World.Gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.PiMan.RecieverMod.World.Biomes.BiomeMaze;
import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.World.Biomes.BiomeAbandoned;
import com.PiMan.RecieverMod.World.Biomes.BiomeOld;
import com.PiMan.RecieverMod.World.Biomes.BiomeWasteland;
import com.PiMan.RecieverMod.World.Gen.ChunkGeneratorReceiver.SimpleEntry;
import com.PiMan.RecieverMod.World.Gen.Structure.WorldGenStructure;
import com.PiMan.RecieverMod.World.Gen.Structure.WorldGenWastelandTower;
import com.PiMan.RecieverMod.util.handlers.LootTables;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;

public class ChunkGeneratorMaze extends ChunkGeneratorReceiver {

	public static List<ChunkPos> addedStructure = new ArrayList<ChunkPos>();
	private final int floorHeight = 58;
	private final int roofHeight = 64;
	
	private static final List<IBlockState> DEFAULT_FILLER = new ArrayList<IBlockState>(); {
		DEFAULT_FILLER.add(Blocks.STONE.getDefaultState());

	}

	public ChunkGeneratorMaze(World worldIn, long seed, WorldType worldType) {
		super(worldIn, seed, false, false, false, false, "");
		this.terrainType = worldType;
		this.world.setSeaLevel(0);
	}
	
	@Override
	public void populate(int x, int z)
    {
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.world.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());
        boolean flag = false;
        ChunkPos chunkpos = new ChunkPos(x, z);

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);

        if (this.mapFeaturesEnabled)
        {
            if (this.settings.useMineShafts)
            {
                this.mineshaftGenerator.generateStructure(this.world, this.rand, chunkpos);
            }

            if (this.settings.useVillages)
            {
                flag = this.villageGenerator.generateStructure(this.world, this.rand, chunkpos);
            }

            if (this.settings.useStrongholds)
            {
                this.strongholdGenerator.generateStructure(this.world, this.rand, chunkpos);
            }

            if (this.settings.useTemples)
            {
                this.scatteredFeatureGenerator.generateStructure(this.world, this.rand, chunkpos);
            }

            if (this.settings.useMonuments)
            {
                this.oceanMonumentGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
        }
                
        if (this.terrainFeaturesEnabled) {
	        if (biome != Biomes.DESERT && biome != Biomes.DESERT_HILLS && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0)
	        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE))
	        {
	            int i1 = this.rand.nextInt(16) + 8;
	            int j1 = this.rand.nextInt(256);
	            int k1 = this.rand.nextInt(16) + 8;
	            (new WorldGenLakes(Blocks.WATER)).generate(this.world, this.rand, blockpos.add(i1, j1, k1));
	        }
	
	        if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes)
	        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA))
	        {
	            int i2 = this.rand.nextInt(16) + 8;
	            int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
	            int k3 = this.rand.nextInt(16) + 8;
	
	            if (l2 < this.world.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0)
	            {
	                (new WorldGenLakes(Blocks.LAVA)).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
	            }
	        }

	        if (this.settings.useDungeons)
	        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON))
	        {
	            for (int j2 = 0; j2 < this.settings.dungeonChance; ++j2)
	            {
	                int i3 = this.rand.nextInt(16) + 8;
	                int l3 = this.rand.nextInt(256);
	                int l1 = this.rand.nextInt(16) + 8;
	                (new WorldGenDungeons()).generate(this.world, this.rand, blockpos.add(i3, l3, l1));
	            }
	        }
        }

        if (this.decorate) {
        	biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
        }
        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS))
        WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
        blockpos = blockpos.add(8, 0, 8);

        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE))
        {
        for (int k2 = 0; k2 < 16; ++k2)
        {
            for (int j3 = 0; j3 < 16; ++j3)
            {
                BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
                BlockPos blockpos2 = blockpos1.down();

                if (this.world.canBlockFreezeWater(blockpos2))
                {
                    this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
                }

                if (this.world.canSnowAt(blockpos1, true))
                {
                    this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
                }
            }
        }
        }//Forge: End ICE

        placeFillerBlocks(blockpos);
        
        setWalls(blockpos);
        
       // Main.LOGGER.info("spawning structure\n");

        this.customStructureTest.generateStructure(this.world, this.rand, chunkpos);

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);

        BlockFalling.fallInstantly = false;
    }
	
	@Override
    public void setBlocksInChunkFlat(int height, ChunkPrimer primer) {
    	for (int i = 0; i < 16; i++) {
    		for (int j = height; j > 0; j--) {
    			for (int k = 0; k < 16; k++) {
    				primer.setBlockState(i, j, k, STONE);
    			}
    		}
    	}
    }
	
	@Override
	protected void generateHeightmap(int p_185978_1_, int p_185978_2_, int p_185978_3_) {
		
        int i = 0;
        int j = 0;

        for (int k = 0; k < 5; ++k)
        {
            for (int l = 0; l < 5; ++l)
            {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                int i1 = 2;
                Biome biome = this.biomesForGeneration[k + 2 + (l + 2) * 10];

                for (int j1 = -2; j1 <= 2; ++j1)
                {
                    for (int k1 = -2; k1 <= 2; ++k1)
                    {
                        Biome biome1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
                        if (!(biome1 instanceof BiomeWasteland)) {
                        	biome1 = biome;
                        }
                        float f5 = this.settings.biomeDepthOffSet + biome1.getBaseHeight() * this.settings.biomeDepthWeight;
                        float f6 = this.settings.biomeScaleOffset + biome1.getHeightVariation() * this.settings.biomeScaleWeight;

                        if (this.terrainType == WorldType.AMPLIFIED && f5 > 0.0F)
                        {
                            f5 = 1.0F + f5 * 2.0F;
                            f6 = 1.0F + f6 * 4.0F;
                        }

                        float f7 = this.biomeWeights[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);

                        if (biome1.getBaseHeight() > biome.getBaseHeight())
                        {
                            f7 /= 2.0F;
                        }

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }

                f2 = f2 / f4;
                f3 = f3 / f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;

                ++j;
                double d8 = (double)f3;
                double d9 = (double)f2;
                d8 = d8 * (double)this.settings.baseSize / 8.0D;
                double d0 = (double)this.settings.baseSize + d8 * 4.0D;

                for (int l1 = 0; l1 < 33; ++l1)
                {
                    double d1 = ((double)l1 - d0) * (double)this.settings.stretchY * 128.0D / 256.0D / d9;

                    if (d1 < 0.0D)
                    {
                        d1 *= 4.0D;
                    }

                    double d5 = -d1;

                    if (l1 > 29)
                    {
                        double d6 = (double)((float)(l1 - 29) / 3.0F);
                        d5 = d5 * (1.0D - d6) + -10.0D * d6;
                    }
                    
                    this.heightMap[i] = d5;
                    ++i;
                }
            }	
        }
	}
        
	
	private void setFillerBlock(BlockPos blockpos) {
		
		Biome biome = world.getBiome(blockpos);
		
		if (blockpos.getY() > getRoofHeight() - 1) {
			if (biome instanceof BiomeMaze) {
	        	BiomeMaze biomeroom = (BiomeMaze) biome;
	        	FILLER = biomeroom.roofBlocks;
	        }
	        
	        else {
	        	FILLER = DEFAULT_FILLER;
	        }		
		}
		else if (blockpos.getY() > getFloorHeight() - 1) {
	        if (biome instanceof BiomeMaze) {
	        	BiomeMaze biomeroom = (BiomeMaze) biome;
	        	FILLER = biomeroom.wallBlocks;
	        }
	        
	        else {
	        	FILLER = DEFAULT_FILLER;
	        }		
		}
		else if (blockpos.getY() == getFloorHeight() - 1) {
	        if (biome instanceof BiomeMaze) {
	        	BiomeMaze biomeroom = (BiomeMaze) biome;
	        	FILLER = biomeroom.floorBlocks;
	        }
	        
	        else {
	        	FILLER = DEFAULT_FILLER;
	        }		
		}
		else {
	        if (biome instanceof BiomeMaze) {
	        	BiomeMaze biomeroom = (BiomeMaze) biome;
	        	FILLER = biomeroom.groundBlocks;
	        }
	        
	        else {
	        	FILLER = DEFAULT_FILLER;
	        }
		}
		
		if (FILLER.isEmpty()) {
			System.out.println("Unable to Get FILLER");
			FILLER = DEFAULT_FILLER;
		}
		
	}

	@Override
	public void placeFillerBlocks(BlockPos blockpos) {
    	for (int k = 0; k < 16; k++) {
	    	for (int i = 0; i < 16; i++) {
		    	for (int j = 255; j > 0; j--) {
		    		BlockPos pos = blockpos.add(i, j, k);
		    		setFillerBlock(pos);
		    		try {
		    			IBlockState state = this.world.getBlockState(pos);
		    			if (state == STONE) {
		    				IBlockState blockstate;
    						blockstate = FILLER.get(rand.nextInt(FILLER.size()));
	    					this.world.setBlockState(pos, blockstate);
	    				}
		    		}
		    		catch (Exception e) {
		    			System.out.println("Error with j = " + j + " and i = " + i + " and k = " + k);
		    		}
		    	}
	    	}
    	}
	}
	
	private void setWalls(BlockPos blockpos) {
		
		int data = rand.nextInt(524289);
		
		BlockPos pos = new BlockPos(blockpos);
		
		final IBlockState AIR = Blocks.AIR.getDefaultState();
				
		for (int l = 0; l < 9; l++) {
			for (int w = -1; w <= 1; w++) {
				for (int j = getFloorHeight(); j < getRoofHeight(); j++) {
					if ((data & 1) == 1) {
						setBlockState(pos.add(l, j, w), AIR);
					}
					if ((data & 2) == 2) {
						setBlockState(pos.add(w, j, l), AIR);
					}
					if ((data & 4) == 4) {
						setBlockState(pos.add(l, j, w + 4), AIR);
					}
					if ((data & 8) == 8) {
						setBlockState(pos.add(w + 4, j, l), AIR);
					}
					if ((data & 16) == 16) {
						setBlockState(pos.add(l, j, w + 8), AIR);
					}
					if ((data & 32) == 32) {
						setBlockState(pos.add(w + 8, j, l), AIR);
					}
					if ((data & 64) == 64) {
						setBlockState(pos.add(l + 8, j, w), AIR);
					}
					if ((data & 128) == 128) {
						setBlockState(pos.add(w, j, l + 8), AIR);
					}
					if ((data & 256) == 256) {
						setBlockState(pos.add(l, j, w + 12), AIR);
					}
					if ((data & 512) == 512) {
						setBlockState(pos.add(w + 12, j, l), AIR);
					}
					if ((data & 1024) == 1024) {
						setBlockState(pos.add(l + 8, j, w + 4), AIR);
					}
					if ((data & 2048) == 2048) {
						setBlockState(pos.add(w + 4, j, l + 8), AIR);
					}
					if ((data & 4096) == 4096) {
						setBlockState(pos.add(l + 8, j, w + 8), AIR);
					}
					if ((data & 8192) == 8192) {
						setBlockState(pos.add(w + 8, j, l + 8), AIR);
					}
					if ((data & 16384) == 16384) {
						setBlockState(pos.add(l, j, w + 16), AIR);
					}
					if ((data & 32768) == 32768) {
						setBlockState(pos.add(w + 16, j, l), AIR);
					}
					if ((data & 65536) == 65536) {
						setBlockState(pos.add(l + 8, j, w + 12), AIR);
					}
					if ((data & 131072) == 131072) {
						setBlockState(pos.add(w + 12, j, l + 8), AIR);
					}
					if ((data & 262144) == 262144) {
						setBlockState(pos.add(l + 8, j, w + 16), AIR);
					}
					if ((data & 524288) == 524288) {
						setBlockState(pos.add(w + 16, j, l + 8), AIR);
					}
				}
			}
		}
	}
	
	private void setBlockState(BlockPos pos, IBlockState state) {
		if (customStructureTest.isPositionInStructure(world, pos)) {
			return;
		}
/*		
		if (state == Blocks.AIR.getDefaultState() && this.rand.nextInt(100) == 0 && pos.getY() == floorHeight && world.getBiome(pos) instanceof BiomeMaze) {
			BiomeMaze biome = (BiomeMaze) world.getBiome(pos);
			ResourceLocation lootTable = biome.getLootTable();
			
			if (lootTable != null) {
				IBlockState state2 = Blocks.CHEST.getDefaultState();
				this.world.setBlockState(pos, state2);
				
				TileEntityChest chest = (TileEntityChest) this.world.getTileEntity(pos);
				chest.setLootTable(lootTable, this.rand.nextLong());
			}
		}
//*/
		world.setBlockState(pos, state);
	}

	public int getFloorHeight() {
		return floorHeight;
	}

	public int getRoofHeight() {
		return roofHeight;
	}
}
