package com.badday.ss.api;


/**
 * Can provide gases // Generator
 * @author KolesnikovAK
 *
 */

@SuppressWarnings("rawtypes")
public interface IGasNetworkSource
{
	public float getGasPressure();
	public void setGasPressure();
	
	public int getGasTemperature();
	public void setGasTemperature();

	public void setNetwork(IGasNetwork network);
	public void onNetworkChanged();
}