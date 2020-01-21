package com.PiMan.RecieverMod.Entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class RenderRPG extends RenderEntity {
	
	private ItemStack item;

	public RenderRPG(RenderManager renderManagerIn, ItemStack stack) {
		super(renderManagerIn);
		this.item = stack;
	}
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		//super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		float angle1 = (1 - partialTicks) * entity.prevRotationPitch + partialTicks * entity.rotationPitch;
		float angle2 = (1 - partialTicks) * entity.prevRotationYaw + partialTicks * entity.rotationYaw;
		
		GlStateManager.rotate(angle2, 0, 1, 0);
		GlStateManager.rotate(angle1, -1, 0, 0);
		Minecraft.getMinecraft().getRenderItem().renderItem(item, TransformType.GROUND);
		GlStateManager.popMatrix();
	}

}
