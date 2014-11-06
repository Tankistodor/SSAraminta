package com.badday.ss.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkElement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author KolesnikovAK
 * 
 *         Seal gases on bay
 * 
 */
public class SSBlockAirVent extends BlockContainer implements IGasNetworkElement {

	private IIcon[] iconBuffer;
	//private int ICON_INACTIVE_SIDE = 0, ICON_BOTTOM = 1, ICON_SIDE_ACTIVATED = 2;
	private int ICON_SIDE = 0, ICON_SIDE_ON = 1, ICON_SIDE_BACK = 2;

	public SSBlockAirVent(String assetName) {
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

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBuffer = new IIcon[3];
		//iconBuffer[ICON_INACTIVE_SIDE] = par1IconRegister.registerIcon("ss:airgenSideInactive");
		//iconBuffer[ICON_BOTTOM] = par1IconRegister.registerIcon("ss:contBottom");
		//iconBuffer[ICON_SIDE_ACTIVATED] = par1IconRegister.registerIcon("ss:airgenSideActive");
		iconBuffer[ICON_SIDE] = par1IconRegister.registerIcon("ss:blockGasVentSide");
		iconBuffer[ICON_SIDE_ON] = par1IconRegister.registerIcon("ss:blockGasVentOn");
		iconBuffer[ICON_SIDE_BACK] = par1IconRegister.registerIcon("ss:blockGasVentBack");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		
		if (side == 1) {
			if (metadata == 0) {
				return iconBuffer[ICON_SIDE_BACK];
			} else if (metadata == 1) {
				return iconBuffer[ICON_SIDE_ON];
			}
		}
		
		if (side == 0) {
			if (metadata == 0) // Inactive state
			{
				return iconBuffer[ICON_SIDE_ON];
			} else if (metadata == 1) {
				return iconBuffer[ICON_SIDE_BACK];
			}
		}

		return iconBuffer[ICON_SIDE];
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
	public TileEntity createTileEntity(World world, int meta) {
		return new SSTileEntityAirVent();
	}
	
	/**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
    
    @Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return true;
        
		
		if (entityplayer.getCurrentEquippedItem() != null) {
			String itemName = entityplayer.getCurrentEquippedItem().getUnlocalizedName();
			if (itemName.equals("item.ss.multitool")) {
				
				TileEntity tileEntity = world.getTileEntity(x, y, z);
				
				if (tileEntity instanceof SSTileEntityAirVent) {
					IGasNetwork vent = ((SSTileEntityAirVent) tileEntity).getGasNetwork();
					
					if (SS.Debug) { 
						vent.printDebugInfo();
						((SSTileEntityAirVent) tileEntity).printDebugInfo();
					}
				} 

			} else if (itemName.equals("ic2.itemToolWrenchElectric")) {
				// Rotate AirVent
				if (side == 0) {
					world.setBlockMetadataWithNotify(x, y, z, 0, 3);
	    			entityplayer.getCurrentEquippedItem().damageItem(1,entityplayer);
				} else if (side == 1) {
					world.setBlockMetadataWithNotify(x, y, z, 1, 3);
	    			entityplayer.getCurrentEquippedItem().damageItem(1,entityplayer);
				}
			}
			
		}
        return true;
    }
    
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        TileEntity te = par1World.getTileEntity(par2, par3, par4);

        if (te != null)
        {
            te.invalidate();
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

}
