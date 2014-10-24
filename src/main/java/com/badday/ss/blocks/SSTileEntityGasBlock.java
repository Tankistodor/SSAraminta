package com.badday.ss.blocks;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.core.utils.BlockVec3;

public class SSTileEntityGasBlock extends TileEntity {

	/**
	 * Head of air vent
	 */
	public BlockVec3 head = BlockVec3.INVALID_VECTOR;

	public SSTileEntityGasBlock() {
		super();
		if (SS.isServer()) {
			//this.head = new BlockVec3(xCoord, yCoord, zCoord);

			/*if (SS.Debug)
				System.out.println("[" + SS.MODNAME + "] create TEGasBlock on " + head.toString() + " " + isNormalAir());
				*/
		}
	}

	public boolean isNormalAir() {
		if (SS.isServer() && head != null && head.equals(BlockVec3.INVALID_VECTOR) && worldObj != null) {
			TileEntity t = head.getTileEntity(worldObj);
			if (t instanceof SSTileEntityAirVent) {
				return ((SSTileEntityAirVent) t).isNormalAir();
			}
		}
		return false;
	}

	
	public SSTileEntityAirVent getTileEntityAirVent() {
		TileEntity t = head.getTileEntity(worldObj);
		if (t instanceof SSTileEntityAirVent)
			return (SSTileEntityAirVent) t;
		return null;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.head.x = tag.getInteger("headX");
		this.head.y = tag.getInteger("headY");
		this.head.z = tag.getInteger("headZ");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("headX", this.head.x);
		tag.setInteger("headY", this.head.y);
		tag.setInteger("headZ", this.head.z);
	}

	public void setHead(BlockVec3 head) {
		this.head = head;		
	}

}
