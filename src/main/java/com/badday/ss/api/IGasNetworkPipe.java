package com.badday.ss.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/*
 * GasPipe
 */
public interface IGasNetworkPipe
{

    /**
     * Gets a list of all the connected TileEntities that this conductor is
     * connected to. The array's length should be always the 6 adjacent wires.
     *
     * @return
     */
    public TileEntity[] getAdjacentConnections();
    
    /**
     * Refreshes the conductor
     */
    public void refresh();

    public void reset();
    
    public void onNetworkChanged();
    
    public boolean canConnect(ForgeDirection direction);
    
    public IGasNetwork getNetwork();
	    
    public void setNetwork(IGasNetwork network);
    
    public void resetNetwork();
    
    public void validate();
    
    public void invalidate();
    
}