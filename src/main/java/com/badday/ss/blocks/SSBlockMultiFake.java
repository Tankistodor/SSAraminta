package com.badday.ss.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.core.utils.BlockVec3;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockMultiFake extends BlockContainer {

	public static final Material machine = new Material(MapColor.ironColor);

	private IIcon fakeIcons;

	public SSBlockMultiFake(String assetName) {
		super(machine);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockTextureName(SS.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
		this.setResistance(1000000000000000.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.fakeIcons = par1IconRegister.registerIcon(SS.ASSET_PREFIX + "fakeBlock");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return this.fakeIcons;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

    @Override
    public TileEntity createNewTileEntity(World var1, int meta)
    {
        return new SSTileEntityMultiFake();
    }

	@Override
	public boolean canDropFromExplosion(Explosion par1Explosion) {
		return false;
	}

	/**
	 * Copy hardness block from main block 
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getBlockHardness(net.minecraft.world.World, int, int, int)
	 */
	@Override
    public float getBlockHardness(World par1World, int par2, int par3, int par4)
    {
        TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);

        if (tileEntity instanceof SSTileEntityMultiFake)
        {
            BlockVec3 mainBlockPosition = ((SSTileEntityMultiFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null)
            {
                return mainBlockPosition.getBlock(par1World).getBlockHardness(par1World, par2, par3, par4);
            }
        }

        return this.blockHardness;
    }
	
	/**
	 * Delete main block if fakeblock is destroy
	 */
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof SSTileEntityMultiFake)
        {
            ((SSTileEntityMultiFake) tileEntity).onBlockRemoval(tileEntity);
        }

        super.breakBlock(world, x, y, z, block, par6);
    }
	
	/**
	 * Create fake block for event onCreate in normal block 
	 * @param worldObj
	 * @param position
	 * @param mainBlock
	 * @param meta
	 */
	public void makeFakeBlock(World worldObj, BlockVec3 position, BlockVec3 mainBlock, int meta) {
		worldObj.setBlock(position.x, position.y, position.z, this, meta, 3);
		((SSTileEntityMultiFake) worldObj.getTileEntity(position.x, position.y, position.z)).setMainBlock(mainBlock);
	}

	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i1, float f1, float f2, float f3) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te == null || (te instanceof SSTileEntityMultiFake)) {
			((SSTileEntityMultiFake) te).onBlockActivated(world, x, y, z, player);
			return true;
		}
		return true;
	}
	
}
