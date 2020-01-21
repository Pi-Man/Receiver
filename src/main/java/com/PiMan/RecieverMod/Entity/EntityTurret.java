package com.PiMan.RecieverMod.Entity;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Entity.ai.EntityAIAttackRangedLOS;
import com.PiMan.RecieverMod.Entity.ai.EntityAITargetPlayerInFront;
import com.PiMan.RecieverMod.Entity.ai.EntityAITurretSearch;
import com.PiMan.RecieverMod.World.Dimensions.Maze.DimensionMaze;
import com.PiMan.RecieverMod.World.Gen.ChunkGeneratorMaze;
import com.PiMan.RecieverMod.util.handlers.LootTables;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;
import com.google.common.base.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;

public class EntityTurret extends EntityMob implements IRangedAttackMob {
	
	private ISound sound;

	public EntityTurret(World worldIn) {
		super(worldIn);
		this.setSize(1, 1.05F);
		if (worldIn.isRemote) {
			Main.proxy.playLoopingEntitySound(this, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.minecart.riding")), SoundCategory.HOSTILE);
		}
	}
	
	@Override
	protected void initEntityAI() {
		
		this.tasks.taskEntries.clear();
		
		this.tasks.addTask(2, new EntityAIAttackRangedLOS(this, 0D, 2, 30));
		this.tasks.addTask(1, new EntityAITurretSearch(this, 3F));
        this.tasks.addTask(0, new EntityAITargetPlayerInFront(this));
	}
	
	@Override
	public float getEyeHeight() {
		return 0.9875F;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100D);
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		while (this.rotationYawHead >= 180) {
			this.rotationYawHead -= 360;
		}
		while (this.rotationYawHead < -180) {
			this.rotationYawHead += 360;
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
				
		if (!world.isRemote) {
			//System.out.println("Shooting bullet");

			EntityBullet bullet = new EntityBullet(world, this, 1200);
			
			bullet.shoot(this, this.rotationPitch, this.rotationYawHead, 0F, 7.7F*3F, 1);
			bullet.setDamage(10F);
			bullet.setKnockbackStrength(0);
			world.spawnEntity(bullet);
		}
		
		world.playSound(null, this.posX, this.posY, this.posZ, SoundsHandler.getSoundEvent(Sounds.COLT_1911_SHOT), SoundCategory.HOSTILE, 1F, 1F);
		
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		
	}
	
	@Override
	public boolean canBeHitWithPotion() {
		return false;
	}
	
	@Override
	public boolean canPickUpLoot() {
		return false;
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return null; //LootTables.ENTITIES_TURRET;
	}
	
	@Override
	public boolean getCanSpawnHere() {
		IChunkGenerator generator = ((ChunkProviderServer)world.getChunkProvider()).chunkGenerator;
		
		if (generator instanceof ChunkGeneratorMaze) {
			ChunkGeneratorMaze mazeGenerator = (ChunkGeneratorMaze) generator;
			return this.rand.nextInt(3) == 0 && this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.posY > mazeGenerator.getFloorHeight() - 1 && this.posY < mazeGenerator.getRoofHeight() - 1;
		}
		else {
			return false;
		}
	}
	
	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}
	
	public void onTargetFound() {
		world.playSound(null, posX, posY, posZ, SoundsHandler.getSoundEvent(Sounds.TURRET_TARGET), SoundCategory.HOSTILE, 1, 1.0F);
	}
	
	public void onTargetLost() {
		world.playSound(null, posX, posY, posZ, SoundsHandler.getSoundEvent(Sounds.TURRET_TARGET), SoundCategory.HOSTILE, 1, 0.75F);
	}
	
}
