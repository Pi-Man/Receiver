package com.PiMan.RecieverMod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.PiMan.RecieverMod.KeyBinding.Keybinds;
import com.PiMan.RecieverMod.World.Gen.Structure.MapGenCustomStructureTest;
import com.PiMan.RecieverMod.World.Gen.Structure.WorldGenStructureComponentTest;
import com.PiMan.RecieverMod.World.Gen.Structure.WorldGenWastelandTower;
import com.PiMan.RecieverMod.client.gui.GuiHandler;
import com.PiMan.RecieverMod.crafting.AccessoryIngredientFactory;
import com.PiMan.RecieverMod.proxy.CommonProxy;
import com.PiMan.RecieverMod.tileEntity.TileEntityBulletCrafter;
import com.PiMan.RecieverMod.util.CapabilityHandler;
import com.PiMan.RecieverMod.util.IItemData;
import com.PiMan.RecieverMod.util.IModelGun;
import com.PiMan.RecieverMod.util.ItemDataStorage;
import com.PiMan.RecieverMod.util.ItemData;
import com.PiMan.RecieverMod.util.Reference;
import com.PiMan.RecieverMod.util.handlers.KeyInputHandler;
import com.PiMan.RecieverMod.util.handlers.MiscEventHandler;
import com.PiMan.RecieverMod.util.handlers.NetworkHandler;
import com.PiMan.RecieverMod.util.handlers.PlayerRenderHandler;
import com.PiMan.RecieverMod.util.handlers.RegistryHandler;
import com.PiMan.RecieverMod.util.handlers.ServerEventHandler;
import com.PiMan.RecieverMod.util.handlers.SoundsHandler;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import paulscode.sound.SoundSystemConfig;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);
	
	public static SoundsHandler soundsHandler;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event)
	{
		
		NetworkHandler.init();
		
		proxy.preInit();
		
		MapGenStructureIO.registerStructure(MapGenCustomStructureTest.Start.class, "Tower");
		WorldGenStructureComponentTest.registerScatteredFeaturePieces();
		
		GameRegistry.registerWorldGenerator(new WorldGenWastelandTower(), 0);
		
		CapabilityManager.INSTANCE.register(IItemData.class, new ItemDataStorage(), ItemData::new);
		MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
		GameRegistry.registerTileEntity(TileEntityBulletCrafter.class, new ResourceLocation(Reference.MOD_ID, "bullet_crafter"));
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		soundsHandler = new SoundsHandler();
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new GuiHandler());
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}
	
	
	
	//HELPER FUNCTIONS
	
	public static int sign(double x) {
		return (int) (Math.abs(x)/x);
	}
	
	public static double invLerp(double a, double b, double c) {
		return (c - a) / (b - a);
	}
	
	public static double lerp(double a, double b, double k) {
		return (1 - k)*a + k*b;
	}
	
	public static RayTraceResult rayTraceEntity(Vec3d vec3d1, Vec3d vec3d2, Entity entity) {
				
		double x1 = vec3d1.x;
		double x2 = vec3d2.x;
		double y1 = vec3d1.y;
		double y2 = vec3d2.y;
		double z1 = vec3d1.z;
		double z2 = vec3d2.z;
		
		double x;
		double y;
		double z;
		
		double k = 0;
		
		AxisAlignedBB entityAABB = entity.getEntityBoundingBox();
		
		if (entityAABB == null) {
			LOGGER.error("AABB is NULL");
			return null;
		}
		
		double dx = x2 - x1;
		double dy = y2 - y1;
		double dz = z2 - z1;
		
		if (dx >= 0) {
			k = invLerp(x1, x2, entityAABB.minX);
			x = entityAABB.minX;
			y = lerp(y1, y2, k);
			z = lerp(z1, z2, k);
			if (y >= entityAABB.maxY) {
				if (dy > 0) {
					//LOGGER.error("Ray Not in EntityAABB");
					return null;
				}
				if (z >= entityAABB.maxZ) {
					if (dz > 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(y1, y2, entityAABB.maxY);
					x = lerp(x1, x2, k);
					y = entityAABB.maxY;
					z = lerp(z1, z2, k);
					if (x > entityAABB.maxX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z >= entityAABB.maxZ) {
						k = invLerp(z1, z2, entityAABB.maxZ);
						x = lerp(x1, x2, k);
						y = lerp(y1, y2, k);
						z = entityAABB.maxZ;
						if (x > entityAABB.maxX || y < entityAABB.minY) {
							//LOGGER.error("Ray Not in EntityAABB");
							return null;
						}
						//LOGGER.info("Created Ray Trace Result");
						return new RayTraceResult(entity, new Vec3d(x, y, z));
					}
					if (z < entityAABB.minZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					//LOGGER.info("Created Ray Trace Result");
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z < entityAABB.minZ) {
					if (dz < 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(y1, y2, entityAABB.maxY);
					x = lerp(x1, x2, k);
					y = entityAABB.maxY;
					z = lerp(z1, z2, k);
					if (x > entityAABB.maxX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z < entityAABB.minZ) {
						k = invLerp(z1, z2, entityAABB.minZ);
						x = lerp(x1, x2, k);
						y = lerp(y1, y2, k);
						z = entityAABB.minZ;
						if (x > entityAABB.maxX || y < entityAABB.minY) {
							//LOGGER.error("Ray Not in EntityAABB");
							return null;
						}
						return new RayTraceResult(entity, new Vec3d(x, y, z));
					}
					if (z > entityAABB.maxZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z >= entityAABB.minZ && z < entityAABB.maxZ) {
					k = invLerp(y1, y2, entityAABB.maxY);
					x = lerp(x1, x2, k);
					y = entityAABB.maxY;
					z = lerp(z1, z2, k);
					if (x > entityAABB.maxX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z < entityAABB.minZ || z > entityAABB.maxZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
			}
			if (y < entityAABB.minY) {
				if (dy < 0) {
					//LOGGER.error("Ray Not in EntityAABB");
					return null;
				}
				if (z >= entityAABB.maxZ) {
					if (dz > 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(y1, y2, entityAABB.minY);
					x = lerp(x1, x2, k);
					y = entityAABB.minY;
					z = lerp(z1, z2, k);
					if (x > entityAABB.maxX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z >= entityAABB.maxZ) {
						k = invLerp(z1, z2, entityAABB.maxZ);
						x = lerp(x1, x2, k);
						y = lerp(y1, y2, k);
						z = entityAABB.maxZ;
						if (x > entityAABB.maxX || y > entityAABB.maxY) {
							//LOGGER.error("Ray Not in EntityAABB");
							return null;
						}
						return new RayTraceResult(entity, new Vec3d(x, y, z));
					}
					if (z < entityAABB.minZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z < entityAABB.minZ) {
					if (dz < 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(y1, y2, entityAABB.minY);
					x = lerp(x1, x2, k);
					y = entityAABB.minY;
					z = lerp(z1, z2, k);
					if (x > entityAABB.maxX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z < entityAABB.minZ) {
						k = invLerp(z1, z2, entityAABB.minZ);
						x = lerp(x1, x2, k);
						y = lerp(y1, y2, k);
						z = entityAABB.minZ;
						if (x > entityAABB.maxX || y > entityAABB.maxY) {
							//LOGGER.error("Ray Not in EntityAABB");
							return null;
						}
						return new RayTraceResult(entity, new Vec3d(x, y, z));
					}
					if (z > entityAABB.maxZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z >= entityAABB.minZ && z < entityAABB.maxZ) {
					k = invLerp(y1, y2, entityAABB.minY);
					x = lerp(x1, x2, k);
					y = entityAABB.minY;
					z = lerp(z1, z2, k);
					if (x > entityAABB.maxX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z < entityAABB.minZ || z > entityAABB.maxZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
			}
			if (y >= entityAABB.minY && y < entityAABB.maxY) {
				if (z < entityAABB.minZ) {
					if (dz < 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(z1, z2, entityAABB.minZ);
					x = lerp(x1, x2, k);
					y = lerp(y1, y2, k);
					z = entityAABB.minZ;
					if (x > entityAABB.maxX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (y < entityAABB.minY || y > entityAABB.maxY) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z >= entityAABB.maxZ) {
					if (dz > 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(z1, z2, entityAABB.maxZ);
					x = lerp(x1, x2, k);
					y = lerp(y1, y2, k);
					z = entityAABB.maxZ;
					if (x > entityAABB.maxX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (y < entityAABB.minY || y > entityAABB.maxY) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				return new RayTraceResult(entity, new Vec3d(x, y, z));
			}
		}
		if (dx < 0) {
			k = invLerp(x1, x2, entityAABB.maxX);
			x = entityAABB.maxX;
			y = lerp(y1, y2, k);
			z = lerp(z1, z2, k);
			if (y >= entityAABB.maxY) {
				if (dy > 0) {
					//LOGGER.error("Ray Not in EntityAABB");
					return null;
				}
				if (z >= entityAABB.maxZ) {
					if (dz > 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(y1, y2, entityAABB.maxY);
					x = lerp(x1, x2, k);
					y = entityAABB.maxY;
					z = lerp(z1, z2, k);
					if (x < entityAABB.minX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z >= entityAABB.maxZ) {
						k = invLerp(z1, z2, entityAABB.maxZ);
						x = lerp(x1, x2, k);
						y = lerp(y1, y2, k);
						z = entityAABB.maxZ;
						if (x < entityAABB.minX || y < entityAABB.minY) {
							//LOGGER.error("Ray Not in EntityAABB");
							return null;
						}
						return new RayTraceResult(entity, new Vec3d(x, y, z));
					}
					if (z < entityAABB.minZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z < entityAABB.minZ) {
					if (dz < 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(y1, y2, entityAABB.maxY);
					x = lerp(x1, x2, k);
					y = entityAABB.maxY;
					z = lerp(z1, z2, k);
					if (x < entityAABB.minX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z < entityAABB.minZ) {
						k = invLerp(z1, z2, entityAABB.minZ);
						x = lerp(x1, x2, k);
						y = lerp(y1, y2, k);
						z = entityAABB.minZ;
						if (x < entityAABB.minX || y < entityAABB.minY) {
							//LOGGER.error("Ray Not in EntityAABB");
							return null;
						}
						return new RayTraceResult(entity, new Vec3d(x, y, z));
					}
					if (z > entityAABB.maxZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z >= entityAABB.minZ && z < entityAABB.maxZ) {
					k = invLerp(y1, y2, entityAABB.maxY);
					x = lerp(x1, x2, k);
					y = entityAABB.maxY;
					z = lerp(z1, z2, k);
					if (x < entityAABB.minX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z < entityAABB.minZ || z > entityAABB.maxZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
			}
			if (y < entityAABB.minY) {
				if (dy < 0) {
					//LOGGER.error("Ray Not in EntityAABB");
					return null;
				}
				if (z >= entityAABB.maxZ) {
					if (dz > 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(y1, y2, entityAABB.minY);
					x = lerp(x1, x2, k);
					y = entityAABB.minY;
					z = lerp(z1, z2, k);
					if (x < entityAABB.minX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z >= entityAABB.maxZ) {
						k = invLerp(z1, z2, entityAABB.maxZ);
						x = lerp(x1, x2, k);
						y = lerp(y1, y2, k);
						z = entityAABB.maxZ;
						if (x < entityAABB.minX || y > entityAABB.maxY) {
							//LOGGER.error("Ray Not in EntityAABB");
							return null;
						}
						return new RayTraceResult(entity, new Vec3d(x, y, z));
					}
					if (z < entityAABB.minZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z < entityAABB.minZ) {
					if (dz < 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(y1, y2, entityAABB.minY);
					x = lerp(x1, x2, k);
					y = entityAABB.minY;
					z = lerp(z1, z2, k);
					if (x < entityAABB.minX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z < entityAABB.minZ) {
						k = invLerp(z1, z2, entityAABB.minZ);
						x = lerp(x1, x2, k);
						y = lerp(y1, y2, k);
						z = entityAABB.minZ;
						if (x < entityAABB.minX || y > entityAABB.maxY) {
							//LOGGER.error("Ray Not in EntityAABB");
							return null;
						}
						return new RayTraceResult(entity, new Vec3d(x, y, z));
					}
					if (z > entityAABB.maxZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z >= entityAABB.minZ && z < entityAABB.maxZ) {
					k = invLerp(y1, y2, entityAABB.minY);
					x = lerp(x1, x2, k);
					y = entityAABB.minY;
					z = lerp(z1, z2, k);
					if (x < entityAABB.minX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (z < entityAABB.minZ || z > entityAABB.maxZ) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
			}
			if (y >= entityAABB.minY && y < entityAABB.maxY) {
				if (z < entityAABB.minZ) {
					if (dz < 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(z1, z2, entityAABB.minZ);
					x = lerp(x1, x2, k);
					y = lerp(y1, y2, k);
					z = entityAABB.minZ;
					if (x < entityAABB.minX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (y < entityAABB.minY || y > entityAABB.maxY) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				if (z >= entityAABB.maxZ) {
					if (dz > 0) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					k = invLerp(z1, z2, entityAABB.maxZ);
					x = lerp(x1, x2, k);
					y = lerp(y1, y2, k);
					z = entityAABB.maxZ;
					if (x < entityAABB.minX) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					if (y < entityAABB.minY || y > entityAABB.maxY) {
						//LOGGER.error("Ray Not in EntityAABB");
						return null;
					}
					return new RayTraceResult(entity, new Vec3d(x, y, z));
				}
				return new RayTraceResult(entity, new Vec3d(x, y, z));
			}
		}
		LOGGER.error("Ray Not in EntityAABB catch");
		return null;
	}
}
