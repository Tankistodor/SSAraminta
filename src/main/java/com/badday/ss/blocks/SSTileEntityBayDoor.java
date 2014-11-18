package com.badday.ss.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.badday.ss.api.IBoundingBoxType;
import com.badday.ss.api.IDoorState;

public class SSTileEntityBayDoor extends TileEntity {
	
	private int openingTime = 6;
	private int lastMetadata = -1;
	private int timer = 0;
	protected IDoorState state = IDoorState.CLOSED;
	protected long startTime;
	protected long startNanoTime;
	private boolean moving;
	
	protected int type = 0;
	
	protected SSTileEntityBayDoor topDoor;
	
	public SSTileEntityBayDoor (int meta) {
		this.type = meta;
	}
	
	
	@Override
	public void updateEntity()
	{
		if (!moving)
			return;

		timer++;
		if (startTime + openingTime < worldObj.getTotalWorldTime())
		{
			setDoorState(state == IDoorState.CLOSING ? IDoorState.CLOSED : IDoorState.OPENED);
			timer = 0;
		}
	}
	
	
	public long getStartTime()
	{
		return startTime;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}
	
	public long getStartNanoTime()
	{
		return startNanoTime;
	}

	public int getTimer()
	{
		return timer;
	}

	public void setTimer(int timer)
	{
		this.timer = timer;
	}

	public IDoorState getState()
	{
		return state;
	}

	public void setState(IDoorState state)
	{
		this.state = state;
	}
	
	public boolean isMoving()
	{
		return moving;
	}

	public void setMoving(boolean moving)
	{
		this.moving = moving;
	}
	
	/*public IDoorMovement getMovement()
	{
		return getDescriptor() != null ? getDescriptor().getMovement() : null;
	}*/

	public int getDirection()
	{
		return getBlockMetadata() & 3;
	}
	
	public boolean isEastWest()
	{
		return ((getBlockMetadata() & SSBlockBayDoor.DIR_EASTWEST) != 0);
		//return getBlockMetadata() & 3;
	}
	
	@Override
	public int getBlockMetadata()
	{
		if (lastMetadata != blockMetadata || blockMetadata == -1)
		{
			blockMetadata = SSBlockBayDoor.getFullMetadata(worldObj, xCoord, yCoord, zCoord);
			lastMetadata = blockMetadata;
		}

		return blockMetadata;
	}
	
	public boolean isOpened()
	{
		return (getBlockMetadata() & SSBlockBayDoor.FLAG_OPENED) != 0;
	}

	public boolean isReversed()
	{
		return (getBlockMetadata() & SSBlockBayDoor.FLAG_REVERSED) != 0;
	}

	public boolean isPowered()
	{
		return getWorldObj().isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)
				|| getWorldObj().isBlockIndirectlyGettingPowered(xCoord, yCoord + 1, zCoord);
	}
	
	
	/**
	 * Open or close this DoorTileEntity
	 */
	public void openOrCloseDoor()
	{
		IDoorState newState = state == IDoorState.OPENED ? IDoorState.CLOSING : IDoorState.OPENING;
		setDoorState(newState);

		SSTileEntityBayDoor te = getDoubleDoor();
		if (te != null)
			te.setDoorState(newState);
	}

	/**
	 * Change the current state of this DoorTileEntity
	 *
	 * @param newSate
	 */
	public void setDoorState(IDoorState newState)
	{
		if (state == newState)
			return;

		state = newState;
		if (getWorldObj() == null)
			return;

		if (state == IDoorState.CLOSING || state == IDoorState.OPENING)
		{
			timer = moving ? openingTime - timer : 0;
			startTime = worldObj.getTotalWorldTime() - timer;
			startNanoTime = System.nanoTime();
			moving = true;
		}
		else
		{
			int metadata = getBlockMetadata();
			if (getBlockType() instanceof SSBlockBayDoor)
				metadata = metadata & 7;
			metadata = state == IDoorState.OPENED ? metadata | SSBlockBayDoor.FLAG_OPENED : metadata & ~SSBlockBayDoor.FLAG_OPENED;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
			moving = false;
		}

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		//FIXME playSound();
	}

	/**
	 * Find the corresponding double door for this DoorTileEntity
	 *
	 * @return
	 */
	public SSTileEntityBayDoor getDoubleDoor()
	{
		//if (!descriptor.isDoubleDoor())
		//	return null;
/*
		int dir = getDirection();
		boolean reversed = isReversed();
		SSTileEntityBayDoor te;
		int x = xCoord;
		int z = zCoord;

		if (dir == SSBlockBayDoor.DIR_NORTH)
			x += (reversed ? 1 : -1);
		else if (dir == SSBlockBayDoor.DIR_SOUTH)
			x += (reversed ? -1 : 1);
		else if (dir == SSBlockBayDoor.DIR_EAST)
			z += (reversed ? 1 : -1);
		else if (dir == SSBlockBayDoor.DIR_WEST)
			z += (reversed ? -1 : 1);

		te = SSBlockBayDoor.getDoor(worldObj, x, yCoord, z);*/
		//if (te != null && isMatchingDoubleDoor(te))
		//	return te;
		//FIXME

		return null;
	}

	
	
	/**
	 * Play sound for the block
	 */
	/**
	public void playSound()
	{
		if (worldObj.isRemote)
			return;

		String soundPath = null;
		if (descriptor.getSound() != null)
			soundPath = descriptor.getSound().getSoundPath(state);
		if (soundPath != null)
			getWorldObj().playSoundEffect(xCoord, yCoord, zCoord, soundPath, 1F, 1F);
	}*/
	
	
	/**
	 * Change the state of this DoorTileEntity based on powered
	 */
	public void setPowered(boolean powered)
	{
		if (isOpened() == powered && !isMoving())
			return;

		/*
		SSTileEntityBayDoor te = getDoubleDoor();
		if (!powered && te != null && te.isPowered())
			return;
		*/
		
		IDoorState newState = powered ? IDoorState.OPENING : IDoorState.CLOSING;
		setDoorState(newState);

		/*if (te != null)
			te.setDoorState(newState);*/
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		setDoorState(IDoorState.values()[nbt.getInteger("state")]);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("state", state.ordinal());
	}
	
	
	/**
	 * Specify the bounding box ourselves otherwise, the block bounding box would be use. (And it should be at this point {0, 0, 0})
	 */
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1);
	}

	@Override
	public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z)
	{
		return oldBlock != newBlock;
	}
	
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		this.readFromNBT(packet.func_148857_g());
	}
	
	public boolean isTopBlock(int x, int y, int z)
	{
		return x == xCoord && y == yCoord + 1 && z == zCoord;
	}
	

	//================
	
	
	public void onBlockPlaced(ItemStack itemStack) {
		
	}


	public IBoundingBoxType getMovement() {
		return null;
	}


	public Object getOpeningTime() {
		return this.openingTime;
	}

}
