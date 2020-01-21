package com.PiMan.RecieverMod.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

public class ModelTest extends ItemOverrideList {
	
	private IBakedModel bullet;
	
    private final List<ItemOverride> overrides = Lists.<ItemOverride>newArrayList();

	public ModelTest(List<ItemOverride> overridesIn, IBakedModel bullet) {
		super(overridesIn);
		this.bullet = bullet;
		System.out.println("New ModelTest");
	}
	
	public static List<ResourceLocation> getParts(ResourceLocation loc) {
		
		//System.out.println("Getting Parts From: " + loc);
		
		List<ResourceLocation> list = new ArrayList<ResourceLocation>();
		
		Gson gson = new Gson();
		try {
			ResourceLocation location = new ResourceLocation(Reference.MOD_ID, loc.getResourcePath() + ".json");
			if (!location.getResourcePath().contains("models/item/")) {
				location = new ResourceLocation(Reference.MOD_ID, "models/item/" + location.getResourcePath());
			}
			//System.out.println(location);
			InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			JsonElement je = gson.fromJson(reader, JsonElement.class);
			JsonObject json = je.getAsJsonObject();
			
			JsonArray modelArray = (JsonArray) json.get("submodels");
			
			if (modelArray != null) {
				
				for (Object modelobject : modelArray) {
					JsonObject jsonmodel = (JsonObject) modelobject;
					ResourceLocation modelLocation = new ResourceLocation(jsonmodel.get("model").getAsString());
					//System.out.println(modelLocation);
					list.add(modelLocation);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		//System.out.println(list);
		
		return list;
		
	}	
	public static List<ModelResourceLocation> getModelParts(ResourceLocation loc) {

		List<ModelResourceLocation> list = new ArrayList<ModelResourceLocation>();
		
		Gson gson = new Gson();
		try {
			ResourceLocation location = new ResourceLocation(Reference.MOD_ID, loc.getResourcePath() + ".json");
			System.out.println(location);
			InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			JsonElement je = gson.fromJson(reader, JsonElement.class);
			JsonObject json = je.getAsJsonObject();
			
			JsonArray modelArray = (JsonArray) json.get("submodels");
			
			if (modelArray != null) {
				
				for (Object modelobject : modelArray) {
					JsonObject jsonmodel = (JsonObject) modelobject;
					ResourceLocation modelLocation = new ResourceLocation(jsonmodel.get("model").getAsString());
					System.out.println(modelLocation);
					list.add(new ModelResourceLocation(modelLocation, "inventory"));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		System.out.println(list);
		
		return list;
		
	}
	
}
