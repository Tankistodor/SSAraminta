package com.badday.ss.api;

/*
 * AirVent
 */
public interface IGasNetworkVent {
	
	public void setNetwork(IGasNetwork network);

	public void onNetworkChanged();

}
