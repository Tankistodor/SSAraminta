package com.badday.ss.core.atmos;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.events.RebuildNetworkPoint;

/**
 * 
 * @author KolesnikovAK
 * 
 */
public class SSGasNetwork implements IGasNetwork {

	public World world;
	public List<BlockVec3> pipes = new LinkedList<BlockVec3>(); // Pipes
	public List<IGasNetworkSource> sources = new LinkedList<IGasNetworkSource>(); // Pipes
	public List<IGasNetworkVent> vents = new LinkedList<IGasNetworkVent>(); // Pipes
	
	public SSGasNetwork(World world) {
		super();
		this.world = world;
	}

	// ==============================================================

	@Override
	public String toString() {
		return "GasNetwork[" + this.hashCode() + "|Pipes:" + this.pipes.size() + "|Src:" + sources.size() + "|V:" + vents.size() + 
				"|Bay:"+this.getVetnSize();
	}

	@Override
	public void setPipes(List<BlockVec3> pipes2) {

		this.pipes = pipes2;
	}

	@Override
	public void setVents(List<BlockVec3> vents2) {
		List<IGasNetworkVent> ventList = new LinkedList<IGasNetworkVent>();
		for (BlockVec3 p : vents2) {
			TileEntity vent = p.getTileEntity(this.world);
			if (vent instanceof IGasNetworkVent) {
				((IGasNetworkVent) vent).setNetwork(this);
				ventList.add((IGasNetworkVent) vent);
				((IGasNetworkVent) vent).onNetworkChanged();
			}
		}
		this.vents = ventList;
	}

	@Override
	public void setSources(List<BlockVec3> sourcers2) {
		List<IGasNetworkSource> srcList = new LinkedList<IGasNetworkSource>();
		for (BlockVec3 p : sourcers2) {
			TileEntity src = p.getTileEntity(this.world);
			if (src instanceof IGasNetworkSource) {
				((IGasNetworkSource) src).setNetwork(this);
				srcList.add((IGasNetworkSource) src);
				((IGasNetworkSource) src).onNetworkChanged();
			}
		}
		this.sources = srcList;
	}

	@Override
	public List<BlockVec3> getPipes() {
		return this.pipes;
	}

	@Override
	public List<? extends IGasNetworkSource> getSources() {
		return this.sources;
	}

	@Override
	public List<? extends IGasNetworkVent> getVents() {
		return this.vents;
	}

	
	/** User for pipes */
	/*
	@Deprecated
	public static BlockVec3[] getAdjacent1(World w, BlockVec3 position) {

		BlockVec3[] adjacentConnections = new BlockVec3[ForgeDirection.VALID_DIRECTIONS.length];

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			Block block = position.getBlockOnSide(w, direction.ordinal());
			if (block.equals(SSConfig.ssBlockGasPipe))
				adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
		}
		return adjacentConnections;
	}
	*/
	
	//@Deprecated
	/** 
	 * Moved to GasUtils
	 * @param w
	 * @param position
	 * @return
	 */
	/*
	public static BlockVec3[] getAdjacentAll(World w, BlockVec3 position) {

		BlockVec3[] adjacentConnections = new BlockVec3[ForgeDirection.VALID_DIRECTIONS.length];

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			Block block = position.getBlockOnSide(w, direction.ordinal());
			
			TileEntity src = position.getTileEntityOnSide(w, direction.ordinal());
			if (direction.equals(ForgeDirection.DOWN) && src instanceof IGasNetworkSource ) {
				adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
			}
			if (direction.equals(ForgeDirection.DOWN) && src instanceof IGasNetworkVent)
						adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
			
			if (block.equals(SSConfig.ssBlockGasPipe))
				adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
			
		}
		return adjacentConnections;
	}
	*/

	@Override
	@Deprecated
	public float nipPressure() {
		return 0.0f;
		/*
		float returnPressure = 0;
		for (IGasNetworkSource g : this.sources) {
			returnPressure =+ g.nipGas();
		}
		
		return returnPressure;
		
		
		Float pressure = 0.0f;
		int V = 0;
		
		//FIXME: Calculate pressure on GasMixer
		for (IGasNetworkVent vent : this.getVents()) {
			//if (vent.getActive())
				if (vent.getSealed() && vent.getActive()) { 
					V += vent.getBaySize();
				} else if (!vent.getSealed() && !vent.getActive())  {
					V += 64*64; // If desealed bat - more rate usages gases
				}
		}
		
		if (SS.Debug) System.out.println("    Sealed Bay capacity: " + V);
		
		// This is test mixture 
		List<GasMixture> sumMix = new ArrayList<GasMixture>();
		sumMix.add(new GasMixture(SSConfig.fluidOxygenGas,20));
		sumMix.add(new GasMixture(SSConfig.fluidNitrogenGas,75));
		sumMix.add(new GasMixture(SSConfig.fluidHydrogen,1));
		sumMix.add(new GasMixture(SSConfig.fluidMethaneGas,1));
		sumMix.add(new GasMixture(SSConfig.fluidHelium,1));
		sumMix.add(new GasMixture(SSConfig.fluidCarbonDioxide,2));

		pressure = GasUtils.getGasPressure(sumMix,V,SSConfig.ssDefaultTemperature);
		
		if (V > 0) {
			for (IGasNetworkSource src : this.getSources()) {
				//TODO: pressure =+ GasUtils.getGasPressure(src.getGasMix, V, SSConfig.ssDefaultTemperature);
			}
		}
		return pressure*SSConfig.ssGasPressureConst;*/
	}
	
	
	public void printDebugInfo() {
		System.out.println("NET : " + this);
	}

	@Override
	public int getVetnSize() {
		int sum = 0;
		for (IGasNetworkVent v : this.getVents()) {
			if (v.getSealed() && v.getActive()) {
				sum =+ v.getBaySize();
			} else if (v.getActive()) {
				sum =+ 64*64;
			}
		}
		return sum;
	}

	@Override
	public void removeSource(IGasNetworkSource node) {
		this.sources.remove(node);
	}

	@Override
	public void removeVent(IGasNetworkVent node) {
		this.vents.remove(node);
	}

	@Override
	public void removePipe(BlockVec3 node) {
		this.pipes.remove(node);
	}
	
}
