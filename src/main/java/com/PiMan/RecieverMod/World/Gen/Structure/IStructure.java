package com.PiMan.RecieverMod.World.Gen.Structure;

import com.PiMan.RecieverMod.proxy.CommonProxy;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public interface IStructure {
	public static WorldServer worldserver = (WorldServer) CommonProxy.INSTANCE.getWorld(0);
}
