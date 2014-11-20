package com.badday.ss.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SSTileEntityAirlockFrame extends TileEntity {
	
	private SSTileEntityAirlockFrameController mainBlock;

	public void setMainBlock(SSTileEntityAirlockFrameController main) {
		this.mainBlock = main;
	}
	
	public SSTileEntityAirlockFrameController getMainBlock() {
		return this.mainBlock;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
	}
	
}
