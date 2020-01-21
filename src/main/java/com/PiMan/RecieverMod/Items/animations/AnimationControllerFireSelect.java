package com.PiMan.RecieverMod.Items.animations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class AnimationControllerFireSelect implements IAnimationController {
	
	private final HashSet<Modes> options;
	
	public AnimationControllerFireSelect(Modes... modes) {
		options = new HashSet<>(Arrays.asList(modes));
	}

	@Override
	public List<ItemPropertyWrapper> getProperties() {
		List<ItemPropertyWrapper> list = new ArrayList<>();
		
		list.add(new ItemPropertyWrapper("mode", new IItemPropertyGetter() {
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if (worldIn == null) {
					worldIn = Minecraft.getMinecraft().world;
				}
	        	if (worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
	        		return 0.0F;
	        	}
	        	NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
				NBTTagCompound oldnbt = (NBTTagCompound) nbt.getTag("prev");
				
				Modes[] modes = options.toArray(new Modes[0]);
				
				int newVal = modes[nbt.getInteger("mode")].ordinal();
				int oldVal = modes[oldnbt.getInteger("mode")].ordinal();
				
				float pt = RenderPartialTickHandler.renderPartialTick;
				
				float f = (newVal - oldVal) * pt + oldVal;
				
				return f;
			}
		}));
		
		return list;
	}

	@Override
	public void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, NBTTagCompound nbt, ItemGun gun) {
		if (entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityIn;
			if (KeyInputHandler.isKeyPressed(KeyPresses.Safety)) {
				nbt.setInteger("mode", (nbt.getInteger("mode") + 1)%options.size());
			}
		}
	}
	
	public enum Modes {
		SAFETY,
		SEMI,
		AUTO;
	}

}
