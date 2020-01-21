package com.PiMan.RecieverMod.Entity.ai;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.PiMan.RecieverMod.Entity.EntityTurret;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class EntityAITargetPlayerInFront extends EntityAIBase {
	
    private static final Logger LOGGER = LogManager.getLogger();
    /** The entity that use this AI */
    private final EntityLiving entityLiving;
    /** Use to determine if an entity correspond to specification */
    private final Predicate<Entity> predicate;
    /** Used to compare two entities */
    private final EntityAINearestAttackableTarget.Sorter sorter;
    /** The current target */
    private EntityLivingBase entityTarget;

    public EntityAITargetPlayerInFront(EntityLiving entityLivingIn)
    {
        this.entityLiving = entityLivingIn;

        this.predicate = new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                if (!(p_apply_1_ instanceof EntityPlayer))
                {
                    return false;
                }
                else if (((EntityPlayer)p_apply_1_).capabilities.disableDamage)
                {
                    return false;
                }
                else
                {
                    double d0 = EntityAITargetPlayerInFront.this.maxTargetRange();

                    if (p_apply_1_.isSneaking())
                    {
                        d0 *= 0.800000011920929D;
                    }

                    if (p_apply_1_.isInvisible())
                    {
                        float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();

                        if (f < 0.1F)
                        {
                            f = 0.1F;
                        }

                        d0 *= (double)(0.7F * f);
                    }

                    return (double)p_apply_1_.getDistance(EntityAITargetPlayerInFront.this.entityLiving) > d0 ? false : EntityAITarget.isSuitableTarget(EntityAITargetPlayerInFront.this.entityLiving, (EntityLivingBase)p_apply_1_, false, true);
                }
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(entityLivingIn);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        double d0 = this.maxTargetRange();
        List<EntityPlayer> list = this.entityLiving.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, this.entityLiving.getEntityBoundingBox().grow(d0, 4.0D, d0), this.predicate);
        Collections.sort(list, this.sorter);

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            for (EntityPlayer player : list) {
            	if (isInSight(player)) {
            		return true;
            	}
            }
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        EntityLivingBase entitylivingbase = this.entityLiving.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage)
        {
            return false;
        }
        else
        {
            Team team = this.entityLiving.getTeam();
            Team team1 = entitylivingbase.getTeam();

            if (team != null && team1 == team)
            {
                return false;
            }
            else
            {
                double d0 = this.maxTargetRange();

                if (this.entityLiving.getDistanceSq(entitylivingbase) > d0 * d0)
                {
                    return false;
                }
                else
                {
                    return (!(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative()) && isInSight((EntityPlayer)entitylivingbase);
                }
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entityLiving.setAttackTarget(this.entityTarget);
        
        if (this.entityLiving instanceof EntityTurret) {
        	EntityTurret turret = (EntityTurret) this.entityLiving;
        	
        	turret.onTargetFound();
        	
        }
        
        super.startExecuting();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.entityLiving.setAttackTarget((EntityLivingBase)null);
        if (this.entityLiving instanceof EntityTurret) {
        	EntityTurret turret = (EntityTurret) this.entityLiving;
        	
        	turret.onTargetLost();
        	
        }
        super.startExecuting();
    }

    /**
     * Return the max target range of the entiity (16 by default)
     */
    protected double maxTargetRange()
    {
        IAttributeInstance iattributeinstance = this.entityLiving.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
    
    private boolean isInSight(EntityPlayer player) {
    	
    	double x = player.posX - this.entityLiving.posX;
    	double z = player.posZ - this.entityLiving.posZ;
    	    	
    	double angle = Math.atan2(-x, z) * 180 / Math.PI;
    	
    	if (Math.abs(angle - this.entityLiving.rotationYawHead) < 30 && this.entityLiving.getEntitySenses().canSee(player)) {
    		this.entityTarget = player;
    		return true;
    	}
    	    	
    	return false;
    }
	
}
