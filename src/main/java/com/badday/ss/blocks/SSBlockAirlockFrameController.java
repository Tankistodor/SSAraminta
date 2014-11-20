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

public class SSBlockAirlockFrameController extends Block implements ISSSealedBlock, ITileEntityProvider {

	public final static int MT_UNCOMPLITE = 0;
	public final static int MT_OPENED = 1;
	public final static int MT_OFF = 2;
	public final static int MT_ON = 3;
	public final static int MT_ON1 = 4;
	public final static int MT_ON2 = 5;
	public final static int MT_ON3 = 6;
	public final static int MT_ON4 = 7;
	public final static int MT_LOCKED = 15;

	public IIcon accessOnIcon;
	public IIcon accessOffIcon;
	public IIcon accessLockedIcon;
	public IIcon accessOn1Icon;
	public IIcon accessOn2Icon;
	public IIcon accessOn3Icon;
	public IIcon accessOn4Icon;
	public IIcon accessOpenedIcon;

	public SSBlockAirlockFrameController(String asset) {
		super(Material.iron);
		this.setResistance(SSConfig.ssBayCasingResistance);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);

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
			if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
				return this.blockIcon;
			return this.accessOnIcon;
	}
	
	public IIcon getIconFromMeta(int meta) {
		switch (meta) {
		case MT_OPENED:
			return this.accessOpenedIcon;
		case MT_OFF:
			return this.accessOffIcon;
		case MT_ON:
			return this.accessOnIcon;
		case MT_ON1:
			return this.accessOn1Icon;
		case MT_ON2:
			return this.accessOn2Icon;
		case MT_ON3:
			return this.accessOn3Icon;
		case MT_ON4:
			return this.accessOn4Icon;
		case MT_LOCKED:
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

	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new SSTileEntityAirlockFrameController();
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return false;

		if (entityplayer.getCurrentEquippedItem() != null) {
			String itemName = entityplayer.getCurrentEquippedItem().getUnlocalizedName();
			if (itemName.equals("item.ss.multitool")) {
				SSTileEntityAirlockFrameController te = WorldUtils.get(world, x, y, z, SSTileEntityAirlockFrameController.class);
				if (te != null) {
					if (entityplayer.isSneaking()) {
						boolean right = te.checkStructure();
						entityplayer.addChatMessage(new ChatComponentText("[SSAraminta] Airlock Structure: " + right));
						return true;
					} else {
						if (te.getStatus() == 1) {
							te.setStatus((byte)2);
							world.setBlockMetadataWithNotify(x, y, z, 2, 2);
						} else {
							te.setStatus((byte)1);
							world.setBlockMetadataWithNotify(x, y, z, 1, 2);
						}
						return true;
					}
				}
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
           	world.setBlockMetadataWithNotify(x, y, z,1,2);
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

		if (tile instanceof SSTileEntityAirlockFrame) {
			SSTileEntityAirlockFrameController main = ((SSTileEntityAirlockFrame) tile).getMainBlock();
			if (main instanceof SSTileEntityAirlockFrameController) {
				main.setStatus((byte)0);
				world.setBlockMetadataWithNotify(main.xCoord, main.yCoord, main.zCoord, 1,2);
			}
			
		}
		
		super.breakBlock(world, x, y, z, block, par6);
	}

	@Override
	public int damageDropped(int metadata) {
		return 0;
	}

}
