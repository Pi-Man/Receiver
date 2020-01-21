package com.PiMan.RecieverMod.Items.guns;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Entity.EntityBullet;
import com.PiMan.RecieverMod.Items.IItemInit;
import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.animations.AnimationConrollerSlide;
import com.PiMan.RecieverMod.Items.animations.AnimationControllerADS;
import com.PiMan.RecieverMod.Items.animations.AnimationControllerHammer;
import com.PiMan.RecieverMod.Items.animations.AnimationControllerMag;
import com.PiMan.RecieverMod.Items.animations.AnimationControllerShoot;
import com.PiMan.RecieverMod.Items.mags.ItemClip;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageEject;
import com.PiMan.RecieverMod.Packets.MessageFlashServer;
import com.PiMan.RecieverMod.Packets.MessagePlaySound;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.IHasModel;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.FlashHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemColt extends ItemGun implements IItemInit {

	public ItemColt(String name) {
		super(name);
	}
	
	@Override
	public void Init() {
		this.drift = 10;
		this.spreadX = 0.5;
		this.spreadY = 0.5;
		this.ammo = ModItems.BULLET45;
		this.casing = ModItems.BULLET45CASING;
		this.mag = ModItems.COLTCLIP;
		
		this.animationControllers.add(new AnimationControllerADS());
		this.animationControllers.add(new AnimationControllerShoot(nbt->nbt.getInteger("SlideFrame") == 0));
		this.animationControllers.add(new AnimationControllerHammer(false));
		this.animationControllers.add(new AnimationControllerMag());
		this.animationControllers.add(new AnimationConrollerSlide());
		
		List<ItemPropertyWrapper> properties = new ArrayList<>();
		animationControllers.forEach(controller -> properties.addAll(controller.getProperties()));
		properties.forEach(property -> addPropertyOverride(property.getName(), property.getOverride()));
	}
	
	@Override
	public SoundEvent getShootSound() {
		return SoundsHandler.getSoundEvent(Sounds.COLT_1911_SHOT);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		boolean flag = false;

		if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).getHeldItemMainhand().equals(stack)) {
						
			EntityPlayer player = (EntityPlayer)entityIn;
			
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
	}

	@Override
	public float getDefaultZoomFactor(ItemStack stack) {
		return 0.9F;
	}
}
