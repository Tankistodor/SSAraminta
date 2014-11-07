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
import com.badday.ss.SSConfig;
import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkElement;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.atmos.GasUtils;
import com.badday.ss.core.atmos.SSGasNetwork;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.events.RebuildNetworkEvent;

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
	private int ICON_SIDE = 0, ICON_SIDE_FRONT = 1, ICON_SIDE_BACK = 2, ICON_SIDE_FRONTON=3;

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
		iconBuffer = new IIcon[4];
		//iconBuffer[ICON_INACTIVE_SIDE] = par1IconRegister.registerIcon("ss:airgenSideInactive");
		//iconBuffer[ICON_BOTTOM] = par1IconRegister.registerIcon("ss:contBottom");
		//iconBuffer[ICON_SIDE_ACTIVATED] = par1IconRegister.registerIcon("ss:airgenSideActive");
		iconBuffer[ICON_SIDE] = par1IconRegister.registerIcon("ss:blockGasVentSide");
		iconBuffer[ICON_SIDE_FRONT] = par1IconRegister.registerIcon("ss:blockGasVentFront");
		iconBuffer[ICON_SIDE_BACK] = par1IconRegister.registerIcon("ss:blockGasVentBack");
		iconBuffer[ICON_SIDE_FRONTON] = par1IconRegister.registerIcon("ss:blockGasVentFrontOn");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		
		if (side == 1) {
			if (metadata == 0) {
				return iconBuffer[ICON_SIDE_BACK];
			} else if (metadata == 1) {
				return iconBuffer[ICON_SIDE_FRONT];
			} else if (metadata == 2) {
				return iconBuffer[ICON_SIDE_BACK];
			} else if (metadata == 3) {
				return iconBuffer[ICON_SIDE_FRONTON];
			}
		}
		
		if (side == 0) {
			if (metadata == 0) {
				return iconBuffer[ICON_SIDE_FRONT];
			} else if (metadata == 1) {
				return iconBuffer[ICON_SIDE_BACK];
			} else if (metadata == 2) {
				return iconBuffer[ICON_SIDE_FRONTON];
			} else if (metadata == 3) {
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
					IGasNetwork net = ((SSTileEntityAirVent) tileEntity).getGasNetwork();
					
					if (SS.Debug) { 
						((SSTileEntityAirVent) tileEntity).printDebugInfo();
					}
				} 

			} else if (itemName.equals("ic2.itemToolWrenchElectric") || itemName.equals("item.thermalexpansion.tool.wrench")) {
				// Rotate AirVent
				int oldMeta = world.getBlockMetadata(x, y, z);
				int oldSide = oldMeta & 1;
				
				if (side != oldSide && side >= 0 && side < 2) {
					if (side == 0 && side != oldSide) {
						world.setBlockMetadataWithNotify(x, y, z, oldMeta & 2, 3); // old meta 1 or 3 -> 0 or 2
						entityplayer.getCurrentEquippedItem().damageItem(1, entityplayer);
						
						TileEntity tileEntity = world.getTileEntity(x, y, z);
						((IGasNetworkVent) tileEntity).getGasNetwork().removeVent((IGasNetworkVent) tileEntity);
						// Rebuild new network on UP from Vent
						if (SS.Debug) System.out.println("Try to rebild on UP");
						if (world.getBlock(x, y+1, z) == SSConfig.ssBlockGasPipe) {
							GasUtils.registeredEventRebuildGasNetwork(new RebuildNetworkEvent(world,new BlockVec3(x,y+1,z)));
						}
						
					} else if (side == 1 && side != oldSide) {
						world.setBlockMetadataWithNotify(x, y, z, oldMeta | 1, 3); // old meta 0 or 2 -> 1 or 3
						entityplayer.getCurrentEquippedItem().damageItem(1, entityplayer);
						TileEntity tileEntity = world.getTileEntity(x, y, z);
						((IGasNetworkVent) tileEntity).getGasNetwork().removeVent((IGasNetworkVent) tileEntity);
						// Rebuild new network on DOWN from Vent
						if (SS.Debug) System.out.println("Try to rebild on DOWN");
						if (world.getBlock(x, y-1, z) == SSConfig.ssBlockGasPipe) {
							GasUtils.registeredEventRebuildGasNetwork(new RebuildNetworkEvent(world,new BlockVec3(x,y-1,z)));
						}
					}
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
