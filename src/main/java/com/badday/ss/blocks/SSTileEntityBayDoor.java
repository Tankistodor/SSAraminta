package com.badday.ss.blocks;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.api.IDoorFlags;
import com.badday.ss.api.IDoorState;
import com.badday.ss.core.utils.WorldUtils;

public class SSTileEntityBayDoor extends TileEntity {
	
	public static final int maxOpenTime = 10;

	protected boolean removed = false;
	protected IDoorState state = IDoorState.CLOSED;
	protected long startTime;
	protected long startNanoTime;
	
	protected SSTileEntityBayDoor topDoor;
	protected Set<SSTileEntityBayDoor> childDoors = new HashSet<SSTileEntityBayDoor>();
	
	public SSTileEntityBayDoor () {
		
	}
	
	public int getDirection()
	{
		return this.blockMetadata & 3;
	}

	public long getStartNanoTime()
	{
		return startNanoTime;
	}

	public boolean isOpened()
	{
		return (getBlockMetadata() & IDoorFlags.FLAG_OPENED) != 0;
	}

	public boolean isReversed()
	{
		return (getBlockMetadata() & IDoorFlags.FLAG_REVERSED) != 0;
	}
	
	public boolean isTopDoor()
	{
		return this == getTopDoor();
	}

	public SSTileEntityBayDoor getTopDoor()
	{
		if (this.topDoor == null)
			this.add();
		return this.topDoor;
	}
	
	public void add()
	{
		SSTileEntityBayDoor te = findTopBlock();
		te.setTopBlock(te);
	}

	public Set<SSTileEntityBayDoor> getChildDoors()
	{
		return childDoors;
	}

	public SSTileEntityBayDoor getDoor(ForgeDirection dir)
	{
		SSTileEntityBayDoor te = WorldUtils.get(getWorldObj(), xCoord + dir.offsetX, yCoord
				+ dir.offsetY, zCoord + dir.offsetZ, SSTileEntityBayDoor.class);
		if (te == null)
			return null;
		if (te.getDirection() != getDirection())
			return null;
		if (te.removed)
			return null;

		return te;
	}

	public SSTileEntityBayDoor findTopBlock()
	{
		SSTileEntityBayDoor te = this;
		SSTileEntityBayDoor ret = this;
		while ((te = te.getDoor(ForgeDirection.UP)) != null)
			ret = te;

		return ret;
	}
	
	public void setTopBlock(SSTileEntityBayDoor topDoor)
	{
		childDoors.clear();
		this.topDoor = topDoor;
		if (isTopDoor())
		{
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, getBlockMetadata() | IDoorFlags.FLAG_TOPBLOCK, 2);
			SSTileEntityBayDoor te = this;
			while ((te = te.getDoor(ForgeDirection.DOWN)) != null)
			{
				te.setTopBlock(this);
				childDoors.add(te);
			}
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		else
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, getBlockMetadata() & ~IDoorFlags.FLAG_TOPBLOCK, 2);
	}
	
	public void remove()
	{
		this.removed = true;
		SSTileEntityBayDoor te = getDoor(ForgeDirection.DOWN);
		if (te != null)
			te.setTopBlock(te);

		te = getDoor(ForgeDirection.UP);
		if (te != null)
		{
			te = te.findTopBlock();
			te.setTopBlock(te);
		}
	}

	public void changeState()
	{
		if (!isTopDoor())
		{
			getTopDoor().changeState();
			return;
		}

		if (state == IDoorState.OPENING || state == IDoorState.CLOSING)
			return;

		startTime = worldObj.getTotalWorldTime();
		startNanoTime = System.nanoTime();
		if (state == IDoorState.CLOSED)
			this.setState(IDoorState.OPENING);
		else if (state == IDoorState.OPENED)
			this.setState(IDoorState.CLOSING);

		SSTileEntityBayDoor te = getDoor(SSBlockBayDoor.isEastOrWest(blockMetadata) ? ForgeDirection.NORTH : ForgeDirection.EAST);
		if (te != null)
			te.changeState();
		te = getDoor(SSBlockBayDoor.isEastOrWest(blockMetadata) ? ForgeDirection.SOUTH : ForgeDirection.WEST);
		if (te != null)
			te.changeState();

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	private void setState(IDoorState newState)
	{
		if (state == newState)
			return;

		state = newState;

		if (getWorldObj() != null)
		{
			if (state == IDoorState.CLOSED)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, blockMetadata & ~IDoorFlags.FLAG_OPENED, 2);
			else
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, blockMetadata | IDoorFlags.FLAG_OPENED, 2);
		}

		if (isTopDoor())
		{
			for (SSTileEntityBayDoor te : childDoors)
				te.setState(newState);
		}
	}
	
	@Override
	public void updateEntity()
	{
		if (!isTopDoor())
			return;

		if (state == IDoorState.CLOSED || state == IDoorState.OPENED)
			return;

		if (startTime + (childDoors.size() + 1) * maxOpenTime < worldObj.getTotalWorldTime())
		{
			if (state == IDoorState.CLOSING)
				setState(IDoorState.CLOSED);
			else if (state == IDoorState.OPENING)
				setState(IDoorState.OPENED);
		}
	}
	
	/**
	 * Specify the bounding box ourselves otherwise, the block bounding box would be use. (And it should be at this point {0, 0, 0})
	 */
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getBoundingBox(xCoord - childDoors.size(), yCoord - childDoors.size(), zCoord - childDoors.size(), xCoord
				+ childDoors.size() + 1, yCoord + 1, zCoord + childDoors.size() + 1);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("state", state.ordinal());
		tag.setLong("startTime", startTime);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		state = IDoorState.values()[tag.getInteger("state")];
		startTime = tag.getLong("startTime");
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		add();
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);

	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		this.readFromNBT(packet.func_148857_g());
		add();
	}
}
