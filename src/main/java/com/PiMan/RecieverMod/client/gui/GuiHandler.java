package com.PiMan.RecieverMod.client.gui;

import com.PiMan.RecieverMod.inventory.ContainerBulletCrafter;
import com.PiMan.RecieverMod.tileEntity.TileEntityBulletCrafter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (EnumGui.values()[ID]) {
		case BULLET_CRAFTER:
			return new ContainerBulletCrafter(player.inventory, (TileEntityBulletCrafter) world.getTileEntity(new BlockPos(x, y, z)));
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (EnumGui.values()[ID]) {
		case BULLET_CRAFTER:
			return new GuiBulletCrafter(player.inventory, (TileEntityBulletCrafter) world.getTileEntity(new BlockPos(x, y, z)));
		default:
			return null;
		
		}
	}
	
	public enum EnumGui {
		BULLET_CRAFTER;
	}

}
