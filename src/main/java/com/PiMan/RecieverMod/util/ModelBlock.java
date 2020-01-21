package com.PiMan.RecieverMod.util;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.vector.Vector3f;

import com.PiMan.RecieverMod.client.model.ElementFace;
import com.PiMan.RecieverMod.util.handlers.ModelElement;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBlock
{
    private static final Logger LOGGER = LogManager.getLogger();
    @VisibleForTesting
    static final Gson SERIALIZER = (new GsonBuilder()).registerTypeAdapter(ModelBlock.class, new ModelBlock.Deserializer()).registerTypeAdapter(ModelElement.class, new ModelElement.Deserializer()).registerTypeAdapter(ElementFace.class, new ElementFace.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3fDeserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransformsDeserializer()).registerTypeAdapter(ItemOverride.class, new ItemOverrideDeserializer()).create();
    private final Map<UUID, ModelElement> elements;
    private final boolean gui3d;
    public final boolean ambientOcclusion;
    private final ItemCameraTransforms cameraTransforms;
    private final List<ItemOverride> overrides;
    public String name = "";
    @VisibleForTesting
    public final Map<String, String> textures;

    public static ModelBlock deserialize(Reader readerIn)
    {
        return (ModelBlock)JsonUtils.gsonDeserialize(SERIALIZER, readerIn, ModelBlock.class, false);
    }

    public static ModelBlock deserialize(String jsonString)
    {
        return deserialize(new StringReader(jsonString));
    }

    public ModelBlock(Map<UUID, ModelElement> elementsIn, Map<String, String> texturesIn, boolean ambientOcclusionIn, boolean gui3dIn, ItemCameraTransforms cameraTransformsIn, List<ItemOverride> overridesIn)
    {
        this.elements = elementsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.textures = texturesIn;
        this.cameraTransforms = cameraTransformsIn;
        this.overrides = overridesIn;
    }

    public Map<UUID, ModelElement> getElements()
    {
        return this.elements;
    }

    public boolean isAmbientOcclusion()
    {
        return this.ambientOcclusion;
    }

    public boolean isGui3d()
    {
        return this.gui3d;
    }

    public Collection<ResourceLocation> getOverrideLocations()
    {
        Set<ResourceLocation> set = Sets.<ResourceLocation>newHashSet();

        for (ItemOverride itemoverride : this.overrides)
        {
            set.add(itemoverride.getLocation());
        }

        return set;
    }

    public List<ItemOverride> getOverrides()
    {
        return this.overrides;
    }

    public ItemOverrideList createOverrides()
    {
        return this.overrides.isEmpty() ? ItemOverrideList.NONE : new ItemOverrideList(this.overrides);
    }

    public boolean isTexturePresent(String textureName)
    {
        return !"missingno".equals(this.resolveTextureName(textureName));
    }

    public String resolveTextureName(String textureName)
    {
        if (!this.startsWithHash(textureName))
        {
            textureName = '#' + textureName;
        }

        return this.resolveTextureName(textureName, new ModelBlock.Bookkeep(this));
    }

    private String resolveTextureName(String textureName, ModelBlock.Bookkeep p_178302_2_)
    {
        if (this.startsWithHash(textureName))
        {
            if (this == p_178302_2_.modelExt)
            {
                LOGGER.warn("Unable to resolve texture due to upward reference: {} in {}", textureName, this.name);
                return "missingno";
            }
            else
            {
                String s = this.textures.get(textureName.substring(1));

                p_178302_2_.modelExt = this;

                if (s != null && this.startsWithHash(s))
                {
                    s = p_178302_2_.model.resolveTextureName(s, p_178302_2_);
                }

                return s != null && !this.startsWithHash(s) ? s : "missingno";
            }
        }
        else
        {
            return textureName;
        }
    }

    private boolean startsWithHash(String hash)
    {
        return hash.charAt(0) == '#';
    }

    public ItemCameraTransforms getAllTransforms()
    {
        ItemTransformVec3f itemtransformvec3f = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
        ItemTransformVec3f itemtransformvec3f1 = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        ItemTransformVec3f itemtransformvec3f2 = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND);
        ItemTransformVec3f itemtransformvec3f3 = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
        ItemTransformVec3f itemtransformvec3f4 = this.getTransform(ItemCameraTransforms.TransformType.HEAD);
        ItemTransformVec3f itemtransformvec3f5 = this.getTransform(ItemCameraTransforms.TransformType.GUI);
        ItemTransformVec3f itemtransformvec3f6 = this.getTransform(ItemCameraTransforms.TransformType.GROUND);
        ItemTransformVec3f itemtransformvec3f7 = this.getTransform(ItemCameraTransforms.TransformType.FIXED);
        return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5, itemtransformvec3f6, itemtransformvec3f7);
    }

    private ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType type)
    {
        return this.cameraTransforms.getTransform(type);
    }

    @SideOnly(Side.CLIENT)
    static final class Bookkeep
        {
            public final ModelBlock model;
            public ModelBlock modelExt;

            private Bookkeep(ModelBlock modelIn)
            {
                this.model = modelIn;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class Deserializer implements JsonDeserializer<ModelBlock>
        {
            public ModelBlock deserialize(JsonElement baseElement, Type type, JsonDeserializationContext context) throws JsonParseException
            {
            	
            	JsonObject baseObject = (JsonObject) baseElement;
            	
            	this.checkVersion(baseElement);
            	
            	Map<UUID, ModelElement> elements = this.getElements(baseObject, context);
            	
            	Map<String, String> textures = this.getTextures(baseObject);
            	
                ItemCameraTransforms itemcameratransforms = ItemCameraTransforms.DEFAULT;

                if (baseObject.has("display"))
                {
                    JsonObject jsonobject1 = JsonUtils.getJsonObject(baseObject, "display");
                    itemcameratransforms = (ItemCameraTransforms)context.deserialize(jsonobject1, ItemCameraTransforms.class);
                }
            	
            	boolean ambientOclusion = this.getAmbientOcclusionEnabled(baseObject);
            	
            	List<ItemOverride> overrides = this.getItemOverrides(context, baseObject);
            	
            	return new ModelBlock(elements, textures, ambientOclusion, true, itemcameratransforms, overrides);
            	
            }
            
            private void checkVersion(JsonElement element) {
            	
				JsonObject object = element.getAsJsonObject();
				
				JsonObject meta = object.getAsJsonObject("meta");
				
				if (!JsonUtils.getString(meta, "format_version", "").startsWith("3")) {
					throw new JsonParseException("Version must be at least 3.0");
				}
				
				if (!JsonUtils.getString(meta, "model_format", "").equals("free")) {
					throw new JsonParseException("Model must be free Model");
				}
				
				if (JsonUtils.getBoolean(meta, "box_uv", true)) {
					throw new JsonParseException("Box UV not supported");
				}
			}
            

            protected List<ItemOverride> getItemOverrides(JsonDeserializationContext deserializationContext, JsonObject object)
            {
                List<ItemOverride> list = Lists.<ItemOverride>newArrayList();

                if (object.has("overrides"))
                {
                    for (JsonElement jsonelement : JsonUtils.getJsonArray(object, "overrides"))
                    {
                        list.add((ItemOverride)deserializationContext.deserialize(jsonelement, ItemOverride.class));
                    }
                }

                return list;
            }

            private Map<String, String> getTextures(JsonObject object)
            {
                Map<String, String> map = Maps.<String, String>newHashMap();

                if (object.has("textures"))
                {
                    JsonArray texturesArray = object.getAsJsonArray("textures");

                    for (JsonElement textureElement : texturesArray)
                    {
                    	JsonObject textureObject = (JsonObject) textureElement;
                        map.put(textureObject.get("id").getAsString(), new ResourceLocation(textureObject.get("namespace").getAsString(), textureObject.get("folder").getAsString() + "/" + textureObject.get("name").getAsString().replace(".png", "")).toString());
                    }
                }

                return map;
            }

            protected boolean getAmbientOcclusionEnabled(JsonObject object)
            {
                return JsonUtils.getBoolean(object, "ambientocclusion", true);
            }
            
            private Map<UUID, ModelElement> getElements(JsonObject jsonObject, JsonDeserializationContext context) {
            	
            	JsonArray elementsArray = jsonObject.getAsJsonArray("elements");
            	
            	Map<UUID, ModelElement> map = new HashMap<>();
            	
            	for (JsonElement jsonElement : elementsArray) {
            		ModelElement modelElement = context.deserialize(jsonElement, ModelElement.class);
            		map.put(modelElement.uuid, modelElement);
            	}
            	
            	return map;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class LoopException extends RuntimeException
        {
        }
    
    @SideOnly(Side.CLIENT)
    static class ItemCameraTransformsDeserializer implements JsonDeserializer<ItemCameraTransforms>
        {
            public ItemCameraTransforms deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
            {
                JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
                ItemTransformVec3f itemtransformvec3f = this.getTransform(p_deserialize_3_, jsonobject, "thirdperson_righthand");
                ItemTransformVec3f itemtransformvec3f1 = this.getTransform(p_deserialize_3_, jsonobject, "thirdperson_lefthand");

                if (itemtransformvec3f1 == ItemTransformVec3f.DEFAULT)
                {
                    itemtransformvec3f1 = itemtransformvec3f;
                }

                ItemTransformVec3f itemtransformvec3f2 = this.getTransform(p_deserialize_3_, jsonobject, "firstperson_righthand");
                ItemTransformVec3f itemtransformvec3f3 = this.getTransform(p_deserialize_3_, jsonobject, "firstperson_lefthand");

                if (itemtransformvec3f3 == ItemTransformVec3f.DEFAULT)
                {
                    itemtransformvec3f3 = itemtransformvec3f2;
                }

                ItemTransformVec3f itemtransformvec3f4 = this.getTransform(p_deserialize_3_, jsonobject, "head");
                ItemTransformVec3f itemtransformvec3f5 = this.getTransform(p_deserialize_3_, jsonobject, "gui");
                ItemTransformVec3f itemtransformvec3f6 = this.getTransform(p_deserialize_3_, jsonobject, "ground");
                ItemTransformVec3f itemtransformvec3f7 = this.getTransform(p_deserialize_3_, jsonobject, "fixed");
                return new ItemCameraTransforms(itemtransformvec3f1, itemtransformvec3f, itemtransformvec3f3, itemtransformvec3f2, itemtransformvec3f4, itemtransformvec3f5, itemtransformvec3f6, itemtransformvec3f7);
            }

            private ItemTransformVec3f getTransform(JsonDeserializationContext p_181683_1_, JsonObject p_181683_2_, String p_181683_3_)
            {
                return p_181683_2_.has(p_181683_3_) ? (ItemTransformVec3f)p_181683_1_.deserialize(p_181683_2_.get(p_181683_3_), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
            }
        }
    
    @SideOnly(Side.CLIENT)
    static class ItemTransformVec3fDeserializer implements JsonDeserializer<ItemTransformVec3f>
        {
            private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
            private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
            private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

            public ItemTransformVec3f deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
            {
                JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
                Vector3f vector3f = this.parseVector3f(jsonobject, "rotation", ROTATION_DEFAULT);
                Vector3f vector3f1 = this.parseVector3f(jsonobject, "translation", TRANSLATION_DEFAULT);
                vector3f1.scale(0.0625F);
                vector3f1.x = MathHelper.clamp(vector3f1.x, -5.0F, 5.0F);
                vector3f1.y = MathHelper.clamp(vector3f1.y, -5.0F, 5.0F);
                vector3f1.z = MathHelper.clamp(vector3f1.z, -5.0F, 5.0F);
                Vector3f vector3f2 = this.parseVector3f(jsonobject, "scale", SCALE_DEFAULT);
                vector3f2.x = MathHelper.clamp(vector3f2.x, -4.0F, 4.0F);
                vector3f2.y = MathHelper.clamp(vector3f2.y, -4.0F, 4.0F);
                vector3f2.z = MathHelper.clamp(vector3f2.z, -4.0F, 4.0F);
                return new ItemTransformVec3f(vector3f, vector3f1, vector3f2);
            }

            private Vector3f parseVector3f(JsonObject jsonObject, String key, Vector3f defaultValue)
            {
                if (!jsonObject.has(key))
                {
                    return defaultValue;
                }
                else
                {
                    JsonArray jsonarray = JsonUtils.getJsonArray(jsonObject, key);

                    if (jsonarray.size() != 3)
                    {
                        throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());
                    }
                    else
                    {
                        float[] afloat = new float[3];

                        for (int i = 0; i < afloat.length; ++i)
                        {
                            afloat[i] = JsonUtils.getFloat(jsonarray.get(i), key + "[" + i + "]");
                        }

                        return new Vector3f(afloat[0], afloat[1], afloat[2]);
                    }
                }
            }
        }
    
    @SideOnly(Side.CLIENT)
    static class ItemOverrideDeserializer implements JsonDeserializer<ItemOverride>
        {
            public ItemOverride deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
            {
                JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
                ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "model"));
                Map<ResourceLocation, Float> map = this.makeMapResourceValues(jsonobject);
                return new ItemOverride(resourcelocation, map);
            }

            protected Map<ResourceLocation, Float> makeMapResourceValues(JsonObject p_188025_1_)
            {
                Map<ResourceLocation, Float> map = Maps.<ResourceLocation, Float>newLinkedHashMap();
                JsonObject jsonobject = JsonUtils.getJsonObject(p_188025_1_, "predicate");

                for (Entry<String, JsonElement> entry : jsonobject.entrySet())
                {
                    map.put(new ResourceLocation(entry.getKey()), Float.valueOf(JsonUtils.getFloat(entry.getValue(), entry.getKey())));
                }

                return map;
            }
        }
}