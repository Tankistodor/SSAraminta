package com.badday.ss.blocks;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.block.wiring.TileEntityCable;
import ic2.core.network.NetworkManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class SSTileEntityIC2Cable  extends TileEntity implements IEnergyConductor, INetworkTileEntityEventListener {
	
	public boolean addedToEnergyNet = false;
	public short color = 0;
	public byte connectivity = 0;
	
	public SSTileEntityIC2Cable() {
		super();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
	}
	
	@Override
	public void validate() {
		if (!this.worldObj.isRemote) {
		      MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
		      this.addedToEnergyNet = true;
		    }
		super.validate();
	}
	
	@Override
	public void invalidate() {
		if ((!this.worldObj.isRemote) && (this.addedToEnergyNet)) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			this.addedToEnergyNet = false;
		}
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		if ((!this.worldObj.isRemote) && (this.addedToEnergyNet)) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			this.addedToEnergyNet = false;
		}
		super.onChunkUnload();
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		return true;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return true;
	}

	@Override
	public void onNetworkEvent(int event) {
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);		
	}

	@Override
	public double getConductionLoss() {
		return 0.45D;
	}

	/**
	 * Max Capacity
	 */
	@Override
	public double getInsulationEnergyAbsorption() {
		return 2048;
	}

	@Override
	public double getInsulationBreakdownEnergy() {
		return 9001.0D;
	}

	@Override
	public double getConductorBreakdownEnergy() {
		return 9021.0D;
	}

	@Override
	public void removeInsulation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeConductor() {
		this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);

	    ((NetworkManager)IC2.network.get()).initiateTileEntityEvent(this, 0, true);
	}
	
}
