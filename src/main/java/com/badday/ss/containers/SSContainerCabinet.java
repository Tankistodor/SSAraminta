package com.badday.ss.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class SSContainerCabinet extends Container {
    private IronChestType type;
    private EntityPlayer player;
    private IInventory chest;
	
	
	@Override
	public boolean canInteractWith(EntityPlayer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
