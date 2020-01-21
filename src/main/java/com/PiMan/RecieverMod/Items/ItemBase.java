package com.PiMan.RecieverMod.Items;

import java.util.Random;
import java.util.UUID;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.CreativeTab;
import com.PiMan.RecieverMod.util.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ItemBase extends Item implements IHasModel{

	protected Random rand = new Random();

	public ItemBase(String name) {
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTab.Receiver_Mod);
		
		ModItems.ITEMS.add(this);
	}
	
	public NBTTagCompound checkNBTTags(ItemStack stack) {
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		if (nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		
		if (!nbt.hasKey("UUID") && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			UUID id = MathHelper.getRandomUUID(this.rand );
			nbt.setString("UUID", id.toString());
			System.out.println("Set UUID for: " + stack.getItem());
		}
		
		return nbt;
	}
	
	public NBTTagCompound getNBTTag(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		
		if (nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		
		return nbt;
	}
	
	@Override
	public void registerModels() {
			Main.proxy.registerItemRenderer(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}
}