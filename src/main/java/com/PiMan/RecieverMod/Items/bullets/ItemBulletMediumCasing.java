package com.PiMan.RecieverMod.Items.bullets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemBulletMediumCasing extends ItemBullet {

	public ItemBulletMediumCasing(String name) {
		super(name, "bullet45casing");
	}

	@Override
	public void fire(World world, EntityPlayer player, float entityAccuracy, float gunAccuracy, int life) {
		
	}

}
