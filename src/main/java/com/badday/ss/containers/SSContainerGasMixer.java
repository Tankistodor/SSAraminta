package com.badday.ss.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class SSContainerGasMixer extends Container {
	
    private EntityPlayer player;
    private IInventory gasMixerInv;

	public SSContainerGasMixer(IInventory playerInventory, IInventory gasMixerInv,int xSize, int ySize) {
		super();
		player = ((InventoryPlayer) playerInventory).player;
		this.gasMixerInv = gasMixerInv;
		this.gasMixerInv.openInventory();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.gasMixerInv.isUseableByPlayer(player);
	}

	@Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        gasMixerInv.closeInventory();
    }
	
}
