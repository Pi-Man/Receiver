package com.PiMan.RecieverMod.Entity.ai;

import com.PiMan.RecieverMod.Entity.EntityTurret;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.world.WorldServer;

public class EntityAITurretSearch extends EntityAIBase {
	
	private EntityTurret turret;
	private float rotation;
	
	public EntityAITurretSearch(EntityTurret turret, float rotation) {
		this.turret = turret;
		this.rotation = rotation;
		this.setMutexBits(7);
	}

	@Override
	public boolean shouldExecute() {
				
		if (this.turret == null) {
			return false;
		}
		
		if (this.turret.getAttackTarget() != null) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public void updateTask() {
				
		float f1 = this.turret.rotationYawHead + rotation;

		this.turret.renderYawOffset += rotation;
	
		this.turret.setRotationYawHead(f1);
		
	}
	
	@Override
	public void resetTask() {
		
	}

}
