package com.PiMan.RecieverMod.init;

import java.util.function.Function;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.Entity.EntityGrenade;
import com.PiMan.RecieverMod.Entity.EntityItems;
import com.PiMan.RecieverMod.Entity.EntityRPG;
import com.PiMan.RecieverMod.Entity.EntityTurret;
import com.PiMan.RecieverMod.Entity.EntityDummyTarget;
import com.PiMan.RecieverMod.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext.EntityTarget;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.internal.FMLMessage.EntitySpawnMessage;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;

public class EntityInit {
	
	public static void registerEntities() {
		int i = 0;
		registerEntity("bullet", EntityBullet.class, i++, 64, Integer.MAX_VALUE, false, false, 0, 0, false);
		registerEntity("rpg", EntityRPG.class, i++, 64, Integer.MAX_VALUE, false, false, 0, 0, false);
		registerEntity("grenade", EntityGrenade.class, i++, 64, 1, true, false, 0, 0, false);
		registerEntity("dummy_target", EntityDummyTarget.class, i++, 64, 1, true, true, 0, 0, false);
		registerEntity("turret", EntityTurret.class, i++, 64, 1, true, true, 13421772, 7829367, false);
		registerEntity("items", EntityItems.class, i++, 0, 10, false, false, 0, 0, false);
	}

	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int trackingRange, int trackingfrequency, boolean velocitydata, boolean hasEgg, int color1, int color2, boolean cancleVanillaSpawn) {
		if (hasEgg) {
			EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, name), entity, name, id, Main.instance, trackingRange, trackingfrequency, velocitydata, color1, color2);
		}
		else {
			EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, name), entity, name, id, Main.instance, trackingRange, trackingfrequency, velocitydata);
		}
		if (cancleVanillaSpawn) {
			EntityRegistry.instance().lookupModSpawn(Loader.instance().getIndexedModList().get(Reference.MOD_ID), id).setCustomSpawning(new Function<EntitySpawnMessage, Entity>() {
				public Entity apply(EntitySpawnMessage t) {
					Entity entity = EntityRegistry.instance().lookupModSpawn(Loader.instance().getIndexedModList().get(Reference.MOD_ID), id).newInstance(Minecraft.getMinecraft().world);
					
					entity.setVelocity(0, 0, 0);
					
					return entity;
				};
			}, false);
		}
	}
}
