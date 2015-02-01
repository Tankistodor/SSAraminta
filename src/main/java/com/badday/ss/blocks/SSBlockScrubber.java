package com.badday.ss.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.SS;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockScrubber extends BlockContainer {

	private IIcon[] iconBuffer;
	private int ICON_BOTTOM = 0, ICON_TOP = 1, ICON_INACTIVE_SIDE = 2, ICON_ACTIVE_SIDE = 3;
	
	public SSBlockScrubber(String assetName) {
		super(Material.rock);
		this.setResistance(1000.0F);
		this.setHardness(330.0F);
		this.setBlockTextureName(SS.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
		disableStats();
	}
	
	
	/**
	 * Overridden by {@link #createTileEntity(World, int)}
	 */
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new SSTileEntityScrubber();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBuffer = new IIcon[4];
		iconBuffer[ICON_BOTTOM] = par1IconRegister.registerIcon("ss:blockScrubberBottom");
		iconBuffer[ICON_TOP] = par1IconRegister.registerIcon("ss:blockScrubberTop");
		iconBuffer[ICON_INACTIVE_SIDE] = par1IconRegister.registerIcon("ss:blockScrubberSideOff");
		iconBuffer[ICON_ACTIVE_SIDE] = par1IconRegister.registerIcon("ss:blockScrubberSideOn");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {

		if (side > 1 && side < 5) {
			return iconBuffer[ICON_INACTIVE_SIDE];
		}
		if (side == 1) {
			return iconBuffer[ICON_TOP];
		}
		return iconBuffer[ICON_BOTTOM];
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({ "all" })
	public IIcon getIcon(IBlockAccess iblockaccess, int x, int y, int z, int side) {

		if (side > 1 && side < 6) {
			if (iblockaccess.getBlockMetadata(x, y, z) == 1) {
					return iconBuffer[ICON_ACTIVE_SIDE];	
				} else {
					return iconBuffer[ICON_INACTIVE_SIDE];
				}
		}
		if (side == 1) {
			return iconBuffer[ICON_TOP];
		}
		return iconBuffer[ICON_BOTTOM];
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return true;
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || entityplayer.isSneaking()) {
                return false;
        }
        //code to open gui explained later
        entityplayer.openGui(SS.instance, 0, world, x, y, z);

		return true;
	}
	
}
