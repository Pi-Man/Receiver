package com.PiMan.RecieverMod.KeyBinding;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Keybinds {
	
	public static KeyBinding addBullet = new KeyBinding("key.addbullet", Keyboard.KEY_Z, "key.categories.guncontrols");
	public static KeyBinding removeBullet = new KeyBinding("key.removebullet", Keyboard.KEY_R, "key.categories.guncontrols");
	public static KeyBinding removeClip = new KeyBinding("key.removeclip", Keyboard.KEY_G, "key.categories.guncontrols");
	public static KeyBinding slideLock = new KeyBinding("key.slidelock", Keyboard.KEY_T, "key.categories.guncontrols");
	public static KeyBinding safety = new KeyBinding("key.saftey", Keyboard.KEY_V, "key.categories.guncontrols");
	
	public static void register() {
		
		ClientRegistry.registerKeyBinding(addBullet);
		ClientRegistry.registerKeyBinding(removeBullet);
		ClientRegistry.registerKeyBinding(removeClip);
		ClientRegistry.registerKeyBinding(slideLock);
		ClientRegistry.registerKeyBinding(safety);
		
	}	
}
