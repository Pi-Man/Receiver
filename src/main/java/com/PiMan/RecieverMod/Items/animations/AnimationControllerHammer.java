package com.PiMan.RecieverMod.Items.animations;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AnimationControllerHammer implements IAnimationController {
	
	private final boolean doubleAction;
	
	public AnimationControllerHammer(boolean doubleAction) {
		this.doubleAction = doubleAction;
	}

	@Override
	public List<ItemPropertyWrapper> getProperties() {
		List<ItemPropertyWrapper> list = new ArrayList<>();
		
		list.add(new ItemPropertyWrapper("hammer", new IItemPropertyGetter() {
	        @Override
			@SideOnly(Side.CLIENT)
	        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
	        {
	        	if (worldIn == null) {
	        		worldIn = Minecraft.getMinecraft().world;
	        	}
	        	
	        	if (worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
	        		return 0.0F;
	        	}
	        	NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
				NBTTagCompound oldnbt = (NBTTagCompound) nbt.getTag("prev");
				
				if (oldnbt == null) {
					return 0.0F;
				}
				
				float pt = RenderPartialTickHandler.renderPartialTick;
				
	            float j = (oldnbt.getBoolean("hammer") ? 1.0F : 0.0F) * (1 - pt) + (nbt.getBoolean("hammer") ? 1.0F : 0.0F) * pt;
	                        
	            return j;
	        }
	    }));
		return list;
	}

	@Override
	public void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, NBTTagCompound nbt, ItemGun gun) {
		if (nbt.getBoolean("hammer") && KeyInputHandler.isKeyDown(KeyPresses.LeftClick) && (!nbt.getBoolean("held") || nbt.getBoolean("Auto"))) {
			nbt.setBoolean("hammer", false);
		}
		else if ((doubleAction && KeyInputHandler.isKeyPressed(KeyPresses.LeftClick)) || KeyInputHandler.isKeyPressed(KeyPresses.Safety)) {
			nbt.setBoolean("hammer", true);
		}
	}

}
