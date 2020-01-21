package com.PiMan.RecieverMod.util;

import com.PiMan.RecieverMod.init.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTab {
    public static CreativeTabs Receiver_Mod = new CreativeTabs("receiver_mod") {

        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.COLT, 1);
        }

        @SideOnly(Side.CLIENT)
        public boolean hasSearchBar() {
            return false;
        }
    };
}
