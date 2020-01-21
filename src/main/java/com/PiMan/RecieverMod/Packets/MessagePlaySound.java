package com.PiMan.RecieverMod.Packets;

import com.PiMan.RecieverMod.util.handlers.SoundsHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler.Sounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

public class MessagePlaySound extends MessageBase<MessagePlaySound> {

	private int sound;
	
	public MessagePlaySound() {}
	
	public MessagePlaySound(int sound) {
		this.sound = sound;
	}
	
	public MessagePlaySound(Sounds sound) {
		this.sound = sound.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		sound = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(sound);
	}

	@Override
	public void handleClientSide(MessagePlaySound message, EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleServerSide(MessagePlaySound message, EntityPlayer player) {
		// TODO Auto-generated method stub
		player.world.playSound(null, player.posX, player.posY, player.posZ, SoundsHandler.getSoundEvent(message.sound), SoundCategory.PLAYERS, 1, 1);
	}

}
