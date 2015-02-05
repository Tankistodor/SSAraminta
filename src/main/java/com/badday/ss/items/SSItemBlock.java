package com.badday.ss.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import com.badday.ss.SS;

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
