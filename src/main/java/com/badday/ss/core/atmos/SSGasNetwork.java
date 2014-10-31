package com.badday.ss.core.atmos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkPipe;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.utils.BlockVec3;

import cpw.mods.fml.common.FMLLog;

/**
 * 
 * @author KolesnikovAK
 * 
 */
public class SSGasNetwork implements IGasNetwork {

	public World world;
	public List<IGasNetworkPipe> pipes = new LinkedList<IGasNetworkPipe>(); // Pipes
	public List<IGasNetworkSource> sources = new LinkedList<IGasNetworkSource>(); // Pipes
	public List<IGasNetworkVent> vents = new LinkedList<IGasNetworkVent>(); // Pipes

	public SSGasNetwork(World world) {
		super();
		this.world = world;
	}

	public void rebuildNetwork(SSGasNetwork node) {
		try {
			Iterator<IGasNetworkPipe> it = node.pipes.iterator();
			while (it.hasNext()) {
				IGasNetworkPipe pipe = (IGasNetworkPipe) it.next();
			}
		} finally {

		}

	}

	public static TileEntity[] getAdjacentOxygenConnections(TileEntity tile) {
		TileEntity[] adjacentConnections = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];

		// boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();

		BlockVec3 thisVec = new BlockVec3(tile);
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorldObj(), direction);

			if (tileEntity instanceof IGasNetworkPipe) {
				if (((IGasNetworkPipe) tileEntity).canConnect(direction.getOpposite())) {
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}
			/*
			 * else if (isMekLoaded) { if (tileEntity instanceof ITubeConnection
			 * && (!(tileEntity instanceof IGasTransmitter) ||
			 * TransmissionType.checkTransmissionType(tileEntity,
			 * TransmissionType.GAS, tileEntity))) { if (((ITubeConnection)
			 * tileEntity).canTubeConnect(direction)) {
			 * adjacentConnections[direction.ordinal()] = tileEntity; } } }
			 */
		}

		return adjacentConnections;
	}

	@Override
	public float produce(float sendAmount, TileEntity... ignoreTiles) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRequest(TileEntity... ignoreTiles) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void refresh() {
		try {
			Iterator<IGasNetworkPipe> it = this.pipes.iterator();

			while (it.hasNext()) {
				IGasNetworkPipe pipe = it.next();

				if (pipe == null) {
					it.remove();
					continue;
				}

				pipe.onNetworkChanged();

				if (((TileEntity) pipe).isInvalid() || ((TileEntity) pipe).getWorldObj() == null) {
					it.remove();
					continue;
				} else {
					pipe.setNetwork(this);
				}
			}
		} catch (Exception e) {
			FMLLog.severe("Failed to refresh gas pipe network.");
			e.printStackTrace();
		}

		try {
			Iterator<IGasNetworkSource> it = this.sources.iterator();

			while (it.hasNext()) {
				IGasNetworkSource src = it.next();

				if (src == null) {
					it.remove();
					continue;
				}

				src.onNetworkChanged();

				if (((TileEntity) src).isInvalid() || ((TileEntity) src).getWorldObj() == null) {
					it.remove();
					continue;
				} else {
					src.setNetwork(this);
				}
			}
		} catch (Exception e) {
			FMLLog.severe("Failed to refresh gas sources network.");
			e.printStackTrace();
		}

		try {
			Iterator<IGasNetworkVent> it = this.vents.iterator();

			while (it.hasNext()) {
				IGasNetworkVent vent = it.next();

				if (vent == null) {
					it.remove();
					continue;
				}

				vent.onNetworkChanged();

				if (((TileEntity) vent).isInvalid() || ((TileEntity) vent).getWorldObj() == null) {
					it.remove();
					continue;
				} else {
					vent.setNetwork(this);
				}
			}
		} catch (Exception e) {
			FMLLog.severe("Failed to refresh oxygen vents network.");
			e.printStackTrace();
		}
	}

	public synchronized void mergeTo(IGasNetworkPipe pipe) {
		if (pipe.getNetwork() != this) {
			GasPathfinder finder = new GasPathfinder(((TileEntity) pipe).getWorldObj(), new BlockVec3((TileEntity) pipe));
			List<BlockVec3> partNetwork = finder.exploreNetwork();

			boolean newNet = true;
			for (BlockVec3 p : partNetwork) {
				TileEntity nodeTile = p.getTileEntity(((TileEntity) pipe).getWorldObj());
				if (nodeTile instanceof IGasNetworkPipe) {
					if (this.pipes.contains((IGasNetworkPipe) nodeTile)) {
						newNet = false;
						break;
					}
				}
			}

			if (newNet) {
				this.pipes.addAll(finder.getPipesAsTile());
				this.sources.addAll(finder.getSourcesAsTile());
				this.vents.addAll(finder.getVentsAsTile());
				this.refresh();
			}

		}
	}

	@Override
	public IGasNetwork merge(IGasNetwork network) {
		if (network != null && network != this) {
			SSGasNetwork newNetwork = new SSGasNetwork(this.world);
			newNetwork.pipes.addAll(this.pipes);
			newNetwork.pipes.addAll(network.getPipes());
			newNetwork.refresh();
			newNetwork.recalculate();
			return newNetwork;
		}
		return this;
	}

	@Override
	public synchronized void split(IGasNetworkPipe splitPoint) {
		boolean[] dealtWith = { false, false, false, false, false, false };
		List<SSGasNetwork> newNetWorks = new ArrayList<SSGasNetwork>();
		boolean linked = false;

		if (splitPoint instanceof TileEntity) {
			this.pipes.remove(splitPoint);

			/**
			 * Loop through the connected blocks and attempt to see if there are
			 * connections between the two points elsewhere.
			 */
			TileEntity[] connectedBlocks = splitPoint.getAdjacentConnections();

			for (int a = 0; a < connectedBlocks.length; a++) {
				if (connectedBlocks[a] instanceof IGasNetworkPipe) {
					TileEntity connectedBlockA = connectedBlocks[a];

					if (!dealtWith[a]) {
						GasPathfinder finder = new GasPathfinder(((TileEntity) splitPoint).getWorldObj(), new BlockVec3(connectedBlockA), new BlockVec3(
								(TileEntity) splitPoint));
						List<BlockVec3> results = finder.exploreNetwork();

						linked = false;

						for (int b = a; b < connectedBlocks.length; b++) {
							if (a == b) {
								continue;
							}

							if (connectedBlocks[b] instanceof IGasNetworkPipe) {
								TileEntity connectedBlockB = connectedBlocks[b];

								/**
								 * The connections A and B are still intact
								 * elsewhere. Set all references of wire
								 * connection into one network.
								 */
								if (results.contains(new BlockVec3(connectedBlockB))) {
									dealtWith[b] = true;
									linked = true;
								}
							}
						}

						if (!linked) {
							// Create new net
							SSGasNetwork newNetwork = new SSGasNetwork(((TileEntity) splitPoint).getWorldObj());
							newNetwork.setPipes(finder.getPipes());
							newNetwork.setVents(finder.getVents());
							newNetwork.setSources(finder.getSources());
							newNetwork.recalculate();
							this.refresh();
							this.recalculate();
						}

					}
				}

			}
		}
	}

	@Override
	public String toString() {
		return "GasNetwork[" + this.hashCode() + "|Pipes:" + this.pipes.size() + "|Acceptors:" + "]";
	}

	@Override
	public void recalculate() {

	}

	@Override
	public void setPipes(List<BlockVec3> pipes2) {
		List<IGasNetworkPipe> pipeList = new LinkedList<IGasNetworkPipe>();
		for (BlockVec3 p : pipes2) {
			TileEntity pipe = p.getTileEntity(this.world);
			if (pipe instanceof IGasNetworkPipe) {
				((IGasNetworkPipe) pipe).setNetwork(this);
				((IGasNetworkPipe) pipe).onNetworkChanged();
				pipeList.add((IGasNetworkPipe) pipe);
			}
		}
		this.pipes = pipeList;
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
	public List<? extends IGasNetworkPipe> getPipes() {
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

}
