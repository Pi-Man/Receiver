package com.PiMan.RecieverMod.Items.accesories;

import com.PiMan.RecieverMod.Items.ItemBase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemAccessories extends ItemBase {
	
	private int type;
	
	private static final IItemPropertyGetter MODEL_GETTER = new IItemPropertyGetter() {

		@Override
		public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
			if (!stack.hasTagCompound()) {
				return 0;
			}
			
			return stack.getTagCompound().getBoolean("model") ? 1 : 0;
			
		}
		
	};

	public ItemAccessories(String name, int type) {
		super(name);
		this.maxStackSize = 1;
		this.addPropertyOverride(new ResourceLocation("model"), MODEL_GETTER);
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
	public float getZoomFactor() {
		return 1F/6F;
	}

}
