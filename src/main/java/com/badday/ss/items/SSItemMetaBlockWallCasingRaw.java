package com.badday.ss.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.badday.ss.SSConfig;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemMetaBlockWallCasingRaw extends SSItemMetaBlock {

	public SSItemMetaBlockWallCasingRaw(Block block) {
		super(block);
		this.setMaxDamage(0);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	@Override
	public int getMetadata(int damageValue) {
		return damageValue;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return SSConfig.ssWallCasingRaw_unlocalizedName[itemstack.getItemDamage()];
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
		list.add("Unmounted and uncunstructed wall casing");
        //this.getUnlocalizedName() + ".description"; 
	   	super.addInformation(itemstack, player, list, flag);
    }
	
}
