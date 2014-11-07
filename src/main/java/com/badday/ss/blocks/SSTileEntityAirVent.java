 package com.badday.ss.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.badday.ss.SS;
import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.atmos.GasMixture;
import com.badday.ss.core.atmos.GasUtils;
import com.badday.ss.core.atmos.SSFindSealedBay;
import com.badday.ss.core.atmos.SSGasNetwork;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.events.AirNetAddEvent;
import com.badday.ss.events.AirNetRemoveEvent;
import com.badday.ss.events.GasVentRecalculateEvents;
import com.badday.ss.events.RebuildNetworkEvent;

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
	
	public GasMixture tank = new GasMixture();

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
				
				
				if (this.baySize > 0) {
					this.tank.setCapacity(this.baySize * 10);
					this.sealed = true;
				} else {
					this.tank = new GasMixture();
					this.tank.setCapacity(0);
					this.sealed = false;
				}
			}
			
			if (this.getActive()) MinecraftForge.EVENT_BUS.post(new GasVentRecalculateEvents(this)); //get some gas from source
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

	}

	@Override
	public void invalidate() {
		super.invalidate();
		this.findSealedBay = null;
		if (added_to_airvent_net) {
			MinecraftForge.EVENT_BUS.post(new AirNetRemoveEvent(new BlockVec3(this)));
			added_to_airvent_net = false;
		}
		if (this.gasNetwork != null) {
			this.getGasNetwork().removeVent(this);;
		}
		
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (added_to_airvent_net) {
			MinecraftForge.EVENT_BUS.post(new AirNetRemoveEvent(new BlockVec3(this)));
			added_to_airvent_net = false;
		}
		if (this.gasNetwork != null) {
			this.getGasNetwork().removeVent(this);;
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
	public void readFromNBT(NBTTagCompound tags) {
		super.readFromNBT(tags);
		int count = tags.getInteger("tankCount");
		this.tank.setCapacity(tags.getInteger("tankCapacity"));
		for (int i=0; i < count; i++) {
			if (tags.getBoolean("hasFluid" + i)) {
				this.tank.addGas(FluidRegistry.getFluidStack(tags.getString("fluidName" + i), tags.getInteger("amount" + i)));
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tags) {
		super.writeToNBT(tags);
		tags.setInteger("tankCount", this.tank.mixtureTank.size());
		tags.setInteger("tankCapacity", this.tank.getCapacity());
		int i = 0;
		for (FluidTank t : this.tank.mixtureTank) {
			FluidStack liquid = t.getFluid();
			if (liquid != null && liquid.amount > 0) {
				i++;
				tags.setBoolean("hasFluid"+i, liquid != null);
				tags.setString("fluidName"+i, liquid.getFluid().getName());
	            tags.setInteger("amount"+i, liquid.amount);
			}
		}
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
				GasUtils.registeredEventRebuildGasNetwork(new RebuildNetworkEvent(worldObj,new BlockVec3(this.xCoord,this.yCoord+1,this.zCoord))); else
					GasUtils.registeredEventRebuildGasNetwork(new RebuildNetworkEvent(worldObj,new BlockVec3(this.xCoord,this.yCoord-1,this.zCoord)));
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
		System.out.println("Vent: "+this.toString() + " Net: "+this.gasNetwork + " Size: "+this.getBaySize() + 
				" S:"+this.getSealed() + " A:"+ this.getActive());
	}

	@Override
	public boolean canConnectFrom(ForgeDirection direction) {
		if (this.blockMetadata == 0 && direction.equals(ForgeDirection.DOWN)) {
			return true;
		}
		if (this.blockMetadata == 1 && direction.equals(ForgeDirection.UP)) {
			return true;
		}
		return false;
	}

	@Override
	public GasMixture getTank() {
		return this.tank;
	}

	@Override
	public float getPressure() {
		return this.getTank().getPressure();
	}

}
