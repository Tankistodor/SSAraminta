package com.badday.ss.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSGasBlock;
import com.badday.ss.core.utils.GasPressure;

public class SSBlockGas extends BlockContainer implements ISSGasBlock {

	private final int TICK_RATE = 200;

	public SSBlockGas(String assetName) {
		super(Material.air);
		this.setResistance(1000.0F);
		this.setHardness(0.0F);
		this.setBlockTextureName(SS.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
		this.setStepSound(new SoundType("sand", 0.0F, 1.0F));
		this.setLightLevel(0.0F);
		this.setCreativeTab(SS.ssTab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new SSTileEntityGasBlock();
	}

	@Override
	public boolean canReplace(World world, int x, int y, int z, int side, ItemStack stack) {
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return true;
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1
	 * for alpha
	 */
	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public int getMobilityFlag() {
		return 1;
	}

	@Override
	public Item getItemDropped(int var1, Random var2, int var3) {
		return Item.getItemFromBlock(Blocks.air);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		final Block block = par1IBlockAccess.getBlock(par2, par3, par4);
		if (block == this || block == SSConfig.ssBlockAir) {
			return false;
		} else {
			return block instanceof BlockAir && par5 >= 0 && par5 <= 5;
		}
	}

	/**
	 * Проверяем, если рядом газ, то надо дать комманду перепроверить весь объем вент решетке 
	 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block blockBroken) {
		if (blockBroken != Blocks.air) {
			TileEntity tt = world.getTileEntity(x, y, z);
			if (tt instanceof SSTileEntityGasBlock) {
				SSTileEntityGasBlock t = (SSTileEntityGasBlock) tt;
				if (t != null) {
					if (t.getTileEntityAirVent() != null) {
						((SSTileEntityAirVent) t.getTileEntityAirVent()).gasPressure.fullcheck();
						if (SS.Debug) {
							System.out.println("[" + SS.MODNAME + "] onNeighborBlockChange " + blockBroken.toString() + " x:" + x + " y:" + y + " z:" + z);
						}

					}
				}
			}
		}
	}

	/**
	 * Returns if this block is collidable. Args: x, y, z
	 */
	@Override
	public boolean isCollidable() {
		return false;
	}

}
