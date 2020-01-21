package com.PiMan.RecieverMod.World.Types.Layer;

import com.PiMan.RecieverMod.Main;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerSmoothTypes extends GenLayer {
	
	private boolean down;

	public GenLayerSmoothTypes(long seed, GenLayer parent, boolean down) {
		super(seed);
		this.parent = parent;
		this.down = down;
	}
	
	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
		if (down) {
			return getIntsDown(areaX, areaY, areaWidth, areaHeight);
		}
		else {
			return getIntsUp(areaX, areaY, areaWidth, areaHeight);
		}
	}

	public int[] getIntsDown(int areaX, int areaY, int areaWidth, int areaHeight) {
		
        int i = areaX - 1;
        int j = areaY - 1;
        int k = 1 + areaWidth + 1;
        int l = 1 + areaHeight + 1;
        int[] aint = this.parent.getInts(i, j, k, l);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i1 = 0; i1 < areaHeight; ++i1)
        {
            for (int j1 = 0; j1 < areaWidth; ++j1)
            {
                this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));
                int k1 = aint[j1 + 1 + (i1 + 1) * k];

                int l1 = aint[j1 + 1 + (i1 + 1 - 1) * k];
                int i2 = aint[j1 + 1 + 1 + (i1 + 1) * k];
                int j2 = aint[j1 + 1 - 1 + (i1 + 1) * k];
                int k2 = aint[j1 + 1 + (i1 + 1 + 1) * k];
                
                int l2 = Math.max(Math.max(l1, i2), Math.max(j2, k2));
                
                int l3 = k1 - l2;

                if (Math.abs(l3) > 1) {
                	k1 = l2 + 1 * Main.sign(l3);
                }

                aint1[j1 + i1 * areaWidth] = k1;
            }
        }

        return aint1;
		
	}
	
	public int[] getIntsUp(int areaX, int areaY, int areaWidth, int areaHeight) {
		
        int i = areaX - 1;
        int j = areaY - 1;
        int k = 1 + areaWidth + 1;
        int l = 1 + areaHeight + 1;
        int[] aint = this.parent.getInts(i, j, k, l);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i1 = 0; i1 < areaHeight; ++i1)
        {
            for (int j1 = 0; j1 < areaWidth; ++j1)
            {
                this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));
                int k1 = aint[j1 + 1 + (i1 + 1) * k];

                int l1 = aint[j1 + 1 + (i1 + 1 - 1) * k];
                int i2 = aint[j1 + 1 + 1 + (i1 + 1) * k];
                int j2 = aint[j1 + 1 - 1 + (i1 + 1) * k];
                int k2 = aint[j1 + 1 + (i1 + 1 + 1) * k];
                
                int l2 = Math.min(Math.min(l1, i2), Math.min(j2, k2));
                
                int l3 = k1 - l2;

                if (Math.abs(l3) > 1) {
                	k1 = l2 + 1 * Main.sign(l3);
                }

                aint1[j1 + i1 * areaWidth] = k1;
            }
        }

        return aint1;
		
	}
}
