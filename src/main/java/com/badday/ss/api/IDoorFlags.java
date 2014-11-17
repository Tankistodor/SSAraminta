package com.badday.ss.api;

public class IDoorFlags {
	
	public static final int DIR_WEST = 0;
	public static final int DIR_NORTH = 1;
	public static final int DIR_EAST = 2;
	public static final int DIR_SOUTH = 3;

	public static final int FLAG_OPENED = 1 << 2;
	public static final int FLAG_TOPBLOCK = 1 << 3;
	public static final int FLAG_REVERSED = 1 << 4;
	
}
