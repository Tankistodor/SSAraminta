package com.badday.ss.items;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.core.init.InternalName;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemBaseElectric extends SSItem implements IElectricItem, IItemHudInfo {

	protected final double maxCharge;
	protected final double transferLimit;
	protected final int tier;

	public SSItemBaseElectric(String name, double maxCharge, double transferLimit, int tier) {
		super();

		setUnlocalizedName(name);

		this.maxCharge = maxCharge;
		this.transferLimit = transferLimit;
		this.tier = tier;

		setMaxDamage(27);
		setMaxStackSize(1);
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack itemStack = new ItemStack(this, 1);

		if (getChargedItem(itemStack) == this) {
			ItemStack charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, (1.0D / 0.0D), 2147483647, true, false);
			itemList.add(charged);
		}

		if (getEmptyItem(itemStack) == this) {
			ItemStack charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, 0.0D, 2147483647, true, false);
			itemList.add(charged);
		}
	}

	@Override
	public List<String> getHudInfo(ItemStack itemStack) {
	    List info = new LinkedList();
	    info.add(ElectricItem.manager.getToolTip(itemStack));
	    return info;
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return false;
	}

	@Override
	public Item getChargedItem(ItemStack itemStack) {
		return this;
	}

	@Override
	public Item getEmptyItem(ItemStack itemStack) {
		return this;
	}

	@Override
	public double getMaxCharge(ItemStack itemStack) {
		return this.maxCharge;
	}

	@Override
	public int getTier(ItemStack itemStack) {
		return this.tier;
	}

	@Override
	public double getTransferLimit(ItemStack itemStack) {
		return this.transferLimit;
	}

}
