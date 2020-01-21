package com.PiMan.RecieverMod.Items.guns;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.IItemInit;
import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.animations.AnimationControllerADS;
import com.PiMan.RecieverMod.Items.animations.AnimationControllerCylinder;
import com.PiMan.RecieverMod.Items.animations.AnimationControllerHammer;
import com.PiMan.RecieverMod.Items.animations.AnimationControllerShoot;
import com.PiMan.RecieverMod.Items.animations.IAnimationController;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemData;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.FlashHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Item44Magnum extends ItemGun implements IItemInit {

	public Item44Magnum(String name) {
		super(name);
	}
	
	@Override
	public void Init() {
		this.drift = 20;
		this.spreadX = 0.5;
		this.spreadY = 0.5;
		this.ammo = ModItems.BULLET45;
		this.casing = ModItems.BULLET45CASING;
		
		this.animationControllers.add(new AnimationControllerADS());
		this.animationControllers.add(new AnimationControllerShoot(nbt->!nbt.getBoolean("open")));
		this.animationControllers.add(new AnimationControllerCylinder(0.02));
		this.animationControllers.add(new AnimationControllerHammer(true));
		
		List<ItemPropertyWrapper> properties = new ArrayList<>();
		animationControllers.forEach(controller -> properties.addAll(controller.getProperties()));
		properties.forEach(property -> addPropertyOverride(property.getName(), property.getOverride()));
	}
	
	static final double friction = 0.02;
	
	public double getSpin(NBTTagCompound nbt) {
		return nbt.getDouble("theta") + (nbt.getBoolean("hammer") && !nbt.getBoolean("open") ? 0.5 : 0);
	}
	
	@Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		if (worldIn.isRemote) {
			
			NBTTagCompound tag = this.getNBTTag(stack);
			
			NBTTagCompound baseTag = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
			
			NBTTagCompound nbt = baseTag.getCompoundTag(tag.getString("UUID"));
			
			NBTTagCompound oldnbt = nbt.copy();
			oldnbt.removeTag("prev");
			nbt.setTag("prev", oldnbt);
			
			animationControllers.forEach(controller -> controller.update(stack, worldIn, entityIn, itemSlot, isSelected, nbt, (ItemGun) stack.getItem()));
			
			NetworkHandler.sendToServer(new MessageUpdateNBT(stack, itemSlot, nbt));
	    }
	}
	
	@Override
	public SoundEvent getShootSound() {
		return SoundsHandler.getSoundEvent(Sounds.COLT_1911_SHOT);
	}

	@Override
	public float getDefaultZoomFactor(ItemStack stack) {
		return 0.9F;
	}
}
