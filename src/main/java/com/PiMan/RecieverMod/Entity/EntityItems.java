package com.PiMan.RecieverMod.Entity;

import java.util.List;
import java.util.Random;

import com.PiMan.RecieverMod.World.Biomes.BiomeMaze;
import com.PiMan.RecieverMod.World.Gen.ChunkGeneratorMaze;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class EntityItems extends EntityMob {
	
	public EntityItems(World worldIn) {
		super(worldIn);
	}
	
	@Override
	public void onUpdate() {
		this.setDead();
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {		
		
		BiomeMaze biome = (BiomeMaze) world.getBiome(new BlockPos(this));
		ResourceLocation lootTable = biome.getLootTable();
		
        if (lootTable != null && !world.isRemote)
        {
            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(lootTable);
            lootTable = null;
            Random random = new Random();
                
            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);

            List<ItemStack> items = loottable.generateLootForPools(random, lootcontext$builder.build());
            
            for (ItemStack stack : items) {
            	EntityItem itementity = new EntityItem(this.world, this.posX, this.posY, this.posZ, stack);
            	itementity.getEntityData().setBoolean("NoDespawn", true);
            	this.world.spawnEntity(itementity);
            }
        }
        
        return livingdata;
	}
	
	@Override
	public boolean getCanSpawnHere() {
		IChunkGenerator generator = ((ChunkProviderServer)world.getChunkProvider()).chunkGenerator;
		
		if (generator instanceof ChunkGeneratorMaze) {
			ChunkGeneratorMaze mazeGenerator = (ChunkGeneratorMaze) generator;
			return this.posY > mazeGenerator.getFloorHeight() - 1 && this.posY < mazeGenerator.getRoofHeight() - 1;
		}
		else {
			return false;
		}
	}
	
	@Override
	public int getMaxSpawnedInChunk() {
		return 4;
	}

}
