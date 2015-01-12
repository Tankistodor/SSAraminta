package com.badday.ss.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
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

public class SSBlockAirlockFrame extends BlockContainer implements ISSSealedBlock {


	public SSBlockAirlockFrame(String asset) {
		super(Material.iron);
		this.setResistance(SSConfig.ssBayCasingResistance);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);

		GameRegistry.registerTileEntity(SSTileEntityAirlockFrame.class, asset);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new SSTileEntityAirlockFrame();
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new SSTileEntityAirlockFrame();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)));
	}

	/**
	 * Show in inventory
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.blockIcon;
	}
	
	/**
	 * render in world
	 */
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return this.blockIcon;
	}

	/*
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new SSTileEntityAirlockFrame();
	}*/
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return false;

		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return false;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof SSTileEntityAirlockFrame) {
			SSTileEntityAirlockFrameController main = ((SSTileEntityAirlockFrame) tile).getMainBlock();
			if (main instanceof SSTileEntityAirlockFrameController) {
				main.openDoor();
				main.energy = 0;
				main.setStatus(SSBlockAirlockFrameController.MT_UNCOMPLITE);
				main.state = 2;
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
