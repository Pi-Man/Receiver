package com.PiMan.RecieverMod.World.Gen.Structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.World.Gen.ChunkGeneratorMaze;
import com.PiMan.RecieverMod.init.BiomeInit;
import com.PiMan.RecieverMod.util.Reference;
import com.mojang.realmsclient.dto.WorldTemplate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.StructureEndCityPieces;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class WorldGenStructureComponentTest {
	
    public static void registerScatteredFeaturePieces()
    {
        MapGenStructureIO.registerStructureComponent(WorldGenStructureComponentTest.Tower.class, "WTower");
    }
    
    public static class Tower extends Feature
    {
    	private String name;
        private Rotation rotation;
        /** Whether this template should overwrite existing blocks. Replaces only air if false. */
        private boolean overwrite;
        
        private static Map<Biome, IBlockState> baseBlocks = new HashMap<>();
        static {
        	baseBlocks.put(BiomeInit.WASTELAND, Blocks.SANDSTONE.getStateFromMeta(2));
        	baseBlocks.put(BiomeInit.ABANDONED, Blocks.STONEBRICK.getStateFromMeta(2));
        	baseBlocks.put(BiomeInit.OLD, Blocks.STONEBRICK.getStateFromMeta(0));
        	baseBlocks.put(BiomeInit.NEW, Blocks.CONCRETE.getStateFromMeta(8));
        }
        
        private static Map<Biome, IBlockState> stairBlocks = new HashMap<>();
        static {
        	stairBlocks.put(BiomeInit.WASTELAND, Blocks.SANDSTONE_STAIRS.getDefaultState());
        	stairBlocks.put(BiomeInit.ABANDONED, Blocks.STONE_STAIRS.getDefaultState());
        	stairBlocks.put(BiomeInit.OLD, Blocks.STONE_BRICK_STAIRS.getDefaultState());
        	stairBlocks.put(BiomeInit.NEW, Blocks.QUARTZ_STAIRS.getDefaultState());
        }
        
        private static Map<Biome, IBlockState> stairFloorBlocks = new HashMap<>();
        static {
        	stairFloorBlocks.put(BiomeInit.WASTELAND, Blocks.SANDSTONE.getStateFromMeta(0));
        	stairFloorBlocks.put(BiomeInit.ABANDONED, Blocks.COBBLESTONE.getDefaultState());
        	stairFloorBlocks.put(BiomeInit.OLD, Blocks.STONEBRICK.getStateFromMeta(0));
        	stairFloorBlocks.put(BiomeInit.NEW, Blocks.CONCRETE.getStateFromMeta(0));
        }
        
        private static Map<Biome, IBlockState> highlightBlocks = new HashMap<>();
        static {
        	highlightBlocks.put(BiomeInit.WASTELAND, Blocks.SANDSTONE.getStateFromMeta(1));
        	highlightBlocks.put(BiomeInit.ABANDONED, Blocks.STONEBRICK.getStateFromMeta(1));
        	highlightBlocks.put(BiomeInit.OLD, Blocks.STONEBRICK.getStateFromMeta(3));
        	highlightBlocks.put(BiomeInit.NEW, Blocks.CONCRETE.getStateFromMeta(0));
        }
        
        private static Map<Biome, IBlockState> floorHighlightBlocks = new HashMap<>();
        static {
        	floorHighlightBlocks.put(BiomeInit.WASTELAND, Blocks.SAND.getDefaultState());
        	floorHighlightBlocks.put(BiomeInit.ABANDONED, Blocks.STONEBRICK.getStateFromMeta(2));
        	floorHighlightBlocks.put(BiomeInit.OLD, Blocks.STONEBRICK.getStateFromMeta(0));
        	floorHighlightBlocks.put(BiomeInit.NEW, Blocks.CONCRETE.getStateFromMeta(0));
        }
        
        private static Map<Biome, IBlockState> slabBlocks = new HashMap<>();
        static {
        	slabBlocks.put(BiomeInit.WASTELAND, Blocks.STONE_SLAB.getStateFromMeta(1 + 8));
        	slabBlocks.put(BiomeInit.ABANDONED, Blocks.STONE_SLAB.getStateFromMeta(3 + 8));
        	slabBlocks.put(BiomeInit.OLD, Blocks.STONE_SLAB.getStateFromMeta(0 + 8));
        	slabBlocks.put(BiomeInit.NEW, Blocks.STONE_SLAB.getStateFromMeta(7 + 8));
        }

        public Tower()
        {
        }

        public Tower(TemplateManager templateManager, String name, BlockPos pos, Rotation rot, boolean overwriteIn)
        {
            super(0);
            this.name = name;
            this.templatePosition = pos;
            this.rotation = rot;
            this.overwrite = overwriteIn;
            this.loadTemplate(templateManager, name);
        }
        
        @Override
        protected void setup(Template templateIn, BlockPos pos, PlacementSettings settings) {
        	super.setup(templateIn, pos, settings);
        	switch(this.placeSettings.getRotation()) {
        	case NONE:
        		this.boundingBox.maxX -= 1;
        		this.boundingBox.maxZ -= 1;
        		break;
			case CLOCKWISE_180:
        		this.boundingBox.minX += 1;
        		this.boundingBox.minZ += 1;
				break;
			case CLOCKWISE_90:
        		this.boundingBox.minX += 1;
        		this.boundingBox.maxZ -= 1;
				break;
			case COUNTERCLOCKWISE_90:
        		this.boundingBox.maxX -= 1;
        		this.boundingBox.minZ += 1;
				break;
			default:
				break;
        	}
        }

        private void loadTemplate(TemplateManager templateManager, String name)
        {
            Template template = templateManager.getTemplate((MinecraftServer)null, new ResourceLocation(Reference.MOD_ID, name));
            PlacementSettings placementsettings = new PlacementSettings().setRotation(this.rotation);
            this.setup(template, this.templatePosition, placementsettings);
        }
        
        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
    		this.placeSettings.setBoundingBox(structureBoundingBoxIn);
        	if (this.name.equals("wasteland_tower")) {
                TemplateManager manager = worldIn.getSaveHandler().getStructureTemplateManager();
                Template template1 = manager.getTemplate(worldIn.getMinecraftServer(), new ResourceLocation(Reference.MOD_ID, name + "/" + name + "_base"));
                Template template2 = manager.getTemplate(worldIn.getMinecraftServer(), new ResourceLocation(Reference.MOD_ID, name + "/" + name + "_body"));
                Template template3 = manager.getTemplate(worldIn.getMinecraftServer(), new ResourceLocation(Reference.MOD_ID, name + "/" + name + "_top"));
                BlockPos pos = new BlockPos(templatePosition);
                Biome biome = worldIn.getBiome(pos);
                template1.addBlocksToWorld(worldIn, pos, new PlacementProcessor(pos, placeSettings, biome), placeSettings, 2);
                pos = pos.add(0, template1.getSize().getY(), 0);
                template2.addBlocksToWorld(worldIn, pos, new PlacementProcessor(pos, placeSettings, biome), placeSettings, 2);
                pos = pos.add(0, template2.getSize().getY(), 0);
                template2.addBlocksToWorld(worldIn, pos, new PlacementProcessor(pos, placeSettings, biome), placeSettings, 2);
                pos = pos.add(0, template2.getSize().getY(), 0);
                template2.addBlocksToWorld(worldIn, pos, new PlacementProcessor(pos, placeSettings, biome), placeSettings, 2);
                pos = pos.add(0, template2.getSize().getY(), 0);
                template2.addBlocksToWorld(worldIn, pos, new PlacementProcessor(pos, placeSettings, biome), placeSettings, 2);
                pos = pos.add(0, template2.getSize().getY(), 0);
                template3.addBlocksToWorld(worldIn, pos, new PlacementProcessor(pos, placeSettings, biome), placeSettings, 2);
                return true;
        	}
            this.template.addBlocksToWorld(worldIn, this.templatePosition, this.placeSettings, 18);
            Map<BlockPos, String> map = this.template.getDataBlocks(this.templatePosition, this.placeSettings);

            for (Entry<BlockPos, String> entry : map.entrySet())
            {
                String s = entry.getValue();
                this.handleDataMarker(s, entry.getKey(), worldIn, randomIn, structureBoundingBoxIn);
            }

            return true;
        }

        /**
         * (abstract) Helper method to write subclass data to NBT
         */
        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setString("name", this.name);
            tagCompound.setString("Rot", this.rotation.name());
            tagCompound.setBoolean("OW", this.overwrite);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager manager)
        {
            super.readStructureFromNBT(tagCompound, manager);
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.overwrite = tagCompound.getBoolean("OW");
            this.name = tagCompound.getString("name");
            this.loadTemplate(manager, this.name);
        }
        
        protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb)
        {
            if (function.startsWith("Chest"))
            {
                BlockPos blockpos = pos.down();

                if (sbb.isVecInside(blockpos))
                {
                    TileEntity tileentity = worldIn.getTileEntity(blockpos);

                    if (tileentity instanceof TileEntityChest)
                    {
                        ((TileEntityChest)tileentity).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, rand.nextLong());
                    }
                }
            }
        }
        
    	private class PlacementProcessor implements ITemplateProcessor {
    		
    		private float chance;
    		private Random rand;
    		private Biome biome;
    		
    		public PlacementProcessor(BlockPos pos, PlacementSettings settings, Biome biome) {
    			this.chance = settings.getIntegrity();
    			this.rand = settings.getRandom(pos);
    			this.biome = biome;
    		}
    	
    		@Override
    		public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) {
    			
    			IBlockState state = blockInfoIn.blockState;
    			
    			if (state.equals(Blocks.SANDSTONE.getStateFromMeta(2))) {
    				IBlockState state2 = baseBlocks.get(biome);
    				if (state2 != null) {
    					blockInfoIn = new Template.BlockInfo(blockInfoIn.pos, state2, blockInfoIn.tileentityData);
    				}
    			}
    			else if (state.getBlock().equals(Blocks.SANDSTONE_STAIRS)) {
    				IBlockState state2 = stairBlocks.get(biome);
    				if (state2 != null) {
    					state2 = state2.getBlock().getStateFromMeta(state.getBlock().getMetaFromState(state));
    					blockInfoIn = new Template.BlockInfo(blockInfoIn.pos, state2, blockInfoIn.tileentityData);
    				}    			
				}
    			else if (state.equals(Blocks.SANDSTONE.getStateFromMeta(0))) {
    				IBlockState state2 = stairFloorBlocks.get(biome);
    				if (state2 != null) {
    					blockInfoIn = new Template.BlockInfo(blockInfoIn.pos, state2, blockInfoIn.tileentityData);
    				}    			
				}
    			else if (state.equals(Blocks.SANDSTONE.getStateFromMeta(1))) {
    				IBlockState state2 = highlightBlocks.get(biome);
    				if (state2 != null) {
    					blockInfoIn = new Template.BlockInfo(blockInfoIn.pos, state2, blockInfoIn.tileentityData);
    				}
				}
    			else if (state.equals(Blocks.SAND.getDefaultState())) {
    				IBlockState state2 = floorHighlightBlocks.get(biome);
    				if (state2 != null) {
    					blockInfoIn = new Template.BlockInfo(blockInfoIn.pos, state2, blockInfoIn.tileentityData);
    				}
				}
    			else if (state.equals(Blocks.STONE_SLAB.getStateFromMeta(1 + 8))) {
    				IBlockState state2 = slabBlocks.get(biome);
    				if (state2 != null) {
    					blockInfoIn = new Template.BlockInfo(blockInfoIn.pos, state2, blockInfoIn.tileentityData);
    				}
    			}
    			
                return this.chance < 1.0F && this.rand.nextFloat() > this.chance ? new Template.BlockInfo(blockInfoIn.pos, Blocks.AIR.getDefaultState(), blockInfoIn.tileentityData) : blockInfoIn;
    		}
    		
    	}
        
    }
	
}

