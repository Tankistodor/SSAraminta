package com.badday.ss.blocks;

import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.core.utils.WorldUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SSTileEntityAirlockFrame extends TileEntity {

	private BlockVec3 mainBlockPosition;

	public void setMainBlock(BlockVec3 main) {
		this.mainBlockPosition = main;
	}
	
	public SSTileEntityAirlockFrameController getMainBlock() {
		if (this.mainBlockPosition == null || this.mainBlockPosition.equals(BlockVec3.INVALID_VECTOR)) return null;
		TileEntity t = this.mainBlockPosition.getTileEntity(worldObj);
		if (t instanceof SSTileEntityAirlockFrameController) return (SSTileEntityAirlockFrameController) t;
		return WorldUtils.get(worldObj, this.mainBlockPosition.x, this.mainBlockPosition.y, this.mainBlockPosition.z, SSTileEntityAirlockFrameController.class);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.mainBlockPosition = new BlockVec3(nbt.getCompoundTag("mainBlockPosition"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if (this.mainBlockPosition != null && !this.mainBlockPosition.equals(BlockVec3.INVALID_VECTOR)) {
			nbt.setTag("mainBlockPosition", this.mainBlockPosition.writeToNBT(new NBTTagCompound()));
		}
	}
	
}
