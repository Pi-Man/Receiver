package com.PiMan.RecieverMod.Items.animations;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

import com.PiMan.RecieverMod.Items.ItemPropertyWrapper;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessagePlaySound;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class AnimationControllerMag implements IAnimationController {

	@Override
	public List<ItemPropertyWrapper> getProperties() {
		List<ItemPropertyWrapper> list = new ArrayList<>();
		
		list.add(new ItemPropertyWrapper("mag", new IItemPropertyGetter() {
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if (worldIn == null) {
					worldIn = Minecraft.getMinecraft().world;
				}
				
				if (worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
					return 0.0F;
				}
				
				NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
				NBTTagCompound oldnbt = nbt.getCompoundTag("prev");
				
				float pt = RenderPartialTickHandler.renderPartialTick;
				
	            float j = (oldnbt.getString("mag").isEmpty() ? 0.0F : 1.0F) * (1 - pt) + (nbt.getString("mag").isEmpty() ? 0.0F : 1.0F) * pt;
				
				return j;
			}
		}));
		
		return list;
	}

	@Override
	public void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, NBTTagCompound nbt, ItemGun gun) {
		if (entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityIn;
			if (worldIn.isRemote) {
				NBTTagCompound baseTag = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
				if (KeyInputHandler.isKeyPressed(KeyPresses.AddBullet) && nbt.getString("mag").isEmpty()) {
					System.out.println("Add Clip Pressed");
					
					int clipslot = gun.findClip(player);
										
					if (clipslot != -1) {
						ItemStack clip = player.inventory.getStackInSlot(clipslot);
						//System.out.println("Clip Found: " + clip + clip.getTagCompound());
						NetworkHandler.sendToServer(new MessageAddToInventory(clip, -1, clipslot));
						NBTTagCompound clipTag = baseTag.getCompoundTag(clip.getTagCompound().getString("UUID"));
						nbt.setTag("Bullets", clipTag.getTagList("Bullets", 8));
						nbt.setString("mag", clipTag.getString("UUID"));
						NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_MAG_IN));
					}
				}
				if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveClip)) {
					
					if (!nbt.getString("mag").isEmpty()) {
						NBTTagCompound clipNBT = new NBTTagCompound();
						
						clipNBT.setTag("Bullets", nbt.getTagList("Bullets", 8));
						clipNBT.setString("UUID", nbt.getString("mag"));
						nbt.setInteger("Bullets", 0);
						nbt.setString("mag", "");
						
						ItemStack clip = new ItemStack(gun.mag);
						
						clip.setTagCompound(new NBTTagCompound());
						clip.getTagCompound().setString("UUID", clipNBT.getString("UUID"));
						baseTag.setTag(clipNBT.getString("UUID"), clipNBT);
						
						NetworkHandler.sendToServer(new MessageAddToInventory(clip, 1, player.inventory.getSizeInventory() - 1));
						
						NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_MAG_OUT));
					}
					else if (gun.isClip(player.getHeldItemOffhand())) {
						TreeMap<Integer, Pair<ItemStack, Integer>> mags = new TreeMap<Integer, Pair<ItemStack, Integer>>();
						for (int i = 0; i < player.inventory.getSizeInventory() - 1; i++) {
							ItemStack itemstack = player.inventory.getStackInSlot(i);
							if (gun.isClip(itemstack)) {
								mags.put(baseTag.getCompoundTag(itemstack.getTagCompound().getString("UUID")).getInteger("Bullets"), Pair.of(itemstack, i));
							}
						}
						if (!mags.isEmpty()) {
							int slot = mags.lastEntry().getValue().getRight();
							ItemStack oldstack = player.getHeldItemOffhand();
							ItemStack newstack = mags.lastEntry().getValue().getLeft();
							NetworkHandler.sendToServer(new MessageAddToInventory(newstack, -1, slot));
							NetworkHandler.sendToServer(new MessageAddToInventory(oldstack, -1, player.inventory.getSizeInventory() - 1));
							NetworkHandler.sendToServer(new MessageAddToInventory(oldstack, 1, slot));
							NetworkHandler.sendToServer(new MessageAddToInventory(newstack, 1, player.inventory.getSizeInventory() - 1));
						}
					}
				}
			}
		}
	}

}
