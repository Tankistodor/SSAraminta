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

import com.badday.ss.blocks.SSTileEntityAirlockFrameController;

public class SSContainerAirlockController  extends Container implements INetworkTileEntityEventListener {

	private EntityPlayer player;
	private final SSTileEntityAirlockFrameController tileEntity;
	
	
	public SSContainerAirlockController(IInventory playerInventory, SSTileEntityAirlockFrameController tileEntity) {
		super();
		player = ((InventoryPlayer) playerInventory).player;
		this.tileEntity =  tileEntity;
		this.tileEntity.openInventory();
		layoutContainer(playerInventory, this.tileEntity, 176, 111);
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
	

	protected void layoutContainer(IInventory playerInventory, IInventory chestInventory, int xSize, int ySize) {
		// Make charge slot
		addSlotToContainer(makeSlot(chestInventory, 0, 8, 7));
		
		int leftCol = (xSize - 162) / 2 + 1;
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
                addSlotToContainer(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18
                        - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            addSlotToContainer(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18 , ySize - 24));
        }
		
	}
	
	/**
     * 
     * (non-Javadoc)
     * @see net.minecraft.inventory.Container#transferStackInSlot(net.minecraft.entity.player.EntityPlayer, int)
     *
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        final Slot slot = (Slot) this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            final ItemStack stack = slot.getStack();
            var2 = stack.copy();

            if (par1 == 0)
            {
                if (!this.mergeItemStack(stack, b - 36, b, true))
                {
                    return null;
                }
            }
            else
            {
                if (stack.getItem() instanceof ic2.api.item.ISpecialElectricItem)
                {
                    if (!this.mergeItemStack(stack, 0, 1, false))
                    {
                        return null;
                    }
                }
                else
                {
                    if (par1 < b - 9)
                    {
                        if (!this.mergeItemStack(stack, b - 9, b, false))
                        {
                            return null;
                        }
                    }
                    else if (!this.mergeItemStack(stack, b - 36, b - 9, false))
                    {
                        return null;
                    }
                }
            }

            if (stack.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (stack.stackSize == var2.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, stack);
        }

        return var2;
    }
	
	public Slot makeSlot(IInventory chestInventory, int index, int x, int y) {
		return new Slot(chestInventory, index, x, y);
	}
	
	@Override
	public void onNetworkEvent(int event) {
		// TODO Auto-generated method stub
		
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
	
	public List<String> getNetworkedFields() {
		Vector<String> vector = new Vector<String>();
		vector.add("sideEW");
		vector.add("side");
		vector.add("status");
		vector.add("energy");
		vector.add("constuctionFrameRight");
		return vector;
	}
	
}
