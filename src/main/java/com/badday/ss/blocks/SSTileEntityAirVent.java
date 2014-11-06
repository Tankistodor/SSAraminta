 package com.badday.ss.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

import com.badday.ss.SS;
import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.atmos.SSFindSealedBay;
import com.badday.ss.core.atmos.SSGasNetwork;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.events.AirNetAddEvent;
import com.badday.ss.events.AirNetRemoveEvent;
import com.badday.ss.events.GasNetworkRecalculateEvents;

import cpw.mods.fml.common.FMLCommonHandler;

public class SSTileEntityAirVent extends TileEntity implements IGasNetworkVent {

	private final int COOLDOWN = 20;
	public long ticks = 0;
	public SSFindSealedBay findSealedBay = null;
	/** AirVent is emits gasmixture */
	public boolean active = false;
	/** AirVent in sealed bay */
	public boolean sealed = false;
	/** AirVent is conneted to AirNet */
	public boolean added_to_airvent_net = false;
	/** BaySize */
	public int baySize = 0;
	public boolean normalGas = true;
	public IGasNetwork gasNetwork;

	@Override
	public void updateEntity() {

		super.updateEntity();

		this.tickBeat();

		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			return;
		}

		// Air vent works only in spaces
		if (worldObj.provider.dimensionId != SS.instance.spaceDimID) {
			return;
		}
		
		
		if (!added_to_airvent_net) {
			MinecraftForge.EVENT_BUS.post(new AirNetAddEvent(new BlockVec3(this),this));
			added_to_airvent_net = true;
		}

		if (!this.worldObj.isRemote && this.ticks % COOLDOWN == 0) {

			if (this.findSealedBay == null) {
				this.findSealedBay = new SSFindSealedBay(worldObj, new BlockVec3(this));
			}

		}

		if (!this.worldObj.isRemote && this.ticks % (COOLDOWN * 5) == 0) {

			if (this.findSealedBay != null) {
				this.sealed = this.findSealedBay.fullcheck();
				this.active = this.findSealedBay.getActive();
				this.baySize = this.findSealedBay.getSize();
				System.out.println("BaySize: " + this.baySize);
				if (this.baySize > 0)
					this.sealed = true;
			}
			
			if (this.getActive())
				MinecraftForge.EVENT_BUS.post(new GasNetworkRecalculateEvents(this.getGasNetwork())); //demind
		}

	}

	public void tickBeat() {
		if (this.ticks == 0) {
			this.initiate();
		}

		if (this.ticks >= Long.MAX_VALUE) {
			this.ticks = 1;
		}

		this.ticks++;
	}

	public void initiate() {
		// this.gasPressure = new GasPressure(worldObj, new BlockVec3(this));
	}

	@Override
	public void invalidate() {
		super.invalidate();
		this.findSealedBay = null;
		if (added_to_airvent_net) {
			MinecraftForge.EVENT_BUS.post(new AirNetRemoveEvent(new BlockVec3(this)));
			added_to_airvent_net = false;
		}
	}

	@Override
	public void onChunkUnload() {
		if (added_to_airvent_net) {
			MinecraftForge.EVENT_BUS.post(new AirNetRemoveEvent(new BlockVec3(this)));
			added_to_airvent_net = false;
		}
	}

	/**
	 * Gas suitable for breathing
	 * 
	 * @return
	 */
	public boolean isNormalAir() {
		// TODO Добавить определение смеси газов и пригодность для дыхания
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
	}

	@Override
	public void onNetworkChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IGasNetwork getGasNetwork() {
		if (gasNetwork == null) {
			this.gasNetwork = new SSGasNetwork(worldObj);
			if (this.getBlockMetadata() == 0)
				this.gasNetwork.rebuildNetworkFromVent(worldObj, new BlockVec3(this.xCoord,this.yCoord,this.zCoord),1); else
				this.gasNetwork.rebuildNetworkFromVent(worldObj, new BlockVec3(this.xCoord,this.yCoord,this.zCoord),0);
		}
		return this.gasNetwork;
	}
	
	@Override
	public void setNetwork(IGasNetwork network) {
		this.gasNetwork = (IGasNetwork) network;
	}

	@Override
	public int getBaySize() {
		//TODO may be test add reinit findSeal
		return this.baySize;
	}

	@Override
	public boolean getActive() {
		return this.active;
	}

	@Override
	public boolean getSealed() {
		return this.sealed;
	}

	public void printDebugInfo() {
		System.out.println("Vent: "+this.toString());
		System.out.println("    Network: "+this.gasNetwork);
		System.out.println("    Size: "+this.getBaySize());
		System.out.println("    Sealed: "+this.getSealed() + " active: "+ this.getActive());
	}

}
