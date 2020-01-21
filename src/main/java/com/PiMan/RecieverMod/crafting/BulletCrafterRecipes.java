package com.PiMan.RecieverMod.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.PiMan.RecieverMod.init.ModItems;
import com.google.common.collect.Lists;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BulletCrafterRecipes {
	
	private static final BulletCrafterRecipes INSTANCE = new BulletCrafterRecipes();
	private final Map<ArrayList<ItemStack>, ItemStack> recipes = new HashMap<ArrayList<ItemStack>, ItemStack>();
	
	public static BulletCrafterRecipes instance() {
		return INSTANCE;
	}
	
	public BulletCrafterRecipes() {
		this.addRecipe(new ItemStack(ModItems.BULLET45, 64), new ItemStack(ModItems.BULLET45CASING, 64), new ItemStack(Items.REDSTONE), new ItemStack(Items.GUNPOWDER, 8), new ItemStack(Items.IRON_INGOT));
		this.addRecipe(new ItemStack(ModItems.BULLET22, 32), new ItemStack(ModItems.BULLET22CASING, 32), new ItemStack(Items.REDSTONE), new ItemStack(Items.GUNPOWDER, 4), new ItemStack(Items.IRON_INGOT));
		this.addRecipe(new ItemStack(ModItems.BULLET38SPECIAL, 64), new ItemStack(ModItems.BULLET38SPECIALCASING, 64), new ItemStack(Items.REDSTONE), new ItemStack(Items.GUNPOWDER, 8), new ItemStack(Items.IRON_INGOT));
		this.addRecipe(new ItemStack(ModItems.BULLET9MM, 64), new ItemStack(ModItems.BULLET9MMCASING, 64), new ItemStack(Items.REDSTONE), new ItemStack(Items.GUNPOWDER, 8), new ItemStack(Items.IRON_INGOT));
		this.addRecipe(new ItemStack(ModItems.BULLETSHOTGUN, 16), new ItemStack(ModItems.BULLETSHOTGUNCASING, 16), new ItemStack(Items.REDSTONE), new ItemStack(Items.GUNPOWDER, 8), new ItemStack(Items.IRON_INGOT));
	}
	
	public void addRecipe(ItemStack result, ItemStack... ingredients) {
		recipes.put(Lists.newArrayList(ingredients), result);
	}
	
	public ItemStack getResult(List<ItemStack> list) {
				
		for (Entry<ArrayList<ItemStack>, ItemStack> entry : recipes.entrySet()) {
			if (this.compareLists(entry.getKey(), list)) {
				return entry.getValue();
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	public ArrayList<ItemStack> getIngredients(List<ItemStack> ingredients) {
		
		for (Entry<ArrayList<ItemStack>, ItemStack> entry : recipes.entrySet()) {
			if (this.compareLists(entry.getKey(), ingredients)) {
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	private boolean compareLists(List<ItemStack> list1, List<ItemStack> list2) {
		if (list1.size() == list2.size()) {
			for (int i = 0; i < list1.size(); i++) {
				ItemStack stack1 = list1.get(i);
				ItemStack stack2 = list2.get(i);
				
				if (stack1.getItem() != stack2.getItem() || stack1.getMetadata() != stack2.getMetadata() || stack2.getCount() < stack1.getCount()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
