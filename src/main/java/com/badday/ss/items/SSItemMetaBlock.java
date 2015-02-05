package com.badday.ss.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.badday.ss.SS;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemMetaBlock extends ItemBlock {

	public SSItemMetaBlock(Block block) {
		super(block);
	}

	@Override
	public int getMetadata(int damageValue) {
		return damageValue;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return this.getBlock().getIcon(1, meta);
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return SS.ssTab;
	}

	protected Block getBlock() {
		return field_150939_a;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getBlock().getUnlocalizedName() + "." + itemstack.getItemDamage();
	}
}
