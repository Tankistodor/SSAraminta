package com.badday.ss.items;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemHidden extends ItemBlock {

	public SSItemHidden(Block block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return this.field_150939_a.getIcon(0, par1);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		int metadata = itemstack.getItemDamage();
		String blockName = "";
		blockName = null;
		return this.field_150939_a.getUnlocalizedName() + "." + blockName;
	}

	@Override
	public String getUnlocalizedName() {
		return this.field_150939_a.getUnlocalizedName() + ".0";
	}

}
