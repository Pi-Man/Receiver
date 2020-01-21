package com.PiMan.RecieverMod.Items;

import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.util.ReceiverTeleporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemCassette extends ItemBase {

	public ItemCassette(String name) {
		super(name);
		this.setMaxStackSize(11);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		if (!worldIn.isRemote) {
			if (playerIn.dimension != ModConfig.mazeID) {
				ReceiverTeleporter.teleportToDimension(playerIn, ModConfig.mazeID, playerIn.posX, playerIn.posY, playerIn.posZ);
				stack = ItemStack.EMPTY;
			}
			else if (stack.getCount() >= 11) {
				ReceiverTeleporter.teleportToDimension(playerIn, 0, playerIn.posX, playerIn.posY, playerIn.posZ);
				stack = ItemStack.EMPTY;
			}
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

}
