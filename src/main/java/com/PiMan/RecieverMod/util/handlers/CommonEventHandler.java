package com.PiMan.RecieverMod.util.handlers;

import com.PiMan.RecieverMod.Entity.EntityDummyTarget;
import com.PiMan.RecieverMod.Packets.MessageDamageParticles;
import com.PiMan.RecieverMod.Packets.MessageSyncConfig;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber
public class CommonEventHandler {
    
    @SubscribeEvent
    public static void damageEvent(LivingDamageEvent event) {
    	if(!event.getEntityLiving().world.isRemote) {
    		EntityLivingBase entity = event.getEntityLiving();
    		boolean showAll = ModConfig.damageParticles;
    		if (entity instanceof EntityDummyTarget || showAll) {
	    		float damage = event.getAmount();
	    		NetworkHandler.sendToAll(new MessageDamageParticles(damage, entity.posX, entity.posY + entity.height + 0.1, entity.posZ));
    		}
    	}
    }
    
    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent event) {
    	if (event.getModID().equals(Reference.MOD_ID)) {
    		ConfigManager.sync(Reference.MOD_ID, Type.INSTANCE);
    		if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
    			NetworkHandler.sendToAll(new MessageSyncConfig());
    		}
    	}
    }
    
    @SubscribeEvent
    public static void entitySpawn(EntityJoinWorldEvent event) {
    	Entity entity = event.getEntity();
    	if (entity instanceof EntityItem && entity.getEntityData().getBoolean("NoDespawn")) {
    		EntityItem item = (EntityItem) entity;
    		NBTTagCompound nbt = new NBTTagCompound();
    		item.writeEntityToNBT(nbt);
    		nbt.setShort("Age", Short.MIN_VALUE);
    		item.readEntityFromNBT(nbt);
    		//System.out.println("Setting life to infinite");
    	}
    }
}
