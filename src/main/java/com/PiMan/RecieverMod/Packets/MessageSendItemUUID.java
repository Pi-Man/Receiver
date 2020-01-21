package com.PiMan.RecieverMod.Packets;

import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.util.ItemDataProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageSendItemUUID extends MessageBase<MessageSendItemUUID> {
	
	private String uuid;
	private int slot;
	
	public MessageSendItemUUID() {}

	public MessageSendItemUUID(String uuid, int slot) {
		this.uuid = uuid;
		this.slot = slot;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.uuid = ByteBufUtils.readUTF8String(buf);
		this.slot = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, uuid);
		buf.writeInt(slot);
	}

	@Override
	public void handleClientSide(MessageSendItemUUID message, EntityPlayer player) {
		ItemStack stack;
		if (message.slot == -1) {
			stack = player.getHeldItemOffhand();
		}
		else {
			stack = player.inventory.getStackInSlot(message.slot);
		}
		if (stack.hasCapability(ItemDataProvider.ITEMDATA_CAP, null)) {
			NBTTagCompound nbt = stack.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
			nbt.setString("UUID", message.uuid);
		}
	}

	@Override
	public void handleServerSide(MessageSendItemUUID message, EntityPlayer player) {

	}

}
