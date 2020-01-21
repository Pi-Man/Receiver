package com.PiMan.RecieverMod.init;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.blocks.BlockBulletCrafter;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {
	
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block BULLET_CRAFTER = new BlockBulletCrafter("bullet_crafter", Material.ROCK);	
}
