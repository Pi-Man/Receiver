package com.PiMan.RecieverMod.Packets;

import java.lang.reflect.InvocationTargetException;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageSpawnEntity extends MessageBase<MessageSpawnEntity> {
	
	private NBTTagCompound nbt;
	private String name;

	public MessageSpawnEntity() {}
	
	public MessageSpawnEntity(Entity entity) {
		this.nbt = entity.serializeNBT();
		this.name = entity.getClass().getName();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.nbt = ByteBufUtils.readTag(buf);
		this.name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbt);
		ByteBufUtils.writeUTF8String(buf, name);
	}

	@Override
	public void handleClientSide(MessageSpawnEntity message, EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(MessageSpawnEntity message, EntityPlayer player) {
		
		try {
			Entity entity = (Entity) Class.forName(message.name).getDeclaredConstructor(World.class).newInstance(player.world);
			
			entity.readFromNBT(message.nbt);
			
			player.world.spawnEntity(entity);
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
