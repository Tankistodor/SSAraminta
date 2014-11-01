package com.badday.ss.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkPipe;
import com.badday.ss.core.atmos.SSGasNetwork;
import com.badday.ss.core.utils.BlockVec3;

public class SSTileEntityGasPipe extends TileEntity implements IGasNetworkPipe {

	public long ticks = 0;
	private byte lastPipeColor = -1;
	private SSGasNetwork network;

	public TileEntity[] adjacentConnections = null;

	public SSTileEntityGasPipe(byte meta) {
		super();
	}

	public boolean canConnect(ForgeDirection direction) {
		TileEntity adjacentTile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, direction);

		if (adjacentTile instanceof SSTileEntityGasPipe) {
			return this.getColor() == ((SSTileEntityGasPipe) adjacentTile).getColor();
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

	        if (!this.worldObj.isRemote && this.ticks % 60 == 0 && this.lastPipeColor != this.getColor())
	        {
	            //GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), this.worldObj.provider.dimensionId);
	            this.lastPipeColor = this.getColor();
	        }
	    }
	@Override
	public void validate() {
		super.validate();

		if (this.worldObj != null && this.worldObj.isRemote) {
			final BlockVec3 thisVec = new BlockVec3(this);
			this.worldObj.func_147479_m(thisVec.x, thisVec.y, thisVec.z);
		}
	}

	public byte getColor() {
		return (byte) this.blockMetadata;
	}

	@SuppressWarnings("unchecked")
	public void setColor(byte col) {
		this.blockMetadata = col;

		if (this.worldObj != null) {
			if (this.worldObj.isRemote) {
				final BlockVec3 thisVec = new BlockVec3(this);
				this.worldObj.func_147479_m(thisVec.x, thisVec.y, thisVec.z);
			} else {
				this.getNetwork().split(this);
				this.resetNetwork();
			}
		}
	}

	public void onAdjacentColorChanged(ForgeDirection direction) {
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);

		if (!this.worldObj.isRemote) {
			this.refresh();
		}
	}

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
		// TODO Auto-generated method stub

	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);

		final byte by = par1NBTTagCompound.getByte("pipeColor");
		this.setColor(by);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setByte("pipeColor", this.getColor());
	}

}
