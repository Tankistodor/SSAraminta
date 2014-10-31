package com.badday.ss.core.atmos;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkPipe;

public class SSTileEntityGasPipe extends TileEntity implements IGasNetworkPipe {

	@Override
	public TileEntity[] getAdjacentConnections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canConnect(ForgeDirection direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SSGasNetwork getNetwork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNetwork(IGasNetwork network) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetNetwork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

}
