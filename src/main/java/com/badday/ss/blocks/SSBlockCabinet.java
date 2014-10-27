package com.badday.ss.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockCabinet extends BlockContainer {

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;
	
	public SSBlockCabinet(String asset) {
		super(Material.iron);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
		this.isBlockContainer = true;
		// 30 - to blocks on vertical
		setBlockBounds(1 / 16.0F, 1 / 16.0F, 1 / 16.0F, 15 / 16.0F, 30 / 16.0F, 15 / 16.0F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Overridden by {@link #createTileEntity(World, int)}
	 */
	@Override
	public TileEntity createNewTileEntity(World w, int i) {
		return null;
	}

	@Override
	public TileEntity createTileEntity(World arg0, int arg1) {
		return new SSTileEntityCabinet();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventPar) {
		TileEntity te = world.getTileEntity(x, y, z);
		return ((te != null) ? te.receiveClientEvent(eventId, eventPar) : false);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType() {
		return SSConfig.cabinetRenderId;
	}

	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName().substring(5);
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		this.icons = new IIcon[SSConfig.ssCabinet_unlocalizedName.length];
		for (int i = 0; i < SSConfig.ssCabinet_unlocalizedName.length; i++) {
			
			if (SS.Debug)
				System.out.println("[" + SS.MODNAME + "] try to registerBlockIcon "
						+ SSConfig.ssCabinet_unlocalizedName[i] + " " + i);
			
			icons[i] = icon.registerIcon(SS.ASSET_PREFIX
					+ SSConfig.ssCabinet_unlocalizedName[i]);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta >= this.icons.length) {
			if (SS.Debug)
				System.out.println("[" + SS.MODNAME + "] try getIcon Icons.length: "
						+ this.icons.length + " meta: "+ meta);
			return null;
		}
		try {
			return this.icons[meta];
		} catch (Exception e) {
			SS.LogMSG("Error BlockID: " + this.getUnlocalizedName() + " Meta: " + meta);

		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
	}	
	
	/*
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBuffer = new IIcon[6];
		// iconBuffer[0] =
		// par1IconRegister.registerIcon("ss:"+this.getUnlocalizedName());
		iconBuffer[0] = par1IconRegister.registerIcon("ss:iron_top");
		iconBuffer[1] = par1IconRegister.registerIcon("ss:iron_top");
		iconBuffer[2] = par1IconRegister.registerIcon("ss:iron_side");
		iconBuffer[3] = par1IconRegister.registerIcon("ss:iron_front");
		iconBuffer[4] = par1IconRegister.registerIcon("ss:iron_side");
		iconBuffer[5] = par1IconRegister.registerIcon("ss:iron_side");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		// TODO:
		return iconBuffer[side];
	}*/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item par1, CreativeTabs tab, List list) {
		for (int i = 0; i < SSConfig.ssCabinet_unlocalizedName.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		world.markBlockForUpdate(i, j, k);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> items = Lists.newArrayList();
		ItemStack stack = new ItemStack(this, 1, metadata);
		items.add(stack);
		return items;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3) {
		TileEntity te = world.getTileEntity(i, j, k);

		if (te == null || !(te instanceof SSTileEntityCabinet)) {
			return true;
		}

		if (world.isSideSolid(i, j + 1, k, ForgeDirection.DOWN)) {
			return true;
		}

		if (world.isRemote) {
			return true;
		}

		player.openGui(SS.instance, ((SSTileEntityCabinet) te).getType(), world, i, j, k);
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack itemStack) {
		byte chestFacing = 0;
		int facing = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		if (facing == 0) {
			chestFacing = 2;
		}
		if (facing == 1) {
			chestFacing = 5;
		}
		if (facing == 2) {
			chestFacing = 3;
		}
		if (facing == 3) {
			chestFacing = 4;
		}
		TileEntity te = world.getTileEntity(i, j, k);
		if (te != null && te instanceof SSTileEntityCabinet) {
			SSTileEntityCabinet teic = (SSTileEntityCabinet) te;
			teic.wasPlaced(entityliving, itemStack);
			teic.setFacing(chestFacing);
			world.markBlockForUpdate(i, j, k);
		}
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}
	
	/**
	 * Check is top block is clean
	 */
	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
		Block block = world.getBlock(x, y + 1, z);

		if (block.getMaterial() != Material.air && !block.isReplaceable(world, x, y + 1, z)) {
			return false;
		}
		return true;
	}

	/*
    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case BASIC_METADATA:
            return GCCoreUtil.translate("tile.solarBasic.description");
        case ADVANCED_METADATA:
            return GCCoreUtil.translate("tile.solarAdv.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }*/
	
}
