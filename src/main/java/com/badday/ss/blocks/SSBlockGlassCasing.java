package com.badday.ss.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.badday.ss.SS;
import com.badday.ss.api.ISSSealedBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockGlassCasing extends SSMetaBlock implements ISSSealedBlock {

	public SSBlockGlassCasing(String assetName) {
		super(assetName, Material.glass);
		this.setBlockUnbreakable();
        this.setBlockTextureName(SS.ASSET_PREFIX + assetName);
        this.setBlockName(assetName);
		this.setResistance(330.0F);
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
		disableStats();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		this.icons = new IIcon[1];
		icons[0] = icon.registerIcon(SS.ASSET_PREFIX
					+ this.getUnlocalizedName());
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	public String getUnlocalizedName() {
		return "blockGlassCasing";
	}
}
