package com.badday.ss.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.badday.ss.api.IMultiBlock;
import com.badday.ss.core.utils.BlockVec3;

import cpw.mods.fml.relauncher.Side;

public class SSTileEntityMultiFake extends TileEntity {

	public BlockVec3 mainBlockPosition;
	

	public void setMainBlock(BlockVec3 mainBlock) {
		this.mainBlockPosition = mainBlock;

		if (!this.worldObj.isRemote) {
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer) {
		if (this.mainBlockPosition != null) {
			TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition.x, this.mainBlockPosition.y, this.mainBlockPosition.z);

			if (tileEntity != null) {
				if (tileEntity instanceof IMultiBlock) {
					return ((IMultiBlock) tileEntity).onActivated(par5EntityPlayer);
				}
			}
		}

		return false;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.mainBlockPosition = new BlockVec3(nbt.getCompoundTag("mainBlockPosition"));
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		if (this.mainBlockPosition != null) {
			nbt.setTag("mainBlockPosition", this.mainBlockPosition.writeToNBT(new NBTTagCompound()));
		}
	}

	public void onBlockRemoval(TileEntity callingBlock) {

		if (this.mainBlockPosition != null) {
			TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition.x, this.mainBlockPosition.y, this.mainBlockPosition.z);

			if (tileEntity != null && tileEntity instanceof IMultiBlock) {
				IMultiBlock mainBlock = (IMultiBlock) tileEntity;
				mainBlock.onDestroy(this);
			}
		}

		final BlockVec3 thisBlock = new BlockVec3(this);
		this.worldObj.setBlockToAir(thisBlock.x, thisBlock.y + 1, thisBlock.z);
		this.worldObj.func_147480_a(thisBlock.x, thisBlock.y, thisBlock.z, true);

	}

}
