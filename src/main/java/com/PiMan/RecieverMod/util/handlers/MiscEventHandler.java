package com.PiMan.RecieverMod.util.handlers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Entity.EntityDummyTarget;
import com.PiMan.RecieverMod.Items.guns.ItemColt;
import com.PiMan.RecieverMod.Items.guns.ItemGlock;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.Items.guns.ItemRifle;
import com.PiMan.RecieverMod.Items.guns.ItemShotgun;
import com.PiMan.RecieverMod.Packets.MessageDamageParticles;
import com.PiMan.RecieverMod.Packets.MessageFlashServer;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.IModelClip;
import com.PiMan.RecieverMod.util.IModelGun;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.ModelTest;
import com.PiMan.RecieverMod.util.Reference;

import ca.weblite.objc.Proxy;
import io.netty.channel.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(Side.CLIENT)
public class MiscEventHandler {

	private static boolean cancleBob;
	
	private static boolean bob;
	
	private static float mouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
	
	@SubscribeEvent
	public static void onFOVUpdate(FOVUpdateEvent event) {

		ItemStack stack = event.getEntity().getHeldItemMainhand();
		
		if (stack.getItem() instanceof ItemGun) {
			ItemGun gun = (ItemGun)stack.getItem();
			
			NBTTagCompound nbt = Minecraft.getMinecraft().world.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(gun.getNBTTag(stack).getString("UUID"));
			
			if (nbt.getBoolean("ADS")) {
				event.setNewfov(gun.getZoomFactor(stack));
			}
		}
		
	}
	
    @SubscribeEvent
    public static void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
    	EntityPlayer player = Main.proxy.getPlayer();
    	if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && player.getHeldItemMainhand().getItem() instanceof ItemGun) {
    		event.setCanceled(true);
    	}
    }
    
    @SubscribeEvent
    public static void renderTick(RenderTickEvent event) {
    	if (event.phase == Phase.START) {
    		bob = Minecraft.getMinecraft().gameSettings.viewBobbing;
    		if (cancleBob) {
    			Minecraft.getMinecraft().gameSettings.viewBobbing = false;
    		}
    		mouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
    		if (Minecraft.getMinecraft().player != null) {
	    		ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
	    		if (stack.getItem() instanceof ItemGun) {
	    			ItemGun gun = (ItemGun)stack.getItem();
	    			NBTTagCompound itemdata = Minecraft.getMinecraft().world.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(gun.getNBTTag(stack).getString("UUID"));
	    			if (itemdata.getBoolean("ADS")) {
		    			float f = gun.getZoomFactor(stack);
		    			Minecraft.getMinecraft().gameSettings.mouseSensitivity = mouseSensitivity * f;
	    			}
	    		}
    		}
    	}
    	if (event.phase == Phase.END) {
    		Minecraft.getMinecraft().gameSettings.viewBobbing = bob;
    		Minecraft.getMinecraft().gameSettings.mouseSensitivity = mouseSensitivity;
    	}
    }
    
    @SubscribeEvent
    public static void ClientTick(ClientTickEvent event) {

    	if (event.phase == Phase.START) {
    		cancleBob = false;
    		mouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
    		if (Minecraft.getMinecraft().player != null) {
	    		ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
	    		if (stack.getItem() instanceof ItemGun) {
	    			ItemGun gun = (ItemGun)stack.getItem();
	    			NBTTagCompound itemdata = Minecraft.getMinecraft().world.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(gun.getNBTTag(stack).getString("UUID"));
	    			if (itemdata.getBoolean("ADS")) {
		    			float f = gun.getZoomFactor(stack);
		    			Minecraft.getMinecraft().gameSettings.mouseSensitivity = mouseSensitivity * f;
	    			}
	    		}
    		}
    	}
    	else if (event.phase == Phase.END) {
    		Minecraft.getMinecraft().gameSettings.mouseSensitivity = mouseSensitivity;
    	}
    	
    }
    
    //@SubscribeEvent
    public static void InjectClientNetworkIntercepter(ClientConnectedToServerEvent event) {
    	NetworkManager handler = event.getManager();
    
    	NetHandlerPlayClient currentNetHandler = (NetHandlerPlayClient) handler.getNetHandler();
    	
    	handler.setNetHandler(new ClientNetworkIntercepter(Minecraft.getMinecraft(), null, handler, currentNetHandler.getGameProfile()));
    	
    	Main.LOGGER.info(handler.getNetHandler());
    }
    
    public static void cancleBob() {
    	cancleBob = true;
    }
}
