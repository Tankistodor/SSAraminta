package com.badday.ss;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class SSCreativeTab extends CreativeTabs {

	//private final int itemForTab;
	
	public SSCreativeTab(String label) {
		super(label);
	}
	
	@Override
	public Item getTabIconItem() {
		return GameRegistry.findItem("SS", "welder");
	}


}
