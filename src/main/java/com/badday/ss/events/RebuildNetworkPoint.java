package com.badday.ss.events;

import net.minecraft.world.World;

import com.badday.ss.core.utils.BlockVec3;

public class RebuildNetworkPoint {
	public final BlockVec3 coords;
	public final World world;
	
	public RebuildNetworkPoint(World world, BlockVec3 block) {
	 	this.world = world;
	    this.coords = block;
	  }		
}
