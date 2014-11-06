package com.badday.ss.api;

import net.minecraftforge.common.util.ForgeDirection;

/*
 * AirVent
 */
public interface IGasNetworkVent {
	
	public IGasNetwork getGasNetwork();
	
	public void setNetwork(IGasNetwork network);

	public void onNetworkChanged();
	
	public int getBaySize();
	
	public boolean getActive();
	public boolean getSealed();
	
	public boolean canConnectFrom(ForgeDirection direction);

}
