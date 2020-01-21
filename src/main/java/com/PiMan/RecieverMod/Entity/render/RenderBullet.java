package com.PiMan.RecieverMod.Entity.render;

import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.util.Reference;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBullet extends RenderArrow<EntityBullet>{

	public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/entity/bullet.png");
	
	public RenderBullet(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBullet entity) {
		return TEXTURES;		
	}
}
