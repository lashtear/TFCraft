package TFC.Blocks;

import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import TFC.Items.ItemProPick;
import TFC.Items.ItemWeapon;
import TFC.TileEntities.TileEntityToolRack;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemHoe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemTool;
import net.minecraft.src.Material;
import net.minecraft.src.TFCBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockToolRack extends BlockContainer
{

	public BlockToolRack(int par1)
	{
		super(par1, Material.wood);
		this.blockIndexInTexture = 96;
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getTextureFile()
	{
		// TODO Auto-generated method stub
		return "/bioxx/terrablocks2.png";
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public int getBlockTexture(int woodType)
	{
		return blockIndexInTexture + woodType;
	}

	public int getRenderType()
	{
		return TFCBlocks.toolRackRenderId;
	}

	public void Eject(World par1World, int par2, int par3, int par4)
	{
		if((TileEntityToolRack)par1World.getBlockTileEntity(par2, par3, par4)!=null)
		{
			TileEntityToolRack te;
			te = (TileEntityToolRack)par1World.getBlockTileEntity(par2, par3, par4);
			te.ejectContents();
			par1World.removeBlockTileEntity(par2, par3, par4);
		}
	}

	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
		{
			TileEntityToolRack te = (TileEntityToolRack) world.getBlockTileEntity(i, j, k);
			int dir = world.getBlockMetadata(i, j, k);
			if(te != null)
			{
				if(dir == 0)
				{
					if(hitX < 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 0);
					}
					else if(hitX > 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 1);
					}
					else if(hitX < 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 2);
					}
					else if(hitX > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 3);
					}
				}
				else if(dir == 1)
				{
					if(hitZ < 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 0);
					}
					else if(hitZ > 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 1);
					}
					else if(hitZ < 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 2);
					}
					else if(hitZ > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 3);
					}
				}
				else if(dir == 2)
				{
					if(hitX < 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 0);
					}
					else if(hitX > 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 1);
					}
					else if(hitX < 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 2);
					}
					else if(hitX > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 3);
					}
				}
				else if(dir == 3)
				{
					if(hitZ < 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 0);
					}
					else if(hitZ > 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 1);
					}
					else if(hitZ < 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 2);
					}
					else if(hitZ > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 3);
					}
				}
				te.broadcastPacketInRange(te.createUpdatePacket());
				return true;
			}
		}
		return false;
	}

	private void handleArea(World world, int i, int j, int k,EntityPlayer entityplayer, TileEntityToolRack te, int slot) 
	{
		boolean hasToolInHand = entityplayer.getCurrentEquippedItem() != null && 
				(entityplayer.getCurrentEquippedItem().getItem() instanceof ItemTool || entityplayer.getCurrentEquippedItem().getItem() instanceof ItemWeapon  || 
						entityplayer.getCurrentEquippedItem().getItem() instanceof ItemHoe || entityplayer.getCurrentEquippedItem().getItem() instanceof ItemProPick);
		if(te.storage[slot] == null && hasToolInHand)
		{
			te.storage[slot] = entityplayer.getCurrentEquippedItem().copy();
			entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
		}
		else if(te.storage[slot] != null)
		{
			te.ejectItem(slot);
			te.storage[slot] = null;
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int blockId, int metadata)
	{
		Eject(world, x, y, z);
		super.breakBlock(world, x, y, z, blockId, metadata);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		// TODO Auto-generated method stub
		return new TileEntityToolRack();
	}

	public void setBlockBoundsBasedOnState(IBlockAccess access, int i, int j, int k) 
	{
		int dir = access.getBlockMetadata(i, j, k);

		if(dir == 0)
		{
			this.setBlockBounds(0.0F, 0F, 0.85F, 1F, 1F, 1F);
		}
		else if(dir == 1)
		{
			this.setBlockBounds(0.0F, 0F, 0.0F, 0.15F, 1F, 1F);
		}
		else if(dir == 2)
		{
			this.setBlockBounds(0.0F, 0F, 0.00F, 1F, 1F, 0.15F);
		}
		else if(dir == 3)
		{
			this.setBlockBounds(0.85F, 0F, 0.0F, 1F, 1F, 1F);
		}
	}
	
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) 
	{
		int dir = world.getBlockMetadata(i, j, k);

		if(dir == 0)
		{
			if(!world.isBlockOpaqueCube(i, j, k+1))
			{
				Eject(world,i,j,k);
				world.setBlock(i, j, k, 0);
			}
		}
		else if(dir == 1)
		{
			if(!world.isBlockOpaqueCube(i-1, j, k))
			{
				Eject(world,i,j,k);
				world.setBlock(i, j, k, 0);
			}
		}
		else if(dir == 2)
		{
			if(!world.isBlockOpaqueCube(i, j, k-1))
			{
				Eject(world,i,j,k);
				world.setBlock(i, j, k, 0);
			}
		}
		else if(dir == 3)
		{
			if(!world.isBlockOpaqueCube(i+1, j, k))
			{
				Eject(world,i,j,k);
				world.setBlock(i, j, k, 0);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	for(int i = 0; i < 16; i++)
    		par3List.add(new ItemStack(par1, 1, i));
    }

}
