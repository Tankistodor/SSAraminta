package com.badday.ss.core.atmos;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.core.utils.AirVentNet;
import com.badday.ss.core.utils.BlockVec3;

/**
 * 
 * @author KolesnikovAK
 *
 */
public final class FindNearestVent {
	
	public static int getDistance(World world, BlockVec3 coords) {
		//First - search nearby airvents
	    List airvents = AirVentNet.getNearbyAirvents(world, coords.x, coords.y, coords.z);
	    if (airvents.isEmpty()) {
	      return 0; //You are dead, man
	    }
	    
	    //Get distance to all nearby airvents
	    List distances = new ArrayList();
	    Pathfinding finder;
	    long time1, time2, all_time;
	    all_time = System.nanoTime();
	    for(Object airvent: airvents) {
	      //TODO: Add check for enabled

	      finder = new Pathfinding(world, (BlockVec3) airvent, coords);
	      time1 = System.nanoTime();
	      while (!finder.isDone()) {
	        time2 = System.nanoTime();
	        finder.iterate(Pathfinding.PATH_ITERATIONS);
	        long time3 = System.nanoTime();
	        System.out.println("[" + SS.MODNAME + "] In iterate time: " + (time3 - time2) / 1000000.0D);
	        if ((time2 - time1) > 500 * 1000000.0D) {
	          System.out.println("[" + SS.MODNAME + "] Pathfinding to GasVent is timeout");
	          break;
	        }
	      }

	      if(SS.Debug) {
	         //System.out.println("[" + SS.MODNAME + "] full time:" + (System.nanoTime() - all_time) / 1000000.0D);
	      }

	      if(finder.isDone()) {
	        return finder.getResult().size();
	      }


	    }

	    return 0;
	}
}
