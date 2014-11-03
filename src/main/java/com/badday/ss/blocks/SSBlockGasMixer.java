package com.badday.ss.blocks;

import com.badday.ss.SS;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class SSBlockGasMixer  extends BlockContainer {

	private IIcon[] iconBuffer;
	private int ICON_BOTTOM = 0, ICON_TOP = 1, ICON_INACTIVE_SIDE = 2, ICON_ACTIVE_SIDE = 3;
	
	public SSBlockGasMixer(String assetName) {
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
		return new SSTileEntityGasMixer();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBuffer = new IIcon[4];
		iconBuffer[ICON_BOTTOM] = par1IconRegister.registerIcon("ss:blockGasMixerBottom");
		iconBuffer[ICON_TOP] = par1IconRegister.registerIcon("ss:blockGasMixerTop");
		iconBuffer[ICON_INACTIVE_SIDE] = par1IconRegister.registerIcon("ss:blockGasMixerSideOff");
		iconBuffer[ICON_ACTIVE_SIDE] = par1IconRegister.registerIcon("ss:blockGasMixerSideOn");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
	
		if (side > 1 && side < 5) {
			if (metadata == 0) // Inactive state
			{
				return iconBuffer[ICON_INACTIVE_SIDE];
			} else if (metadata == 1) {
				return iconBuffer[ICON_ACTIVE_SIDE];
			}
		}
		if (side == 1) {
			return iconBuffer[ICON_TOP];
		}

		return iconBuffer[ICON_BOTTOM];
	}
	
}