abstract class Feature extends StructureComponentTemplate
{
    /** The size of the bounding box for this feature in the X axis */
    protected int width;
    /** The size of the bounding box for this feature in the Y axis */
    protected int height;
    /** The size of the bounding box for this feature in the Z axis */
    protected int depth;
    protected int horizontalPos = -1;

    public Feature()
    {
    }

    protected Feature(Random rand, int x, int y, int z, int sizeX, int sizeY, int sizeZ)
    {
        super(0);
        this.width = sizeX;
        this.height = sizeY;
        this.depth = sizeZ;
        this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));

        if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z)
        {
            this.boundingBox = new StructureBoundingBox(x, y, z, x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1);
        }
        else
        {
            this.boundingBox = new StructureBoundingBox(x, y, z, x + sizeZ - 1, y + sizeY - 1, z + sizeX - 1);
        }
    }

    public Feature(int i) {
    	super(i);
	}

	/**
     * (abstract) Helper method to write subclass data to NBT
     */
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
    	super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("Width", this.width);
        tagCompound.setInteger("Height", this.height);
        tagCompound.setInteger("Depth", this.depth);
        tagCompound.setInteger("HPos", this.horizontalPos);
    }

    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
    {
    	super.readStructureFromNBT(tagCompound, p_143011_2_);
        this.width = tagCompound.getInteger("Width");
        this.height = tagCompound.getInteger("Height");
        this.depth = tagCompound.getInteger("Depth");
        this.horizontalPos = tagCompound.getInteger("HPos");
    }

    /**
     * Calculates and offsets this structure boundingbox to average ground level
     */
    protected boolean setHeightTo(World worldIn, StructureBoundingBox structurebb, int height)
    {
        if (this.horizontalPos >= 0)
        {
            return true;
        }
        else
        {
            this.horizontalPos = height;
            this.boundingBox.offset(0, height - this.boundingBox.minY, 0);
            return true;
        }
    }
}

