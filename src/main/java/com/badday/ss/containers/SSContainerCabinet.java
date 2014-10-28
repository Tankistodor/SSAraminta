package com.badday.ss.containers;

import com.badday.ss.SSConfig;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SSContainerCabinet extends Container {
    private EntityPlayer player;
    private IInventory chest;
    private final int ROWLENGHT = 9;//9
    private final int ROWCOUNT = SSConfig.ssCabinetSize/ROWLENGHT; 
	
    public SSContainerCabinet(IInventory playerInventory, IInventory chestInventory, int xSize, int ySize)
    {
        chest = chestInventory;
        player = ((InventoryPlayer) playerInventory).player;
        chestInventory.openInventory();
        layoutContainer(playerInventory, chestInventory, xSize, ySize);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return chest.isUseableByPlayer(player);
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer p, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < SSConfig.ssCabinetSize) // TankeFix
            {
                if (!mergeItemStack(itemstack1, SSConfig.ssCabinetSize, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, SSConfig.ssCabinetSize, false))
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
        chest.closeInventory();
    }

    protected void layoutContainer(IInventory playerInventory, IInventory chestInventory, int xSize, int ySize)
    {
		for (int chestRow = 0; chestRow < ROWCOUNT; chestRow++) {
			for (int chestCol = 0; chestCol < ROWLENGHT; chestCol++) {
				addSlotToContainer(makeSlot(chestInventory, chestCol + chestRow * ROWLENGHT, 8 + chestCol * 18, 8 + chestRow * 18));
			}
		}

        int leftCol = (xSize - 162) / 2 + 1;
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
                addSlotToContainer(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18-4, ySize - (4 - playerInvRow) * 18
                        - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            addSlotToContainer(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18 - 4, ySize - 24));
        }
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
