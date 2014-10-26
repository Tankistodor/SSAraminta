package com.badday.ss.events;

import com.badday.ss.core.utils.BlockVec3;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * Created by userad on 26/10/14.
 */
public class AirNetRemoveEvent extends Event{
  public final BlockVec3 coords;

  public AirNetRemoveEvent(BlockVec3 block) {
    this.coords = block;
  }

}
