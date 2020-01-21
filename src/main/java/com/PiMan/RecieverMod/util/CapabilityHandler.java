package com.PiMan.RecieverMod.util;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.ItemBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class CapabilityHandler {
	
	public static final ResourceLocation ITEM_CAP = new ResourceLocation(Reference.MOD_ID, "item");
	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<World> event) {
		event.addCapability(ITEM_CAP, new ItemDataProvider());
	}
}
