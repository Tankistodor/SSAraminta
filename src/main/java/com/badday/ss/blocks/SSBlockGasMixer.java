package com.badday.ss.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.badday.ss.SS;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockGasMixer extends BlockContainer {

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
	
	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({ "all" })
	// @Override (client only)
	public IIcon getIcon(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		SSTileEntityGasMixer tile = (SSTileEntityGasMixer) iblockaccess.getTileEntity(x, y, z);

		if (side > 1 && side < 6) {
			SSTileEntityGasMixer thistile = (SSTileEntityGasMixer) iblockaccess.getTileEntity(x, y, z);
			if (thistile.getTankInfo(ForgeDirection.getOrientation(side-2))[0] != null) // Inactive state
			{
				if (thistile.getTankInfo(ForgeDirection.getOrientation(side-2))[0].fluid != null &&
						thistile.getTankInfo(ForgeDirection.getOrientation(side-2))[0].fluid.amount>0) {
					return iconBuffer[ICON_ACTIVE_SIDE];	
				} else {
					return iconBuffer[ICON_INACTIVE_SIDE];
				}
				
			} else  {
				return iconBuffer[ICON_INACTIVE_SIDE];
			}
		}
		if (side == 1) {
			return iconBuffer[ICON_TOP];
		}
		return iconBuffer[ICON_BOTTOM];
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return true;

		if (entityplayer.getCurrentEquippedItem() != null) {
			String itemName = entityplayer.getCurrentEquippedItem().getUnlocalizedName();
			if (itemName.equals("item.ss.multitool")) {

				TileEntity tileEntity = world.getTileEntity(x, y, z);

				if (tileEntity instanceof SSTileEntityGasMixer) {
					SSTileEntityGasMixer tile = ((SSTileEntityGasMixer) tileEntity);

					if (SS.Debug) {
						System.out.println(" GasMixer info: " + tile.toString());
						for (int s = 0; s < 4; s++) {
							FluidTankInfo[] info = tile.getTankInfo(ForgeDirection.getOrientation(s + 2));

							for (int i = 0; i < info.length; i++) {
								FluidStack liquid = info[i].fluid;
								if (liquid != null)
									System.out.println("    Tank" + s + ": " + info[i].fluid.getUnlocalizedName() + " - " + info[i].capacity + "/"+info[i].fluid.amount);
							}

						}
					}
				}

			}

		}
		return false;
	}

}
