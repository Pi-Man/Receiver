package com.PiMan.RecieverMod.Entity;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Packets.MessageDamageParticles;
import com.PiMan.RecieverMod.Particle.ParticleNumber;
import com.PiMan.RecieverMod.util.ReceiverTeleporter;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.TextureStitcher;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDummyTarget extends EntityLiving {

	private int damagetime = 0;

	public EntityDummyTarget(World worldIn) {
		super(worldIn);
		this.setNoAI(true);
		this.height = 2F;
	}

	@Override
	public float getEyeHeight() {
		return 1.75F;
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
	}
	
    @Override
    public boolean canBePushed()
    {
        return false;
    }
    
    @Override
    protected void collideWithEntity(Entity entityIn)
    {
    }
    
    @Override    
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
    	    	
    	//this.setDead();
    	
    	if (!world.isRemote) {
	    	
	    	if (player.dimension != 2) {
	    		
	    		//ReceiverTeleporter.teleportToDimension(player, 2, player.posX, player.posY, player.posZ);
	    		//System.out.println("Teleporting to 2");
	    	
	    	}
	    	else {
	    		
	    		//ReceiverTeleporter.teleportToDimension(player, 0, player.posX, player.posY, player.posZ);
	    		//System.out.println("Teleporting to 0");
	    		
	    	}
    	
    	}
    	
		return EnumActionResult.FAIL;
    }
    
    @Override
    public void setHealth(float health) {}
    
    @Override
    public void onKillCommand()
    {
        this.setDead();
		System.out.println("Set Dead");
    }

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		List<ItemStack> list = new ArrayList<ItemStack>(1);
		list.add(ItemStack.EMPTY);
		return list;
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		
	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return null;
	}
	
    @Override
    public EnumPushReaction getPushReaction()
    {
        return EnumPushReaction.NORMAL;
    }

}
