package com.badday.ss.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.badday.ss.SS;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class SSItem extends Item {

	//protected Icon[] textures;

	public SSItem() {
		super();
		this.setCreativeTab(SS.ssTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		//return textures[meta];
		return this.itemIcon;
	}
}
