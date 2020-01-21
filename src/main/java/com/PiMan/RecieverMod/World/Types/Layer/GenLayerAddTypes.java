package com.PiMan.RecieverMod.World.Types.Layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddTypes extends GenLayer {
	
	public GenLayerAddTypes(long seed, GenLayer parent) {
		super(seed);
		this.parent = parent;
	}

	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
		
	       int i = areaX - 1;
	        int j = areaY - 1;
	        int k = areaWidth + 2;
	        int l = areaHeight + 2;
	        int[] aint = this.parent.getInts(i, j, k, l);
	        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

	        for (int i1 = 0; i1 < areaHeight; ++i1)
	        {
	            for (int j1 = 0; j1 < areaWidth; ++j1)
	            {
	                int k1 = aint[j1 + 1 + (i1 + 1) * k];
	                this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));

	                if (k1 == 0)
	                {
	                    aint1[j1 + i1 * areaWidth] = 0;
	                }
	                else
	                {
	                    int l1 = this.nextInt(4) + 1;

	                    aint1[j1 + i1 * areaWidth] = l1;
	                }
	            }
	        }

	        return aint1;
	}
}