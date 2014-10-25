package com.badday.ss.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockCabinet extends BlockContainer {

	private IIcon[] iconBuffer;
	
	protected SSBlockCabinet(String asset) {
		super(Material.wood);
		setStepSound(soundTypeMetal);
		isBlockContainer = true;
		setBlockBounds(0.0625F, 0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
     * Overridden by {@link #createTileEntity(World, int)}
     */
    @Override
    public TileEntity createNewTileEntity(World w, int i)
    {
    	return null;  
    }
	
	@Override
	public TileEntity createTileEntity(World arg0, int arg1) {
		return new SSTileEntityCabinet();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventPar) {
		TileEntity te = world.getTileEntity(x, y, z);
		return ((te != null) ? te.receiveClientEvent(eventId, eventPar) : false);
	}
	
	@Override
    public int getRenderType()
    {
        return 22;
    }

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		// TODO: 

		return iconBuffer[0];
	}

}
