package com.badday.ss.events;

import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.utils.BlockVec3;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by userad on 26/10/14.
 */
public class AirNetAddEvent extends Event {
  public final BlockVec3 coords;

  public AirNetAddEvent(BlockVec3 block, IGasNetworkVent vent) {
    this.coords = block;
    vent.getGasNetwork();    
  }


}
