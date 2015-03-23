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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.badday.ss.SSConfig;
import com.badday.ss.core.atmos.FindNearestVentJob;
import com.badday.ss.core.utils.BlockVec3;

public class SSTileEntityScrubber extends TileEntity implements IFluidHandler,IInventory, ISidedInventory, INetworkDataProvider, INetworkUpdateListener,
		INetworkClientTileEntityEventListener, IEnergySink {

	private final int COOLDOWN = 20;
	public long ticks = 0;
	private BlockVec3 nearestAirVent = BlockVec3.INVALID_VECTOR;
	private FindNearestVentJob job;
	
	private ItemStack[] containingItems = new ItemStack[1];

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
	
	public ItemStack getDischargeSlot() {
		if (this.containingItems != null && this.containingItems.length > 0) {
			return this.containingItems[0];
		} else {
			this.containingItems = new ItemStack[1];
		}
		return this.containingItems[0];
	}
	
	public void setDischargeSlot(ItemStack slot) {
		if (this.containingItems != null && this.containingItems.length > 0) {
			this.containingItems[0] = slot;
		} else {
			this.containingItems = new ItemStack[1];
			this.containingItems[0] = slot;
		}
 	}
	
	@Override
	public void readFromNBT(NBTTagCompound tags) {
		super.readFromNBT(tags);

		final NBTTagList var2 = tags.getTagList("Items", 10);
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length) {
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		for (int i = 0; i < 4; i++) {
			if (tags.getBoolean("hasFluid" + i)) {
				tank[i].setFluid(FluidRegistry.getFluidStack(tags.getString("fluidName" + i), tags.getInteger("amount" + i)));
			} else
				tank[i].setFluid(null);
		}

		this.energy = tags.getDouble("energy");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tags) {
		super.writeToNBT(tags);
		
		final NBTTagList list = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3) {
			if (this.containingItems[var3] != null) {
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				list.appendTag(var4);
			}
		}

		tags.setTag("Items", list);
		
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
		this.tickBeat();
		if (!this.worldObj.isRemote) {
			if (this.maxEnergy - this.energy >= 1.0D) {

				if (this.getDischargeSlot() != null) {
					double amount = ElectricItem.manager.discharge(this.getDischargeSlot(), 1, 1, true, true, false);
					if (amount > 0.0D) {
						this.energy += amount;

					markDirty();
					}
				}
			}
			
			if (this.ticks % (COOLDOWN * 5) == 0) {
				if (job == null) {
					job = new FindNearestVentJob(this.worldObj, new BlockVec3(this.xCoord,this.yCoord,this.zCoord));
					job.run();
				} else {
					if (!job.isAlive()) {
						this.nearestAirVent = job.getNearestVent();
						job = null;
					}
				}
			}
			
			if ((this.ticks % (COOLDOWN * 3) == 0) && (!this.nearestAirVent.equals(BlockVec3.INVALID_VECTOR))) {
				// Try to get _non_ O2 and N gases and put it into scrubber tank
				SSTileEntityAirVent airVent = (SSTileEntityAirVent) this.nearestAirVent.getTileEntity(this.worldObj);
				if (airVent != null && this.energy > 15) {
					for (FluidTank fluidtank : airVent.tank.mixtureTank) {
							if (fluidtank != null && fluidtank.getFluid() != null && !fluidtank.getFluid().getUnlocalizedName().equals("fluid.oxygen") && !fluidtank.getFluid().getUnlocalizedName().equals("fluid.nitrogen")
									&& fluidtank != null && fluidtank.getFluid() != null && !fluidtank.getFluid().getUnlocalizedName().equals("fluid.O.name") && !fluidtank.getFluid().getUnlocalizedName().equals("fluid.N.name")) {
								FluidStack newRes = fluidtank.drain(15, true);
								
								int firstNull=0;
								int i = 0;
								boolean inject = false;
								
								for (FluidTank t : this.tank) {
									if (t != null && t.getFluid() != null && t.getFluid().getUnlocalizedName().equals(newRes.getUnlocalizedName())) {
										t.fill(newRes, true);
										this.energy -= 15;
										inject = true;
										markDirty();
										break;
									}
									if (t == null || t.getFluid() == null) {
										firstNull = i;
									}
									i++;
								}
								
								if (!inject) {
									markDirty();
									this.energy -= 15;
									this.tank[firstNull] = new FluidTank(newRes,FluidContainerRegistry.BUCKET_VOLUME);
								}
								
						}
					}
				}
			}
			
		}

		this.guiChargeLevel = Math.min(1.0F, (float) this.energy / this.maxEnergy);
	}
	
	private void tickBeat() {

		if (this.ticks >= Long.MAX_VALUE) {
			this.ticks = 1;
		}

		this.ticks++;
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
		 double e = Math.min(amount, this.maxEnergy - this.energy);
		    this.energy += e;
		    return amount - e;
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
		//vector.add("nearestAirVent");
		vector.add("energy");
		vector.add("guiChargeLevel");
		return vector;
	}

	@Override
	public void openInventory() {
		if (worldObj == null) return;	
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, SSConfig.ssBlockScrubber, 1, numUsingPlayers);
	}
	
	@Override
	public void closeInventory() {
		if (worldObj == null) return;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, SSConfig.ssBlockScrubber, 1, numUsingPlayers);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.containingItems[i] != null) {
			if (containingItems[i].stackSize <= j) {
				ItemStack itemstack = containingItems[i];
				containingItems[i] = null;
				markDirty();
				return itemstack;
			}
			ItemStack itemstack1 = containingItems[i].splitStack(j);
			if (containingItems[i].stackSize == 0) {
				containingItems[i] = null;
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
		return this.containingItems[slot];
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
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0 };
    }
	
	@Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }
	
	@Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        return slotID == 0;
    }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return ((worldObj.getTileEntity(xCoord, yCoord, zCoord) == this)
				&& (entityplayer.getDistance((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D));
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.containingItems[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		markDirty();
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
			this.tank[direction.ordinal()-2].drain(arg1.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection direction, int maxDrain, boolean doDrain) {
		if (direction.ordinal() > 1 && direction.ordinal() < 6) {
			if (this.tank[direction.ordinal()-2].getFluid() != null) {
				markDirty();
				return this.tank[direction.ordinal()-2].drain(maxDrain, doDrain);
			}
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

	public String getFluidTooltips(int tankId) {
		if (tank[tankId].getFluid() != null)
			return tank[tankId].getInfo().fluid.getLocalizedName()+" "+tank[tankId].getFluidAmount()+"/"+tank[tankId].getCapacity();
		return "empty";
	}
}
