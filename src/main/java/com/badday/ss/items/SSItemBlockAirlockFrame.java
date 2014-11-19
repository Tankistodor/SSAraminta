package com.badday.ss.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemBlockAirlockFrame extends SSItemMetaBlock {

	public SSItemBlockAirlockFrame(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if (itemstack.getItemDamage() == 0)
			return "Airlock Frame"; 
		else
			return "Airlock Controller";
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.getBlock().getIcon(1, meta);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag)
	{
		list.add("Sealed Airlock Frame.");
	   	super.addInformation(itemstack, player, list, flag);
    }
	
}
