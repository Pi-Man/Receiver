package com.PiMan.RecieverMod.World.Dimensions.Maze;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.World.Biomes.BiomeProviderMaze;
import com.PiMan.RecieverMod.World.Gen.ChunkGeneratorMaze;
import com.PiMan.RecieverMod.World.Gen.ChunkGeneratorReceiver;
import com.PiMan.RecieverMod.init.BiomeInit;
import com.PiMan.RecieverMod.init.DimensionInit;
import com.PiMan.RecieverMod.util.ReceiverTeleporter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;

public class DimensionMaze extends WorldProvider {
	
	private static final int SPAWNRADIUS = 2000;
	
	@Override
	protected void init() {
        this.hasSkyLight = true;
		this.biomeProvider = Main.proxy.RECEIVER.getBiomeProvider(world);
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionInit.MAZE;
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkGeneratorMaze(world, getSeed(), Main.proxy.RECEIVER);
	}
	
	@Override
	public BlockPos getRandomizedSpawnPoint() {
		BlockPos pos;
		do {
			pos = new BlockPos(world.rand.nextInt(SPAWNRADIUS*2) - SPAWNRADIUS, 58, world.rand.nextInt(SPAWNRADIUS*2) - SPAWNRADIUS);
		} while (!biomeProvider.getBiomesToSpawnIn().contains(world.getBiome(pos)));
		
		if (!world.isAreaLoaded(pos, 32)) {
			ReceiverTeleporter.generateworld(pos, (WorldServer) world);
		}
		
		IBlockState state = world.getBlockState(pos);
		
		while (state.getBlock() != Blocks.AIR) {
			pos = pos.add(1, 0, 1);
			state = world.getBlockState(pos);
		}
		
		return pos;
	}
	
	@Override
	public boolean canRespawnHere() {
		return true;
	}
	
	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

}