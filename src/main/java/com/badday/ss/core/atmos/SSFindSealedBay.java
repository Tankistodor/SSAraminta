package com.badday.ss.core.atmos;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.badday.ss.SSConfig;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.api.ISSSealedBlock;
import com.badday.ss.core.utils.BlockVec3;

public class SSFindSealedBay {

	private World world;
	public BlockVec3 head;
	private int bayBlocks = 0;
	private List<BlockVec3> nextLayer = new LinkedList<BlockVec3>();
	private List<BlockVec3> currentLayer = new LinkedList<BlockVec3>();
	private HashSet<BlockVec3> checked = new HashSet<BlockVec3>();
	private boolean sealed;
	private boolean active;
	private int checkCount = SSConfig.ssAirVentBlockArea;
	
	public SSFindSealedBay(World world1, BlockVec3 vec) {
		this.world = world1;
		this.head = vec;
		this.checkCount = SSConfig.ssAirVentBlockArea;
		this.sealed = this.fullcheck();
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

	public boolean fullcheck() {

		this.checkCount = SSConfig.ssAirVentBlockArea;

		nextLayer = new LinkedList<BlockVec3>();
		currentLayer = new LinkedList<BlockVec3>();

		currentLayer.add(this.head);
		Block headblock = this.head.getBlockIDsafe_noChunkLoad(this.world);
		
		checked = new HashSet<BlockVec3>();
		this.sealed = true; // Try to enable air vent
		this.active = true;
		this.bayBlocks = 0;
		this.doLayer();

		return this.sealed;
	}

	private void doLayer() {
		// Local variables are fractionally faster than statics
		Block airID = Blocks.air;
		Block AirVentID = SSConfig.ssBlockAirVent;
		HashSet<BlockVec3> checkedLocal = new HashSet<BlockVec3>();
		LinkedList nextLayer = new LinkedList<BlockVec3>();
		this.bayBlocks = 0;
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
									this.bayBlocks++;
									nextLayer.add(sideVec);
								} else if (id == null) {
									// Broken through to the void or the
									// stratosphere (above y==255) - set
									// unsealed and abort
									this.bayBlocks = 0;
									this.checkCount = 0;
									this.sealed = false;
									this.active = false;
									return;
								} else if (id == AirVentID) {
									// TODO: Проверить, является ли вентиляция
									// той же что и мы проверям. Если нет
									if (!((this.head.x == sideVec.x) && (this.head.y == sideVec.y) && (this.head.z == sideVec.z))) {
										TileEntity te = sideVec.getTileEntity(world);
										if (te instanceof IGasNetworkVent) {
											if (((IGasNetworkVent) te).getActive() == true) {
												this.bayBlocks = 0;
												this.checkCount = 0;
												this.sealed = true;
												this.active = false;
												return;
											}
										}
									}
								} else if (this.canBlockPassAirCheck(id, sideVec, side)) {
									this.bayBlocks++;
									nextLayer.add(sideVec);
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
								if (id == null || id == airID || this.canBlockPassAirCheck(id, sideVec, side)) {
									this.sealed = false;
									this.active = false;
									this.bayBlocks = 0;
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
	
	public int getSize() {
		return this.bayBlocks;
	}
	
	public boolean getActive() {
		return this.active;
	}
}
