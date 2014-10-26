package com.badday.ss.events;

import com.badday.ss.core.utils.BlockVec3;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by userad on 26/10/14.
 */
public class AirNetAddEvent extends Event {
  public final BlockVec3 coords;

  public AirNetAddEvent(BlockVec3 block) {
    this.coords = block;
  }


}
