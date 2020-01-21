package com.PiMan.RecieverMod.Particle;

import java.util.Random;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

public class ParticleNumber extends Particle {

	public ParticleNumber(World worldIn, double posXIn, double posYIn, double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);
        this.particleScale = 2;
        this.particleBlue = 0;
        this.particleMaxAge = 20;
        this.particleGravity = 1F;
    }
	
	public ParticleNumber(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, TextureAtlasSprite texture) {
		this(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, 1, texture);
	}
	
	public ParticleNumber(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float scale, TextureAtlasSprite texture) {
		this(worldIn, xCoordIn, yCoordIn, zCoordIn);
		this.motionX = xSpeedIn;
		this.motionY = ySpeedIn * scale;
		this.motionZ = zSpeedIn;
		this.particleScale = scale;
		this.particleGravity *= scale * 0.5;
		setParticleTexture(texture);
    }
	
	@Override
	public int getFXLayer() {
		return 1;
	}
	
}
