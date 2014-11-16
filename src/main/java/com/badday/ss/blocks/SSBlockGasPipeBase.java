package com.badday.ss.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.badday.ss.core.atmos.GasUtils;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.events.RebuildNetworkPoint;

public class SSBlockGasPipeBase extends Block {
	
	protected SSBlockGasPipeBase(Material arg0) {
		super(arg0);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		if (!world.isRemote && GasUtils.getAdjacentAllCount(world, new BlockVec3(x,y,z))>1) { //FIXME - Called 2 times ? Why?
			// Rebild network
			GasUtils.registeredEventRebuildGasNetwork(new RebuildNetworkPoint(world,new BlockVec3(x,y,z)));
		}
		world.markBlockForUpdate(x, y, z);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (GasUtils.getAdjacentAllCount(world, new BlockVec3(x,y,z)) > 1) {
			// Rebild network
			for (BlockVec3 node : GasUtils.getAdjacentAll(world, new BlockVec3(x,y,z))) {
				if (node != null) {
					GasUtils.registeredEventRebuildGasNetwork(new RebuildNetworkPoint(world,node.clone()));
				}
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
	
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        world.func_147479_m(x, y, z);
        
        /*if (block instanceof SSBlockAirVent) {
        	TileEntity tileEntity = world.getTileEntity(x, y, z);
        	if (tileEntity instanceof IGasNetworkSource || tileEntity instanceof IGasNetworkVent) {
        		SSGasNetwork gasNetwork = new SSGasNetwork(world);
    			gasNetwork.rebuildNetworkFromVent(world, new BlockVec3(x,y,z));
        	}
        }*/
    }

    @Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return true;

		if (entityplayer.getCurrentEquippedItem() != null) {
			String itemName = entityplayer.getCurrentEquippedItem().getUnlocalizedName();
			if (itemName.equals("item.ss.multitool")) {
				//GasUtils.registeredEventRebuildGasNetwork(new RebuildNetworkEvent(world,new BlockVec3(x,y,z)));
			}
 		}
		return false;
	}
    

}
