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
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class IModelClip implements IModel {
	
	private static IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
	
	public static final IModelClip MODEL = new IModelClip();
	
	@Nullable
	private ResourceLocation baseLocation;
	
	private ModelBlock baseModel;
	
	public IModelClip() {
		this(null);
	}
	
	public IModelClip(@Nullable ResourceLocation baseLocation) {
		this.baseLocation = baseLocation;
		
		ModelBlock model = null;

		if (baseLocation != null) {

			BufferedReader reader = getModelReader(baseLocation);
			
			if (reader != null) {
				model = ModelBlock.deserialize(reader);
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
    	
    	List<ResourceLocation> list = getParts(baseLocation);
    	
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
    	
    	//System.out.println(list);
    	
        if (list != null)
            builder.addAll(list);

        return builder.build();
    }
    
    public IModel getMissingModel() {
    	return ModelLoaderRegistry.getMissingModel();
    }
    
    @Override
    public IBakedModel bake(IModelState state, final VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    	
    	//System.out.println("Baking Whole Model");
    	    	
        ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(baseModel.getAllTransforms());
    	
    	ItemOverrideList overrides = ClipOverrideHandler.INSTANCE;
    	
    	List<ResourceLocation> locations = new ArrayList<ResourceLocation>();
    	
    	locations.add(baseLocation);
    	locations.addAll(getDependenciesForMap());
    	    	    	
    	List<IBakedModel> models = new ArrayList<IBakedModel>();
    	
    	for (ResourceLocation location : locations) {
    		IBakedModel model = bakeModelPart(state, format, bakedTextureGetter, location);
    		models.add(model);
    	}
    	
    	return new BakedModelClip(this, models, PerspectiveMapWrapper.getTransforms(baseModel.getAllTransforms()), bakedTextureGetter.apply(getTexture()), format, overrides, Maps.newHashMap());
    	
    }

    public IBakedModel bakeModelPart(IModelState state, final VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ResourceLocation location)
    {
		//System.out.println("Baking Model: " + location);
		
		
		if (state instanceof MapModelState) {
			//System.out.println("Getting New State");
			state = ((MapModelState) state).getState(location);
		}
        
		ModelBlock model = null;

		if (location != null) {

			try {
				InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Reference.MOD_ID, "models/item/" + location.getResourcePath().replace("-", "") + ".json")).getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				model = ModelBlock.deserialize(reader);
	
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
    	
    	
        if(!Attributes.moreSpecific(format, Attributes.DEFAULT_BAKED_FORMAT))
        {
            throw new IllegalArgumentException("can't bake vanilla models to the format that doesn't fit into the default one: " + format);
        }

        if(model == null) return getMissingModel().bake(getMissingModel().getDefaultState(), format, bakedTextureGetter);

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
    public IModelClip process(ImmutableMap<String, String> customData)
    {
		
		//System.out.println("Model Process");
		
		
        return new IModelClip(baseLocation);
    }
	
	@Override
	public IModelClip retexture(ImmutableMap<String, String> textures)
    {

		//System.out.println("Retexturing");
		
        ResourceLocation base = baseLocation;

        if (textures.containsKey("base"))
            base = new ResourceLocation(textures.get("base"));

        return new IModelClip(base);
    }
	
	protected static BufferedReader getModelReader(ResourceLocation modelLocation) {
		
		if (modelLocation.getResourcePath().contains("model")) {
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
					JsonObject predicates = jsonmodel.getAsJsonObject("predicates");
					for (Entry<String, JsonElement> entry : predicates.entrySet()) {
						JsonArray array = entry.getValue().getAsJsonArray();
						for (int i = 0; i < array.size(); i++) {
							if (array.get(i).getAsJsonObject().get("value").isJsonPrimitive() && array.get(i).getAsJsonObject().getAsJsonPrimitive("value").getAsString().equals("default")) continue;
							//System.out.println("added: " + modelLocation);
							list.add(modelLocation);
						}
					}
					//System.out.println(modelLocation);
					//list.add(modelLocation);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println(list);
		
		return list;
		
	}
	
	protected BakedQuad makeBakedQuad(BlockPart p_177589_1_, BlockPartFace p_177589_2_, TextureAtlasSprite p_177589_3_, EnumFacing p_177589_4_, net.minecraftforge.common.model.ITransformation p_177589_5_, boolean p_177589_6_)
    {
        return new FaceBakery().makeBakedQuad(p_177589_1_.positionFrom, p_177589_1_.positionTo, p_177589_2_, p_177589_3_, p_177589_4_, p_177589_5_, p_177589_1_.partRotation, p_177589_6_, p_177589_1_.shade);
    }
	
	public enum ClipModelLoader implements ICustomModelLoader {
		
		INSTANCE;

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {
			
		}

		@Override
		public boolean accepts(ResourceLocation modelLocation) {
			return modelLocation.getResourceDomain().equals(Reference.MOD_ID) && modelLocation.getResourcePath().contains("_clip_");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception {
			Main.LOGGER.info("Loading Model: " + modelLocation);
			return new IModelClip(modelLocation);
		}
	}
	
	private static class ClipOverrideHandler extends ItemOverrideList {

		public static final ClipOverrideHandler INSTANCE = new ClipOverrideHandler();
		
		public ClipOverrideHandler() {
			super(ImmutableList.of());
		}
		
        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
			
        	BakedModelClip model = (BakedModelClip)originalModel;
        	        	        				        	
        	BufferedReader reader = getModelReader(model.parent.baseLocation);
            Gson gson = new Gson();
            
			JsonElement je = gson.fromJson(reader, JsonElement.class);
			if (je == null) {
				System.out.println("ERROR: JSON IS NULL");
				return originalModel;
			}
			JsonObject json = je.getAsJsonObject();
			
			List<String> names = getPredicates(json);
			
        	String Key = "";
        	
        	for (String name : names) {
        		IItemPropertyGetter iitempropertygetter = stack.getItem().getPropertyGetter(new ResourceLocation(name));
        		
        		if (iitempropertygetter == null) {
        			System.out.println("ERROR: ITEM PROPERTY: " + name + " IS NULL");
        			continue;
        		}
        		
        		//System.out.println(((Float) iitempropertygetter.apply(stack, world, entity)).toString());
        		
        		Key = Key.concat(((Float) iitempropertygetter.apply(stack, world, entity)).toString());
        		
        	}
        	
        	//System.out.println("Getting Correct BakedModel for Key: " + Key);

        	if (!model.cache.containsKey(Key)) {
        		//System.out.println("Creating New BakedModel For Key: " + Key);
        		
        		IModel parent = model.parent;
        		        		
        		Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                
                Map<Wrapper, IModelState> map = new HashMap<Wrapper, IModelState>();
                
    			List<TRSRTransformation> transformations = new ArrayList<TRSRTransformation>();
    			
    			transformations.add(TRSRTransformation.identity());
    			
    			transformations.addAll(getSubTransformations(json, stack, world, entity));
    			    			
                int i = 0;
                
                for (ResourceLocation location : ((IModelClip)parent).getDependenciesForMap()) {
                	Map<ItemCameraTransforms.TransformType, TRSRTransformation> map2 = new HashMap<ItemCameraTransforms.TransformType, TRSRTransformation>();
                	for (ItemCameraTransforms.TransformType type : ItemCameraTransforms.TransformType.values()) {
                		map2.put(type, transformations.get(i));
                	}
                	map.put(MapModelState.wrap(location), new SimpleModelState(ImmutableMap.copyOf(map2), Optional.ofNullable(transformations.get(i))));
                	i++;
                }
                
                MapModelState state = new MapModelState(map);
                
                IBakedModel bakedModel = parent.bake(state, model.format, textureGetter);
                model.cache.put(Key, bakedModel);
        	}

        	BakedModelClip newModel = (BakedModelClip) model.cache.get(Key);
        	
        	ImmutableMap<TransformType, TRSRTransformation> baseTransforms = getBaseTransforms(json, stack, world, entity, model);
        	
        	if (baseTransforms != null) {
        		
        		newModel.setCameraTransforms(baseTransforms);
        		
        	}
        	
        	return newModel;
        }
		
	}
	
	private static ImmutableMap<TransformType, TRSRTransformation> getBaseTransforms(JsonObject json, ItemStack stack, World world, EntityLivingBase entity, BakedModelClip model) {
		
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
	        			
	        			if (transformMap.get(typeName) == null) {
	        				//System.out.println("Added New TransformType");
	        				transformBuilder.put(typeName, TRSRTransformation.identity());
	        				continue;
	        			}
	        			
						TRSRTransformation transform = TRSRTransformation.identity();
		        		
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
																		
									if (Math.abs(value - iitempropertygetter.apply(stack, world, entity)) < 0.001) {
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
		            					Quat4f rotLeft = null;
		            					if (jsonRotate != null) {
		            						//System.out.println("Setting rotation");
		            						float[] floats = new float[4];
		            						int i = 0;
		            						for (JsonElement element : jsonRotate) {
		            							floats[i++] = element.getAsFloat();
		            						}
		            						floats[3] = 1;
		            						rotLeft = new Quat4f(floats);
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
		            					
		            					transform = transform.compose(TRSRTransformation.blockCenterToCorner(new TRSRTransformation(translate, rotLeft, scale, null)));
		            					
		            							
		            					//System.out.println("Transform set to: " + transform);
		
									}
								}
								else {
		    	    				System.out.println("ERROR: PREDICATE VALUE TYPE IS NOT SUPPORTED");
								}
			        		}
		        		}
		        		else {
		        			//System.out.println("WARNING: " + typeName.toString() + " NOT FOUND");
		        		}
		        		
    					transformBuilder.put(typeName, transformMap.get(typeName).compose(transform));
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
								
				if (predicates != null) {

					for (Entry<String, JsonElement> entry : predicates.entrySet()) {
						
						String name = entry.getKey();
						
						if (entry.getValue().isJsonArray()) {
						
    						JsonArray predicate = (JsonArray) entry.getValue();
    						
    		        		IItemPropertyGetter iitempropertygetter = stack.getItem().getPropertyGetter(new ResourceLocation(name));
    		        		
    		        		if (iitempropertygetter == null) {
    		        			System.out.println("ERROR: PROPERTY DOES NOT EXIST");
    		        			continue;
    		        		}
    		        		
    		        		if (predicate == null) {
    		        			System.out.println("ERROR: PREDICATE IS NULL");
    		        			continue;
    		        		}
    		        		    		        		
    		        		TRSRTransformation defaultTransform = new TRSRTransformation(null, null, null, null);
    		        		
    		        		for (Object predicateObject : predicate) {
    		        			
    		        			JsonObject itemProperty = (JsonObject) predicateObject;
    						
    		        			if (!itemProperty.get("value").isJsonPrimitive()) {
    		        				System.out.println("ERROR: PROPERTY IS NOT JSONPRIMITIVE");
    		        				continue;
    		        			}

    		        			JsonPrimitive valuePrimitive = itemProperty.getAsJsonPrimitive("value");

	    						//System.out.println("Setting Transforms for: " + name);
    		        			
    		        			if (valuePrimitive.isString() && valuePrimitive.getAsString().matches("default")) {
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
                					Quat4f rotLeft = null;
                					if (jsonRotate != null) {
	            						//System.out.println("Setting rotation");
                						float[] floats = new float[4];
                						int i = 0;
                						for (JsonElement element : jsonRotate) {
                							floats[i++] = element.getAsFloat();
                						}
                						floats[3] = 1;
                						rotLeft = new Quat4f(floats);
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
                					
                					defaultTransform = new TRSRTransformation(translate, rotLeft, scale, null);
                					        		        			
	    						}
    		        			else if (valuePrimitive.isNumber()) {
	    							float value = valuePrimitive.getAsFloat();
	    							
	    							if (iitempropertygetter.apply(stack, world, entity) >= value) {
	    								
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
	                					Quat4f rotLeft = null;
	                					if (jsonRotate != null) {
		            						//System.out.println("Setting rotation");
	                						float[] floats = new float[4];
	                						int i = 0;
	                						for (JsonElement element : jsonRotate) {
	                							floats[i++] = element.getAsFloat();
	                						}
	                						floats[3] = 1;
	                						rotLeft = new Quat4f(floats);
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
	                					
	                					TRSRTransformation transform = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(translate, rotLeft, scale, null));
	                					
		            					transformations.add(transform);

		            					//System.out.println("Transform set to: " + transform);

	    							} else {
	    								
	        		        			transformations.add(defaultTransform);

	    							}
	    						}
	    						else {
	        	    				System.out.println("ERROR: PREDICATE VALUE TYPE IS NOT SUPPORTED");
	    						}
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
	
	private static final class BakedModelClip implements IBakedModel
    {
        private final IModelClip parent;
        private final List<IBakedModel> models;
        private ImmutableMap<TransformType, TRSRTransformation> transforms;
        private final TextureAtlasSprite particle;
        private final VertexFormat format;
        private final ItemOverrideList overrides;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change

        public BakedModelClip(IModelClip parent,
                              List<IBakedModel> models,
                              ImmutableMap<TransformType, TRSRTransformation> transforms,
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

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
						
			List<BakedQuad> quads = new ArrayList<BakedQuad>();
			
			for (IBakedModel model : models) {
				quads.addAll(model.getQuads(state, side, rand));
			}
			
			return quads;
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
	        return PerspectiveMapWrapper.handlePerspective(this, transforms, type);
	    }
		
		public void setCameraTransforms (ImmutableMap<TransformType, TRSRTransformation> transforms) {
			this.transforms = transforms;
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
