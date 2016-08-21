package com.bioxx.tfc.Blocks.Vanilla;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.Blocks.BlockTerra;
import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.api.TFCBlocks;

public class BlockCustomSnow extends BlockTerra
{
	public BlockCustomSnow()
	{
		super(Material.snow);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setTickRandomly(true);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
	{
		Block below = world.getBlock(i, j - 1, k);
		int belowMeta = world.getBlockMetadata(i, j-1, k) & 7;
		Block here = world.getBlock(i, j, k);
		Material belowM = below.getMaterial();

		//		if (here == TFCBlocks.snow) return true;
		if (below == TFCBlocks.ice
			|| belowM.isLiquid()
			|| below == TFCBlocks.pottery
			|| below instanceof BlockRailBase)
			return false;
		if (below == TFCBlocks.leaves
			|| below == TFCBlocks.leaves2
			|| below == TFCBlocks.thatch
			|| (below == TFCBlocks.snow
				&& belowMeta == 7
				&& here == Blocks.air))
			return true;
		return World.doesBlockHaveSolidTopSurface(world, i, j - 1, k);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		float f = 1f/8f;
		int meta = world.getBlockMetadata(x, y, z) & 7;
		Block below = world.getBlock (x,y-1,z);
		float bottom = (below == TFCBlocks.snow ? -0.5f : 0f);
		float top = bottom + f*(meta+1)/2;
		
		return AxisAlignedBB.getBoundingBox(x + this.minX, y + bottom, z + this.minZ, x + this.maxX, y + top, z + this.maxZ);
	}
	
	@Override
	public int getRenderType()
	{
		return TFCBlocks.snowRenderId;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
	{
		dropBlockAsItem(world, x, y, z, meta, 0);
		player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
	}

	@Override
	public Item getItemDropped(int i, Random r, int j)
	{
		return Items.snowball;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		// meta	 speed
		//	  0	 0.98	-  one layer
		//	  7	 0.10	-  eight layers = like leaves
		
		int meta = world.getBlockMetadata(x, y, z) & 7;
		double speed = 0.98 - 0.125 * meta;
		entity.motionX *= speed;
		entity.motionZ *= speed;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b)
	{
		final int[] dx = new int[] {0, 0, -1, 1};
		final int[] dz = new int[] {-1, 1, 0, 0};

		// if(!canPlaceBlockAt(world, x, y, z))
		// {
		// 	world.setBlock(x, y, z, Blocks.air, 0, 2);
		// 	return;
		// }

		float temp = TFC_Climate.getHeightAdjustedTemp(world, x, y, z);
		if (temp > 10) {
			world.setBlock (x,y,z,Blocks.air);
			
			
		}

		boolean moved = false;
		int meta = world.getBlockMetadata (x,y,z);
		for (int i = 0; meta > 0 && i < 4; ++i) {
			int neighborH = neighborSnowHeight (world, x+dx[i], y, z+dz[i]);
			if (neighborH < 8
				&& meta+1 > neighborH
				&& canPlaceBlockAt (world, x+dx[i], y, z+dz[i])) {
				world.setBlock (x+dx[i], y, z+dz[i], TFCBlocks.snow);
				world.setBlockMetadataWithNotify (x+dx[i], y, z+dz[i], neighborH, 2);
				--meta;
				moved = true;
			}
		}
		if (moved) {
			world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		}
	}

	@Override
	public int quantityDropped(Random r)
	{
		return 1;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess bAccess, int x, int y, int z)
	{
		int meta = bAccess.getBlockMetadata(x, y, z) & 7;
		float top = (meta + 1) / 8.0F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, top, 1.0F);
	}

	@Override
	public int tickRate(World world)
	{
		return 20;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random r)
	{
		// if (!canPlaceBlockAt(world, x, y, z))
		// {
		// 	world.setBlock(x, y, z, Blocks.air, 0, 2);
		// 	return;
		// }

		int meta = world.getBlockMetadata(x, y, z) & 7;

		float temp = TFC_Climate.getHeightAdjustedTemp(world, x, y, z);
		if (temp <= 0 && world.isRaining())	 //Raining and Below Freezing
		{
			// if (r.nextInt(5) == 0)
			// {
			int max = (world.getBlock(x, y - 1, z).getMaterial() == Material.leaves) ? 3 : 7;
			if(meta < max && canAddSnow(world, x, y, z, meta)) {
				world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
			}
			// }
		}
		else if (temp > 10)	 // to hot for snow (probably chunk loading error)
		{
			world.setBlock(x, y, z, Blocks.air, 0, 0x2);
		}
		else if (temp > 0 && world.isRaining())	 //Raining and above freezing
		{
			if (r.nextInt(5) == 0)
			{
				if (meta > 0)
					world.setBlockMetadataWithNotify(x, y, z, meta - 1, 2);
				else
					world.setBlock(x, y, z, Blocks.air, 0, 0x2);
			}
		}
		else if (temp > 0)	//Above freezing, not raining
		{
			if (r.nextInt(20-((int)(temp*2f))) == 0)
			{
				if(meta > 0)
					world.setBlockMetadataWithNotify(x, y, z, meta - 1, 2);
				else
					world.setBlock(x, y, z, Blocks.air, 0, 0x2);
			}
		}
	}

	private static int neighborSnowHeight (World world, int x, int y, int z)
	{
		Block b = world.getBlock (x, y, z);
		if (b == Blocks.air) {
			return 0;
		} else if (b == TFCBlocks.snow) {
			int bmeta = world.getBlockMetadata (x,y,z);
			return bmeta&7+1;
		} else {
			return 8;
		}
	}
	@Override
	public void registerBlockIcons(IIconRegister registerer)
	{
		this.blockIcon = registerer.registerIcon(Reference.MOD_ID + ":snow");
	}

	private boolean canAddSnowCheckNeighbors(World world, int x, int y, int z, int meta)
	{
		Block block = world.getBlock(x, y, z);
		
		if (block.getMaterial() == Material.snow)  // if neighbor is snow, allow up to one additional level
			return meta <= (world.getBlockMetadata(x, y, z) & 7)+1;
		else if (block == TFCBlocks.leaves || block == TFCBlocks.leaves2)  // 4 levels if adjacent to leaves (instead of just one level)
			return meta < 3;
		else if (block.isOpaqueCube ()) return true;
		else return false;
		// else if (block.isNormalCube())	// if neighbor is a normal block (opaque, render as normal, not power),
		// 	return meta < 6;			// up to 7 - leave the top layer empty so we just can see the block
	}

	private boolean canAddSnow(World world, int x, int y, int z, int meta)
	{
		return
			canAddSnowCheckNeighbors(world, x + 1, y, z, meta)
			|| canAddSnowCheckNeighbors(world, x - 1, y, z, meta)
			|| canAddSnowCheckNeighbors(world, x, y, z + 1, meta)
			|| canAddSnowCheckNeighbors(world, x, y, z - 1, meta);
	}
}
