package com.PiMan.RecieverMod.Items.bullets;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.Reference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class ItemBullet extends ItemBase {
	
	private final String model;
	
	public ItemBullet(String name, String model) {
		super(name);
		this.model = model;
		maxStackSize = 64;
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, model), "inventory"));
	}
	
	public abstract void fire(World world, EntityPlayer player, float entityAccuracy, float gunAccuracy, int life);
	
}
