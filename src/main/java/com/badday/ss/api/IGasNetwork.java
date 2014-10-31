package com.badday.ss.api;

import java.util.List;

import net.minecraft.tileentity.TileEntity;

/**
 * 
 * @author KolesnikovAK
 *
 */
public interface IGasNetwork
{
    /**
     * Produces oxygen in this oxygen network.
     *
     * @return Rejected energy in Joules.
     */
	@Deprecated
    public float produce(float sendAmount, TileEntity... ignoreTiles);

    /**
     * Gets the total amount of gas requested/needed in the gas
     * network.
     *
     * @param ignoreTiles The TileEntities to ignore during this calculation (optional).
     */
    public float getRequest(TileEntity... ignoreTiles);
    
    /**
     * Refreshes and cleans up conductor references of this network, as well as
     * updating the acceptor set.
     */
    public void refresh();

    /**
     * Gets the Set of conductors that make up this network.
     *
     * @return conductor set
     */
    public List<IGasNetworkPipe> getTransmitters();

    /**
     * Creates a new network that makes up the current network and the network
     * defined in the parameters. Be sure to refresh the new network inside this
     * method.
     *
     * @param network - network to merge
     */
    public IGasNetwork merge(IGasNetwork network);
    
    
    /**
     * Split network in to to diferent network
     * @param target
     */
    public void split(IGasNetworkPipe target);
    
}