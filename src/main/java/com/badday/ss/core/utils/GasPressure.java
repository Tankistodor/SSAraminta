package com.badday.ss.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSSealedBlock;
import com.badday.ss.blocks.SSTileEntityAirVent;
import com.badday.ss.blocks.SSTileEntityGasBlock;

public class GasPressure {

	private World world;
	public BlockVec3 head;
	private List<BlockVec3> blockToReplace = new LinkedList<BlockVec3>();
	private List<BlockVec3> nextLayer = new LinkedList<BlockVec3>();
	private List<BlockVec3> currentLayer = new LinkedList<BlockVec3>();
	private HashSet<BlockVec3> checked = new HashSet<BlockVec3>();
	private boolean sealed;
	int checkCount = SSConfig.ssAirVentBlockArea;

	public GasPressure(World world1, BlockVec3 vec) {
		this.world = world1;
		this.head = vec;
		this.checkCount = SSConfig.ssAirVentBlockArea;

		this.fullcheck();
	}

	/**
	 * Return true if block not permeable for any gases
	 * 
	 * @param world
	 * @param block
	 * @param vec
	 * @param side
	 * @return
	 */
	public static boolean canBlockPassAir(World world, Block block, BlockVec3 vec, int side) {
		if (block instanceof ISSSealedBlock) {
			return false;
		}

		// TODO add IC2 block
		return true;

	}

	/**
	 * Full check area around Vent
	 * 
	 * @param ssTileEntityAirVent
	 */

	public void fullcheck() {

		long time1 = System.nanoTime();

		this.checkCount = SSConfig.ssAirVentBlockArea;

		nextLayer = new LinkedList<BlockVec3>();
		currentLayer = new LinkedList<BlockVec3>();

		currentLayer.add(this.head);
		Block headblock = this.head.getBlockIDsafe_noChunkLoad(this.world);
		if (Blocks.air == headblock) {
			this.blockToReplace.add(this.head.clone());
		}

		checked = new HashSet<BlockVec3>();
		this.sealed = true; // Try to enable air vent
		this.doLayer();

		long time2 = System.nanoTime();

		if (this.sealed && !this.blockToReplace.isEmpty()) {
			// TODO механизм замены блоков если
			replace_block();
		}
		long time3 = System.nanoTime();

		if (SS.Debug) {
			System.out.println("[" + SS.MODNAME + "] AirVent Check Completed at x: " + this.head.x + " y: " + this.head.y + " z: " + this.head.z);
			System.out.println("   Sealed: " + this.sealed);
			System.out.println("   Loop Time taken: " + (time2 - time1) / 1000000.0D + "ms");
			System.out.println("   Place Time taken: " + (time3 - time2) / 1000000.0D + "ms");
			System.out.println("   Total Time taken: " + (time3 - time1) / 1000000.0D + "ms");
			System.out.println("   Block Replaced: " + this.blockToReplace.size() + " blocks");
			System.out.println("   Looped through: " + this.checked.size() + " blocks");
		}

	}

	private void replace_block() {

		for (BlockVec3 b : blockToReplace) {
			world.setBlock(b.x, b.y, b.z, SSConfig.ssBlockGas, 0, 2);
			TileEntity t = b.getTileEntity(world);
			if (t instanceof SSTileEntityGasBlock) {
				((SSTileEntityGasBlock) t).setHead(this.head);
			}
		}

	}

