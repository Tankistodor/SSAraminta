package com.badday.ss.core.atmos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.api.IGasNetworkConsumer;
import com.badday.ss.api.IGasNetworkPipe;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.core.utils.BlockVec3;

public class GasPathfinder {

	public GasPathfinder(BlockVec3 start) {

	}

	public List<BlockVec3> sources = new LinkedList<BlockVec3>();
	public List<BlockVec3> consumers = new LinkedList<BlockVec3>();

	public List<BlockVec3> iterated = new ArrayList<BlockVec3>();
	public List<BlockVec3> toIgnore = new ArrayList<BlockVec3>();
	
	private World worldObj;
	private BlockVec3 start;

	public GasPathfinder(World world, BlockVec3 location, BlockVec3... ignore) {
		this.worldObj = world;
		this.start = location;

		if (ignore != null) {
			for (int i = 0; i < ignore.length; i++) {
				toIgnore.add(ignore[i]);
			}
		}
	}

	public void loopAll(BlockVec3 location) {
		/*if (TransmissionType.checkTransmissionType(location.getTileEntity(worldObj), transmissionType)) {
			iterated.add(location);
		} else {
			toIgnore.add(location);
		}*/

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			//BlockVec3 obj = location.getFromSide(direction);
			BlockVec3 obj = location.modifyPositionFromSide(direction);

			if (!iterated.contains(obj) && !toIgnore.contains(obj)) {
				TileEntity tileEntity = obj.getTileEntity(worldObj);
				
				if (tileEntity != null) {
					if (tileEntity instanceof IGasNetworkPipe) {
						loopAll(obj);
					} else if (tileEntity instanceof IGasNetworkSource) {
						if (!this.sources.contains(obj))
							this.sources.add(obj);
					} else if (tileEntity instanceof IGasNetworkConsumer) {
						if (!this.consumers.contains(obj))
							this.consumers.add(obj);
					}
					
				}
				
				
			}
		}
	}

	public List<BlockVec3> exploreNetwork() {
		loopAll(start);

		return iterated;
	}
}
