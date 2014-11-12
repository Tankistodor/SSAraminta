package com.badday.ss.containers;

import com.badday.ss.SSConfig;
import com.badday.ss.items.SSInventoryItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SSContainerItemGasAnalyser extends Container {
	
    private EntityPlayer player;
    private IInventory toolInventory;
    private final int slotCount = 1;
	
	public SSContainerItemGasAnalyser(EntityPlayer player, IInventory playerInventory, SSInventoryItem toolInventory) {
		this.toolInventory = toolInventory;
        player = ((InventoryPlayer) playerInventory).player;
        toolInventory.openInventory();
        //layoutContainer(playerInventory, toolInventory, xSize, ySize);
	}

	@Override
	public boolean canInteractWith(EntityPlayer arg0) {
		//return chest.isUseableByPlayer(player);
		return true;
	}
	
	/**
     * 
     * 
     * (non-Javadoc)
     * @see net.minecraft.inventory.Container#transferStackInSlot(net.minecraft.entity.player.EntityPlayer, int)
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer p, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (!slot.inventory.isItemValidForSlot(i, itemstack)) {
            	return null;
            }
            if (i < this.slotCount) // TankeFix
            {
                if (!mergeItemStack(itemstack1, this.slotCount, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, 1, false))
            {
                return null;
            }
            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
	
    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        toolInventory.closeInventory();
    }
    
    public Slot makeSlot(IInventory chestInventory, int index, int x, int y)
    {
    	return new Slot(chestInventory, index, x, y);
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

}
