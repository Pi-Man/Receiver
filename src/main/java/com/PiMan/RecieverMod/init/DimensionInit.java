package com.PiMan.RecieverMod.init;

import com.PiMan.RecieverMod.World.Dimensions.Maze.DimensionMaze;
import com.PiMan.RecieverMod.config.ModConfig;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class DimensionInit {
	
	public static final DimensionType MAZE = DimensionType.register("Maze", "_maze", ModConfig.mazeID, DimensionMaze.class, false);

	public static void registerDiminsions() {
		DimensionManager.registerDimension(ModConfig.mazeID, MAZE);
	}
}
