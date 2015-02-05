package com.badday.ss.api;

import java.util.List;

import com.badday.ss.core.utils.BlockVec3;

/**
 * 
 * @author KolesnikovAK
 * 
 */
public interface IGasNetwork {
	
	/**
	 * Set new gas pipes to GasNetwork
	 * 
	 * @param List<BlockVec3> pipes2
	 */
	public void setPipes(List<BlockVec3> pipes2);

	public void setVents(List<BlockVec3> vents2);

	public void setSources(List<BlockVec3> sourcers2);

	/**
	 * Gets the Set of gas pipes that make up this network.
	 * 
	 * @return conductor set
	 */
	public List<BlockVec3> getPipes();
	public List<? extends IGasNetworkSource> getSources();
	public List<? extends IGasNetworkVent> getVents();

	public void removeSource(IGasNetworkSource node);
	public void removeVent(IGasNetworkVent node);
	public void removePipe(BlockVec3 node);
	
	public int getVetnSize();
	
	public void printDebugInfo();

	public float nipPressure();
	
}