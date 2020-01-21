package com.PiMan.RecieverMod.Items.bullets;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.util.TransformationBuilder;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class ItemBulletShotgun extends ItemBullet {

	private final float gauge;
	private final float velocity;
	private final int pellets;
	
	public ItemBulletShotgun(String name, float gauge, float velocity, int pellets) {
		super(name, "shotgun_shell");
		this.gauge = gauge;
		this.velocity = velocity;
		this.pellets = pellets;
	}

	@Override
	public void fire(World world, EntityPlayer player, float entityAccuracy, float gunAccuracy, int life) {
    	if (!world.isRemote) {
    		
    		TransformationBuilder transform = new TransformationBuilder().add(null, new Vector3f(player.rotationPitch, player.rotationYaw, 0), null, null, 0);
    		Matrix4f m1 = TRSRTransformation.blockCornerToCenter(transform.build()).getMatrix();
        	
    		for (int i = 0; i < pellets; i++) {
	    		EntityBullet bullet = new EntityBullet(world, player, life);
	    		
				double a = rand.nextDouble() * (gunAccuracy + entityAccuracy) / 360 * Math.PI;
				double b = rand.nextDouble() * Math.PI * 2;
				
				float z = (float) (velocity * Math.cos(a));
				float y = (float) (velocity * Math.sin(a) * Math.sin(b));
				float x = (float) (velocity * Math.sin(a) * Math.cos(b));
	    		
	    		Matrix4f m2 = new Matrix4f();
	    		m2.setColumn(0, x, y, z, 1);
	    		m1.mul(m2);
	    		float[] floats = new float[4];
	    		m1.getColumn(0, floats);
	    		
	    		bullet.posX = player.posX;
	    		bullet.posY = player.posY + player.eyeHeight;
	    		bullet.posZ = player.posZ;
	    		
	    		bullet.motionX = floats[0];
	    		bullet.motionY = floats[1];
	    		bullet.motionZ = floats[2];
	    		
	    		bullet.setDamage(1 / gauge / pellets * velocity * velocity * 1.2f);
	    		
	    		world.spawnEntity(bullet);
    		}
            
            world.playSound(null, player.posX, player.posY, player.posZ, SoundsHandler.getSoundEvent(Sounds.COLT_1911_SHOT), SoundCategory.PLAYERS, 1, 1);
            
    	}
	}

}
