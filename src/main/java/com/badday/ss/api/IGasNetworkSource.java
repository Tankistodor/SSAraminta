package com.badday.ss.api;

import com.badday.ss.core.atmos.GasMixture;


/**
 * Can provide gases // Generator
 * @author KolesnikovAK
 *
 */

@SuppressWarnings("rawtypes")
public interface IGasNetworkSource
{
	
	/** 
	 * Get current GasNetwork or create new
	 * @return IGasNetowrk
	 */
	public IGasNetwork getNetwork();
	
	/**
	 * Set new GasNetwork as IGasNetowork
	 * @param network
	 */
	public void setNetwork(IGasNetwork network);
	
	/**
	 * Event called where network changed (Set Source or Vent)
	 */
	public void onNetworkChanged();
	
	public GasMixture getMyGas();
}