package com.PiMan.RecieverMod.Packets;

import java.util.UUID;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.Items.guns.ItemColt;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.Items.mags.ItemClip;
import com.PiMan.RecieverMod.init.ModItems;
import com.PiMan.RecieverMod.util.IItemData;
import com.PiMan.RecieverMod.util.ItemData;
import com.PiMan.RecieverMod.util.ItemDataProvider;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageUpdateNBT extends MessageBase<MessageUpdateNBT> {
	
	private NBTTagCompound stackNBT;
	private int slot;
	private NBTTagCompound nbt;

	public MessageUpdateNBT() {}

	public MessageUpdateNBT(ItemStack stack, int slot, NBTTagCompound nbt) {
		this.stackNBT = stack.serializeNBT();
		this.slot = slot;
		this.nbt = nbt;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.stackNBT = ByteBufUtils.readTag(buf);
		this.slot = buf.readInt();
		this.nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, stackNBT);
		buf.writeInt(slot);
		ByteBufUtils.writeTag(buf, nbt);
	}

	@Override
	public void handleClientSide(MessageUpdateNBT message, EntityPlayer player) {
		ItemStack stack1 = player.inventory.getStackInSlot(message.slot);
		ItemStack stack2 = new ItemStack(message.stackNBT);
						
		stack1.setTagCompound(getStackTag(stack2));
		
		NBTTagCompound dataTag = player.world.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
		NBTTagCompound itemTag = getItemTag(dataTag, getStackTag(stack2).getString("UUID"));
		itemTag.merge(message.nbt);
		
		player.world.getCapability(ItemDataProvider.ITEMDATA_CAP, null).setItemData(dataTag);
	}

	@Override
	public void handleServerSide(MessageUpdateNBT message, EntityPlayer player) {
		ItemStack stack1 = new ItemStack(message.stackNBT);
		//Main.LOGGER.info(message.nbt);
		ItemStack stack2 = player.inventory.getStackInSlot(message.slot);
						
		if (stack1.getItem() != stack2.getItem()) {
			stack2 = player.getHeldItemOffhand();
		}
		if (stack1.getItem() != stack2.getItem()) {
			Main.LOGGER.info("Items are not Equal");
			return;
		}
		
		String uuid = null;
		short status = -1; 
		
		for ( World world : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
			IItemData itemData = world.getCapability(ItemDataProvider.ITEMDATA_CAP, null);
			
			NBTTagCompound baseTag = itemData.getItemData();
			NBTTagCompound itemTag = null;
			if (uuid == null) {
				if (message.nbt.getString("UUID").isEmpty()) {
					if (stack2.hasTagCompound() && !stack2.getTagCompound().getString("UUID").isEmpty()) {
						uuid = getStackTag(stack2).getString("UUID");
						itemTag = getItemTag(baseTag, uuid);
						status = 0;
					}
					else {
						uuid = UUID.randomUUID().toString();
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setString("UUID", uuid);
						getStackTag(stack2).merge(nbt);
						Main.LOGGER.info("Set UUID To: {}", uuid);
						itemTag = getItemTag(baseTag, uuid);
						itemTag.merge(message.nbt);
						itemTag.setString("UUID", uuid);
						status = 1;
						itemData.setItemData(baseTag);
					}
					NetworkHandler.sendToAll(new MessageUpdateNBT(stack2, message.slot, itemTag));
				}
				else {
					itemTag = getItemTag(baseTag, message.nbt.getString("UUID"));
					itemTag.merge(message.nbt);
					status = 2;
					itemData.setItemData(baseTag);
				}
			}
			else {
				if (status == 0) {
					itemTag = getItemTag(baseTag, uuid);
				}
				else if (status == 1) {
					itemTag = getItemTag(baseTag, uuid);
					itemTag.merge(message.nbt);
					itemData.setItemData(baseTag);
				}
				else if (status == 2) {
					itemTag = getItemTag(baseTag, uuid);
					itemTag.merge(message.nbt);
					itemData.setItemData(baseTag);
				}
			}
		}
	}
	
	private NBTTagCompound getItemTag(NBTTagCompound baseTag, String key) {
		NBTTagCompound itemTag;
		if (baseTag.hasKey(key, 10)) {
			itemTag = baseTag.getCompoundTag(key);
		}
		else {
			itemTag = new NBTTagCompound();
			baseTag.setTag(key, itemTag);
		}
		return itemTag;
	}
	
	private NBTTagCompound getStackTag(ItemStack stack) {
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		if (nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		
		return nbt;
		
	}
}
