package com.badday.ss.containers;

import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.badday.ss.SSConfig;
import com.badday.ss.blocks.SSTileEntityScrubber;

public class SSContainerScrubber extends Container implements INetworkTileEntityEventListener {
	
	private EntityPlayer player;
	private SSTileEntityScrubber tileEntity;
	
	public SSContainerScrubber(IInventory playerInventory, SSTileEntityScrubber tileEntity) {
		super();
		player = ((InventoryPlayer) playerInventory).player;
		this.tileEntity =  tileEntity;
		this.tileEntity.openInventory();
		layoutContainer(playerInventory, this.tileEntity, 176, 176);
	}

	protected void layoutContainer(IInventory playerInventory, IInventory chestInventory, int xSize, int ySize) {
		// Make discharge slot
		addSlotToContainer(makeSlot(chestInventory, 0, 8 , 8));
		
		// Player Inventory
		
		int leftCol = (xSize - 160) / 2 + 4;
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
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		Iterator i$;
		if (this.player instanceof EntityPlayerMP) {
			 for (i$ = getNetworkedFields().iterator(); i$.hasNext(); ) { 
				 String name = (String)i$.next();
			 	((NetworkManager)IC2.network.get()).updateTileEntityFieldTo((TileEntity)this.tileEntity, name, (EntityPlayerMP)player);
			 }
		}
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
	
	public Slot makeSlot(IInventory chestInventory, int index, int x, int y)
    {
    	return new Slot(chestInventory, index, x, y);
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

	@Override
	public void onNetworkEvent(int event) {
		// TODO Auto-generated method stub
		
	}
	
	public List<String> getNetworkedFields() {
		Vector<String> vector = new Vector<String>(3);
		vector.add("tank");
		//vector.add("dischargeSlot");
		vector.add("energy");
		vector.add("guiChargeLevel");
		return vector;
	}

}
