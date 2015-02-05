package com.badday.ss.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.World;

import com.badday.ss.SSConfig;

/**
 * Created by userad on 25/10/14.
 * TODO: Add world support
 */
public final class AirVentNet {
  public static ConcurrentHashMap airvents = new ConcurrentHashMap();
  public static ConcurrentHashMap airvents_coord_x = new ConcurrentHashMap();

  public static void registerAirVent(BlockVec3 vent) {
    String index = vent.x + ":" + vent.y + ":" + vent.z;
    airvents.put(index, vent);

    //Fast search, bitch!
    //Anyway, we need better hash function for this
    airvents_coord_x.put(vent.x, index);
  }

  public static void removeAirVent(BlockVec3 vent) {
    String index = vent.x + ":" + vent.y + ":" + vent.z;
    airvents.remove(index);
    airvents_coord_x.remove(vent.x);
  }

  public static List getNearbyAirvents(World w, int x, int y, int z) {
    int radius = SSConfig.ssPathfinderMaxDistance;
    BlockVec3 candidate;
    List neighbours = new ArrayList();
    for(int i= x - radius;i< x + radius; i++) {
      if(airvents_coord_x.get(i) != null) {
        candidate = (BlockVec3) airvents.get(airvents_coord_x.get(i));
        if(((z - candidate.z) * (z - candidate.z) + (y - candidate.y) * (y - candidate.y)) < radius * radius) {
        		neighbours.add(candidate);
        }
      }
    }
    return neighbours;
  }

}
