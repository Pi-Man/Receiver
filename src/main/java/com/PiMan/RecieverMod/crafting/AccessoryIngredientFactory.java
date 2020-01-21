package com.PiMan.RecieverMod.crafting;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.accesories.ItemAccessories;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class AccessoryIngredientFactory implements IIngredientFactory {

	@Override
	public Ingredient parse(JsonContext context, JsonObject json) {
		
		ItemStack stack = CraftingHelper.getItemStackBasic(json, context);
		
		String id = json.getAsJsonPrimitive("accessory").getAsString();
		
		ItemAccessories accessory = (ItemAccessories) Item.getByNameOrId(id);
		
		stack.getOrCreateSubCompound("Accessories").setString(Integer.valueOf(accessory.getType()).toString(), id);
				
		return Ingredient.fromStacks(stack);
	}

}
