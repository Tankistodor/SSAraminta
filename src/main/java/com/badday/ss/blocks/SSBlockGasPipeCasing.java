package com.badday.ss.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.IGasNetworkElement;
import com.badday.ss.api.IGasNetworkPipe;
import com.badday.ss.api.ISSSealedBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockGasPipeCasing extends SSBlockGasPipeBase implements IGasNetworkPipe, IGasNetworkElement, ISSSealedBlock {

	private IIcon[] icons = new IIcon[16];
	
	public SSBlockGasPipeCasing(String asset) {
		super(Material.iron);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
	}
	
	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName().substring(5);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		this.icons = new IIcon[SSConfig.ssGasPipeCasing_unlocalizedName.length];
		for (int i = 0; i < SSConfig.ssGasPipeCasing_unlocalizedName.length; i++) {
			icons[i] = icon.registerIcon(SS.ASSET_PREFIX
					+ SSConfig.ssGasPipeCasing_unlocalizedName[i]);
		}
		this.blockIcon = this.icons[SSConfig.ssGasPipeCasing_unlocalizedName.length-1];
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item par1, CreativeTabs tab, List list) {
		for (int i = 0; i < SSConfig.ssGasPipeCasing_unlocalizedName.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
}
