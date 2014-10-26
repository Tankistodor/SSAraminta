package com.badday.ss.blocks;

import com.badday.ss.events.AirNetAddEvent;
import com.badday.ss.events.AirNetRemoveEvent;
import ic2.api.energy.event.EnergyTileLoadEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.core.utils.GasPressure;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class SSTileEntityAirVent extends TileEntity {

	private final int COOLDOWN = 20;
	public long ticks = 0;
	public GasPressure gasPressure = null;
	public boolean active = false;

  public boolean added_to_airvent_net = false;

	@Override
	public void updateEntity() {

		super.updateEntity();

		this.tickBeat();

		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			return;
		}

    if(!added_to_airvent_net) {
      MinecraftForge.EVENT_BUS.post(new AirNetAddEvent(new BlockVec3(this)));
      added_to_airvent_net = true;
    }

		// Air vent works only in spaces
		if (worldObj.provider.dimensionId != SS.instance.spaceDimID) {
			return;
		}

		if (!this.worldObj.isRemote && this.ticks % COOLDOWN == 0) {

			if (this.gasPressure != null) {
				/*
				 * this.active && this.threadSeal.sealedFinal.get();
				 * this.calculatingSealed = this.active &&
				 * this.threadSeal.looping.get();
				 * this.gasPressure.recheck(this);
				 */

			} else {
				this.gasPressure = new GasPressure(worldObj, new BlockVec3(this));
				if (SS.Debug)
					System.out.println("[" + SS.MODNAME + "] create gasPressure class");
				// this.gasPressure.fullcheck();
			}

		}

		if (!this.worldObj.isRemote && this.ticks % (COOLDOWN * 5) == 0) {
			if (this.gasPressure != null) {
				this.gasPressure.fullcheck();
			}
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
		this.gasPressure = null;
    if(added_to_airvent_net) {
      MinecraftForge.EVENT_BUS.post(new AirNetRemoveEvent(new BlockVec3(this)));
      added_to_airvent_net = false;
    }
	}

  @Override
  public void onChunkUnload() {
    if(added_to_airvent_net) {
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

}
