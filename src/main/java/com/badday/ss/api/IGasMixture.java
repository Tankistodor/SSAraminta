package com.badday.ss.api;

import java.util.List;

import com.badday.ss.core.atmos.GasMixture;

/**
 * 
 * @author xlatm
 *
 */
public interface IGasMixture {
	
	public float getPressure();
	public void setPressure(float newPressure);
	
	public List<GasMixture> getGasMixture();
	public void setGasMixture(GasMixture newMixture);
	
	public float getTemperature();
	public void setTemperature(float newTemperature);

}
