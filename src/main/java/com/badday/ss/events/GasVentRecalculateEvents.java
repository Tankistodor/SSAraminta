package com.badday.ss.events;

import com.badday.ss.SS;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.atmos.GasMixture;

import cpw.mods.fml.common.eventhandler.Event;

public class GasVentRecalculateEvents  extends Event {

	public GasVentRecalculateEvents(IGasNetworkVent vent) {
		//network.recalculate();
		
		
		if (vent.getGasNetwork() != null) {
			
			if (SS.Debug) vent.getGasNetwork().printDebugInfo();
			
			for (IGasNetworkSource src : vent.getGasNetwork().getSources()) {
				GasMixture gas = src.getMyGas();
				vent.getTank().fill(gas);
				
				if (SS.Debug) {
					System.out.println("    "+vent.getTank().toString());
					System.out.println("    Pressure: "+vent.getPressure() + " hPa");
				}
			}
			
		}
		
		
	}
	
}
