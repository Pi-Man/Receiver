package com.PiMan.RecieverMod.Items.bullets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemBulletMediumBullet extends ItemBullet {

	public ItemBulletMediumBullet(String name) {
		super(name, "bullet45bullet");
	}

	@Override
	public void fire(World world, EntityPlayer player, float entityAccuracy, float gunAccuracy, int life) {
		
	}

}
