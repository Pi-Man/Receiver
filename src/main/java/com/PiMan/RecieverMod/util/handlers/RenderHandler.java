package com.PiMan.RecieverMod.util.handlers;

import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.Entity.EntityGrenade;
import com.PiMan.RecieverMod.Entity.EntityRPG;
import com.PiMan.RecieverMod.Entity.EntityTurret;
import com.PiMan.RecieverMod.Entity.EntityDummyTarget;
import com.PiMan.RecieverMod.Entity.render.RenderBullet;
import com.PiMan.RecieverMod.Entity.render.RenderGrenade;
import com.PiMan.RecieverMod.Entity.render.RenderRPG;
import com.PiMan.RecieverMod.Entity.render.RenderTurret;
import com.PiMan.RecieverMod.Entity.render.RenderDummyTarget;
import com.PiMan.RecieverMod.init.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler {
	public static void registerEntityRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new IRenderFactory<EntityBullet>() {
			@Override
			public Render<? super EntityBullet> createRenderFor(RenderManager manager) {
				return new RenderBullet(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, new IRenderFactory<EntityGrenade>() {
			@Override
			public Render<? super EntityGrenade> createRenderFor(RenderManager manager) {
				return new RenderSnowball(manager, ModItems.FRAGGRENADE, Minecraft.getMinecraft().getRenderItem());
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityDummyTarget.class, new IRenderFactory<EntityDummyTarget>() {
			@Override
			public Render<? super EntityDummyTarget> createRenderFor(RenderManager manager) {
				return new RenderDummyTarget(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityTurret.class, new IRenderFactory<EntityTurret>() {
			@Override
			public Render<? super EntityTurret> createRenderFor(RenderManager manager) {
				return new RenderTurret(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityRPG.class, new IRenderFactory<EntityRPG>() {
			@Override
			public Render createRenderFor(RenderManager manager) {
				return new RenderRPG(manager, new ItemStack(ModItems.RPG));
			}
		});
	}
}
