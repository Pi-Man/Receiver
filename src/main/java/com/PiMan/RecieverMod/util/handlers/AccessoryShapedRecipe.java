package com.PiMan.RecieverMod.util.handlers;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.accesories.ItemAccessories;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class AccessoryShapedRecipe extends ShapedRecipes{

	public AccessoryShapedRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
		super(group, width, height, ingredients, result);
	}
	
	private boolean checkMatch(InventoryCrafting inventory, int startx, int starty)
    {
        for (int i = 0; i < inventory.getWidth(); ++i)
        {
            for (int j = 0; j < inventory.getHeight(); ++j)
            {
                int k = i - startx;
                int l = j - starty;
                Ingredient ingredient = Ingredient.EMPTY;

                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight)
                {

                    ingredient = this.recipeItems.get(k + l * this.recipeWidth);
                                        
                }
                ItemStack stack = inventory.getStackInRowAndColumn(i, j);
                
                boolean flag = true;
                
                for (ItemStack ingstack : ingredient.getMatchingStacks()) {
                	if (ingstack.getItem() == stack.getItem()) {
                		flag = false;
                		if (ingstack.getItem() instanceof ItemGun) {
                			if (ingstack.hasTagCompound() && ingstack.getTagCompound().hasKey("Accessories", 10)) {
                				NBTTagCompound ingnbt = ingstack.getOrCreateSubCompound("Accessories");
                				NBTTagCompound nbt = stack.getOrCreateSubCompound("Accessories");
                				for (String key : ingnbt.getKeySet()) {
                					if (!nbt.hasKey(key, 8) || !ingnbt.getString(key).equals(nbt.getString(key))) {
                						flag = true;
                						break;
                					}
                				}
                			}
                		}
        				if (!flag) {
        					break;
        				}
                	}
                }
                
                if (ingredient == Ingredient.EMPTY && stack.isEmpty()) {
                	flag = false;
                }
                
                if (flag) {
                	return false;
                }
                
            }
        }

        return true;
    }
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
    {
        int [] ints = this.getStartLocation(inv);
        return (ints[0] != -1 && ints[1] != -1);
    }
	
	public int[] getStartLocation(InventoryCrafting inv) {
        for (int i = 0; i <= inv.getWidth() - this.recipeWidth; ++i)
        {
            for (int j = 0; j <= inv.getHeight() - this.recipeHeight; ++j)
            {
                if (this.checkMatch(inv, i, j))
                {
                    return new int[] {i, j};
                }
            }
        }

        return new int[] {-1, -1};
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        
        int[] ints = this.getStartLocation(inv);
        
        if (this.getRecipeOutput().getItem() instanceof ItemAccessories) {
        	ItemAccessories item = (ItemAccessories) this.getRecipeOutput().getItem();
            for (int i = 0; i < nonnulllist.size(); ++i)
            {
            	int x = i % inv.getHeight() - ints[0];
            	int y = i / inv.getWidth() - ints[1];
            	if (x >= 0 && x < this.recipeWidth && y >= 0 && y < this.recipeHeight) {
            		Ingredient ingredient = this.recipeItems.get(x + y * this.recipeWidth);
                    ItemStack itemstack = inv.getStackInSlot(i);
                    ItemStack itemstack1 = itemstack.copy();
            		for (ItemStack stack : ingredient.getMatchingStacks()) {
            			if (stack.getItem() == itemstack.getItem()) {
		                    if (itemstack.getItem() instanceof ItemGun) {
			                    NBTTagCompound accessories = itemstack.getOrCreateSubCompound("Accessories");
			                    for (String key : accessories.getKeySet()) {
			                    	itemstack1.getOrCreateSubCompound("Accessories").removeTag(key);
			                    	Main.LOGGER.info("Removed Accessory; {}", accessories.getString(key));
			                    }
		                    }
		                    else {
		                    	itemstack1 = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
		                    }
            			}
            			else {
            				Main.LOGGER.info("Recipe should already be checked to be correct, but it isn't");
            			}
            		}
                    nonnulllist.set(i, itemstack1);
            	}
            }
        }

        return nonnulllist;
    }
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack result = this.getRecipeOutput().copy();
        if (result.getItem() instanceof ItemGun) {
        	ItemGun gun = (ItemGun) result.getItem();
        	for (int i = 0; i < inv.getSizeInventory(); i++) {
        		ItemStack stack = inv.getStackInSlot(i);
    			if (stack.getItem() instanceof ItemAccessories) {
    				ItemAccessories accessory = (ItemAccessories) stack.getItem();
    				result.getOrCreateSubCompound("Accessories").setString(Integer.valueOf(accessory.getType()).toString(), accessory.getRegistryName().toString());
    			}
    			if (stack.getItem() instanceof ItemGun) {
    				ItemGun inggun = (ItemGun) stack.getItem();
    				NBTTagCompound nbt = inggun.getNBTTag(stack);
    				gun.getNBTTag(result).merge(nbt);
    			}
        	}
        }
		return result;
	}
	
	public static class Factory implements IRecipeFactory {

		@Override
		public IRecipe parse(final JsonContext context, final JsonObject json) {
	        String group = JsonUtils.getString(json, "group", "");
	        //if (!group.isEmpty() && group.indexOf(':') == -1)
	        //    group = context.getModId() + ":" + group;

	        Map<Character, Ingredient> ingMap = Maps.newHashMap();
	        for (Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet())
	        {
	            if (entry.getKey().length() != 1)
	                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
	            if (" ".equals(entry.getKey()))
	                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

	            ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
	        }

	        ingMap.put(' ', Ingredient.EMPTY);

	        JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");

	        if (patternJ.size() == 0)
	            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");

	        String[] pattern = new String[patternJ.size()];
	        for (int x = 0; x < pattern.length; ++x)
	        {
	            String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
	            if (x > 0 && pattern[0].length() != line.length())
	                throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
	            pattern[x] = line;
	        }

	        ShapedPrimer primer = new ShapedPrimer();
	        primer.width = pattern[0].length();
	        primer.height = pattern.length;
	        primer.input = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);

	        Set<Character> keys = Sets.newHashSet(ingMap.keySet());
	        keys.remove(' ');

	        int x = 0;
	        for (String line : pattern)
	        {
	            for (char chr : line.toCharArray())
	            {
	                Ingredient ing = ingMap.get(chr);
	                if (ing == null)
	                    throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
	                primer.input.set(x++, ing);
	                keys.remove(chr);
	            }
	        }

	        if (!keys.isEmpty())
	            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);

	        //ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), context);
	        ItemStack result = CraftingHelper.getIngredient(json.getAsJsonObject("result"), context).getMatchingStacks()[0];
	        return new AccessoryShapedRecipe(group, primer.width, primer.height, primer.input, result);
		}
	}
}
