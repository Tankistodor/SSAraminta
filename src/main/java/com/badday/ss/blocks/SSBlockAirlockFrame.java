package com.badday.ss.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSSealedBlock;
import com.badday.ss.core.utils.WorldUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockAirlockFrame extends Block implements ISSSealedBlock, ITileEntityProvider {

	public static int METADATA_AIR_LOCK_FRAME = 0;
	public static int METADATA_AIR_LOCK_CONTROLLER = 1;

	public IIcon accessOnIcon;
	public IIcon accessOffIcon;
	public IIcon accessLockedIcon;
	public IIcon accessOn1Icon;
	public IIcon accessOn2Icon;
	public IIcon accessOn3Icon;
	public IIcon accessOn4Icon;
	public IIcon accessOpenedIcon;

	public SSBlockAirlockFrame(String asset) {
		super(Material.iron);
		this.setResistance(SSConfig.ssBayCasingResistance);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);

		//GameRegistry.registerTileEntity(SSTileEntityAirlockFrame.class, asset);
		GameRegistry.registerTileEntity(SSTileEntityAirlockFrameController.class, asset);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)));
		this.accessOnIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_on");
		this.accessOffIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_off");
		this.accessLockedIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_locked");
		this.accessOn1Icon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_p1");
		this.accessOn2Icon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_p2");
		this.accessOn3Icon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_p3");
		this.accessOn4Icon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_p4");
		this.accessOpenedIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_opened");
	}

	/**
	 * Show in inventory
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta >= SSBlockAirlockFrame.METADATA_AIR_LOCK_CONTROLLER) {
			if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
				return this.blockIcon;
			return this.accessOnIcon;
		} else
			return this.blockIcon;
	}
	
	public IIcon getIconFromMeta(int meta) {
		switch (meta) {
		case 1:
			return this.accessOpenedIcon;
		case 2:
			return this.accessOffIcon;
		case 3:
			return this.accessOnIcon;
		case 11:
			return this.accessOn1Icon;
		case 12:
			return this.accessOn2Icon;
		case 13:
			return this.accessOn3Icon;
		case 14:
			return this.accessOn4Icon;
		case 15:
			return this.accessLockedIcon;
		default:
			break;
		}
		return this.blockIcon;
	}
	

	/**
	 * render in world
	 */
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta >= SSBlockAirlockFrame.METADATA_AIR_LOCK_CONTROLLER) {
			if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
				return this.blockIcon;
			
			SSTileEntityAirlockFrameController te = WorldUtils.get(world, x, y, z, SSTileEntityAirlockFrameController.class);
			
			if (te != null) { 
				if ((side == ForgeDirection.EAST.ordinal() || side == ForgeDirection.WEST.ordinal()) && !te.getEW()) 
					return getIconFromMeta(meta);
				if ((side == ForgeDirection.NORTH.ordinal() || side == ForgeDirection.SOUTH.ordinal()) && te.getEW())
					return getIconFromMeta(meta);
				else
					return this.blockIcon;
			}
			return this.blockIcon;
		} else {
			return this.blockIcon;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, SSBlockAirlockFrame.METADATA_AIR_LOCK_FRAME));
		par3List.add(new ItemStack(par1, 1, SSBlockAirlockFrame.METADATA_AIR_LOCK_CONTROLLER));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		if (metadata < SSBlockAirlockFrame.METADATA_AIR_LOCK_CONTROLLER) {
			return new SSTileEntityAirlockFrame();
		} else {
			return new SSTileEntityAirlockFrameController();
		}
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return false;
		if (entityplayer.getCurrentEquippedItem() != null) {
			SSTileEntityAirlockFrameController te = WorldUtils.get(world,x,y,z,SSTileEntityAirlockFrameController.class);
			if (te != null) {
				boolean right = te.checkStructure();
				
				entityplayer.addChatMessage(new ChatComponentText("[SSAraminta] Airlock Structure: " + right));
				
				return true;
			}
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
		
		SSTileEntityAirlockFrameController tile = WorldUtils.get(world, x, y, z, SSTileEntityAirlockFrameController.class);
		
        if (tile != null && entityLiving instanceof EntityPlayer)
        {
            //tile.ownerName = ((EntityPlayer) entityLiving).getGameProfile().getName();
        	int facing = MathHelper.floor_double(((entityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        	tile.setEW((facing == 0 || facing == 2));
        	boolean right = tile.checkStructure();
        	System.out.println("Structure Airlock: "+ right);
        	tile.addAirlock();
           	world.markBlockForUpdate(x, y, z);
        }
        
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return false;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof SSTileEntityAirlockFrameController) {
			((SSTileEntityAirlockFrameController) tile).removeAirLock();
		}

		super.breakBlock(world, x, y, z, block, par6);
	}

	@Override
	public int damageDropped(int metadata) {
		if (metadata >= SSBlockAirlockFrame.METADATA_AIR_LOCK_CONTROLLER) {
			return SSBlockAirlockFrame.METADATA_AIR_LOCK_CONTROLLER;
		} else if (metadata >= SSBlockAirlockFrame.METADATA_AIR_LOCK_FRAME) {
			return SSBlockAirlockFrame.METADATA_AIR_LOCK_FRAME;
		}

		return 0;
	}

}
