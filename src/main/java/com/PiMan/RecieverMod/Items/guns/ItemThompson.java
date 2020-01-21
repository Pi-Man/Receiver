package com.PiMan.RecieverMod.Items.guns;

import java.util.TreeMap;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.Items.mags.ItemClipThompson;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageEject;
import com.PiMan.RecieverMod.Packets.MessagePlaySound;
import com.PiMan.RecieverMod.Packets.MessageShoot;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.config.ModConfig;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.TransformationBuilder;
import com.PiMan.RecieverMod.util.handlers.FlashHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.RenderPartialTickHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemThompson extends ItemGun {

	private static final IItemPropertyGetter MAG_GETTER = new IItemPropertyGetter() {
		
		@Override
		@SideOnly(Side.CLIENT)
		public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
			
        	if (worldIn == null) {
        		worldIn = Minecraft.getMinecraft().world;
        	}
			
        	if (worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
        		return 0.0F;
        	}
        	NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
			
			return nbt.getString("Clip ID").isEmpty() ? 0F : 1F;
		}
		
	};
	
	private static final IItemPropertyGetter BOLT_GETTER = new IItemPropertyGetter() {
		
		@Override
		@SideOnly(Side.CLIENT)
		public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
        	if (worldIn == null) {
        		worldIn = Minecraft.getMinecraft().world;
        	}

        	if (worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
        		return 0.0F;
        	}
        	NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
			NBTTagCompound oldnbt = nbt.getCompoundTag("prev");
			
			float newval = nbt.getInteger("Slide Frame");
			float oldval = oldnbt.getInteger("Slide Frame");
			float pt = RenderPartialTickHandler.renderPartialTick;
			
			float f = (1 - pt) * oldval + pt * newval;
			
			if (f > 1) {
				f = ((f - 1) * 0.1F + 1);
			}
			
			return f;
		}
		
	};
	
	public ItemThompson(String name) {
		super(name);
		
		this.drift = 1;
		this.spreadX = 0.1;
		this.spreadY = 0.1;
		
		addPropertyOverride(new ResourceLocation("mag"), MAG_GETTER);
		addPropertyOverride(new ResourceLocation("bolt"), BOLT_GETTER);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		if (entityIn instanceof EntityPlayer) {
			
			EntityPlayer player = (EntityPlayer)entityIn;
			
			if (player.getHeldItemMainhand().equals(stack)) {
				if (worldIn.isRemote) {
					
					NBTTagCompound tag = this.getNBTTag(stack);
					
					NBTTagCompound baseTag = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
					
					NBTTagCompound nbt = baseTag.getCompoundTag(tag.getString("UUID"));
					
					NBTTagCompound oldnbt = nbt.copy();
					oldnbt.removeTag("prev");
					nbt.setTag("prev", oldnbt);
					
					boolean flag = false;
										
					if (KeyInputHandler.isKeyPressed(KeyPresses.LeftClick) || (KeyInputHandler.isKeyDown(KeyPresses.LeftClick) && nbt.getBoolean("auto"))) {
						flag = Shoot(nbt, player, ModConfig.thompsondamage, nbt.getBoolean("ADS") ? 0 : 10, 0, 1, nbt.getInteger("Slide Frame") == 1);
						nbt.setBoolean("Slide Lock", flag);
						if (flag) {
							FlashHandler.CreateFlash(new BlockPos(player.posX, player.posY + 1, player.posZ), player.dimension, 2);
							nbt.getTagList("Bullets", 8).removeTag(nbt.getTagList("Bullets", 8).tagCount() - 1);
							if (!player.isCreative()) {
								NetworkHandler.sendToServer(new MessageEject(new ItemStack(ModItems.BULLET45CASING)));
							}
						}
					}
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.AddBullet) && nbt.getString("Clip ID").isEmpty()) {
												
						int clipslot = findClip(player);
											
						if (clipslot != -1) {
							ItemStack clip = player.inventory.getStackInSlot(clipslot);
							NetworkHandler.sendToServer(new MessageAddToInventory(clip, -1, clipslot));
							NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_MAG_IN));
							NBTTagCompound clipNBT = baseTag.getCompoundTag(clip.getTagCompound().getString("UUID"));
							nbt.setTag("Bullets", clipNBT.getTagList("Bullets", 8));
							nbt.setString("Clip ID", clipNBT.getString("UUID"));
						}
						
					}
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveClip)) {
						
						if (!nbt.getString("Clip ID").isEmpty()) {
							NBTTagCompound clipNBT = new NBTTagCompound();
							
							clipNBT.setTag("Bullets", nbt.getTagList("Bullets", 8));
							clipNBT.setString("UUID", nbt.getString("Clip ID"));
							nbt.removeTag("Bullets");
							nbt.setString("Clip ID", "");
							
							ItemStack clip = new ItemStack(ModItems.THOMPSONCLIP);
							this.getNBTTag(clip).setString("UUID", clipNBT.getString("UUID"));
							
							baseTag.setTag(clip.getTagCompound().getString("UUID"), clipNBT);
							
							//System.out.println("" + nbt + clipNBT);
							
							NetworkHandler.sendToServer(new MessagePlaySound(Sounds.GLOCK_MAG_OUT));
							
							NetworkHandler.sendToServer(new MessageAddToInventory(clip, 1, player.inventory.getSizeInventory() - 1));
						}
						else if (this.isClip(player.getHeldItemOffhand())) {
							TreeMap<Integer, Pair<ItemStack, Integer>> mags = new TreeMap<Integer, Pair<ItemStack, Integer>>();
							for (int i = 0; i < player.inventory.getSizeInventory() - 1; i++) {
								ItemStack itemstack = player.inventory.getStackInSlot(i);
								if (this.isClip(itemstack)) {
									NBTTagCompound clipNBT = baseTag.getCompoundTag(itemstack.getTagCompound().getString("UUID"));
									mags.put(clipNBT.getTagList("Bullets", 8).tagCount(), Pair.of(itemstack, i));
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
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.Safety)) {
						nbt.setBoolean("auto", !nbt.getBoolean("auto"));
					}
					
					nbt.setString("BulletChambered", nbt.getTagList("Bullets", 8).getStringTagAt(nbt.getTagList("Bullets", 8).tagCount() - 1));
					
					nbt.setBoolean("ADS", KeyInputHandler.isKeyDown(KeyPresses.RightClick));
					
					if (KeyInputHandler.isKeyDown(KeyPresses.RemoveBullet)) {
						if (nbt.getInteger("Slide Frame") < 2) {
							nbt.setInteger("Slide Frame", nbt.getInteger("Slide Frame") + 1);
						}
					}
					else {
						if (nbt.getInteger("Slide Frame") == 2) {
							nbt.setBoolean("Slide Lock", true);
						}
						if ((nbt.getInteger("Slide Frame") > 0 && !nbt.getBoolean("Slide Lock")) || nbt.getInteger("Slide Frame") > 1) {
							nbt.setInteger("Slide Frame", nbt.getInteger("Slide Frame") - 1);
						}
					}
					
					NetworkHandler.sendToServer(new MessageUpdateNBT(stack, itemSlot, nbt));
					
				}
			}
		}
	}

	@Override
	public NBTTagCompound checkNBTTags(ItemStack stack) {
		
		NBTTagCompound nbt = super.checkNBTTags(stack);
		
		return nbt;
	}
	
	@Override
	public SoundEvent getShootSound() {
		return SoundsHandler.getSoundEvent(Sounds.COLT_1911_SHOT);
	}

	@Override
	public float getDefaultZoomFactor(ItemStack stack) {
		return 0.9F;
	}
	
	@Override
	public Matrix4f getAccessoryTransform(int type) {
		if (type == 1) {
			TransformationBuilder builder = new TransformationBuilder();
			builder.add(new Vector3f(-0.097F, 0.2F, 0.2F), new Vector3f(0, -90, 0), null, new Vector3f(0.75F, 0.75F, 0.75F), 0);
			return builder.build().getMatrix();
		}
		else {
			return super.getAccessoryTransform(type);
		}
	}
	
}
