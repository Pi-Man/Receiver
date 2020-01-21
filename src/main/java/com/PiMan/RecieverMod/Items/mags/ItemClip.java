package com.PiMan.RecieverMod.Items.mags;

import java.util.List;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.IItemInit;
import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.Packets.MessageAddToInventory;
import com.PiMan.RecieverMod.Packets.MessageUpdateNBT;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.IHasModel;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemClip extends ItemBase implements IItemInit {
	
	protected int maxAmmo;
	
	protected Item ammo;

	public ItemClip(String name) {
		super(name);
		
		this.maxStackSize = 1;
		this.setMaxDamage(0);
	
		this.addPropertyOverride(new ResourceLocation("bullets"), new IItemPropertyGetter()
	    {
	        @Override
			@SideOnly(Side.CLIENT)
	        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
	        {
	        	if (worldIn == null && entityIn != null) {
	        		worldIn = entityIn.world;
	        	}
	        	
	        	if (entityIn == null || worldIn == null || !worldIn.hasCapability(ItemDataProvider.ITEMDATA_CAP, null) || !stack.hasTagCompound()) {
	        		return 0.0F;
	        	}
	        	NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(stack.getTagCompound().getString("UUID"));
	        	
	            return nbt.getTagList("Bullets", 8).tagCount();
	        }
	    });
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("ID: " + ((ItemBase)stack.getItem()).getNBTTag(stack).getString("UUID"));
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof EntityPlayer) {
						
			EntityPlayer player = (EntityPlayer) entityIn;
			
			NBTTagCompound tag = this.getNBTTag(stack);
			
			NBTTagCompound nbt = worldIn.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData().getCompoundTag(tag.getString("UUID"));
			if (!nbt.hasKey("Bullets", 9)) {
				nbt.setTag("Bullets", new NBTTagList());
			}
			
			if (player.getHeldItemMainhand().equals(stack)) {
										
				if (worldIn.isRemote) {
					if (KeyInputHandler.isKeyPressed(KeyPresses.AddBullet)) {
						if (nbt.getTagList("Bullets", 8).tagCount() < this.maxAmmo) {
							
							ItemStack ammo = findAmmo(player);
							
							if (!ammo.isEmpty()) {
								nbt.getTagList("Bullets", 8).appendTag(new NBTTagString(ammo.getItem().getRegistryName().toString()));
								NetworkHandler.sendToServer(new MessageAddToInventory(ammo, -1));
							}
						}
					}
					
					if (KeyInputHandler.isKeyPressed(KeyPresses.RemoveBullet)) {
						if (nbt.getTagList("Bullets", 8).tagCount() > 0) {
							nbt.getTagList("Bullets", 8).removeTag(nbt.getTagList("Bullets", 8).tagCount() - 1);
							NetworkHandler.sendToServer(new MessageAddToInventory(this.ammo, 1));
						}
					}
					//Main.LOGGER.info("Sending nbt at slot {}", itemSlot);
				}
			}
			if (worldIn.isRemote) {
				NetworkHandler.sendToServer(new MessageUpdateNBT(stack, itemSlot, nbt));
			}
		}
	}
	
	private ItemStack findAmmo(EntityPlayer player) {
		for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = player.inventory.getStackInSlot(i);

            if (this.isBullet(itemstack))
            {
                return itemstack;
            }
        }

        return ItemStack.EMPTY;
	}
	
	protected boolean isBullet(ItemStack stack) {
        return stack.getItem() == this.ammo;
    }
	
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		NBTTagCompound nbt1 = oldStack.getTagCompound();
		NBTTagCompound nbt2 = newStack.getTagCompound();
		
		boolean flag = nbt1 == null && nbt2 == null || nbt1.equals(nbt2);
        return slotChanged || !flag;
    }
}
