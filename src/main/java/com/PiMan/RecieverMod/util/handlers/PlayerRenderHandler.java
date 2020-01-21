package com.PiMan.RecieverMod.util.handlers;

import com.PiMan.RecieverMod.Items.guns.ItemGun;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(Side.CLIENT)
public class PlayerRenderHandler {

	@SubscribeEvent
	public static void playerRender(RenderPlayerEvent.Pre event) {
		/*
		EntityPlayer entityPlayer = event.getEntityPlayer();
		
		String name = entityPlayer.getName();
		
		NetworkHandler.sendToServer(new MessageGetPlayerNBT(name));
		
		//IInvisibility I = entityPlayer.getCapability(InvisibilityProvider.INVISIBILITY_CAP, null);
		
		if (entityPlayer.getEntityData().hasKey("IsHidden") && entityPlayer.getEntityData().getBoolean("IsHidden")) {
			entityPlayer.setInvisible(true);
			event.setCanceled(true);
		}
		else {
			entityPlayer.setInvisible(false);
		}
		*/
	}
	
	@SubscribeEvent
	public static void EntityRender(RenderLivingEvent.Pre event) {
		
		EntityLivingBase entity = event.getEntity();
		
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;
			if (player.getHeldItemMainhand().getItem() instanceof ItemGun) {
			
				RenderLivingBase renderer = event.getRenderer();
				
				ModelPlayer model = (ModelPlayer) renderer.getMainModel();
				
				model.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
				model.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
		}
	}
}
