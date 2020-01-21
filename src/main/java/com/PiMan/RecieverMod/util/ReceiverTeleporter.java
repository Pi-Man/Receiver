package com.PiMan.RecieverMod.util;

import com.PiMan.RecieverMod.Main;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class ReceiverTeleporter extends Teleporter {
	
	private final WorldServer world;
	private double x, y, z;

	public ReceiverTeleporter(WorldServer worldIn, double x, double y, double z) {
		super(worldIn);
		this.world = worldIn;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw) {
		this.world.getBlockState(new BlockPos((int)this.x, (int)this.y, (int)this.z));
		entityIn.setPosition(x, y, z);
		entityIn.motionX = 0F;
		entityIn.motionY = 0F;
		entityIn.motionZ = 0F;
	}
	
	@Override
	public void placeEntity(World world, Entity entity, float yaw) {
		Main.LOGGER.info("Placing Entity");
		super.placeEntity(world, entity, yaw);
	}
	
	public static void teleportToDimension(EntityPlayer player, int dimension, double x, double y, double z) {
		int oldDimension = player.dimension;
		EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
		MinecraftServer server = player.getEntityWorld().getMinecraftServer();
		WorldServer worldServer = server.getWorld(dimension);
		if(worldServer == null || server == null) {
			throw new IllegalArgumentException("Dimension: " + dimension + " doesn't exist!");
		}
		worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(entityplayermp, dimension, new ReceiverTeleporter(worldServer, x, y, z));
		BlockPos pos = player.getPosition();
		if (dimension == 2) {		
			
			pos = pos.add(0, 58 - pos.getY(), 0);
			player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
			
			IBlockState state = worldServer.getBlockState(pos);
			
			generateworld(pos, worldServer);

			while (state.getBlock() != Blocks.AIR) {
				pos = pos.add(1, 0, 1);
				state = worldServer.getBlockState(pos);
			}
		}
		else {
			pos = worldServer.getTopSolidOrLiquidBlock(pos);
			BlockPos bedPos = player.getBedLocation(dimension);
			if (bedPos == null) {
				pos = worldServer.getSpawnPoint();
			}
			else {
				pos = player.getBedSpawnLocation(worldServer, bedPos, true);
			}
			
			while (!worldServer.isAirBlock(pos) || !worldServer.isAirBlock(pos.up())) {
				pos = pos.up();
			}
			
			player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());

		}
	}
	
	public static void generateworld(BlockPos blockpos, WorldServer worldserver) {
		
		Main.LOGGER.info("Generating Spawn Area");
		
        int i = 2;
        int k = 16 * i;
        int j = i * 2 + 1;
        int j1 = j * j;
        int i1 = 0;
        
        long k1 = MinecraftServer.getCurrentTimeMillis();

        for (int l1 = -k; l1 <= k; l1 += 16)
        {
            for (int i2 = -k; i2 <= k; i2 += 16) {

                long j2 = MinecraftServer.getCurrentTimeMillis();
            	
                if (j2 - k1 > 1000L)
                {
                    Main.LOGGER.info("{}%", i1 * 100 / j1);
                    k1 = j2;
                }
            	
                ++i1;
                worldserver.getChunkProvider().provideChunk(blockpos.getX() + l1 >> 4, blockpos.getZ() + i2 >> 4);
            }
        }
        
		Main.LOGGER.info("Done Generating Spawn Area");

	}

}
