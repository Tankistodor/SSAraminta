package com.badday.ss.api;

import java.util.Collection;
import java.util.List;

import com.badday.ss.core.utils.BlockVec3;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * 
 * @author KolesnikovAK
 * 
 */
public interface IGasNetwork {
	
	

	public void rebuildNetwork(World w, BlockVec3 node);

	/**
	 * Calculate new values of gas pressure and mixture
	 */
	public void recalculate();

	/**
	 * Set new gas pipes
	 * 
	 * @param pipes2
	 */
	public void setPipes(List<BlockVec3> pipes2);

	public void setVents(List<BlockVec3> vents2);

	public void setSources(List<BlockVec3> sourcers2);

	/**
	 * Gets the Set of gas pipes that make up this network.
	 * 
	 * @return conductor set
	 */
	public List<BlockVec3> getPipes();
	public List<? extends IGasNetworkSource> getSources();
	public List<? extends IGasNetworkVent> getVents();

	public int getVetnSize();
	
	public void printDebugInfo();

	public void rebuildNetworkFromVent(World worldObj, BlockVec3 blockVec3);

	public float nipPressure();
	
}