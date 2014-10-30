package com.badday.ss.core.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badday.ss.api.IGasNetworkProvider;

import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import net.minecraft.tileentity.TileEntity;

/**
 * 
 * @author KolesnikovAK
 *
 */
public class GasNetworks {
	public List<GasNetworkNode> network = new LinkedList<GasNetworkNode>();

	public void addNodeToNetWork(GasNetworkNode node) {
		if (!network.contains(node)) {
			network.add(node);
		}
	}
	
	public boolean mergeNetwork(GasNetworkNode node1,GasNetworkNode node2) {
		
		if (node1 == node2) return false;
		
		boolean result = false;
		List<TileEntity> srcPipe = new LinkedList<TileEntity>();
		List<TileEntity> tarPipe = new LinkedList<TileEntity>();
		
		for (TileEntity gasTileEntity: node1.transmitersBlocks) {
			if (node2.transmitersBlocks.contains(gasTileEntity)) result = true; // Joint pipe exists
		}
		for (TileEntity gasTileEntity: node2.transmitersBlocks) {
			if (node1.transmitersBlocks.contains(gasTileEntity)) result = true; // Crosscheck Joint pipe exists
		}
		if (result) {
			/**
			 * merge source and target
			 */
			GasNetworkNode newNode = new GasNetworkNode();
			node1.consumerBlocks.addAll(node2.consumerBlocks);
			node1.sourceBlocks.addAll(node2.sourceBlocks);
			node1.transmitersBlocks.addAll(node2.transmitersBlocks);
			network.remove(node2);
		}
		return true;
	}
	
	public void rebuildNetwork(GasNetworkNode node) {
		try {
			Iterator<IGasNetworkProvider> it = node.transmitersBlocks.iterator();
            while (it.hasNext())
            {
            	IGasNetworkProvider transmitter = it.next();
            }
		}
        
	}
	
	/**
	 * 
	 */
	public void loadDataFromDisk() {
		
	}
	
	/**
	 * 
	 */
	public void saveDataFromDisk() {
		
	}
}
