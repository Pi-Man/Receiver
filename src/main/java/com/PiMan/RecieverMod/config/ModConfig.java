package com.PiMan.RecieverMod.config;

import com.PiMan.RecieverMod.util.Reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = Reference.MOD_ID, name = "Receiver Config")
public class ModConfig {
	
	@Name("Spawn Damage Particles")
	@Comment("Whether or not to spawn particles indicating damage done")
	public static boolean damageParticles = true;
	
	@Name("Damage Particles Render Range")
	@Comment("How far away damage particles can spawn from the player")
	@RangeDouble(min = 0)
	public static double damageParticlesDistance = 64D;
	
	@RequiresMcRestart
	@Name("Maze Dimension ID")
	@Comment("Change this if there is a Dimension ID Conflict")	
	public static int mazeID = 2;
	
	@Name("Head Shot Multiplier")
	@RangeDouble(min = 1)
	public static double headShotMul = 2D;
	
	@Name("Colt Damage")
	@RangeDouble(min = 0)
	public static double coltdamage = 10D;
	
	@Name("Glock Damage")
	@RangeDouble(min = 0)
	public static double glockdamage = 10D;
	
	@Name("Model 10 Damage")
	@RangeDouble(min = 0)
	public static double model10damage = 15D;
	
	@Name("Rifle Damage")
	@RangeDouble(min = 0)
	public static double rifledamage = 30D;
	
	@Name("Thompson Damage")
	@RangeDouble(min = 0)
	public static double thompsondamage = 10D;
	
	@Name("Remington Pellet Count")
	@RangeInt(min = 1)
	public static int remingtonpelletcount = 40;
	
	@Name("Remington Pellet Damage")
	@RangeDouble(min = 0)
	public static double remingtondamage = 1D;
	
	@Name("Fragmentation Count")
	@RangeInt(min = 1)
	public static int fragcount = 500;
	
	@Name("Fragmentation Damage")
	@RangeDouble(min = 0)
	public static double fragdamage = 5D;
}
