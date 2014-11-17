package com.badday.ss.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.api.IDoorFlags;

public class SSBlockBayDoor extends Block implements ITileEntityProvider{

	public IIcon topLeftIcon;
	public IIcon tolRightIcon;
	public IIcon bottomLeftIcon;
	public IIcon bottomRightIcon;
	
	public boolean singleDoor = false;
	
	protected SSBlockBayDoor(String asset, boolean singleDoor) {
		super(Material.iron);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
		this.singleDoor = singleDoor;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new SSTileEntityBayDoor();
	}
	
	public static boolean isEastOrWest(int metadata)
	{
		return (metadata & 3) == IDoorFlags.DIR_EAST || (metadata & 3) == IDoorFlags.DIR_WEST;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)));
		this.topLeftIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_topLeft");
		this.topLeftIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_topRight");
		if (!this.singleDoor) {
			this.topLeftIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_bottomLeft");
			this.topLeftIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_bottomRight");
		}
	}

	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if ((metadata & Door.FLAG_TOPBLOCK) != 0 && (side == 4 || side == 5))
			return topBlockIcon;
		else
			return blockIcon;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack)
	{
		int metadata = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F - 0.5F) & 3;
		Block block = world.getBlock(x, y + 1, z);
		if (block instanceof GarageDoor)
			metadata = world.getBlockMetadata(x, y + 1, z) & 3;
		else
		{
			block = world.getBlock(x, y - 1, z);
			if (block instanceof GarageDoor)
				metadata = world.getBlockMetadata(x, y - 1, z) & 3;
		}

		world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
		setBlockBoundsBasedOnState(world, x, y, z);

		GarageDoorTileEntity te = TileEntityUtils.getTileEntity(GarageDoorTileEntity.class, world, x, y, z);
		if (te != null)
			te.add();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if ((world.isBlockIndirectlyGettingPowered(x, y, z) || block.canProvidePower()) && block != this)
		{
			GarageDoorTileEntity te = TileEntityUtils.getTileEntity(GarageDoorTileEntity.class, world, x, y, z);
			if (te != null)
				te.changeState();
		}

	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		GarageDoorTileEntity te = TileEntityUtils.getTileEntity(GarageDoorTileEntity.class, world, x, y, z);
		if (te != null)
			te.remove();

		world.removeTileEntity(x, y, z);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		float w = Door.DOOR_WIDTH / 2;
		if ((metadata & Door.FLAG_OPENED) != 0)
			setBlockBounds(0, 0, 0, 0, 0, 0);
		else if (isEastOrWest(metadata))
			setBlockBounds(0.5F - w, 0, 0, 0.5F + w, 1, 1);
		else
			setBlockBounds(0, 0, 0.5F - w, 1, 1, 0.5F + w);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 src, Vec3 dest)
	{
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.collisionRayTrace(world, x, y, z, src, dest);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		if ((metadata & Door.FLAG_OPENED) != 0)
			return null;

		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	 */
	
}
