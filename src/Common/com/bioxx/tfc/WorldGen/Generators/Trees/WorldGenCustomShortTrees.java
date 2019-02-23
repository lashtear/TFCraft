package com.bioxx.tfc.WorldGen.Generators.Trees;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.api.TFCBlocks;

public class WorldGenCustomShortTrees extends WorldGenerator
{
	private Block woodBlock;
	private Block leafBlock;
	private final int treeId;

	public WorldGenCustomShortTrees(boolean flag, int id)
	{
		super(flag);
		woodBlock = TFCBlocks.logNatural;
		leafBlock = TFCBlocks.leaves;
		treeId=id;
	}
	public WorldGenCustomShortTrees(boolean flag, int id, Block logs, Block leaves)
	{
		super(flag);
		woodBlock = logs;
		leafBlock = leaves;
		treeId=id;
	}
	@Override
	public boolean generate(World world, Random random, int xCoord, int yCoord, int zCoord)
	{
		int height = random.nextInt(3) + 4;
		if (yCoord < 1 || yCoord + height + 1 > world.getHeight())
			return false;

		boolean flag = true;
		for (int i1 = yCoord; i1 <= yCoord + 1 + height; i1++)
		{
			byte byte0 = 1;
			if (i1 == yCoord)
				byte0 = 0;
			if (i1 >= yCoord + 1 + height - 2)
				byte0 = 2;

			for (int i2 = xCoord - byte0; i2 <= xCoord + byte0 && flag; i2++)
			{
				for (int l2 = zCoord - byte0; l2 <= zCoord + byte0 && flag; l2++)
				{
					if (i1 >= 0 && i1 < world.getHeight())
					{
						Block j3 = world.getBlock(i2, i1, l2);
						if (!j3.isAir(world, i2, i1, l2) && !j3.canBeReplacedByLeaves(world, i2, i1, l2))
							flag = false;
					}
					else
					{
						flag = false;
					}
				}
			}
		}

		if (!flag)
			return false;

		if (!(TFC_Core.isSoil(world.getBlock(xCoord, yCoord - 1, zCoord)))|| yCoord >= world.getHeight() - height - 1)
			return false;

		for (int gy = yCoord - 3 + height; gy <= yCoord + height; gy++)
		{
			int ry = gy - (yCoord + height); // -3 to 0
			int radiusAtLevel = 1 - ry / 2;  // 3 to 1
			for (int gx = xCoord - radiusAtLevel; gx <= xCoord + radiusAtLevel; gx++)
			{
				int rx = gx - xCoord;
				for (int gz = zCoord - radiusAtLevel; gz <= zCoord + radiusAtLevel; gz++)
				{
					int rz = gz - zCoord;
					if ((Math.abs(rx) != radiusAtLevel || Math.abs(rz) != radiusAtLevel || random.nextInt(2) != 0 && ry != 0) && world.isAirBlock(gx, gy, gz))
						setBlockAndNotifyAdequately(world, gx, gy, gz, leafBlock, treeId);
				}
			}
		}

		for (int l1 = 0; l1 < height; l1++)
		{
			setBlockAndNotifyAdequately(world, xCoord, yCoord + l1, zCoord, woodBlock, treeId);
		}

		return true;
	}
}
