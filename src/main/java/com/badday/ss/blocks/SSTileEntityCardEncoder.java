package com.badday.ss.blocks;

import java.util.List;
import java.util.Vector;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;

import com.badday.ss.api.ITEAccess;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.entity.player.SSPlayerRoles;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class SSTileEntityCardEncoder extends TileEntity implements ITEAccess, IInventory, ISidedInventory, INetworkUpdateListener,
INetworkClientTileEntityEventListener, INetworkDataProvider, IEnergySink {

	private int side = 0;
	// Energy
	public double energy = 0.0D;
	public int maxEnergy = 10000;
	private boolean addedToEnergyNet = false;
			
	public long ticks = 0;
	
	private int numUsingPlayers;
	
	public SSTileEntityCardEncoder() {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updateEntity() {
		super.updateEntity();
	}
	
	public int getSide() {
		System.out.println("getSide "+this.side);
		return this.side;
	}

	public void setSide(int f) {
		System.out.println("setSide "+this.side);
		this.side = f;
		this.markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.energy = nbt.getDouble("energy");
		setSide(nbt.getInteger("side"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setDouble("energy", this.energy);
		nbt.setInteger("side", getSide());
	}

	
	@Override
	public void validate() {
		if (!this.worldObj.isRemote) {
		      MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

		      this.addedToEnergyNet = true;
		    }
		super.validate();
	}
	
	@Override
	public void invalidate() {
		if ((!this.worldObj.isRemote) && (this.addedToEnergyNet)) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			this.addedToEnergyNet = false;
		}
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		if ((!this.worldObj.isRemote) && (this.addedToEnergyNet)) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			this.addedToEnergyNet = false;
		}
		super.onChunkUnload();
	}
	
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		return true;
	}

	@Override
	public double getDemandedEnergy() {
		return this.maxEnergy - this.energy;
	}

	@Override
	public int getSinkTier() {
		return 1;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
		if (this.energy >= this.maxEnergy) {
		      return amount;
		    }
		 double e = Math.min(amount, this.maxEnergy - this.energy);
		    this.energy += e;
		    return amount - e;
	}

	@Override
	public List<String> getNetworkedFields() {
		Vector<String> vector = new Vector<String>();
		vector.add("energy");
		vector.add("side");
		return vector;
	}

	@Override
	public void onNetworkEvent(EntityPlayer player, int event) {
		// when press button in gui ?
		
	}

	@Override
	public void onNetworkUpdate(String field) {
		if (field.equals("side")) 
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public boolean useEnergy(double amount) {
		if (this.energy >= amount) {
			this.energy -= amount;

			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int arg0, ItemStack arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canInsertItem(int arg0, ItemStack arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ItemStack decrStackSize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int arg0, ItemStack arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInventorySlotContents(int arg0, ItemStack arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPlayerHaveAccess(Entity player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addRole(SSPlayerRoles role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRole(SSPlayerRoles role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean switchRole(SSPlayerRoles role) {
		// TODO Auto-generated method stub
		return false;
	}


}
