package com.badday.ss.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSSealedBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockIC2CableCasing extends Block implements ISSSealedBlock {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons = new IIcon[16];
	
	public SSBlockIC2CableCasing(String asset) {
		super(Material.iron);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);

		GameRegistry.registerTileEntity(SSTileEntityIC2Cable.class, asset);
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new SSTileEntityIC2Cable();
	}
	

	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName().substring(5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		this.icons = new IIcon[SSConfig.ssIC2CableCasing_unlocalizedName.length];
		for (int i = 0; i < SSConfig.ssIC2CableCasing_unlocalizedName.length; i++) {
			icons[i] = icon.registerIcon(SS.ASSET_PREFIX
					+ SSConfig.ssIC2CableCasing_unlocalizedName[i]);
		}
		this.blockIcon = this.icons[SSConfig.ssIC2CableCasing_unlocalizedName.length-1];

	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta >= this.icons.length) {
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item par1, CreativeTabs tab, List list) {
		for (int i = 0; i < SSConfig.ssIC2CableCasing_unlocalizedName.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public final boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return false;
	}
}
