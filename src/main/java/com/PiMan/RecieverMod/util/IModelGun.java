package com.PiMan.RecieverMod.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.PiMan.RecieverMod.Main;
import com.PiMan.RecieverMod.Items.guns.ItemGlock;
import com.PiMan.RecieverMod.Items.guns.ItemGun;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler.KeyPresses;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.BuiltInModel;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.MapModelState;
import net.minecraftforge.client.model.MapModelState.Wrapper;
import net.minecraftforge.client.model.ModelDynBucket;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.MultiLayerModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.animation.AnimationItemOverrideList;
import net.minecraftforge.client.model.animation.ModelBlockAnimation;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class IModelGun implements IModel {
	
	private static IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
	
	public static final IModelGun MODEL = new IModelGun();
	
	@Nullable
	private ResourceLocation baseLocation;
	
	private ModelBlock baseModel;
	
	public IModelGun() {
		this(null);
	}
	
	public IModelGun(@Nullable ResourceLocation baseLocation) {
		this.baseLocation = baseLocation;
		
		ModelBlock model = null;

		if (baseLocation != null) {

			BufferedReader reader = getModelReader(baseLocation);
			
			if (reader != null) {
				model = ModelBlock.deserialize(reader);
			}
			else {
				Main.LOGGER.error("Unable to get Model");
			}
			
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.baseModel = model;
	}

	@Override
    public Collection<ResourceLocation> getTextures()
    {
		//System.out.println("Getting Textures");
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

        if (baseLocation != null)
            for (String texture : baseModel.textures.values()) {
            	builder.add(new ResourceLocation(texture));
            }

        return builder.build();
    }
	
	public ResourceLocation getTexture() {
		return new ResourceLocation(Reference.MOD_ID, "items/1911");
	}
	
    public Collection<ResourceLocation> getDependencies() {
    	
    	//System.out.println("Getting Dependencies");
    	ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

    	builder.add(baseLocation);
    	
    	List<ResourceLocation> list = ModelTest.getParts(baseLocation);
    	    	
        if (list != null)
            builder.addAll(list);

        return builder.build();
    }
    
    public Collection<ResourceLocation> getDependenciesForMap() {
    	
    	//System.out.println("Getting Dependencies");
    	ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

    	builder.add(baseLocation);
    	
    	List<ResourceLocation> list = getParts(baseLocation);
    	
    	int i = 0;
    	
    	for (ResourceLocation location : list) {
    		
    		while (list.contains(location)) {
    			location = new ResourceLocation(location.toString() + "-");
    		}
    		
    		list.set(i++, location);
    		
    	}
    	    	
        if (list != null)
            builder.addAll(list);

        return builder.build();
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
			reader.close();
			JsonObject json = je.getAsJsonObject();
			
			JsonArray modelArray = (JsonArray) json.get("submodels");
			
			if (modelArray != null) {
				
				for (Object modelobject : modelArray) {
					JsonObject jsonmodel = (JsonObject) modelobject;
					ResourceLocation modelLocation = new ResourceLocation(jsonmodel.get("model").getAsString());
					JsonObject predicates = jsonmodel.getAsJsonObject("predicates");
					//System.out.println(modelLocation);
					list.add(modelLocation);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//System.out.println(list);
		
		return list;
		
	}
    
    public IModel getMissingModel() {
    	return ModelLoaderRegistry.getMissingModel();
    }
    
    @Override
    public IBakedModel bake(IModelState state, final VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    	
    	//System.out.println("Baking Whole Model");
    	    	
        ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(baseModel.getAllTransforms());
    	
    	ItemOverrideList overrides = GunOverrideHandler.INSTANCE;
    	
    	List<ResourceLocation> locations = new ArrayList<ResourceLocation>();
    	
    	locations.addAll(getDependenciesForMap());
    	    	    	
    	List<IBakedModel> models = new ArrayList<IBakedModel>();
    	
    	for (ResourceLocation location : locations) {
    		//System.out.println(location);
    		IBakedModel model = bakeModelPart(state, format, bakedTextureGetter, location);
    		models.add(model);
    	}
    	
    	return new BakedModelGun(this, models, Maps.newHashMap(PerspectiveMapWrapper.getTransforms(baseModel.getAllTransforms())), bakedTextureGetter.apply(getTexture()), format, overrides, Maps.newHashMap());
    	
    }

    public IBakedModel bakeModelPart(IModelState state, final VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ResourceLocation location)
    {
		//System.out.println("Baking Model: " + location);
		
		
		if (state instanceof MapModelState) {
			System.out.println("Getting New State");
			state = ((MapModelState) state).getState(location);
		}
		
        String modelPath = location.getResourcePath();
        if(location.getResourcePath().startsWith("models/"))
        {
            modelPath = modelPath.substring("models/".length());
        }
        ResourceLocation armatureLocation = new ResourceLocation(location.getResourceDomain(), "armatures/" + modelPath + ".json");
        ModelBlockAnimation animation = ModelBlockAnimation.loadVanillaAnimation(resourceManager, armatureLocation);
		ModelBlock model = null;

		if (location != null) {

			try {
				InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Reference.MOD_ID, "models/item/" + location.getResourcePath().replace("-", "") + ".json")).getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				model = ModelBlock.deserialize(reader);
				reader.close();
	
			} catch (IOException e) {
				System.out.println("Unable to find model: " + location);
				e.printStackTrace();
			}
			
		}
    	
    	
        if(!Attributes.moreSpecific(format, Attributes.DEFAULT_BAKED_FORMAT))
        {
            throw new IllegalArgumentException("can't bake vanilla models to the format that doesn't fit into the default one: " + format);
        }

        if(model == null) {
        	Main.LOGGER.error("Could Not Get Model For {}", location);
        	return getMissingModel().bake(getMissingModel().getDefaultState(), format, bakedTextureGetter);
        }

        List<TRSRTransformation> newTransforms = Lists.newArrayList();
        for(int i = 0; i < model.getElements().size(); i++)
        {
            newTransforms.add(state.apply(Optional.empty()).orElse(TRSRTransformation.identity()));
        }

        ItemCameraTransforms transforms = model.getAllTransforms();
        Map<TransformType, TRSRTransformation> tMap = Maps.newEnumMap(TransformType.class);
        tMap.putAll(PerspectiveMapWrapper.getTransforms(transforms));
        tMap.putAll(PerspectiveMapWrapper.getTransforms(state));
        IModelState perState = new SimpleModelState(ImmutableMap.copyOf(tMap));

        return bakeNormal(model, perState, state, newTransforms, format, bakedTextureGetter, false);
    }
    
	public IBakedModel bakeNormal(ModelBlock model, IModelState perState, final IModelState modelState, List<TRSRTransformation> newTransforms, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, boolean uvLocked) {

		final TRSRTransformation baseState = modelState.apply(Optional.empty()).orElse(TRSRTransformation.identity());
        TextureAtlasSprite particle = bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName("particle")));
        SimpleBakedModel.Builder builder = (new SimpleBakedModel.Builder(model, model.createOverrides())).setTexture(particle);
        for(int i = 0; i < model.getElements().size(); i++)
        {
            if(modelState.apply(Optional.of(Models.getHiddenModelPart(ImmutableList.of(Integer.toString(i))))).isPresent())
            {
                continue;
            }
            BlockPart part = model.getElements().get(i);
            TRSRTransformation transformation = baseState;
            if(newTransforms.get(i) != null)
            {
                transformation = newTransforms.get(i);
                BlockPartRotation rot = part.partRotation;
                if(rot == null) rot = new BlockPartRotation(new org.lwjgl.util.vector.Vector3f(), EnumFacing.Axis.Y, 0, false);
                part = new BlockPart(part.positionFrom, part.positionTo, part.mapFaces, rot, part.shade);
            }
            for(Map.Entry<EnumFacing, BlockPartFace> e : part.mapFaces.entrySet())
            {
                TextureAtlasSprite textureatlassprite1 = bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName(e.getValue().texture)));

                if (e.getValue().cullFace == null || !TRSRTransformation.isInteger(transformation.getMatrix()))
                {
                    builder.addGeneralQuad(makeBakedQuad(part, e.getValue(), textureatlassprite1, e.getKey(), transformation, uvLocked));
                }
                else
                {
                    builder.addFaceQuad(baseState.rotate(e.getValue().cullFace), makeBakedQuad(part, e.getValue(), textureatlassprite1, e.getKey(), transformation, uvLocked));
                }
            }
        }
        
        return new PerspectiveMapWrapperGun(builder.makeBakedModel(), perState);

	}

	@Override
    public IModelGun process(ImmutableMap<String, String> customData)
    {
		
		//System.out.println("Model Process");
		
		
        return new IModelGun(baseLocation);
    }
	
	@Override
	public IModelGun retexture(ImmutableMap<String, String> textures)
    {

		//System.out.println("Retexturing");
		
        ResourceLocation base = baseLocation;

        if (textures.containsKey("base"))
            base = new ResourceLocation(textures.get("base"));

        return new IModelGun(base);
    }
	
	protected static BufferedReader getModelReader(ResourceLocation modelLocation) {
		
		if (modelLocation.getResourcePath().contains("models")) {
			return null;
		}
		
		try {
			InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Reference.MOD_ID, "models/item/" + modelLocation.getResourcePath() + ".json")).getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			return reader;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected ResourceLocation getModelLocation(ResourceLocation location)
    {
        return new ResourceLocation(location.getResourceDomain(), "models/" + location.getResourcePath() + ".json");
    }
	
	protected BakedQuad makeBakedQuad(BlockPart p_177589_1_, BlockPartFace p_177589_2_, TextureAtlasSprite p_177589_3_, EnumFacing p_177589_4_, net.minecraftforge.common.model.ITransformation p_177589_5_, boolean p_177589_6_)
    {
        return new FaceBakery().makeBakedQuad(p_177589_1_.positionFrom, p_177589_1_.positionTo, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.partRotation, p_177589_6_, p_177589_1_.shade);
    }
	
	public enum GunModelLoader implements ICustomModelLoader {
		
		INSTANCE;

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {
			
		}

		@Override
		public boolean accepts(ResourceLocation modelLocation) {
			return modelLocation instanceof ModelResourceLocation && modelLocation.getResourceDomain().equals(Reference.MOD_ID) && modelLocation.getResourcePath().startsWith("_gun_");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception {
			Main.LOGGER.info("Loading Model: " + modelLocation);
			return new IModelGun(modelLocation);
		}
	}
	
	private static class GunOverrideHandler extends ItemOverrideList {

		public static final GunOverrideHandler INSTANCE = new GunOverrideHandler();
		
		public GunOverrideHandler() {
			super(ImmutableList.of());
		}
		
        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
        				
        	BakedModelGun model = (BakedModelGun)originalModel;
    				        	
        	BufferedReader reader = getModelReader(model.parent.baseLocation);
            Gson gson = new Gson();
            JsonElement je = null;
            try {
				je = gson.fromJson(reader, JsonElement.class);
            }
            catch (com.google.gson.JsonSyntaxException e) {
            }
            
            try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			if (je == null) {
				System.out.println("ERROR: JSON IS NULL");
				return originalModel;
			}
            
			JsonObject json = je.getAsJsonObject();

        	model.setSubTransforms(getSubTransformations(json, stack, world, entity));
        	
        	//System.out.println(model.subTransforms.size());
        	
        	ImmutableMap<TransformType, TRSRTransformation> baseTransforms = getBaseTransforms(json, stack, world, entity, model);
        	
        	if (baseTransforms != null) {
        		
        		model.setCameraTransforms(baseTransforms);
        		
        	}
        	
			List<ItemStack> accessories = ((ItemGun)stack.getItem()).getAccesories(stack);
			
			if (accessories != null) {
				model.addAccessories(accessories);
			}
        	
        	return model;
        }
		
	}
	
	private static ImmutableMap<TransformType, TRSRTransformation> getBaseTransforms(JsonObject json, ItemStack stack, World world, EntityLivingBase entity, BakedModelGun model) {
		
        ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(model.parent.baseModel.getAllTransforms());
        
        //System.out.println(transformMap);
        
    	ImmutableMap.Builder<TransformType, TRSRTransformation> transformBuilder = ImmutableMap.builder();
    	
		JsonObject baseTransforms = json.getAsJsonObject("basetransformation");
		
		if (baseTransforms == null) {
			return null;
		}
		
		JsonObject predicates = baseTransforms.getAsJsonObject("predicates");
				
		if (predicates == null) {
			System.out.println("ERROR: BASE PREDICATES NOT FOUND");
		}
		else {
						
			for (Entry<String, JsonElement> entry : predicates.entrySet()) {
				
				String name = entry.getKey();
				
				if (entry.getValue().isJsonObject()) {
					
					JsonObject predicate = (JsonObject) entry.getValue();
					
	        		IItemPropertyGetter iitempropertygetter = stack.getItem().getPropertyGetter(new ResourceLocation(name));
	        		
	        		if (predicate == null) {
	        			System.out.println("ERROR: PREDICATE IS NULL");
	        			continue;
	        		}
	        		
	        		for(TransformType typeName : TransformType.values()) {
	        			
	        			boolean flag = false;
	        			
		        		Vector3f[] defaultTransform = new Vector3f[] {new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1)};
	        			
	        			if (transformMap.get(typeName) == null) {
	        				//System.out.println("Added New TransformType");
	        				transformBuilder.put(typeName, TRSRTransformation.identity());
	        				continue;
	        			}
	        			
						TransformationBuilder transform = new TransformationBuilder();
		        		
		        		JsonArray type = predicate.getAsJsonArray(typeName.toString());
		        		
		        		if (type != null) {
		        			
		        			for (Object predicateObject : type) {
		        			
			        			JsonObject itemProperty = (JsonObject) predicateObject;
			        			
			        			if (!itemProperty.get("value").isJsonPrimitive()) {
			        				System.out.println("ERROR: PROPERTY IS NOT JSONPRIMITIVE");
			        				continue;
			        			}
			        			
			        			if (iitempropertygetter == null) {
			        				System.out.println("ERROR: ITEMPROPERTY NOT FOUND");
			        				continue;
			        			}
		
			        			JsonPrimitive valuePrimitive = itemProperty.getAsJsonPrimitive("value");
		
								//System.out.println("Setting Transforms for: " + name + ": value: " + iitempropertygetter.apply(stack, world, entity));
								
			        			if (valuePrimitive.isNumber()) {
	    							float value = valuePrimitive.getAsFloat();
	    							
	    							//System.out.println(iitempropertygetter.apply(stack, world, entity));
	    							
	    							if (Math.abs(value - iitempropertygetter.apply(stack, world, entity)) < 0.001) {
	    								
	    								flag = true;
	    								
	    								JsonPrimitive orderPrimitive = itemProperty.getAsJsonPrimitive("order");

	    								int order = 0;
		    							
		    							if (orderPrimitive != null) {
		    								order = orderPrimitive.getAsInt();
		    							}
	    								
	    								JsonObject jsonTransform = itemProperty.getAsJsonObject("transformation");
	                					JsonArray jsonTranslate = jsonTransform.getAsJsonArray("translation");
	                					Vector3f translate = null;
	                					if (jsonTranslate != null) {
		            						//System.out.println("Setting translation");
	                						float[] floats = new float[3];
	                						int i = 0;
	                						for (JsonElement element : jsonTranslate) {
	                							floats[i++] = element.getAsFloat();
	                						}
	                						translate = (new Vector3f(floats));
	                					}
	                					JsonArray jsonRotate = jsonTransform.getAsJsonArray("rotation");
	                					Vector3f rotate = null;
	                					if (jsonRotate != null) {
		            						//System.out.println("Setting rotation");
	                						float[] floats = new float[3];
	                						int i = 0;
	                						for (JsonElement element : jsonRotate) {
	                							floats[i++] = element.getAsFloat();
	                						}
	                						rotate = new Vector3f(floats);
	                					}
	                					JsonArray jsonCenter = jsonTransform.getAsJsonArray("center");
	                					Vector3f center = null;
	                					if (jsonCenter != null) {
		            						//System.out.println("Setting center");
	                						float[] floats = new float[3];
	                						int i = 0;
	                						for (JsonElement element : jsonCenter) {
	                							floats[i++] = element.getAsFloat();
	                						}
	                						center = new Vector3f(floats);
	                					}
	                					JsonArray jsonScale = jsonTransform.getAsJsonArray("scale");
	                					Vector3f scale = null;
	                					if (jsonScale != null) {
		            						//System.out.println("Setting scale");
	                						float[] floats = new float[3];
	                						int i = 0;
	                						for (JsonElement element : jsonScale) {
	                							floats[i++] = element.getAsFloat();
	                						}
	                						scale = new Vector3f(floats);
	                					}
	                						                					
	                					transform.add(translate, rotate, center, scale, order);

		            					//System.out.println("Transform set to: " + transform);

	    							}
	    						}
	    						else if (valuePrimitive.isString() && valuePrimitive.getAsString().matches("default")) {
	    							JsonObject jsonTransform = itemProperty.getAsJsonObject("transformation");
                					JsonArray jsonTranslate = jsonTransform.getAsJsonArray("translation");
                					Vector3f translate = null;
                					if (jsonTranslate != null) {
	            						//System.out.println("Setting translation");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonTranslate) {
                							floats[i++] = element.getAsFloat();
                						}
                						translate = (new Vector3f(floats));
                					}
                					JsonArray jsonRotate = jsonTransform.getAsJsonArray("rotation");
                					Vector3f rotate = null;
                					if (jsonRotate != null) {
	            						//System.out.println("Setting rotation");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonRotate) {
                							floats[i++] = element.getAsFloat();
                						}
                						rotate = new Vector3f(floats);
                					}
                					JsonArray jsonCenter = jsonTransform.getAsJsonArray("center");
                					Vector3f center = null;
                					if (jsonCenter != null) {
	            						//System.out.println("Setting center");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonCenter) {
                							floats[i++] = element.getAsFloat();
                						}
                						center = new Vector3f(floats);
                					}
                					JsonArray jsonScale = jsonTransform.getAsJsonArray("scale");
                					Vector3f scale = null;
                					if (jsonScale != null) {
	            						//System.out.println("Setting scale");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonScale) {
                							floats[i++] = element.getAsFloat();
                						}
                						scale = new Vector3f(floats);
                					}
                					                					
                					defaultTransform[0] = translate;
                					defaultTransform[1] = rotate;
                					defaultTransform[2] = center;
                					defaultTransform[3] = scale;
                					
	    						}
	    						else if(valuePrimitive.isString() && valuePrimitive.getAsString().matches("variable")) {
	    							
	    							flag = true;
	    							
	    							float modifier = iitempropertygetter.apply(stack, world, entity);
	    							
	    							JsonPrimitive orderPrimitive = itemProperty.getAsJsonPrimitive("order");
	    							
	    							int order = 0;
	    							
	    							if (orderPrimitive != null) {
	    								order = orderPrimitive.getAsInt();
	    							}
	    							
	    							JsonObject jsonTransform = itemProperty.getAsJsonObject("transformation");
                					JsonArray jsonTranslate = jsonTransform.getAsJsonArray("translation");
                					Vector3f translate = null;
                					if (jsonTranslate != null) {
	            						//System.out.println("Setting translation");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonTranslate) {
                							floats[i++] = element.getAsFloat() * modifier;
                						}
                						translate = (new Vector3f(floats));
                					}
                					JsonArray jsonRotate = jsonTransform.getAsJsonArray("rotation");
                					Vector3f rotate = null;
                					if (jsonRotate != null) {
	            						//System.out.println("Setting rotation");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonRotate) {
                							floats[i++] = element.getAsFloat() * modifier;
                						}
                						rotate = new Vector3f(floats);
                					}
                					JsonArray jsonCenter = jsonTransform.getAsJsonArray("center");
                					Vector3f center = null;
                					if (jsonCenter != null) {
	            						//System.out.println("Setting center");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonCenter) {
                							floats[i++] = element.getAsFloat();
                						}
                						center = new Vector3f(floats);
                					}
                					JsonArray jsonScale = jsonTransform.getAsJsonArray("scale");
                					Vector3f scale = null;
                					if (jsonScale != null) {
	            						//System.out.println("Setting scale");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonScale) {
                							floats[i++] = element.getAsFloat() * modifier;
                						}
                						scale = new Vector3f(floats);
                					}
                   					
                					transform.add(translate, rotate, center, scale, order);
                					
	    						}
								else {
		    	    				System.out.println("ERROR: PREDICATE VALUE TYPE IS NOT SUPPORTED");
								}
			        		}
		        			if (!flag) {
		        				transform.add(defaultTransform[0], defaultTransform[1], defaultTransform[2], defaultTransform[3], 0);
		        			}
		        		}
		        		else {
		        			//System.out.println("WARNING: " + typeName.toString() + " NOT FOUND");
		        		}
		        		
    					transformBuilder.put(typeName, transformMap.get(typeName).compose(transform.build()));
    					//System.out.println(typeName + ": " + transformMap.get(typeName).compose(transform));
	        		}
					transformMap = transformBuilder.build();
					transformBuilder = ImmutableMap.builder();
				}
				else {
					System.out.println("ERROR: " + name + " IS NOT A JSONOBJECT");
				}
				
			}
		}
		return transformMap;
	}
	
	private static List<TRSRTransformation> getSubTransformations(JsonObject json, ItemStack stack, World world, EntityLivingBase entity) {
		
		List<TRSRTransformation> transformations = new ArrayList<TRSRTransformation>();
		
		JsonArray submodels = json.getAsJsonArray("submodels");

		if (submodels != null) {
			
			for (Object submodelObject : submodels) {
				
				JsonObject submodel = (JsonObject) submodelObject;
				JsonObject predicates = submodel.getAsJsonObject("predicates");
				
				TransformationBuilder finalTransform = new TransformationBuilder();
				
				if (predicates != null) {

					for (Entry<String, JsonElement> entry : predicates.entrySet()) {
						
						String name = entry.getKey();
						
						//System.out.println(name);
						
						if (entry.getValue().isJsonArray()) {
						
    						JsonArray predicate = (JsonArray) entry.getValue();
    						
    		        		IItemPropertyGetter iitempropertygetter = stack.getItem().getPropertyGetter(new ResourceLocation(name));
    		        		
    		        		if (iitempropertygetter == null && !name.equals("none")) {
    		        			System.out.println("ERROR: " + name + " PROPERTY DOES NOT EXIST");
    		        			continue;
    		        		}
    		        		
    		        		if (predicate == null) {
    		        			System.out.println("ERROR: PREDICATE IS NULL");
    		        			continue;
    		        		}
    		        		
    		        		boolean flag = false;
    		        		
    		        		Vector3f[] defaultTransform = new Vector3f[] {new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1)};
    		        		
    		        		for (Object predicateObject : predicate) {
    		        			
    		        			JsonObject itemProperty = (JsonObject) predicateObject;
    						
    		        			if (!itemProperty.get("value").isJsonPrimitive()) {
    		        				System.out.println("ERROR: PROPERTY IS NOT JSONPRIMITIVE");
    		        				continue;
    		        			}

    		        			JsonPrimitive valuePrimitive = itemProperty.getAsJsonPrimitive("value");
    		        			
	    						//System.out.println("Setting Transforms for: " + name);
	    						
	    						if (valuePrimitive.isNumber()) {
	    							float value = valuePrimitive.getAsFloat();
	    							
	    							//System.out.println(iitempropertygetter.apply(stack, world, entity));
	    							
	    							if (Math.abs(value - iitempropertygetter.apply(stack, world, entity)) < 0.001) {
	    								
	    								flag = true;
	    								
	    								JsonPrimitive orderPrimitive = itemProperty.getAsJsonPrimitive("order");

	    								int order = 0;
		    							
		    							if (orderPrimitive != null) {
		    								order = orderPrimitive.getAsInt();
		    							}
	    								
	    								JsonObject jsonTransform = itemProperty.getAsJsonObject("transformation");
	                					JsonArray jsonTranslate = jsonTransform.getAsJsonArray("translation");
	                					Vector3f translate = null;
	                					if (jsonTranslate != null) {
		            						//System.out.println("Setting translation");
	                						float[] floats = new float[3];
	                						int i = 0;
	                						for (JsonElement element : jsonTranslate) {
	                							floats[i++] = element.getAsFloat();
	                						}
	                						translate = (new Vector3f(floats));
	                					}
	                					JsonArray jsonRotate = jsonTransform.getAsJsonArray("rotation");
	                					Vector3f rotate = null;
	                					if (jsonRotate != null) {
		            						//System.out.println("Setting rotation");
	                						float[] floats = new float[3];
	                						int i = 0;
	                						for (JsonElement element : jsonRotate) {
	                							floats[i++] = element.getAsFloat();
	                						}
	                						rotate = new Vector3f(floats);
	                					}
	                					JsonArray jsonCenter = jsonTransform.getAsJsonArray("center");
	                					Vector3f center = null;
	                					if (jsonCenter != null) {
		            						//System.out.println("Setting center");
	                						float[] floats = new float[3];
	                						int i = 0;
	                						for (JsonElement element : jsonCenter) {
	                							floats[i++] = element.getAsFloat();
	                						}
	                						center = new Vector3f(floats);
	                					}
	                					JsonArray jsonScale = jsonTransform.getAsJsonArray("scale");
	                					Vector3f scale = null;
	                					if (jsonScale != null) {
		            						//System.out.println("Setting scale");
	                						float[] floats = new float[3];
	                						int i = 0;
	                						for (JsonElement element : jsonScale) {
	                							floats[i++] = element.getAsFloat();
	                						}
	                						scale = new Vector3f(floats);
	                					}
	                						                					
		            					finalTransform.add(translate, rotate, center, scale, order);

		            					//System.out.println("Transform set to: " + transform);

	    							}
	    						}
	    						else if (valuePrimitive.isString() && valuePrimitive.getAsString().matches("default")) {
	    							JsonObject jsonTransform = itemProperty.getAsJsonObject("transformation");
                					JsonArray jsonTranslate = jsonTransform.getAsJsonArray("translation");
                					Vector3f translate = null;
                					if (jsonTranslate != null) {
	            						//System.out.println("Setting translation");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonTranslate) {
                							floats[i++] = element.getAsFloat();
                						}
                						translate = (new Vector3f(floats));
                					}
                					JsonArray jsonRotate = jsonTransform.getAsJsonArray("rotation");
                					Vector3f rotate = null;
                					if (jsonRotate != null) {
	            						//System.out.println("Setting rotation");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonRotate) {
                							floats[i++] = element.getAsFloat();
                						}
                						rotate = new Vector3f(floats);
                					}
                					JsonArray jsonCenter = jsonTransform.getAsJsonArray("center");
                					Vector3f center = null;
                					if (jsonCenter != null) {
	            						//System.out.println("Setting center");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonCenter) {
                							floats[i++] = element.getAsFloat();
                						}
                						center = new Vector3f(floats);
                					}
                					JsonArray jsonScale = jsonTransform.getAsJsonArray("scale");
                					Vector3f scale = null;
                					if (jsonScale != null) {
	            						//System.out.println("Setting scale");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonScale) {
                							floats[i++] = element.getAsFloat();
                						}
                						scale = new Vector3f(floats);
                					}
                					                					
                					defaultTransform[0] = translate;
                					defaultTransform[1] = rotate;
                					defaultTransform[2] = center;
                					defaultTransform[3] = scale;
                					
	    						}
	    						else if(valuePrimitive.isString() && valuePrimitive.getAsString().matches("variable")) {
	    							
	    							flag = true;
	    							
	    							float modifier = iitempropertygetter.apply(stack, world, entity);
	    							
	    							JsonPrimitive orderPrimitive = itemProperty.getAsJsonPrimitive("order");
	    							
	    							int order = 0;
	    							
	    							if (orderPrimitive != null) {
	    								order = orderPrimitive.getAsInt();
	    							}
	    							
	    							JsonObject jsonTransform = itemProperty.getAsJsonObject("transformation");
                					JsonArray jsonTranslate = jsonTransform.getAsJsonArray("translation");
                					Vector3f translate = null;
                					if (jsonTranslate != null) {
	            						//System.out.println("Setting translation");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonTranslate) {
                							floats[i++] = element.getAsFloat() * modifier;
                						}
                						translate = (new Vector3f(floats));
                					}
                					JsonArray jsonRotate = jsonTransform.getAsJsonArray("rotation");
                					Vector3f rotate = null;
                					if (jsonRotate != null) {
	            						//System.out.println("Setting rotation");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonRotate) {
                							floats[i++] = element.getAsFloat() * modifier;
                						}
                						rotate = new Vector3f(floats);
                					}
                					JsonArray jsonCenter = jsonTransform.getAsJsonArray("center");
                					Vector3f center = null;
                					if (jsonCenter != null) {
	            						//System.out.println("Setting center");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonCenter) {
                							floats[i++] = element.getAsFloat();
                						}
                						center = new Vector3f(floats);
                					}
                					JsonArray jsonScale = jsonTransform.getAsJsonArray("scale");
                					Vector3f scale = null;
                					if (jsonScale != null) {
	            						//System.out.println("Setting scale");
                						float[] floats = new float[3];
                						int i = 0;
                						for (JsonElement element : jsonScale) {
                							floats[i++] = element.getAsFloat() * modifier;
                						}
                						scale = new Vector3f(floats);
                					}
                   					
                					finalTransform.add(translate, rotate, center, scale, order);
                					
	    						}
	    						else {
	        	    				System.out.println("ERROR: PREDICATE VALUE TYPE IS NOT SUPPORTED");
	    						}
    		        		}
    		        		
    		        		if (!flag || name.equals("none")) {
    		        			finalTransform.add(defaultTransform[0], defaultTransform[1], defaultTransform[2], defaultTransform[3], 0);
    		        		}
    		        		
      	                }
						else {
							System.out.println("ERROR: " + name + " IS NOT A JSONARRAY");
						}
					}
				}
				else {
    				System.out.println("ERROR: JSON MISSING PREDICATES");
				}
				
				transformations.add(finalTransform.build());
				
			}
		}
		else {
			System.out.println("ERROR: JSON MISSING SUBMODELS");
		}
		
		return transformations;
		
	}
	
	public static class PerspectiveMapWrapperGun extends PerspectiveMapWrapper{
		
		private ImmutableMap<TransformType, TRSRTransformation> transforms;

		public PerspectiveMapWrapperGun(IBakedModel parent, ImmutableMap<TransformType, TRSRTransformation> transforms) {
			super(parent, transforms);
			this.transforms = transforms;
		}
		
		public PerspectiveMapWrapperGun(IBakedModel parent, IModelState state) {
			this(parent, getTransforms(state));
		}

	    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms(IModelState state)
	    {
	        EnumMap<ItemCameraTransforms.TransformType, TRSRTransformation> map = new EnumMap<>(ItemCameraTransforms.TransformType.class);
	        for(ItemCameraTransforms.TransformType type : ItemCameraTransforms.TransformType.values())
	        {
	            Optional<TRSRTransformation> tr = state.apply(Optional.of(type));
	            if(tr.isPresent())
	            {
	                map.put(type, tr.get());
	            }
	        }
	        return ImmutableMap.copyOf(map);
	    }
		
	}
	
	private static final class BakedModelGun implements IBakedModel
    {
        private final IModelGun parent;
        private final List<IBakedModel> models;
        private List<BakedQuad> quads = new ArrayList<BakedQuad>();
        private Map<TransformType, TRSRTransformation> transforms;
        private List<TRSRTransformation> subTransforms = new ArrayList<TRSRTransformation>();
        private final TextureAtlasSprite particle;
        private final VertexFormat format;
        private final ItemOverrideList overrides;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final List<IBakedModel> accessoryModels = new ArrayList<IBakedModel>();

        public BakedModelGun(IModelGun parent,
                              List<IBakedModel> models,
                              Map<TransformType, TRSRTransformation> transforms,
                              TextureAtlasSprite particle,
                              VertexFormat format,
                              ItemOverrideList overrides,
                              Map<String, IBakedModel> cache)
        {
            this.parent = parent;
            this.models = models;
            this.transforms = transforms;
            this.particle = particle;
            this.format = format;
            this.overrides = overrides;
            this.cache = cache;
        }

		public void setSubTransforms(List<TRSRTransformation> subTransformations) {
			this.subTransforms = new ArrayList<TRSRTransformation>();
	        this.subTransforms.add(TRSRTransformation.identity());
	        this.subTransforms.addAll(subTransformations);
	    }
		
		public void addAccessories(List<ItemStack> list) {
			for (ItemStack stack : list) {
				IBakedModel model;
				TRSRTransformation subtransform;
				
				int[] ints = stack.getTagCompound().getIntArray("transform");
				float[] floats = new float[16];
				for (int i = 0; i < 16; i++) {
					floats[i] = Float.intBitsToFloat(ints[i]);
				}
				subtransform = new TRSRTransformation(new Matrix4f(floats));
				
				if (cache.containsKey(stack.toString())) {
					model = cache.get(stack.toString());
				}
				else {
					model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, null, null);
					cache.put(stack.toString(), model);
				}
				subTransforms.add(subtransform);
				for (Entry<TransformType, TRSRTransformation> entry : transforms.entrySet()) {
					if (entry.getKey() == TransformType.FIRST_PERSON_LEFT_HAND || entry.getKey() == TransformType.FIRST_PERSON_RIGHT_HAND) {
						NBTTagCompound datatag = Minecraft.getMinecraft().world.getCapability(ItemDataProvider.ITEMDATA_CAP, null).getItemData();
						NBTTagCompound itemtag = datatag.getCompoundTag(stack.getTagCompound().getString("UUID"));
						if (itemtag.getBoolean("ADS")) {
							//entry.setValue(entry.getValue().compose(TRSRTransformation.blockCenterToCorner(new TRSRTransformation(model.handlePerspective(TransformType.FIRST_PERSON_LEFT_HAND).getRight()))));
							entry.setValue(TRSRTransformation.blockCenterToCorner(new TRSRTransformation(model.handlePerspective(TransformType.FIRST_PERSON_LEFT_HAND).getRight())).compose(subtransform.inverse()));
						}
					}
				}
				if (!models.contains(model)) {
					this.models.add(model);
				}
			}
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
			
			Long start = System.nanoTime();
			
			quads.clear();
			if (this.subTransforms == null) {
				return quads;
			}
			//System.out.println(this.subTransforms.size() + ", " + models.size());
			if (this.subTransforms.size() <= models.size()) {
				int i;
				for (i = 0; i < subTransforms.size(); i++) {

					for (BakedQuad quad : models.get(i).getQuads(state, side, rand)) {
						
						//System.out.println(quad.getFormat());
												
						int[] data = quad.getVertexData().clone();
						
						//System.out.println(data);

						Matrix4f points = new Matrix4f();
						Matrix4f transformation = this.subTransforms.get(i).getMatrix();
						
						if (transformation == TRSRTransformation.identity().getMatrix()) {
							quads.add(quad);
							continue;
						}
						
						points.m00 = Float.intBitsToFloat(data[0]);
						points.m10 = Float.intBitsToFloat(data[1]);
						points.m20 = Float.intBitsToFloat(data[2]);
						points.m30 = 1;
						
						points.m01 = Float.intBitsToFloat(data[7]);
						points.m11 = Float.intBitsToFloat(data[8]);
						points.m21 = Float.intBitsToFloat(data[9]);
						points.m31 = 1;
						
						points.m02 = Float.intBitsToFloat(data[14]);
						points.m12 = Float.intBitsToFloat(data[15]);
						points.m22 = Float.intBitsToFloat(data[16]);
						points.m32 = 1;
						
						points.m03 = Float.intBitsToFloat(data[21]);
						points.m13 = Float.intBitsToFloat(data[22]);
						points.m23 = Float.intBitsToFloat(data[23]);
						points.m33 = 1;
												
						points.mul(transformation, points);
						
						int norm = calculateNormal(points);
												
						data[0] = Float.floatToRawIntBits(points.m00);
						data[1] = Float.floatToRawIntBits(points.m10);
						data[2] = Float.floatToRawIntBits(points.m20);
						data[6] = norm;
	
						data[7] = Float.floatToRawIntBits(points.m01);
						data[8] = Float.floatToRawIntBits(points.m11);
						data[9] = Float.floatToRawIntBits(points.m21);
						data[13] = norm;

						data[14] = Float.floatToRawIntBits(points.m02);
						data[15] = Float.floatToRawIntBits(points.m12);
						data[16] = Float.floatToRawIntBits(points.m22);
						data[20] = norm;

						data[21] = Float.floatToRawIntBits(points.m03);
						data[22] = Float.floatToRawIntBits(points.m13);
						data[23] = Float.floatToRawIntBits(points.m23);
						data[27] = norm;						
						
						BakedQuad newQuad = new BakedQuad(data, quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
						
						quads.add(newQuad);
					}
				}
			}
			//System.out.println(quads.size());
			
			if (quads.size() > 0) {
				//System.out.println("took " + (double)(System.nanoTime() - start)/50000000D + " frames to get " + quads.size() + " quads for model " + this.parent.baseLocation);
			}
			
			return quads;
		}
		
		private int calculateNormal(Matrix4f points) {

		    float xp = points.m03-points.m01;
		    float yp = points.m13-points.m11;
		    float zp = points.m23-points.m21;

		    float xq = points.m02-points.m00;
		    float yq = points.m12-points.m10;
		    float zq = points.m22-points.m20;

		    //Cross Product
		    float xn = yq*zp - zq*yp;
		    float yn = zq*xp - xq*zp;
		    float zn = xq*yp - yq*xp;

		    //Normalize
		    float norm = (float)Math.sqrt(xn*xn + yn*yn + zn*zn);
		    final float SMALL_LENGTH =  1.0E-4F;  //Vec3d.normalise() uses this
		    if (norm < SMALL_LENGTH) norm = 1.0F;  // protect against degenerate quad

		    norm = 1.0F / norm;
		    xn *= norm;
		    yn *= norm;
		    zn *= norm;

		    int x = ((byte)(xn * 127)) & 0xFF;
		    int y = ((byte)(yn * 127)) & 0xFF;
		    int z = ((byte)(zn * 127)) & 0xFF;
		    return x | (y << 0x08) | (z << 0x10);
		}
		
		@Override
		public boolean isAmbientOcclusion() {
			return false;
		}

		@Override
		public boolean isGui3d() {
			return false;
		}

		@Override
		public boolean isBuiltInRenderer() {
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleTexture() {
			return this.particle;
		}

		@Override
		public ItemOverrideList getOverrides() {
			return this.overrides;
		}
		
	    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
	    {
	        return PerspectiveMapWrapper.handlePerspective(this, ImmutableMap.copyOf(transforms), type);
	    }
		
		public void setCameraTransforms (ImmutableMap<TransformType, TRSRTransformation> transforms) {
			this.transforms = Maps.newHashMap(transforms);
		}
		
    }

	public static List<String> getPredicates(JsonObject json) {
		
		List<String> names = new ArrayList<String>();

		JsonArray submodels = json.getAsJsonArray("submodels");
		
		for (Object submodelObject : submodels) {
			
			JsonObject submodel = (JsonObject) submodelObject;
			
			JsonObject predicates = submodel.getAsJsonObject("predicates");
			
			for (Entry<String, JsonElement> entry : predicates.entrySet()) {
				
				String name = entry.getKey();
				
				names.add(name);
								
			}
			
		}
				
		return names;
	}
}
