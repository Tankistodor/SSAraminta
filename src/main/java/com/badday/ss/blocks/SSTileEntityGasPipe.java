package com.badday.ss.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkPipe;
import com.badday.ss.core.utils.BlockVec3;

public class SSTileEntityGasPipe extends TileEntity implements IGasNetworkPipe{
	
	public long ticks = 0;

	public SSTileEntityGasPipe(int meta) {
		// TODO Auto-generated constructor stub
	}

    public boolean canConnect(ForgeDirection direction)
    {
        TileEntity adjacentTile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, direction);

        /*
            if (adjacentTile instanceof IColorable)
            {
                return this.getColor() == ((IColorable) adjacentTile).getColor();
            }

            return true;
        */

        return false;
    }
	
    @Override
    public boolean canUpdate()
    {
        return this.worldObj == null || !this.worldObj.isRemote;

    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }
    
    @Override
    public void validate()
    {
        super.validate();

        if (this.worldObj != null && this.worldObj.isRemote)
        {
            final BlockVec3 thisVec = new BlockVec3(this);
            this.worldObj.func_147479_m(thisVec.x, thisVec.y, thisVec.z);
        }
    }
    
    public byte getColor()
    {
        return (byte) this.blockMetadata;
    }
    
    @SuppressWarnings("unchecked")
    public void setColor(byte col)
    {
        this.blockMetadata = col;

        if (this.worldObj != null)
        {
            if (this.worldObj.isRemote)
            {
                final BlockVec3 thisVec = new BlockVec3(this);
                this.worldObj.func_147479_m(thisVec.x, thisVec.y, thisVec.z);
            }
            else
            {
                this.getNetwork().split(this);
                this.resetNetwork();
            }
        }
    }
    
    public void onAdjacentColorChanged(ForgeDirection direction)
    {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);

        if (!this.worldObj.isRemote)
        {
            this.refresh();
        }
    }

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
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IGasNetwork getNetwork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNetwork(IGasNetwork network) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetNetwork() {
		// TODO Auto-generated method stub
		
	}

}
