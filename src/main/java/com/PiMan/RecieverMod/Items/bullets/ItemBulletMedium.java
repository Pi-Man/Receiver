package com.PiMan.RecieverMod.Items.bullets;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.util.TransformationBuilder;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class ItemBulletMedium extends ItemBullet {
	
	private final float caliber;
	private final float velocity;

	public ItemBulletMedium(String name, float caliber, float velocity, String model) {
		super(name, model);
		this.caliber = caliber;
		this.velocity = velocity;
	}

	@Override
	public void fire(World world, EntityPlayer player, float entityAccuracy, float gunAccuracy, int life) {
    	if (!world.isRemote) {
        	
    		EntityBullet bullet = new EntityBullet(world, player, life);
    		
			double a = rand.nextDouble() * (gunAccuracy + entityAccuracy) / 360 * Math.PI;
			double b = rand.nextDouble() * Math.PI * 2;
			
			float z = (float) (velocity * Math.cos(a));
			float y = (float) (velocity * Math.sin(a) * Math.sin(b));
			float x = (float) (velocity * Math.sin(a) * Math.cos(b));
    		
    		TransformationBuilder transform = new TransformationBuilder().add(null, new Vector3f(player.rotationPitch, player.rotationYaw, 0), null, null, 0);
    		
    		Matrix4f m1 = TRSRTransformation.blockCornerToCenter(transform.build()).getMatrix();
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
    		
    		bullet.setDamage(caliber * caliber * velocity * velocity * 10/81);
    		
    		world.spawnEntity(bullet);
            
            world.playSound(null, player.posX, player.posY, player.posZ, SoundsHandler.getSoundEvent(Sounds.COLT_1911_SHOT), SoundCategory.PLAYERS, 1, 1);
            
    	}
	}
}