	private void doLayer() {
		// Local variables are fractionally faster than statics
		Block airID = Blocks.air;
		Block gasID = SSConfig.ssBlockGas; // Air
		Block AirVentID = SSConfig.ssBlockAirVent;
		HashSet<BlockVec3> checkedLocal = new HashSet<BlockVec3>();
		LinkedList nextLayer = new LinkedList<BlockVec3>();
		this.blockToReplace = new LinkedList<BlockVec3>();
		List<BlockVec3> blockToUnset = new LinkedList<BlockVec3>();

		while (this.sealed && this.currentLayer.size() > 0) {
			int side;
			for (BlockVec3 vec : this.currentLayer) {
				// Bottom = 0, Top = 1, Sides = 2-5
				// This is for side = 0 to 5 - but using do...while() is
				// fractionally quicker
				side = 0;
				do {
					// Skip the side which this was entered from
					// This is also used to skip looking on the solid sides of
					// partially sealable blocks
					if (!vec.sideDone[side]) {
						// The sides 0 to 5 correspond with the ForgeDirections
						// but saves a bit of time not to call ForgeDirection
						BlockVec3 sideVec = vec.newVecSide(side);

						if (!checkedLocal.contains(sideVec)) {
							if (this.checkCount > 0) {
								this.checkCount--;
								checkedLocal.add(sideVec);

								Block id = sideVec.getBlockIDsafe_noChunkLoad(this.world);
								if (id == airID) {
									// Если вакуум
									nextLayer.add(sideVec);
									this.blockToReplace.add(sideVec);
								} else if (id == gasID) {
									// TODO: if () // Проверить, является ли
									// атмосфера из другогой вентиляции
									nextLayer.add(sideVec); // Ставим новые
															// параметры
															// атмосферы
									if (sideVec.getTileEntity(this.world) instanceof SSTileEntityGasBlock) {
										SSTileEntityGasBlock t = (SSTileEntityGasBlock) sideVec.getTileEntity(this.world);
										if (t.head.x != this.head.x || t.head.y != this.head.y || t.head.z != this.head.z) {
											System.out.println("[" + SS.MODNAME + "] AirVent t " + t.head.toString());
											this.blockToReplace.add(sideVec);
										}
									}
									blockToUnset.add(sideVec);
								} else if (id == null) {
									// Broken through to the void or the
									// stratosphere (above y==255) - set
									// unsealed and abort
									this.checkCount = 0;
									this.sealed = false;
									return;
								} else if (this.canBlockPassAirCheck(id, sideVec, side)) {
									nextLayer.add(sideVec);
								} else if (id == AirVentID) {
									// TODO: Проверить, является ли вентиляция
									// той же что и мы проверям
									if ((this.head.x == sideVec.x) && (this.head.y == sideVec.y) && (this.head.z == sideVec.z)) {

									} else {
										this.checkCount = 0;
										this.sealed = false;
										return;
									}
								}
								// If the chunk was unloaded,
								// BlockVec3.getBlockID returns Blocks.bedrock
								// which is a solid block, so the loop will
								// treat that as a sealed edge
								// and not iterate any further in that direction
							}
							// the if (this.isSealed) check here is unnecessary
							// because of the returns
							else {
								Block id = sideVec.getBlockIDsafe_noChunkLoad(this.world);
								// id == null means the void or height y>255,
								// both
								// of which are unsealed obviously
								if (id == null || id == airID || id == gasID || this.canBlockPassAirCheck(id, sideVec, side)) {
									this.sealed = false;
									return;
								}
							}
						}
					}
					side++;
				} while (side < 6);
			}

			// Is there a further layer of air/permeable blocks to test?
			this.currentLayer = nextLayer;
			nextLayer = new LinkedList<BlockVec3>();
		}
		checked = checkedLocal;
	}

	private boolean canBlockPassAirCheck(Block block, BlockVec3 vec, int side) {
		// Check leaves first, because their isOpaqueCube() test depends on
		// graphics settings
		// (See net.minecraft.block.BlockLeaves.isOpaqueCube()!)
		if (block instanceof BlockLeavesBase) {
			return true;
		}

		if (block instanceof ISSSealedBlock) {

			return false;
		}

		// Easy case: airblock, return without checking other sides
		if (block.getMaterial() == Material.air) {
			return true;
		}

		// Not solid from this side, so this is not sealed
		return true;
	}

	/**
	 * Create new Gas block on x,y,z and head vent
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param vecHead
	 */
	public void createGasBlock(int x, int y, int z, BlockVec3 vecHead) {
		this.world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		TileEntity t = this.world.getTileEntity(x, y, z);
		if (t instanceof SSTileEntityGasBlock) {
			((SSTileEntityGasBlock) t).setHead(this.head.clone());
		}

	}

}
