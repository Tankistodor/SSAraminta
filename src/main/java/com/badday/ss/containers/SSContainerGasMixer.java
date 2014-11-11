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
import net.minecraft.tileentity.TileEntity;

import com.badday.ss.blocks.SSTileEntityGasMixer;

public class SSContainerGasMixer extends Container implements INetworkTileEntityEventListener {
	
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
		Vector<String> vector = new Vector<String>(3);
		vector.add("tank");
		vector.add("tankTrust");
		vector.add("totalTrust");
		return vector;
	}

}