package com.PiMan.RecieverMod.Entity.render;

import com.PiMan.RecieverMod.Entity.EntityDummyTarget;
import com.PiMan.RecieverMod.Entity.model.ModelDummyTarget;
import com.PiMan.RecieverMod.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderDummyTarget extends RenderLivingBase<EntityDummyTarget>{
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/dummy_target.png");

	public RenderDummyTarget(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelDummyTarget(), 0);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDummyTarget entity) {
		return TEXTURE;
	}

}
