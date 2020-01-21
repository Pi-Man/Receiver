package com.PiMan.RecieverMod.World.Types.Layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerBase extends GenLayer {

	public GenLayerBase(long p_i2125_1_) {
		super(p_i2125_1_);
	}

	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaHeight; ++i)
        {
            for (int j = 0; j < areaWidth; ++j)
            {
             aint[j + i * areaWidth] = nextInt(2) == 0 ? 4 : 1;
            }
        }

        if (areaX > -areaWidth && areaX <= 0 && areaY > -areaHeight && areaY <= 0)
        {
            aint[-areaX + -areaY * areaWidth] = 1;
        }

        return aint;
	}

}
