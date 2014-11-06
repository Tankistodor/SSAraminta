package com.badday.ss.core.atmos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SSConfig;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.utils.BlockVec3;

public class GasPathfinder {

	public GasPathfinder(BlockVec3 start) {

	}

	public List<BlockVec3> sources = new LinkedList<BlockVec3>();
	public List<BlockVec3> vents = new LinkedList<BlockVec3>();

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

		iterated.add(location);
		
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			//BlockVec3 obj = location.getFromSide(direction);
			BlockVec3 obj = location.clone().newVecSide(direction.ordinal());

			if (!iterated.contains(obj) && !toIgnore.contains(obj)) {
				TileEntity tileEntity = obj.getTileEntity(worldObj);
				Block block = obj.getBlockID(worldObj);
				
				if (block != null && block.equals(SSConfig.ssBlockGasPipe))
				{
					loopAll(obj);
				}
				
				if (tileEntity != null) {
					if (tileEntity instanceof IGasNetworkSource) {
						if (!this.sources.contains(obj))
							this.sources.add(obj);
					} else if (tileEntity instanceof IGasNetworkVent) {
						if (!this.vents.contains(obj))
							this.vents.add(obj);
					}
				}
			}
		}
	}

	public List<BlockVec3> exploreNetwork() {
		loopAll(start);

		return iterated;
	}
	
	public List<BlockVec3> getSources() {
		return this.sources;
	}
	
	public List<BlockVec3> getVents() {
		return this.vents;
	}
	
	public List<BlockVec3> getPipes() {
		return this.iterated;
	}

	public List<IGasNetworkSource> getSourcesAsTile() {
		List<IGasNetworkSource> res = new ArrayList<IGasNetworkSource>();
		for (BlockVec3 p : this.sources) {
			res.add((IGasNetworkSource) p.getTileEntity(worldObj));
		}
		return res;
	}
	
	public List<IGasNetworkVent> getVentsAsTile() {
		List<IGasNetworkVent> res = new ArrayList<IGasNetworkVent>();
		for (BlockVec3 p : this.sources) {
			res.add((IGasNetworkVent) p.getTileEntity(worldObj));
		}
		return res;
	}
	
}
