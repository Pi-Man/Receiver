package com.PiMan.RecieverMod.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ItemBinoculars extends ItemBase {

	public ItemBinoculars(String name) {
		super(name);
		this.setMaxStackSize(1);
	}
	
	@SubscribeEvent
	public static void onFOVUpdate(FOVUpdateEvent event) {
		EntityPlayer player = event.getEntity();
		if (player.getActiveItemStack().getItem() instanceof ItemBinoculars) {
			event.setNewfov(0.1F);
		}
	}
	
	@Override
	 public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		
		playerIn.setActiveHand(handIn);
		
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
	
	@Override
    public int getMaxItemUseDuration(ItemStack stack) {
		return 7200;
	}
}
