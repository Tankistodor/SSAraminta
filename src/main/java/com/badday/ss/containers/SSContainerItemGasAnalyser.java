package com.badday.ss.containers;

import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.badday.ss.SSConfig;
import com.badday.ss.core.atmos.FindNearestVentJob;
import com.badday.ss.core.atmos.GasUtils;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.items.SSInventoryItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class SSContainerItemGasAnalyser extends ContainerBase {
	
	private EntityPlayer player;
    private IInventory toolInventory;
    private final int slotCount = 1;
    
    private List<String> atmosInfo = new ArrayList<String>();
	
    
	public SSContainerItemGasAnalyser(EntityPlayer player, IInventory playerInventory, SSInventoryItem toolInventory) {
		super(toolInventory);
		this.toolInventory = toolInventory;
        player = ((InventoryPlayer) playerInventory).player;
        
        //layoutContainer(playerInventory, toolInventory, xSize, ySize);
        atmosInfo.add("No data or battery is low");
        
        if (!player.worldObj.isRemote) {
        	this.atmosInfo = GasUtils.getAtmosInfoForGui(player);;
        }
		
		toolInventory.openInventory();
	}

	@Override
	public boolean canInteractWith(EntityPlayer arg0) {
		//return chest.isUseableByPlayer(player);
		return true;
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
    
    @Override
    public void detectAndSendChanges() {
    	super.detectAndSendChanges();
    	 ((NetworkManager)IC2.network.get()).sendContainerFields(this, new String[] { "atmosInfo" });
    }

    /*
    @Override
    public List<String> getNetworkedFields() {
		Vector<String> vector = new Vector<String>(3);
		vector.add("atmosInfo");
		return vector;
	}*/

	public List<String> getAmtosInfo() {
		return this.atmosInfo;
	}
    
}
