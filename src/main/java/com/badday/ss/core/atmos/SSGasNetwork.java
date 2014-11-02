package com.badday.ss.core.atmos;

import java.util.ArrayList;
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

	@Override
	public synchronized void rebuildNetwork(World w, BlockVec3 node) {

		GasPathfinder finder = new GasPathfinder(w, node);
		List<BlockVec3> results = finder.exploreNetwork();
		
		
						//SSGasNetwork newNetwork = new SSGasNetwork(w);
						this.setPipes(finder.getPipes());
						this.setVents(finder.getVents());
						this.setSources(finder.getSources());
						this.recalculate();

	}

	public synchronized void rebuildNetwork2(World w, BlockVec3 node) {

		boolean linked = false;
		boolean[] dealtWith = { false, false, false, false, false, false };
		BlockVec3[] connectedBlocks = getAdjacent(w, node);
		if (SS.Debug) System.out.println("connecedBlocks " + connectedBlocks);
		for (int i = 0; i < connectedBlocks.length; i++) {
			if (connectedBlocks[i] != null) {
				BlockVec3 connectedBlockA = connectedBlocks[i];
				if (!dealtWith[i]) {
					GasPathfinder finder = new GasPathfinder(w, connectedBlocks[i], node);
					List<BlockVec3> results = finder.exploreNetwork();

					linked = false;

					for (int b = i; b < connectedBlocks.length; b++) {
						if (b == i) {
							continue;
						}

						BlockVec3 connectedBlockB = connectedBlocks[b];

						/**
						 * The connections A and B are still intact elsewhere.
						 * Set all references of wire connection into one
						 * network.
						 */
						if (results.contains(connectedBlockB)) {
							dealtWith[b] = true;
							linked = true;
						}
					}

					if (!linked) {
						// Create new net
						SSGasNetwork newNetwork = new SSGasNetwork(w);
						newNetwork.setPipes(finder.getPipes());
						newNetwork.setVents(finder.getVents());
						newNetwork.setSources(finder.getSources());
						newNetwork.recalculate();
					}
				}
			}
		}

	}
	
	@Override
	public void recalculate() {

	}

	// ==============================================================

	@Override
	public String toString() {
		return "GasNetwork[" + this.hashCode() + "|Pipes:" + this.pipes.size() + "|Acceptors:" + "]";
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

	public static BlockVec3[] getAdjacent(World w, BlockVec3 position) {

		BlockVec3[] adjacentConnections = new BlockVec3[ForgeDirection.VALID_DIRECTIONS.length];

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			Block block = position.getBlockOnSide(w, direction.ordinal());
			if (block.equals(SSConfig.ssBlockGasPipe))
				adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
		}
		return adjacentConnections;
	}

	public static BlockVec3[] getAdjacentAll(World w, BlockVec3 position) {

		BlockVec3[] adjacentConnections = new BlockVec3[ForgeDirection.VALID_DIRECTIONS.length];

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			Block block = position.getBlockOnSide(w, direction.ordinal());
			
			TileEntity src = position.getTileEntityOnSide(w, direction.ordinal());
			
			if (src instanceof IGasNetworkSource || src instanceof IGasNetworkVent)
				adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
			
			if (block.equals(SSConfig.ssBlockGasPipe))
				adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
			
		}
		return adjacentConnections;
	}

	@Override
	public float getPressure() {
		Float pressure = 0.0f;
		int V = 0;
		for (IGasNetworkVent vent : this.getVents()) {
			//if (vent.getActive())
				if (vent.getSealed() && vent.getActive()) { 
					V += vent.getBaySize();
				} else if (!vent.getSealed() && !vent.getActive())  {
					V += 64*64; // If desealed bat - more rate usages gases
				}
		}
		
		if (SS.Debug) System.out.println("    Sealed Bay capacity: " + V);
		
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
		return pressure*SSConfig.ssGasPressureConst;
	}
	
	
	public void printDebugInfo() {
		System.out.println("NET : " + this);
		System.out.println("    pipes: " + this.getPipes().size());
		System.out.println("    sources: " + this.getSources().size());
		System.out.println("    vents: " + this.getVents().size());
		System.out.println("    pressure: " + this.getPressure() + " hPa");
	}
	
}
