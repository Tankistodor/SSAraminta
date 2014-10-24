package com.badday.ss.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.badday.ss.SS;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemBlock extends ItemBlock {

	public SSItemBlock(Block block) {
		super(block);
		this.setCreativeTab(SS.ssTab);
	}

	/*@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return this.itemIcon;
	}
	
	/*@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister
				.registerIcon(SS.ASSET_PREFIX + ":" + this.getUnlocalizedName());
	}/
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister icon)
    {
        this.itemIcon = icon.registerIcon(SS.ASSET_PREFIX+"airBlock");
    }
	*/
}
