package com.badday.ss.api;


/**
 * Can provide gases // Generator
 * @author KolesnikovAK
 *
 */

@SuppressWarnings("rawtypes")
public interface IGasNetworkSource
{
	
	public int getGasTemperature();
	public void setGasTemperature();

	public IGasNetwork getNetwork();
	public void setNetwork(IGasNetwork network);
	public void onNetworkChanged();
	
	public float nipGas();
}