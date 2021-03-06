package com.badday.ss.core.atmos;

import java.util.Date;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.utils.AirVentNet;
import com.badday.ss.core.utils.BlockVec3;


public class FindNearestVentJob extends Thread {
	
	private World world;
	private BlockVec3 coords;
	private BlockVec3 nearestVent = BlockVec3.INVALID_VECTOR;
	//private Pathfinding pathFinding;
	private boolean stop = false;
	private int maxIterations;
	private boolean done = false;
	public int distance = 0;
	

	
	public FindNearestVentJob(World world, BlockVec3 coords) {
		super("Path Finding Nearest Vent");
		this.world = world;
		this.coords = coords;
		this.start();
	}
	
	@Override
	public void run() {
		
		try {
			Pathfinding finder;
			List airvents = AirVentNet.getNearbyAirvents(world, coords.x, coords.y, coords.z);
			
			if (airvents.isEmpty()) {
				this.distance = 0; // You are dead, man
				return;
			}
			
			long time1, time2, all_time;
			all_time = System.nanoTime();
			
			for(Object airvent: airvents) {

				  //nearestVent = (BlockVec3) airvent;
			      finder = new Pathfinding(world, (BlockVec3) airvent, coords);
			      //nearestVent = (BlockVec3) airvent;

			      while (!finder.isDone()) {
			        
			        
			        if (isTerminated() || finder.isDone()) {
			        	//if (finder.getResult().size() > 0)
			        		nearestVent = (BlockVec3) airvent;
						break;
					}

					long startTime = new Date().getTime();
					long elapsedtime = 0;
			        
			        finder.iterate();
			       
			        if (finder.getResult().size() > 0)
		        		nearestVent = (BlockVec3) airvent;
			        
			        elapsedtime = new Date().getTime() - startTime;
					double timeToWait = elapsedtime * 1.5;
					sleep((long) timeToWait);
			        
			      }
			      
			if (finder != null) {
				if (finder.getResult().size() > 0) {
					distance = finder.getResult().size();
				}
			}
			/*for (Object airvent : airvents) {
				for (int i = 0; i < maxIterations; ++i) {
					if (isTerminated() || pathFinding.isDone()) {
						break;
					}

					long startTime = new Date().getTime();
					long elapsedtime = 0;

					pathFinding.iterate();

					elapsedtime = new Date().getTime() - startTime;
					double timeToWait = elapsedtime * 1.5;
					sleep((long) timeToWait);
				}*/
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			done = true;
		}
	}
	
	public synchronized void terminate() {
		stop = true;
	}

	public synchronized boolean isTerminated() {
		return stop;
	}

	public synchronized boolean isDone() {
		return done;
	}

	public synchronized int getDistance() {
		return distance;
	}

	public synchronized boolean getValidAtmos() {
		if (distance > 0 && !nearestVent.equals(BlockVec3.INVALID_VECTOR)) {
			TileEntity te = nearestVent.getTileEntity(world);
			if (te instanceof IGasNetworkVent) {
				return ((IGasNetworkVent) te).getGasIsNormal(); 
			}
		}
		return false;
	}
	
	public synchronized BlockVec3 getNearestVent() {
		if (distance > 0 && !nearestVent.equals(BlockVec3.INVALID_VECTOR)) {
			return nearestVent.clone();
		}
		return BlockVec3.INVALID_VECTOR;
	}

	
}
