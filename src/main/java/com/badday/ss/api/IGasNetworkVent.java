package com.badday.ss.api;

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

}
