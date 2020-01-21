package com.PiMan.RecieverMod.World.Gen.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.util.Reference;
import com.PiMan.RecieverMod.util.handlers.LootTables;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WorldGenStructure extends WorldGenerator implements IStructure {

	public String structureName;
	
	public PlacementSettings settings;
	
	private final ResourceLocation TOP, BODY, BASE, BTOP, BBODY, BBASE;
	
	private Template top, body, base, btop, bbody, bbase;
	
	private StructureBoundingBox size;
	
	public WorldGenStructure(String name, PlacementSettings settings) {
		this.structureName = name;
		this.settings = settings;
		TOP = new ResourceLocation(Reference.MOD_ID, name + "_tower_top");
		BODY = new ResourceLocation(Reference.MOD_ID, name + "_tower_body");
		BASE = new ResourceLocation(Reference.MOD_ID, name + "_tower_base");
		BTOP = new ResourceLocation(Reference.MOD_ID, name + "_basement_top");
		BBODY = new ResourceLocation(Reference.MOD_ID, name + "_basement_body");
		BBASE = new ResourceLocation(Reference.MOD_ID, name + "_basement_base");
		size = new StructureBoundingBox(settings.getBoundingBox());
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {

		checkTemplates();
		
		this.settings.getBoundingBox().offset(position.getX(), position.getY(), position.getZ());
		
		this.generateStructure(worldIn, position, new ChunkPos(position), rand);
		
		this.settings.setBoundingBox(new StructureBoundingBox(size));
		
		return true;
	}
	
	private void generateStructure(World world, BlockPos pos, ChunkPos chunkpos, Random rand) {
		
		PlacementSettings decaySettings = settings.copy().setIntegrity(0.6F);
		
		checkTemplates();
		
		int u = rand.nextInt(3) + 1;
		
		int d = rand.nextInt(3) + 2;
		
		this.placeBlocks(top, world, rand, pos.add(0, 12*u + 9, 0), settings, null);
		
		BlockPos blockpos;
		
		this.placeBlocks(btop, world, rand, pos.add(0, -3, 0), decaySettings, new DecayProcessor(pos.add(0, -3, 0), settings));
				
		for (int i = 0; i < d; i++) {
			blockpos = pos.add(0, -12*(i + 1) - 3, 0);
			this.placeBlocks(bbody, world, rand, blockpos, decaySettings, new DecayProcessor(blockpos, decaySettings));
		}
		
		this.placeBlocks(bbase, world, rand, pos.add(0, -12*d-6, 0), settings, null);

		this.placeBlocks(base, world, rand, pos, settings, null);
		
		for (int i = 0; i < u; i++) {
			blockpos = pos.add(0, 12*i + 9, 0);
			this.placeBlocks(body, world, rand, blockpos, settings, null);
		}
	}
	
	private void checkTemplates() {
		TemplateManager manager = worldserver.getStructureTemplateManager();
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (top == null) {
			top = manager.get(server, TOP);
		}
		if (body == null) {
			body = manager.get(server, BODY);
		}
		if (base == null) {
			base = manager.get(server, BASE);
		}
		if (btop == null) {
			btop = manager.get(server, BTOP);
		}
		if (bbody == null) {
			bbody = manager.get(server, BBODY);
		}
		if (bbase == null) {
			bbase = manager.get(server, BBASE);
		}
	}
	
	private void placeBlocks(Template template, World world, Random rand, BlockPos pos, PlacementSettings settings, ITemplateProcessor processor) {
		
		if (processor != null) {
			template.addBlocksToWorld(world, pos, processor, settings, 2);
		}
		else {
			template.addBlocksToWorld(world, pos, settings);
		}
		
        Map<BlockPos, String> map = template.getDataBlocks(pos, settings);

        for (Entry<BlockPos, String> entry : map.entrySet())
        {
            String s = entry.getValue();
            this.handleDataMarker(s, entry.getKey(), world, rand, settings.getBoundingBox());
        }
	}
	
    protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb)
    {
        if (function.equals("main"))
        {
            BlockPos blockpos = pos.down();
            
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);

            if (sbb.isVecInside(blockpos))
            {
                TileEntity tileentity = worldIn.getTileEntity(blockpos);

                if (tileentity instanceof TileEntityChest)
                {
                    ((TileEntityChest)tileentity).setLootTable(LootTables.TOWER_MAIN, rand.nextLong());
                }
            }
        }
        else if (function.equals("secondary")) {
            BlockPos blockpos = pos.down();
            
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);

            if (sbb.isVecInside(blockpos))
            {
                TileEntity tileentity = worldIn.getTileEntity(blockpos);

                if (tileentity instanceof TileEntityChest)
                {
                    ((TileEntityChest)tileentity).setLootTable(LootTableList.CHESTS_DESERT_PYRAMID, rand.nextLong());
                }
            }
        }
    }
    
    private class DecayProcessor implements ITemplateProcessor {
        private final float chance;
        private final Random random;

        public DecayProcessor(BlockPos pos, PlacementSettings settings) {
            this.chance = settings.getIntegrity();
            this.random = settings.getRandom(pos.add(pos.getY(), 0, pos.getY()));
        }

        @Nullable
        public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
            return this.chance < 1.0F && this.random.nextFloat() > this.chance ? new Template.BlockInfo(blockInfoIn.pos, Blocks.AIR.getDefaultState(), blockInfoIn.tileentityData) : blockInfoIn;
        }
    }
}
