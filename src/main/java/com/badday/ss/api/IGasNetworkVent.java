package com.badday.ss.api;

import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.core.atmos.GasMixture;

/**
 * GasNetwork interface
 * @author KolesnikovAK
 *
 */
public interface IGasNetworkVent {
	
	/** 
	 * Get current GasNetwork or create new
	 * @return IGasNetowrk
	 */
	public IGasNetwork getGasNetwork();
	
	/**
	 * Set new GasNetwork as IGasNetowork
	 * @param IGasNetwork network
	 */
	public void setNetwork(IGasNetwork network);

	/**
	 * Event called where network changed (Set Source or Vent)
	 */
	public void onNetworkChanged();
	
	/**
	 * Return total bay Size if bay are sealed
	 * @return int
	 */
	public int getBaySize();
	
	/**
	 * Return true if this GasVent is sealed and ready to emit gas to bay
	 * @return boolean
	 */
	public boolean getActive();
	
	/**
	 * Return true if this GasVent placed in sealed bay
	 * @return boolean
	 */
	public boolean getSealed();
	
	public boolean canConnectFrom(ForgeDirection direction);

	public GasMixture getTank();

}
