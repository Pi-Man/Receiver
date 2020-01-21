package com.PiMan.RecieverMod.Items.bullets;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.util.Reference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemBulletSmallBullet extends ItemBullet {

	public ItemBulletSmallBullet(String name) {
		super(name, "bullet22bullet");
	}

	@Override
	public void fire(World world, EntityPlayer player, float entityAccuracy, float gunAccuracy, int life) {
		
	}

}
