package com.PiMan.RecieverMod.util.handlers;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@EventBusSubscriber
public class TextureStitcher
{
	
	private static final TextureMap TM = Minecraft.getMinecraft().getTextureMapBlocks();
	
	public static final ResourceLocation 
		ONE_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/one"),
		TWO_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/two"),
		THREE_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/three"),
		FOUR_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/four"),
		FIVE_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/five"),
		SIX_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/six"),
		SEVEN_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/seven"),
		EIGHT_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/eight"),
		NINE_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/nine"),
		ZERO_LOCATION = new ResourceLocation(Reference.MOD_ID, "particle/zero");
	
	public static TextureAtlasSprite
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		ZERO;
	
    @SubscribeEvent
    public void stitcherEventPre(TextureStitchEvent.Pre event) {
    	Main.LOGGER.info("Adding Textures to TextureMap");
        ONE = event.getMap().registerSprite(ONE_LOCATION);
        TWO = event.getMap().registerSprite(TWO_LOCATION);
        THREE = event.getMap().registerSprite(THREE_LOCATION);
        FOUR = event.getMap().registerSprite(FOUR_LOCATION);
        FIVE = event.getMap().registerSprite(FIVE_LOCATION);
        SIX = event.getMap().registerSprite(SIX_LOCATION);
        SEVEN = event.getMap().registerSprite(SEVEN_LOCATION);
        EIGHT = event.getMap().registerSprite(EIGHT_LOCATION);
        NINE = event.getMap().registerSprite(NINE_LOCATION);
        ZERO = event.getMap().registerSprite(ZERO_LOCATION);
   }
} 
