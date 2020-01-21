package com.PiMan.RecieverMod.Entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityGrenade extends EntityThrowable {

	public EntityGrenade(World worldIn) {
		super(worldIn);
	}

	public EntityGrenade(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityGrenade(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}
		
	public void move(double x, double y, double z)
    {
        double d0 = y;
        double origX = x;
        double origZ = z;

       
        List<AxisAlignedBB> list = this.world.getCollisionBoxes((Entity)null, this.getEntityBoundingBox().expand(x, y, z));

        for (AxisAlignedBB axisalignedbb : list)
        {
            y = axisalignedbb.calculateYOffset(this.getEntityBoundingBox(), y);
        }

        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));

        for (AxisAlignedBB axisalignedbb1 : list)
        {
            x = axisalignedbb1.calculateXOffset(this.getEntityBoundingBox(), x);
        }

        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0D, 0.0D));

        for (AxisAlignedBB axisalignedbb2 : list)
        {
            z = axisalignedbb2.calculateZOffset(this.getEntityBoundingBox(), z);
        }

        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, z));

        this.resetPositionToBB();
        this.onGround = d0 != y && d0 < 0.0D;
        
        if (d0 != y) {
        	this.motionY *= -0.1D;
        }

        if (origX != x)
        {
            this.motionX *= -0.1D;
        }

        if (origZ != z)
        {
            this.motionZ *= -0.1D;
        }
    }
	
	@Override
	public void onUpdate() {
		
		this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= getGravityVelocity();
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        
        if (this.onGround) {
        	this.motionX *= 0.9;
        	this.motionZ *= 0.9;
        }
        
        onEntityUpdate();
	}

	public void resetPositionToBB()
    {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
        this.posY = axisalignedbb.minY;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
    }

	@Override
	protected void onImpact(RayTraceResult result) {

	}
	
	@Override
	public void onEntityUpdate() {
				
		if (!world.isRemote) {
		
			NBTTagCompound nbt = this.getEntityData();
			
			nbt.setInteger("Time", nbt.getInteger("Time") - 1);
			
			if (nbt.getInteger("Time") < 0) {
				//System.out.println("Boom");
				this.world.createExplosion(this, posX, posY, posZ, 2, false);
				for (int i = 0; i < 500; i++) {
					EntityBullet frag = new EntityBullet(world, posX, posY, posZ);
					
					frag.shootingEntity = this.getThrower();
					
					double a = Math.acos(rand.nextDouble()*2 - 1);
					double b = rand.nextDouble() * Math.PI * (onGround ? 1D : 2D);
					
					double x = Math.cos(a);
					double y = Math.sin(a)*Math.sin(b);
					double z = Math.sin(a)*Math.cos(b);
					
					frag.shoot(x, y, z, 1, 0);
					
					frag.setDamage(5D);
					
					world.spawnEntity(frag);
				}
				this.setDead();
			}
		}
	}
	
}
