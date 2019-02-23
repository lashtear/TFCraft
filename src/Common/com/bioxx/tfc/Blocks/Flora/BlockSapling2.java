package com.bioxx.tfc.Blocks.Flora;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.bioxx.tfc.TileEntities.TESapling;
import com.bioxx.tfc.WorldGen.Generators.Trees.WorldGenCustomShortTrees;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.TFCBlocks;

public class BlockSapling2 extends BlockSapling
{
	public BlockSapling2()
	{
		super();
		this.woodNames = new String[Global.WOOD_ALL.length - 16];
		System.arraycopy(Global.WOOD_ALL, 16, woodNames, 0, Global.WOOD_ALL.length - 16);
		this.icons = new IIcon[woodNames.length];
	}

	@Override
	public void growTree(World world, int i, int j, int k, Random rand, long timestamp)
	{
		int meta = world.getBlockMetadata(i, j, k);
		world.setBlockToAir(i, j, k);
		WorldGenerator worldGen =
			// should be true for post-worldgen treegen but working around tfc conventions
			new WorldGenCustomShortTrees(false, meta, TFCBlocks.logNatural2, TFCBlocks.leaves2);

		if (worldGen != null && !worldGen.generate(world, rand, i, j, k))
		{
			world.setBlock(i, j, k, this, meta, 3); // Flag3 = blockupdate and send to client
			if (world.getTileEntity(i, j, k) instanceof TESapling)
			{
				TESapling te = (TESapling) world.getTileEntity(i, j, k);
				te.growTime = timestamp;
				te.enoughSpace = false;
				te.markDirty();
			}
		}
	}
}
