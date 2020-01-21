package com.PiMan.RecieverMod.Items.animations;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.Packets.MessageEject;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AnimationConrollerSlide implements IAnimationController {

	@Override
	public List<ItemPropertyWrapper> getProperties() {
		List<ItemPropertyWrapper> list = new ArrayList<>();
		
		list.add(new ItemPropertyWrapper("slide", new IItemPropertyGetter()
	    {
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
				
				int oldval = oldnbt.getInteger("SlideFrame") == 5 ? 2 : oldnbt.getInteger("SlideFrame");
				
				int newval = nbt.getInteger("SlideFrame") == 5 ? 2 : nbt.getInteger("SlideFrame");
				
	            float j = oldval * (1 - pt) + newval * pt;
	                        
	            return j / 10.0F;
	        }
	    }));
	    
	    list.add(new ItemPropertyWrapper("check", new IItemPropertyGetter()
	    {
	        @Override
			@SideOnly(Side.CLIENT)
	        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
	        {
	        	if (entityIn == null || worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
	        		return 0.0F;
	        	}
	        	NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
	        	NBTTagCompound oldnbt = nbt.getCompoundTag("prev");
	        	
	        	float oldval = oldnbt.getInteger("SlideFrame") == 5 ? 0.3F : oldnbt.getInteger("SlideFrame") < 3 ? oldnbt.getInteger("SlideFrame") / 10.0F : 0.0F;
	        	
	        	float newval = nbt.getInteger("SlideFrame") == 5 ? 0.3F : nbt.getInteger("SlideFrame") < 3 ? nbt.getInteger("SlideFrame") / 10.0F : 0.0F;
	        	
	        	float pt = RenderPartialTickHandler.renderPartialTick;
	        	
	        	if (!KeyInputHandler.isKeyDown(KeyPresses.SlideLock)) {
	        		newval = 0F;
	        	}
	        	if (!(KeyInputHandler.isKeyDown(KeyPresses.SlideLock) || KeyInputHandler.isKeyUnpressed(KeyPresses.SlideLock) && !KeyInputHandler.isKeyPressed(KeyPresses.SlideLock))) {
	        		oldval = 0F;
	        	}
	            return (1 - pt) * oldval + pt * newval;
	        }
	    }));
		
		return list;
	}

	@Override
	public void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, NBTTagCompound nbt, ItemGun gun) {
		if (nbt.getBoolean("fired")) {
			nbt.setInteger("SlideFrame", 4);
		}
		if (KeyInputHandler.isKeyDown(KeyPresses.RemoveBullet)) {
			if (nbt.getInteger("SlideFrame") < 2) {
				nbt.setInteger("SlideFrame", nbt.getInteger("SlideFrame") + 1);
			}
			else if ((nbt.getInteger("SlideFrame") == 2 || nbt.getInteger("SlideFrame") == 5) && KeyInputHandler.isKeyDown(KeyPresses.SlideLock)) {
				//System.out.println("Half Lock");
				nbt.setInteger("SlideFrame", 5);
				((NBTTagCompound) nbt.getTag("prev")).setInteger("SlideFrame", 5);
			}
			else if (nbt.getInteger("SlideFrame") == 5) {
				nbt.setInteger("SlideFrame", 2);
			}
			else if (nbt.getInteger("SlideFrame") < 4) {
				nbt.setInteger("SlideFrame", nbt.getInteger("SlideFrame") + 1);
			}
		}
		if (nbt.getInteger("SlideFrame") == 4) {
			nbt.setBoolean("hammer", true);
			if (nbt.getCompoundTag("prev").getInteger("SlideFrame") < 4) {
				if (!nbt.getString("BulletChambered").isEmpty()) {
					NetworkHandler.sendToServer(new MessageEject(new ItemStack(Item.getByNameOrId(nbt.getString("BulletChambered")))));
					nbt.setString("BulletChambered", "");
				}
			}
		}
		if (!KeyInputHandler.isKeyDown(KeyPresses.RemoveBullet)) {
			if (nbt.getInteger("SlideFrame") == 5) {
				nbt.setInteger("SlideFrame", 2);
			}
			if (nbt.getInteger("SlideFrame") == 4) {
				if (!KeyInputHandler.isKeyDown(KeyPresses.SlideLock) && nbt.getString("BulletChambered").isEmpty() && !nbt.getString("mag").isEmpty() && nbt.getTagList("Bullets", 8).tagCount() == 0) {
					nbt.setBoolean("AutoSlideLock", true);
				}
				else {
					nbt.setBoolean("AutoSlideLock", false);
				}
				nbt.setInteger("SlideFrame", 3);
			}
			if (nbt.getInteger("SlideFrame") == 3 && (KeyInputHandler.isKeyDown(KeyPresses.SlideLock) || nbt.getBoolean("AutoSlideLock")) && !KeyInputHandler.isKeyPressed(KeyPresses.SlideLock)) {
				//System.out.println("Full Lock");
				nbt.setBoolean("AutoSlideLock", true);
			}
			else {
				nbt.setInteger("SlideFrame", 0);
			}
		}
		if (nbt.getInteger("SlideFrame") == 0) {
			nbt.setBoolean("AutoSlideLock", false);
		}
		if (nbt.getBoolean("fired")) {
			nbt.setInteger("SlideFrame", 4);
		}
		if (nbt.getCompoundTag("prev").getInteger("SlideFrame") > 2 && nbt.getCompoundTag("prev").getInteger("SlideFrame") < 5 && nbt.getTagList("Bullets", 8).tagCount() > 0 && nbt.getString("BulletChambered").isEmpty() && (nbt.getInteger("SlideFrame") < nbt.getCompoundTag("prev").getInteger("SlideFrame"))) {
			nbt.setString("BulletChambered", nbt.getTagList("Bullets", 8).getStringTagAt(nbt.getTagList("Bullets", 8).tagCount() - 1));
			nbt.getTagList("Bullets", 8).removeTag(nbt.getTagList("Bullets", 8).tagCount() - 1);
			System.out.println("pickup");
		}
		//System.out.println(nbt);
	}

}
