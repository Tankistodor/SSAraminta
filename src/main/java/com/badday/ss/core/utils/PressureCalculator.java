package com.badday.ss.core.utils;

import com.badday.ss.SS;
import com.badday.ss.core.atmos.Pathfinding;

import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by userad on 26/10/14.
 */
@Deprecated
public final class PressureCalculator {
  private PressureCalculator() {}


  public static double getPressureAt(World world, BlockVec3 coords) {

    //First - search nearby airvents
    List airvents = AirVentNet.getNearbyAirvents(world, coords.x, coords.y, coords.z);
    if (airvents.isEmpty()) {
      return 0.0D; //You are dead, man
    }

    //Get distance to all nearby airvents
    List distances = new ArrayList();
    Pathfinding finder;
    long time1, time2, all_time;
    all_time = System.nanoTime();
    for(Object airvent: airvents) {
      //TODO: Add check for enabled

      finder = new Pathfinding(world, coords, (BlockVec3) airvent);
      time1 = System.nanoTime();
      while (!finder.isDone()) {
        time2 = System.nanoTime();
        finder.iterate(1024);
        if ((time2 - time1) > 500 * 1000000.0D) {
          System.out.println("[" + SS.MODNAME + "] Pathfinding timeout");
          break;
        }
      }

      if(SS.Debug) {
         //System.out.println("[" + SS.MODNAME + "] full time:" + (System.nanoTime() - all_time) / 1000000.0D);
      }

      if(finder.isDone()) {
        distances.add(finder.getResult().size());
      }


    }

    //TODO: Make more reliable formula
    double pressure = 0;
    for(Object distance: distances) {
      pressure += 255.0D / (Integer) distance; //Trolly floating point math
    }

    return pressure;
  }

}
