package com.badday.ss.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.api.IBoundingBoxType;
import com.badday.ss.core.utils.WorldUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockBayDoor extends Block implements ITileEntityProvider{

	public IIcon topLeftIcon;
	public IIcon topRightIcon;
	public IIcon bottomLeftIcon;
	public IIcon bottomRightIcon;
	public IIcon sideIcon;
	
	public boolean singleDoor = false;
	
	public static final float DOOR_WIDTH = 0.1875F;

	public static final int DIR_WEST = 0;
	public static final int DIR_NORTH = 1;
	public static final int DIR_EAST = 2;
	public static final int DIR_SOUTH = 3;
	
	public static final int FLAG_OPENED = 1 << 2;
	public static final int FLAG_TOPBLOCK = 1 << 3;
	public static final int FLAG_REVERSED = 1 << 4;
	
	public SSBlockBayDoor(String asset, boolean singleDoor) {
		super(Material.iron);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
		this.singleDoor = singleDoor;
		
		GameRegistry.registerTileEntity(SSTileEntityBayDoor.class, asset);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		if ((metadata & FLAG_TOPBLOCK) != 0)
			return null;

		return new SSTileEntityBayDoor();
	}

	
	public static boolean isEastOrWest(int metadata)
	{
		return (metadata & 3) == SSBlockBayDoor.DIR_EAST || (metadata & 3) == SSBlockBayDoor.DIR_WEST;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)));
		this.sideIcon= iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_side");
		this.topLeftIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_topLeft");
		this.bottomLeftIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_bottomLeft");
		if (!this.singleDoor) {
			this.topRightIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_topRight");
			this.bottomLeftIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_bottomRight");
		}
	}

	@Override
	public IIcon getIcon(int side, int metadata)
	{
		
		boolean topBlock = (metadata & FLAG_TOPBLOCK) != 0;
		
		
		switch (side)
		{
			case 0:
				return sideIcon;
			case 1:
				return sideIcon;
			case 2:
				return topBlock ? this.topLeftIcon : this.bottomLeftIcon;
			case 3:
				return sideIcon;
			case 4:
				return topBlock ? this.topLeftIcon : this.bottomLeftIcon;
			case 5:
				return sideIcon;
			default:
				return blockIcon;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack)
	{
		SSTileEntityBayDoor te = WorldUtils.get(world, x, y, z, SSTileEntityBayDoor.class);
		if (!(te instanceof SSTileEntityBayDoor))
			return;

		((SSTileEntityBayDoor) te).onBlockPlaced(itemStack);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		int metadata = world.getBlockMetadata(x, y, z);

		if ((metadata & FLAG_TOPBLOCK) == 0)
		{
			boolean flag = false;
			ItemStack itemStack = getDoorItemStack(world, x, y, z);
			if (world.getBlock(x, y + 1, z) != this)
			{
				world.setBlockToAir(x, y, z);
				flag = true;
			}

			if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z))
			{
				world.setBlockToAir(x, y, z);
				flag = true;

				if (world.getBlock(x, y + 1, z) == this)
					world.setBlockToAir(x, y + 1, z);
			}

			if (flag)
			{
				if (!world.isRemote && itemStack != null)
					dropBlockAsItem(world, x, y, z, itemStack);
			}
			else
			{
				SSTileEntityBayDoor te = getDoor(world, x, y, z);
				if (te == null)
					return;

				boolean powered = te.isPowered();
				if ((powered || block.canProvidePower()) && block != this)
					te.setPowered(powered);
			}
		}
		else
		{
			if (world.getBlock(x, y - 1, z) != this)
				world.setBlockToAir(x, y, z);

			if (block != this)
				onNeighborBlockChange(world, x, y - 1, z, block);
		}
	}


	
	
	protected ItemStack getDoorItemStack(IBlockAccess world, int x, int y, int z)
	{
		SSTileEntityBayDoor te = getDoor(world, x, y, z);
		if (!(te instanceof SSTileEntityBayDoor))
			return null;
		return new ItemStack(this, 1);
	}

	/**
	 * Get door tile entity at x, y, z event if the position is the top half of the door
	 *
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static SSTileEntityBayDoor getDoor(IBlockAccess world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		int metadata = getFullMetadata(world, x, y, z);
		if (block instanceof SSBlockBayDoor)
			y -= (metadata & SSBlockBayDoor.FLAG_TOPBLOCK) != 0 ? 1 : 0;

		return WorldUtils.get(world,x,y,z,SSTileEntityBayDoor.class);
	}
	
	/**
	 * Get the full metadata for the door
	 *
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static int getFullMetadata(IBlockAccess world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);

		if (!(block instanceof SSBlockBayDoor))
			return metadata;

		boolean blockTop = (metadata & SSBlockBayDoor.FLAG_TOPBLOCK) != 0;
		int bottomMetadata;
		int topMetadata;

		if (blockTop)
		{
			bottomMetadata = world.getBlockMetadata(x, y - 1, z);
			topMetadata = metadata;
		}
		else
		{
			bottomMetadata = metadata;
			topMetadata = world.getBlockMetadata(x, y + 1, z);
		}

		return bottomMetadata & 7 | (blockTop ? SSBlockBayDoor.FLAG_TOPBLOCK : 0) ;
	}
	
	
	
	//#region BoundingBox
	protected AxisAlignedBB setBlockBounds(AxisAlignedBB aabb)
	{
		if (aabb == null)
			aabb = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);

		setBlockBounds((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
		return aabb;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		SSTileEntityBayDoor te = getDoor(world, x, y, z);
		if (te == null || te.isMoving() || te.getMovement() == null)
			return;

		//may be called for other than RAYTRACE
		setBlockBounds(te.getMovement().getBoundingBox(te, te.isTopBlock(x, y, z), IBoundingBoxType.RAYTRACE));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		SSTileEntityBayDoor te = getDoor(world, x, y, z);
		if (te == null || te.isMoving() || te.getMovement() == null)
			return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);

		AxisAlignedBB aabb = te.getMovement().getBoundingBox(te, te.isTopBlock(x, y, z), IBoundingBoxType.SELECTION);
		if (aabb == null)
			return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);

		return aabb.offset(x, y, z);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		SSTileEntityBayDoor te = getDoor(world, x, y, z);
		if (te == null || te.isMoving() || te.getMovement() == null)
			return null;

		AxisAlignedBB aabb = te.getMovement().getBoundingBox(te, te.isTopBlock(x, y, z), IBoundingBoxType.COLLISION);
		if (aabb == null)
			return null;
		return setBlockBounds(aabb.offset(x, y, z));
	}
	
	
}
