package com.PiMan.RecieverMod.Entity.render;

import com.PiMan.RecieverMod.Entity.EntityGrenade;
import com.PiMan.RecieverMod.util.Reference;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class RenderGrenade extends RenderSnowball<EntityGrenade>{

	public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/entity/bullet.png");
	
	public RenderGrenade(RenderManager renderManagerIn, Item item, RenderItem itemRenderer) {
		super(renderManagerIn, item, itemRenderer);
	}
}
