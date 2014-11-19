package com.badday.ss.blocks;

import net.minecraft.tileentity.TileEntity;

public class SSTileEntityAirlockFrameController extends SSTileEntityAirlockFrame {

	
	/**
	 * false - n/s airLock
	 * true - e/w airLock
	 */
	private boolean sideEW = false; 
	
	/**
	 * Remove airlock (block break)
	 */
	public void removeAirLock() {
		// TODO Auto-generated method stub
		
	}

	public boolean getEW() {
		return this.sideEW;
	}
	
	public void setEW(boolean ew) {
		System.out.println(ew);
		this.sideEW = ew;
	}

}
