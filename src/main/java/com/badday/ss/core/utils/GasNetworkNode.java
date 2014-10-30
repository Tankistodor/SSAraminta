package com.badday.ss.core.utils;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;

public class GasNetworkNode {
	public List<TileEntity> sourceBlocks = new LinkedList<TileEntity>();
	public List<TileEntity> consumerBlocks = new LinkedList<TileEntity>();
	public List<TileEntity> transmitersBlocks = new LinkedList<TileEntity>();
}
