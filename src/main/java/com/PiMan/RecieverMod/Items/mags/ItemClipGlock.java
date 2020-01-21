package com.PiMan.RecieverMod.Items.mags;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.IHasModel;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClipGlock extends ItemClip{

	public ItemClipGlock(String name) {
		super(name);
	}
	
	@Override
	public void Init() {
		this.ammo = ModItems.BULLET9MM;
		this.maxAmmo = 17;
	}

}
