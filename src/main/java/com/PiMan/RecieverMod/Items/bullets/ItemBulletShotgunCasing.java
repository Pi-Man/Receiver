package com.PiMan.RecieverMod.Items.bullets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemBulletShotgunCasing extends ItemBullet {

	public ItemBulletShotgunCasing(String name) {
		super(name, "shotgun_casing");
	}

	@Override
	public void fire(World world, EntityPlayer player, float entityAccuracy, float gunAccuracy, int life) {
		
	}

}
