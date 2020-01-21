package com.PiMan.RecieverMod.Entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * ModelTurret - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class ModelTurret extends ModelBase {
    public ModelRenderer leg1;
    public ModelRenderer base1;
    public ModelRenderer base2;
    public ModelRenderer leg4;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer pitch;
    public ModelRenderer yaw;

    public ModelTurret() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.leg2 = new ModelRenderer(this, 0, 0);
        this.leg2.setRotationPoint(0.0F, 11.800000381469733F, 0.0F);
        this.leg2.addBox(0.0F, -0.5F, -0.5F, 11, 1, 1, 2.7755575615628914E-17F);
        this.setRotateAngle(leg2, 3.3161256313323975F, 0.5759586691856385F, 2.286381244659424F);
        this.yaw = new ModelRenderer(this, 56, 0);
        this.yaw.setRotationPoint(0.0F, 9.200000000000017F, 0.0F);
        this.yaw.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 2.7755575615628914E-17F);
        this.leg4 = new ModelRenderer(this, 24, 2);
        this.leg4.setRotationPoint(0.0F, 11.800000381469733F, 0.0F);
        this.leg4.addBox(0.0F, -0.5F, -0.5F, 11, 1, 1, 2.7755575615628914E-17F);
        this.setRotateAngle(leg4, -3.3161256313323975F, -0.5759586691856385F, 2.286381244659424F);
        this.pitch = new ModelRenderer(this, 18, 4);
        this.pitch.setRotationPoint(0.0F, 9.200000000000017F, 0.0F);
        this.pitch.addBox(-0.5F, -1.0F, -2.5F, 1, 1, 5, 2.7755575615628914E-17F);
        this.leg3 = new ModelRenderer(this, 0, 0);
        this.leg3.setRotationPoint(0.0F, 11.800000381469733F, 0.0F);
        this.leg3.addBox(0.0F, -0.5F, -0.5F, 11, 1, 1, 2.7755575615628914E-17F);
        this.setRotateAngle(leg3, 0.17453292012214658F, -0.5759586691856385F, 0.8552113175392152F);
        this.base1 = new ModelRenderer(this, 0, 4);
        this.base1.setRotationPoint(0.0F, 11.800000381469733F, 0.0F);
        this.base1.addBox(-3.0F, -0.5F, -3.0F, 6, 1, 6, 2.7755575615628914E-17F);
        this.leg1 = new ModelRenderer(this, 0, 0);
        this.leg1.setRotationPoint(0.0F, 11.800000381469733F, 0.0F);
        this.leg1.addBox(0.0F, -0.5F, -0.5F, 11, 1, 1, 2.7755575615628914E-17F);
        this.setRotateAngle(leg1, -0.2094395160675049F, 0.5759586691856385F, 0.8552113175392152F);
        this.base2 = new ModelRenderer(this, 44, 0);
        this.base2.setRotationPoint(0.0F, 12.800000381469726F, 0.0F);
        this.base2.addBox(-2.0F, -0.5F, -2.0F, 4, 1, 4, 2.7755575615628914E-17F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.leg2.offsetX, this.leg2.offsetY, this.leg2.offsetZ);
        GlStateManager.translate(this.leg2.rotationPointX * f5, this.leg2.rotationPointY * f5, this.leg2.rotationPointZ * f5);
        GlStateManager.scale(1.753845390000001D, 1.9132858800000008D, 1.771561000000001D);
        GlStateManager.translate(-this.leg2.offsetX, -this.leg2.offsetY, -this.leg2.offsetZ);
        GlStateManager.translate(-this.leg2.rotationPointX * f5, -this.leg2.rotationPointY * f5, -this.leg2.rotationPointZ * f5);
        this.leg2.render(f5);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.yaw.offsetX, this.yaw.offsetY, this.yaw.offsetZ);
        GlStateManager.translate(this.yaw.rotationPointX * f5, this.yaw.rotationPointY * f5, this.yaw.rotationPointZ * f5);
        GlStateManager.scale(1.753845390000001D, 1.9132858800000008D, 1.771561000000001D);
        GlStateManager.translate(-this.yaw.offsetX, -this.yaw.offsetY, -this.yaw.offsetZ);
        GlStateManager.translate(-this.yaw.rotationPointX * f5, -this.yaw.rotationPointY * f5, -this.yaw.rotationPointZ * f5);
        this.yaw.render(f5);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.leg4.offsetX, this.leg4.offsetY, this.leg4.offsetZ);
        GlStateManager.translate(this.leg4.rotationPointX * f5, this.leg4.rotationPointY * f5, this.leg4.rotationPointZ * f5);
        GlStateManager.scale(1.753845390000001D, 1.9132858800000008D, 1.771561000000001D);
        GlStateManager.translate(-this.leg4.offsetX, -this.leg4.offsetY, -this.leg4.offsetZ);
        GlStateManager.translate(-this.leg4.rotationPointX * f5, -this.leg4.rotationPointY * f5, -this.leg4.rotationPointZ * f5);
        this.leg4.render(f5);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.pitch.offsetX, this.pitch.offsetY, this.pitch.offsetZ);
        GlStateManager.translate(this.pitch.rotationPointX * f5, this.pitch.rotationPointY * f5, this.pitch.rotationPointZ * f5);
        GlStateManager.scale(1.753845390000001D, 1.9132858800000008D, 1.771561000000001D);
        GlStateManager.translate(-this.pitch.offsetX, -this.pitch.offsetY, -this.pitch.offsetZ);
        GlStateManager.translate(-this.pitch.rotationPointX * f5, -this.pitch.rotationPointY * f5, -this.pitch.rotationPointZ * f5);
        this.pitch.render(f5);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.leg3.offsetX, this.leg3.offsetY, this.leg3.offsetZ);
        GlStateManager.translate(this.leg3.rotationPointX * f5, this.leg3.rotationPointY * f5, this.leg3.rotationPointZ * f5);
        GlStateManager.scale(1.753845390000001D, 1.9132858800000008D, 1.771561000000001D);
        GlStateManager.translate(-this.leg3.offsetX, -this.leg3.offsetY, -this.leg3.offsetZ);
        GlStateManager.translate(-this.leg3.rotationPointX * f5, -this.leg3.rotationPointY * f5, -this.leg3.rotationPointZ * f5);
        this.leg3.render(f5);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.base1.offsetX, this.base1.offsetY, this.base1.offsetZ);
        GlStateManager.translate(this.base1.rotationPointX * f5, this.base1.rotationPointY * f5, this.base1.rotationPointZ * f5);
        GlStateManager.scale(1.753845390000001D, 1.9132858800000008D, 1.771561000000001D);
        GlStateManager.translate(-this.base1.offsetX, -this.base1.offsetY, -this.base1.offsetZ);
        GlStateManager.translate(-this.base1.rotationPointX * f5, -this.base1.rotationPointY * f5, -this.base1.rotationPointZ * f5);
        this.base1.render(f5);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.leg1.offsetX, this.leg1.offsetY, this.leg1.offsetZ);
        GlStateManager.translate(this.leg1.rotationPointX * f5, this.leg1.rotationPointY * f5, this.leg1.rotationPointZ * f5);
        GlStateManager.scale(1.753845390000001D, 1.9132858800000008D, 1.771561000000001D);
        GlStateManager.translate(-this.leg1.offsetX, -this.leg1.offsetY, -this.leg1.offsetZ);
        GlStateManager.translate(-this.leg1.rotationPointX * f5, -this.leg1.rotationPointY * f5, -this.leg1.rotationPointZ * f5);
        this.leg1.render(f5);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.base2.offsetX, this.base2.offsetY, this.base2.offsetZ);
        GlStateManager.translate(this.base2.rotationPointX * f5, this.base2.rotationPointY * f5, this.base2.rotationPointZ * f5);
        GlStateManager.scale(1.753845390000001D, 1.9132858800000008D, 1.771561000000001D);
        GlStateManager.translate(-this.base2.offsetX, -this.base2.offsetY, -this.base2.offsetZ);
        GlStateManager.translate(-this.base2.rotationPointX * f5, -this.base2.rotationPointY * f5, -this.base2.rotationPointZ * f5);
        this.base2.render(f5);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
    	
    	this.yaw.rotateAngleY = netHeadYaw * 0.017453292F;
    	this.pitch.rotateAngleX = headPitch * 0.017453292F;
    	this.pitch.rotateAngleY = netHeadYaw * 0.017453292F;
    	
    }
    
    
}
