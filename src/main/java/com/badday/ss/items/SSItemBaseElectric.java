package com.badday.ss.items;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemBaseElectric extends SSItem implements IElectricItem, IItemHudInfo {

	protected int maxCharge;
	protected int transferLimit;
	protected final int tier;

	public SSItemBaseElectric(String name, int maxCharge, int transferLimit, int tier) {
		super();

		setUnlocalizedName(name);

		this.maxCharge = maxCharge;
		this.transferLimit = transferLimit;
		this.tier = tier;

		setMaxDamage(101);
		setMaxStackSize(1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack itemStack = new ItemStack(this, 1);

		if (getChargedItem(itemStack) == this) {
			ItemStack charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, 100, 1, true, false);
			itemList.add(charged);
		}

		if (getEmptyItem(itemStack) == this) {
			ItemStack charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, 0, 1, true, false);
			itemList.add(charged);
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
		ElectricItem.manager.use(stack, 0.0D, player);
		return super.onItemUse(stack, player, world, x, y, z, side, xOffset, yOffset, zOffset);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (((this.tier == 1) && (!ElectricItem.manager.use(stack, 1.0D, player))) || ((this.tier == 2) && (!ElectricItem.manager.use(stack, 128.0D, player))))
	    {
	      return stack;
	    }
		return super.onItemRightClick(stack, world, player);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {
		info.add(StatCollector.translateToLocal("ic2.item.tooltip.PowerTier") + " " + this.tier);
	}

	public List<String> getHudInfo(ItemStack itemStack) {
		List info = new LinkedList();
		info.add(ElectricItem.manager.getToolTip(itemStack));
		info.add(StatCollector.translateToLocal("ic2.item.tooltip.PowerTier") + " " + this.tier);
		return info;
	}

	/*
	 * @Override public List<String> getHudInfo(ItemStack itemStack) { List info
	 * = new LinkedList(); info.add(ElectricItem.manager.getToolTip(itemStack));
	 * return info; }
	 */

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
