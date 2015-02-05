package com.badday.ss.items;

import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import com.badday.ss.SS;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class SSItem extends Item {

	protected IIcon[] textures;

	public SSItem() {
		super();
		this.setCreativeTab(SS.ssTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		if (textures != null) {
			if (meta < this.textures.length) {
				return this.textures[meta];
			}
			return this.textures.length < 1 ? this.itemIcon : this.textures[0];
		} else {
			return this.itemIcon;
		}
	}
}
