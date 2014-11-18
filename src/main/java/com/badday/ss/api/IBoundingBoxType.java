package com.badday.ss.api;

import net.minecraft.util.AxisAlignedBB;

import com.badday.ss.blocks.SSBlockBayDoor;
import com.badday.ss.blocks.SSTileEntityBayDoor;

public enum IBoundingBoxType
{
	SELECTION, COLLISION, RAYTRACE, RENDER;

	public AxisAlignedBB getBoundingBox(SSTileEntityBayDoor te, Object topBlock, IBoundingBoxType collision2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AxisAlignedBB getBoundingBox(SSTileEntityBayDoor tileEntity, boolean topBlock, IBoundingBoxType type)
	{
		if (tileEntity.isOpened() && !topBlock)
			return null;
		int dir = tileEntity.getDirection();

		float x = 0;
		float y = 0;
		float z = 0;
		float X = 1;
		float Y = 1;
		float Z = 1;

		if (dir == SSBlockBayDoor.DIR_NORTH)
			Z = SSBlockBayDoor.DOOR_WIDTH;
		if (dir == SSBlockBayDoor.DIR_SOUTH)
			z = 1 - SSBlockBayDoor.DOOR_WIDTH;
		if (dir == SSBlockBayDoor.DIR_WEST)
			X = SSBlockBayDoor.DOOR_WIDTH;
		if (dir == SSBlockBayDoor.DIR_EAST)
			x = 1 - SSBlockBayDoor.DOOR_WIDTH;

		if (tileEntity.isOpened() && topBlock)
		{
			y += 1 - SSBlockBayDoor.DOOR_WIDTH;
			Y += 1 - SSBlockBayDoor.DOOR_WIDTH;
		}

		if (type == IBoundingBoxType.SELECTION)
		{
			if (topBlock && !tileEntity.isOpened())
				y--;
			else
				Y++;
		}

		return AxisAlignedBB.getBoundingBox(x, y, z, X, Y, Z);
	}

	/*
	private Transformation getTransformation(SSTileEntityBayDoor tileEntity)
	{
		Translation translation = new Translation(0, 0, 0, 0, 2 - SSBlockBayDoor.DOOR_WIDTH, 0);
		translation.reversed(tileEntity.getState() == IDoorState.CLOSING || tileEntity.getState() == IDoorState.CLOSED);
		translation.forTicks(tileEntity.getOpeningTime());

		return translation;
	}

	public Animation[] getAnimations(SSTileEntityBayDoor tileEntity, MalisisModel model, RenderParameters rp)
	{
		return new Animation[] { new Animation(model, getTransformation(tileEntity)) };
	}

	public boolean isSpecial()
	{
		return false;
	}*/

}
