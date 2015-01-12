package com.badday.ss.events;

import com.badday.ss.SS;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.atmos.GasMixture;

import cpw.mods.fml.common.eventhandler.Event;

public class GasVentRecalculateEvents  extends Event {

	public GasVentRecalculateEvents(IGasNetworkVent vent) {
		
		if (vent.getGasNetwork() != null) {
			
			vent.getTank().dispel("fluid.oxygen","fluid.carbondioxide",(int) (vent.getBaySize()/99)); // dispel 1% oxygen -> CO2

			for (IGasNetworkSource src : vent.getGasNetwork().getSources()) {
								
				if (vent.getTank().capacity > vent.getTank().getTotalAmount()) {
					GasMixture gas = src.getMyGas();
					vent.getTank().fill(gas);
				}
				
				if (SS.Debug) {
					System.out.println("    "+vent+" "+vent.getTank().toString());
					System.out.println("    "+vent+" Pressure: "+vent.getPressure() + " hPa Atmos right:" + vent.getGasIsNormal());
				}
			}
			
		}
		
		
	}
	
}
