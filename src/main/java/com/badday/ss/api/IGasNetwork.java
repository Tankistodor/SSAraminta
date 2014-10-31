package com.badday.ss.api;

import java.util.Collection;
import java.util.List;

import com.badday.ss.core.utils.BlockVec3;

import net.minecraft.tileentity.TileEntity;

/**
 * 
 * @author KolesnikovAK
 * 
 */
public interface IGasNetwork {
	/**
	 * Produces oxygen in this oxygen network.
	 * 
	 * @return Rejected energy in Joules.
	 */
	@Deprecated
	public float produce(float sendAmount, TileEntity... ignoreTiles);

	/**
	 * Gets the total amount of gas requested/needed in the gas network.
	 * 
	 * @param ignoreTiles
	 *            The TileEntities to ignore during this calculation (optional).
	 */
	public float getRequest(TileEntity... ignoreTiles);

	/**
	 * Refreshes and cleans up conductor references of this network, as well as
	 * updating the acceptor set.
	 */
	public void refresh();

	/**
	 * Creates a new network that makes up the current network and the pipe or
	 * pipes on parameters. Be sure to refresh the new network inside this method.
	 * 
	 * @param pipe
	 *            - pipe to merge
	 */
	public void mergeTo(IGasNetworkPipe pipe);

	/**
	 * Creates a new network that makes up the current network and the network
	 * defined in the parameters. Be sure to refresh the new network inside this
	 * method.
	 * 
	 * @param network
	 *            - network to merge
	 */
	public IGasNetwork merge(IGasNetwork network);

	/**
	 * Split network in to to diferent network
	 * 
	 * @param target
	 */
	public void split(IGasNetworkPipe target);

	/**
	 * Calculate new values of gas pressure and mixture
	 */
	public void recalculate();

	/**
	 * Set new gas pipes
	 * 
	 * @param pipes2
	 */
	public void setPipes(List<BlockVec3> pipes2);

	public void setVents(List<BlockVec3> vents2);

	public void setSources(List<BlockVec3> sourcers2);

	/**
	 * Gets the Set of gas pipes that make up this network.
	 * 
	 * @return conductor set
	 */
	public List<? extends IGasNetworkPipe> getPipes();
	public List<? extends IGasNetworkSource> getSources();
	public List<? extends IGasNetworkVent> getVents();

}