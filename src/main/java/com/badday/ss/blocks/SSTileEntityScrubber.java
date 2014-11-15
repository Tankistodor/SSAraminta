package com.badday.ss.blocks;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;

import java.util.List;
import java.util.Vector;

import com.badday.ss.SSConfig;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class SSTileEntityScrubber extends TileEntity implements IFluidHandler,IInventory, INetworkDataProvider, INetworkUpdateListener,
		INetworkClientTileEntityEventListener, IEnergySink {

	private ItemStack dischargeSlot;

	public FluidTank[] tank = new FluidTank[4];

	// Energy
	public double energy = 0;
	public int maxEnergy = 5000;
	private boolean addedToEnergyNet = false;
	public float guiChargeLevel = 0;
	
	private int numUsingPlayers;
	
	public SSTileEntityScrubber() {
		super();
		for (int i = 0; i<4; i++ ) {
			this.tank[i] = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME );
		}
	}
	
	public boolean hasTileEntity(int metadata) {
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tags) {
		super.readFromNBT(tags);

		for (int i = 0; i < 4; i++) {
			FluidStack liquid = tank[i].getFluid();
			tags.setBoolean("hasFluid"+i, liquid != null);
		    if (liquid != null)
		        {
		            tags.setString("fluidName"+i, liquid.getFluid().getName());
		            tags.setInteger("amount"+i, liquid.amount);
		        }
		}

		
		this.energy = tags.getDouble("energy");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tags) {
		super.writeToNBT(tags);
		for (int i = 0; i < 4; i++) {
			FluidStack liquid = tank[i].getFluid();
			tags.setBoolean("hasFluid"+i, liquid != null);
		    if (liquid != null)
		        {
		            tags.setString("fluidName"+i, liquid.getFluid().getName());
		            tags.setInteger("amount"+i, liquid.amount);
		        }
		}
		
		tags.setDouble("energy", this.energy);
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
	public void updateEntity() {
		super.updateEntity();
		
		if (!this.worldObj.isRemote) {
			if (this.maxEnergy - this.energy >= 1.0D) {

				if (this.dischargeSlot != null) {
					double amount = ElectricItem.manager.discharge(this.dischargeSlot, 1, 1, true, true, false);
					if (amount > 0.0D) {
						this.energy += amount;

					markDirty();
					}
				}
			}
		}

		this.guiChargeLevel = Math.min(1.0F, (float) this.energy / this.maxEnergy);
	}
	
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		if (direction.equals(ForgeDirection.DOWN)) return true;
		return false;
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
		    this.energy += amount;
		    return 0;
	}

	public double getEnergy() {
		return this.energy;
	}
	
	public boolean useEnergy(double amount) {
		if (this.energy >= amount) {
			this.energy -= amount;

			return true;
		}
		return false;
	}
	
	public int getGuiChargeLevel(int scale) {
		return (int) (this.energy * scale / this.maxEnergy);
	}
	
	/**
	 * Called when player push button in gui
	 */
	@Override
	public void onNetworkEvent(EntityPlayer player, int event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkUpdate(String field) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getNetworkedFields() {
		Vector<String> vector = new Vector<String>(3);
		vector.add("tank");
		//vector.add("dischargeSlot");
		vector.add("energy");
		vector.add("guiChargeLevel");
		return vector;
	}

	@Override
	public void openInventory() {
		if (worldObj == null) return;	
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, SSConfig.ssBlockCabinet, 1, numUsingPlayers);
	}
	
	@Override
	public void closeInventory() {
		if (worldObj == null) return;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, SSConfig.ssBlockCabinet, 1, numUsingPlayers);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.dischargeSlot != null) {
			if (this.dischargeSlot.stackSize <= j) {
				ItemStack itemstack = this.dischargeSlot;
				this.dischargeSlot = null;
				markDirty();
				return itemstack;
			}
			ItemStack itemstack1 = this.dischargeSlot.splitStack(j);
			if (this.dischargeSlot.stackSize == 0) {
				this.dischargeSlot = null;
			}
			markDirty();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public String getInventoryName() {
		return "inventoryScrubber";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot == 0)
			return this.dischargeSlot;
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
                setInventorySlotContents(slot, null);
        }
        return stack;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {

		if (slotID == 0 && itemstack.getItem() instanceof ic2.api.item.ISpecialElectricItem) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return ((worldObj.getTileEntity(xCoord, yCoord, zCoord) == this)
				&& (entityplayer.getDistance((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D));
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.dischargeSlot = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                stack.stackSize = getInventoryStackLimit();
        }  
	}

	@Override
	public boolean canDrain(ForgeDirection direction, Fluid fluid) {
		if (direction.ordinal() > 1 && direction.ordinal() < 6)
			return this.tank[direction.ordinal()].getFluid() != null && this.tank[direction.ordinal()].getFluidAmount() > 0;
		return false;
	}

	@Override
	public boolean canFill(ForgeDirection arg0, Fluid arg1) {
		return false;
	}

	@Override
	public FluidStack drain(ForgeDirection direction, FluidStack arg1, boolean doDrain) {
		if (direction.ordinal() > 1 && direction.ordinal() < 6) {
			this.tank[direction.ordinal()].drain(arg1.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection direction, int maxDrain, boolean doDrain) {
		if (direction.ordinal() > 1 && direction.ordinal() < 6) {
			this.tank[direction.ordinal()].drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public int fill(ForgeDirection arg0, FluidStack arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
FluidStack fluid = null;
        
        int side = from.ordinal()-2;
		if (side >= 0 && side < 4) {
			if (tank[side].getFluid() != null)
				fluid = tank[side].getFluid().copy();
		} else { side = 0; }
		
        return new FluidTankInfo[] { new FluidTankInfo(fluid, tank[side].getCapacity()) };
	}

	/**
	 * Return scaled storage of fluid in tank. Used in Gui
	 * @param tankId
	 * @param scale
	 * @return
	 */
	public int getScaled(int tankId, int scale) {
		if (tank[tankId].getFluid() != null)
			return tank[tankId].getFluidAmount()*scale/tank[tankId].getCapacity();
		return 0;
	}

}
