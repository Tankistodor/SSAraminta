package com.badday.ss.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

import com.badday.ss.blocks.SSTileEntityGasMixer;

public class SSContainerGasMixer extends Container {
	
    private EntityPlayer player;
    private final SSTileEntityGasMixer tileEntity;

	public SSContainerGasMixer(IInventory playerInventory, SSTileEntityGasMixer tileEntity) {
		super();
		player = ((InventoryPlayer) playerInventory).player;
		this.tileEntity =  tileEntity;
		this.tileEntity.openInventory();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileEntity.isUseableByPlayer(player);
	}

	@Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        tileEntity.closeInventory();
    }
	
}
