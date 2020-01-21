package com.PiMan.RecieverMod.util.handlers;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.KeyBinding.Keybinds;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyInputHandler {
		
	@SubscribeEvent
	public void onClientTickEvent(ClientTickEvent event) {
		if (Minecraft.getMinecraft().player != null) {
			if (event.phase == Phase.START) {
				prevScreen = currentScreen;
				currentScreen = Minecraft.getMinecraft().currentScreen;
				checkKeys();
			}
			else if (event.phase == Phase.END){
				scroll = 0;
			}
		}
	}

	@SubscribeEvent
	public void onInput(InputEvent event) {
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		if (player.getHeldItemMainhand().getItem() instanceof ItemGun && !gs.keyBindSprint.isKeyDown()) {
						
			KeyBinding.setKeyBindState(gs.keyBindAttack.getKeyCode(), false);
			while (gs.keyBindAttack.isPressed());
			
			KeyBinding.setKeyBindState(gs.keyBindUseItem.getKeyCode(), false);
			while (gs.keyBindUseItem.isPressed());
			
		}
	}
	
	@SubscribeEvent
	public void resetScrollCancle(InputUpdateEvent event) {
		scrollCancle = false;
	}
	
	@SubscribeEvent
	public void onMouseEvent(MouseEvent event) {
		
		if (event.getButton() == -1 && scrollCancle) {
			event.setCanceled(true);
		}
		
		scroll += Main.sign(event.getDwheel());
	}
	
	static GuiScreen currentScreen = null;
	
	static GuiScreen prevScreen = null;
	
	static GameSettings gs = Minecraft.getMinecraft().gameSettings;
	
	static KeyBinding Keys[] = new KeyBinding[] {
			Keybinds.addBullet, 
			Keybinds.removeBullet, 
			Keybinds.removeClip, 
			Keybinds.slideLock, 
			Keybinds.safety, 
			gs.keyBindAttack, 
			gs.keyBindUseItem,
			gs.keyBindSneak};
	
	static int scroll = 0;
	
	static final int NUMKEYS = Keys.length;
	
	static final int OLD = 1;
	
	static final int NEW = 0;
	
	private static boolean States[][] = new boolean[2][NUMKEYS];
	
	private static boolean scrollCancle = false;
		
	private static void checkKeys() {
						
		for(int i = 0; i < NUMKEYS; i++) {
			
			getStates()[OLD][i] = getStates()[NEW][i];
			
			int keyCode = Keys[i].getKeyCode();
			
			if (keyCode < 0) {
				keyCode += 100;
				getStates()[NEW][i] = Mouse.isButtonDown(keyCode) && !gs.keyBindSprint.isKeyDown();
			}
			else {
				getStates()[NEW][i] = Keyboard.isKeyDown(keyCode);
			}
		}
		
	}
	
	public static boolean isKeyPressed(KeyPresses Key) {
		
		int i;
		
		i = EnumToInt(Key);
		
		if (i == -1 || prevScreen != null) {
			return false;
		}
				
		return (getStates()[NEW][i] && !getStates()[OLD][i]);
		
	}
	
	public static boolean isKeyDown(KeyPresses Key) {
		
		int i;
		
		i = EnumToInt(Key);
		
		if (i == -1 || prevScreen != null) {
			return false;
		}
		
		return (getStates()[NEW][i]);
	}
	
	public static boolean isKeyUnpressed(KeyPresses Key) {
		
		int i;
		
		i = EnumToInt(Key);
		
		if (i == -1 || prevScreen != null) {
			return false;
		}
		
		return(!getStates()[NEW][i] && getStates()[OLD][i]);
	}
	
	public static int getScroll() {
		return scroll;
	}
	
	public static void cancleScroll(boolean cancle) {
		scrollCancle = cancle;
	}
	
	private static int EnumToInt(KeyPresses Key) {
		
		switch(Key) {
		
		case AddBullet:
			return 0;
			
		case RemoveBullet:
			return 1;
			
		case RemoveClip:
			return 2;
			
		case SlideLock:
			return 3;
			
		case Safety:
			return 4;
			
		case LeftClick:
			return 5;
			
		case RightClick:
			return 6;
			
		case Shift:
			return 7;
		
		default: 
			return -1;
		}
	}
	
	public static boolean[][] getStates() {
		return States;
	}

	public static void setStates(boolean states[][]) {
		States = states;
	}

	public enum KeyPresses {
		AddBullet,
		RemoveBullet,
		RemoveClip,
		SlideLock,
		Safety,
		LeftClick,
		RightClick,
		Shift;
	}
}