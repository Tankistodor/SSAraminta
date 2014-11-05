package com.badday.ss.events;

import com.badday.ss.api.IGasNetwork;

import cpw.mods.fml.common.eventhandler.Event;

public class GasNetworkRecalculateEvents  extends Event {

	public GasNetworkRecalculateEvents(IGasNetwork network) {
		network.recalculate();
		network.printDebugInfo();
	}
	
}
