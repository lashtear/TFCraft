package com.bioxx.tfc.WorldGen.GenLayers;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

import com.bioxx.tfc.WorldGen.TFCBiome;

public class GenLayerBiomeEdge extends GenLayerTFC
{
	public GenLayerBiomeEdge(long par1, GenLayer par3GenLayer)
	{
		super(par1);
		this.parent = (GenLayerTFC) par3GenLayer;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	 * amounts, or biomeList[] indices based on the particular GenLayer subclass.
	 */
	@Override
	public int[] getInts(int par1, int par2, int xSize, int zSize)
	{
		int[] inCache = this.parent.getInts(par1 - 1, par2 - 1, xSize + 2, zSize + 2);
		int[] outCache = IntCache.getIntCache(xSize * zSize);
		int var10;
		int var11;
		int var12;
		int var13;

		for (int z = 0; z < zSize; ++z)
		{
			for (int x = 0; x < xSize; ++x)
			{
				this.initChunkSeed(x + par1, z + par2);
				int thisID = inCache[x + 1 + (z + 1) * (xSize + 2)];

				var10 = inCache[x + 1 + (z + 1 - 1) * (xSize + 2)];
				var11 = inCache[x + 1 + 1 + (z + 1) * (xSize + 2)];
				var12 = inCache[x + 1 - 1 + (z + 1) * (xSize + 2)];
				var13 = inCache[x + 1 + (z + 1 + 1) * (xSize + 2)];

				if (thisID == TFCBiome.HighHills.biomeID)
				{
					if (var10 == TFCBiome.HighHills.biomeID && var11 == TFCBiome.HighHills.biomeID && var12 == TFCBiome.HighHills.biomeID && var13 == TFCBiome.HighHills.biomeID)
						outCache[x + z * xSize] = thisID;
					else
						outCache[x + z * xSize] = TFCBiome.HighHillsEdge.biomeID;
				}
				else if (thisID == TFCBiome.Mountains.biomeID)
				{
					if (var10 == TFCBiome.Mountains.biomeID && var11 == TFCBiome.Mountains.biomeID && var12 == TFCBiome.Mountains.biomeID && var13 == TFCBiome.Mountains.biomeID)
						outCache[x + z * xSize] = thisID;
					else
						outCache[x + z * xSize] = TFCBiome.MountainsEdge.biomeID;
				}
				else if (thisID == TFCBiome.swampland.biomeID)
				{
					if (var10 == TFCBiome.swampland.biomeID && var11 == TFCBiome.swampland.biomeID && var12 == TFCBiome.swampland.biomeID && var13 == TFCBiome.swampland.biomeID)
						outCache[x + z * xSize] = thisID;
					else
						outCache[x + z * xSize] = TFCBiome.plains.biomeID;
				}
				else if (thisID == TFCBiome.HighPlains.biomeID)
				{
					if (var10 == TFCBiome.HighPlains.biomeID && var11 == TFCBiome.HighPlains.biomeID && var12 == TFCBiome.HighPlains.biomeID && var13 == TFCBiome.HighPlains.biomeID)
						outCache[x + z * xSize] = thisID;
					else
						outCache[x + z * xSize] = TFCBiome.plains.biomeID;
				}
				else
				{
					outCache[x + z * xSize] = thisID;
				}
				/*if(outCache[x + z * xSize] > 200)
					return null;*/
			}
		}
		return outCache;
	}
}
