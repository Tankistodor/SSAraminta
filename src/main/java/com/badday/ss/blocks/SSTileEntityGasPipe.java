package com.badday.ss.blocks;

import ic2.api.network.INetworkClientTileEntityEventListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkPipe;
import com.badday.ss.core.atmos.SSGasNetwork;
import com.badday.ss.core.utils.BlockVec3;

public class SSTileEntityGasPipe extends TileEntity implements IGasNetworkPipe,INetworkClientTileEntityEventListener {

	public long ticks = 0;
	private SSGasNetwork network;

	public TileEntity[] adjacentConnections = null;

	public SSTileEntityGasPipe() {
		super();
	}
	
	public boolean canConnect(ForgeDirection direction) {
		TileEntity adjacentTile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, direction);

		if (adjacentTile instanceof SSTileEntityGasPipe) {
			return true;
		} else if (adjacentTile instanceof SSTileEntityAirVent || adjacentTile instanceof SSTileEntityGasMixer) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canUpdate() {
		return this.worldObj == null || !this.worldObj.isRemote;

	}

	 @Override
	    public void updateEntity()
	    {
	        super.updateEntity();
	        
	        if (this.ticks == 0)
	        {
	            //this.initiate();
	        }

	        if (this.ticks >= Long.MAX_VALUE)
	        {
	            this.ticks = 1;
	        }

	        this.ticks++;
	    }
	@Override
	public void validate() {
		super.validate();
		
		if (!this.worldObj.isRemote)
    	{
    		this.refresh();
    	}

		if (this.worldObj != null && this.worldObj.isRemote) {
			final BlockVec3 thisVec = new BlockVec3(this);
			this.worldObj.func_147479_m(thisVec.x, thisVec.y, thisVec.z);
		}
	}

	@Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote)
        {
            this.getNetwork().split(this);
        }

        super.invalidate();
    }
	

	/*public void onAdjacentColorChanged(ForgeDirection direction) {
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);

		if (!this.worldObj.isRemote) {
			this.refresh();
		}
	}*/

	@Override
	public TileEntity[] getAdjacentConnections() {
	    /**
         * Cache the adjacentConnections.
         */
        if (this.adjacentConnections == null)
        {
            this.adjacentConnections = SSGasNetwork.getAdjacentOxygenConnections(this);
        }

        return this.adjacentConnections;
	}
	
	public TileEntity[] getAdjacentConnections2() {
	    /**
         * Cache the adjacentConnections.
         */
        if (this.adjacentConnections == null)
        {
            this.adjacentConnections = SSGasNetwork.getAdjacentOxygenConnections2(this);
        }

        return this.adjacentConnections;
	}

	@Override
	public void refresh() {
		if (!this.worldObj.isRemote)
        {
            this.adjacentConnections = null;

            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
            {
                TileEntity tileEntity = new BlockVec3(this).getTileEntityOnSide(this.worldObj, side);

                if (tileEntity != null)
                {
                    if (tileEntity.getClass() == this.getClass() && tileEntity instanceof SSTileEntityGasPipe && !this.getNetwork().equals(((SSTileEntityGasPipe) tileEntity).getNetwork()))
                    {
                        this.setNetwork((IGasNetwork) this.getNetwork().merge(((SSTileEntityGasPipe) tileEntity).getNetwork()));
                    }
                }
            }

            this.getNetwork().refresh();
        }
	}

	@Override
	public void reset() {
		// TODO aa 
	}

	@Override
	public void onNetworkChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public IGasNetwork getNetwork() {
		if (this.network == null)
        {
            this.resetNetwork();
        }

        return this.network;
	}

	@Override
	public void setNetwork(IGasNetwork network1) {
		this.network = (SSGasNetwork) network1;
	}

	@Override
	public void resetNetwork() {
		SSGasNetwork net = new SSGasNetwork(this.worldObj);
		net.pipes.add(this);
		this.network=net;
	}

	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
	}

	@Override
	public void onNetworkEvent(EntityPlayer player, int event) {
		System.out.println("This net event: "+event);		
	}

}
