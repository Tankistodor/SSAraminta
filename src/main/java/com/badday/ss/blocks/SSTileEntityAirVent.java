package com.badday.ss.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.core.utils.GasPressure;

import cpw.mods.fml.common.FMLCommonHandler;

public class SSTileEntityAirVent extends TileEntity {

	private final int COOLDOWN = 20;
	public long ticks = 0;
	public GasPressure gasPressure = null;
	public boolean active = false;

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
