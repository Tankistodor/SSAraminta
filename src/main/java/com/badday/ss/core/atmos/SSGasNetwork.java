package com.badday.ss.core.atmos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkConnector;
import com.badday.ss.api.IGasNetworkPipe;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.core.utils.BlockVec3;

/**
 * 
 * @author KolesnikovAK
 * 
 */
public class SSGasNetwork implements IGasNetwork {

	// *private final Set<ITransmitter> pipes = new HashSet<ITransmitter>();
	public List<IGasNetworkPipe> pipes = new LinkedList<IGasNetworkPipe>(); // Pipes
	public List<IGasNetworkSource> source = new LinkedList<IGasNetworkSource>(); // Pipes

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
		// TODO Auto-generated method stub

	}

	@Override
	public List<IGasNetworkPipe> getTransmitters() {
		return this.pipes;
	}

	@Override
	public IGasNetwork merge(IGasNetwork network) {
		if (network != null && network != this) {
			SSGasNetwork newNetwork = new SSGasNetwork();
			newNetwork.pipes.addAll(this.pipes);
			newNetwork.pipes.addAll(network.getTransmitters());
			newNetwork.refresh();
			return newNetwork;
		}

		return this;
	}

	@Override
	public void split(IGasNetworkPipe splitPoint) {
		if (splitPoint instanceof TileEntity) {
			this.pipes.remove(splitPoint);

			/**
			 * Loop through the connected blocks and attempt to see if there are
			 * connections between the two points elsewhere.
			 */
			TileEntity[] connectedBlocks = splitPoint.getAdjacentConnections();

			for (TileEntity connectedBlockA : connectedBlocks) {
				if (connectedBlockA instanceof IGasNetworkPipe) {
					for (final TileEntity connectedBlockB : connectedBlocks) {
						if (connectedBlockA != connectedBlockB && connectedBlockB instanceof IGasNetworkPipe) {
							GasPathfinder finder = new GasPathfinder(((TileEntity) splitPoint).getWorldObj(), new BlockVec3(connectedBlockB),
									new BlockVec3((TileEntity) splitPoint));
							finder.init(new BlockVec3(connectedBlockA));

							if (finder.results.size() > 0) {
								/**
								 * The connections A and B are still intact
								 * elsewhere. Set all references of wire
								 * connection into one network.
								 */

								for (BlockVec3 node : finder.closedSet) {
									TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).getWorldObj());

									if (nodeTile instanceof SSTileEntityGasPipe) {
										if (nodeTile != splitPoint) {
											((SSTileEntityGasPipe) nodeTile).setNetwork(this);
										}
									}
								}
							} else {
								/**
								 * The connections A and B are not connected
								 * anymore. Give both of them a new network.
								 */
								SSGasNetwork newNetwork = new SSGasNetwork();

								for (BlockVec3 node : finder.closedSet) {
									TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).getWorldObj());

									if (nodeTile instanceof SSTileEntityGasPipe) {
										if (nodeTile != splitPoint) {
											newNetwork.getTransmitters().add((IGasNetworkPipe) nodeTile);
										}
									}
								}

								newNetwork.refresh();
							}
						}
					}
				}
			}
		}
	}
	
	
	
	 @Override
	    public String toString()
	    {
	        return "GasNetwork[" + this.hashCode() + "|Pipes:" + this.pipes.size() + "|Acceptors:" + "]";
	    }

}
