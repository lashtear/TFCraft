package com.bioxx.tfc.WorldGen.GenLayers.River;

import net.minecraft.world.gen.layer.GenLayer;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.WorldGen.TFCBiome;
import com.bioxx.tfc.WorldGen.GenLayers.GenLayerTFC;

public class GenLayerRiverMixTFC extends GenLayerTFC
{
	private GenLayer biomePatternGeneratorChain;
	private GenLayer riverPatternGeneratorChain;
	int[] layerBiomes;
	int[] layerRivers;
	int[] layerOut;
	int xn = 0;
	int xp = 0;
	int zn = 0;
	int zp = 0;

	public GenLayerRiverMixTFC(long par1, GenLayer par3GenLayer, GenLayer par4GenLayer)
	{
		super(par1);
		this.biomePatternGeneratorChain = par3GenLayer;
		this.riverPatternGeneratorChain = par4GenLayer;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	 * amounts, or biomeList[] indices based on the particular GenLayer subclass.
	 */
	@Override
	public int[] getInts(int x, int z, int xSize, int zSize)
	{
		layerBiomes = this.biomePatternGeneratorChain.getInts(x, z, xSize, zSize);
		layerRivers = this.riverPatternGeneratorChain.getInts(x, z, xSize, zSize);
		layerOut = new int[xSize * zSize];

		for (int zElement = 0; zElement < zSize; ++zElement)
		{
			for (int xElement = 0; xElement < xSize; ++xElement)
			{
				int index = xElement + zElement * xSize;
				int b = layerBiomes[index];
				int r = layerRivers[index];

				xn = index-1;
				xp = index+1;
				zn = index-zSize;
				zp = index+zSize;

				if (TFC_Core.isOceanicBiome(b) || TFC_Core.isMountainBiome(b))
					layerOut[index] = b;
				else if (r > 0)
				{
					layerOut[index] = r;

					//Here we make sure that rivers dont run along ocean/beach splits. We turn the river into oceans.
					if (TFC_Core.isBeachBiome(b))
					{
						layerOut[index] = TFCBiome.ocean.biomeID;
						if(inBounds(xn, layerOut) && layerOut[xn] == TFCBiome.river.biomeID)
						{
							layerOut[xn] = TFCBiome.ocean.biomeID;
						}
						if(inBounds(zn, layerOut) && layerOut[zn] == TFCBiome.river.biomeID)
						{
							layerOut[zn] = TFCBiome.ocean.biomeID;
						}
						if(inBounds(zp, layerOut) && TFC_Core.isOceanicBiome(layerBiomes[zp]) && layerRivers[zp] == 0)
						{
							layerOut[index] = b;
						}
						if(inBounds(zn, layerOut) && TFC_Core.isOceanicBiome(layerBiomes[zn]) && layerRivers[zn] == 0)
						{
							layerOut[index] = b;
						}
						if(inBounds(xn, layerOut) && TFC_Core.isOceanicBiome(layerBiomes[xn]) && layerRivers[xn] == 0)
						{
							layerOut[index] = b;
						}
						if(inBounds(xp, layerOut) && TFC_Core.isOceanicBiome(layerBiomes[xp]) && layerRivers[xp] == 0)
						{
							layerOut[index] = b;
						}
					}
				}
				else
					layerOut[index] = b;

				//Similar to above, if we're near a lake, we turn the river into lake.
				removeRiver(index, TFCBiome.lake.biomeID);
				removeRiver(index, TFCBiome.MountainsEdge.biomeID);

				validateInt(layerOut, index);
			}
		}
		return layerOut;
	}

	public void removeRiver(int index, int biomeToReplaceWith)
	{		
		if(layerOut[index] == TFCBiome.river.biomeID)
		{
			if(xn >= 0 && layerBiomes[xn] == biomeToReplaceWith)
			{
				layerOut[index] = biomeToReplaceWith;
			}
			if(zn >= 0 && layerBiomes[zn] == biomeToReplaceWith)
			{
				layerOut[index] = biomeToReplaceWith;
			}
			if(xp < layerBiomes.length && layerBiomes[xp] == biomeToReplaceWith)
			{
				layerOut[index] = biomeToReplaceWith;
			}
			if(zp < layerBiomes.length && layerBiomes[zp] == biomeToReplaceWith)
			{
				layerOut[index] = biomeToReplaceWith;
			}
		}
	}

	public boolean inBounds(int index, int[] array)
	{
		return index < array.length && index >= 0;
	}

	/**
	 * Initialize layer's local worldGenSeed based on its own baseSeed and the world's global seed (passed in as an
	 * argument).
	 */
	@Override
	public void initWorldGenSeed(long par1)
	{
		this.biomePatternGeneratorChain.initWorldGenSeed(par1);
		this.riverPatternGeneratorChain.initWorldGenSeed(par1);
		super.initWorldGenSeed(par1);
	}
}
