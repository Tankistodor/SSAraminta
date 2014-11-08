package com.badday.ss.blocks;

import java.util.List;
import java.util.Vector;

import ic2.api.network.INetworkDataProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.badday.ss.api.IGasNetwork;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.core.atmos.GasMixture;

public class SSTileEntityGasMixer extends TileEntity implements IGasNetworkSource, IFluidHandler, IInventory,INetworkDataProvider {
	
	public FluidTank[] tank = new FluidTank[4];
	public byte[] tankTrust = new byte[4];  // Регуряторы напора
	public byte totalTrust = 1;
	public int pressure[] = new int[4]; // Регуряторы напора

	public int renderOffset[] = new int[4];
	
	public IGasNetwork gasNetwork;
	
	private ItemStack chargeSlot;
	
	public SSTileEntityGasMixer() {
		super();
		for (int i = 0; i<4; i++ ) {
			this.tank[i] = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
			this.tankTrust[i] = 33; // 0-100
			this.totalTrust = 1; // 0-100
		}
	}

	@Override
	public IGasNetwork getNetwork() {
		return this.gasNetwork;
	}
	
	@Override
	public void setNetwork(IGasNetwork network) {
		this.gasNetwork = network;		
	}

	@Override
	public void onNetworkChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if (from.ordinal() > 1 && from.ordinal() < 6)
    		return tank[from.ordinal()-2].fill(new FluidStack(fluid,1), false) > 0;
        return false;
	}

	@Override
    public FluidStack drain (ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        /*if (tank.getFluidAmount() == 0)
            return null;
        if (tank.getFluid().getFluid() != resource.getFluid())
            return null;

        // same fluid, k
        return this.drain(from, resource.amount, doDrain); */
        return null;
    }

	@Override
    public FluidStack drain (ForgeDirection from, int maxDrain, boolean doDrain)
    {
        /*FluidStack amount = tank.drain(maxDrain, doDrain);
        if (amount != null && doDrain)
        {
            renderOffset = -maxDrain;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        return amount;*/
		return null;
    }

	@Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
		int side = from.ordinal()-2;
		if (side >= 0 && side < 4) {
			int amount = tank[side].fill(resource, doFill);
	        if (amount > 0 && doFill)
	        {
	            renderOffset[side] = resource.amount;
	            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	        }
	        return amount;
		}
		return 0;
    }

	@Override
    public FluidTankInfo[] getTankInfo (ForgeDirection from)
    {
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
	
	@Override
	public void readFromNBT(NBTTagCompound tags) {
		super.readFromNBT(tags);
		for (int i = 0; i < 4; i++) {
			this.tankTrust[i] = tags.getByte("tankTrust" + i);
			if (tags.getBoolean("hasFluid" + i)) {
				tank[i].setFluid(FluidRegistry.getFluidStack(tags.getString("fluidName" + i), tags.getInteger("amount" + i)));
			} else
				tank[i].setFluid(null);
		}
		this.totalTrust = tags.getByte("totalTrust");

		NBTTagList nbttaglist = tags.getTagList("Items", Constants.NBT.TAG_COMPOUND);

		NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(0);
		int j = nbttagcompound1.getByte("Slot") & 0xff;
		if (j >= 0 && j < 1) {
			chargeSlot = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound tags) {
		super.writeToNBT(tags);
		for (int i = 0; i < 4; i++) {
			FluidStack liquid = tank[i].getFluid();
			tags.setBoolean("hasFluid"+i, liquid != null);
			tags.setByte("tankTrust"+i,this.tankTrust[i]);
		    if (liquid != null)
		        {
		            tags.setString("fluidName"+i, liquid.getFluid().getName());
		            tags.setInteger("amount"+i, liquid.amount);
		        }
		}
		
		tags.setByte("totalTrust",this.totalTrust);
		
		NBTTagList nbttaglist = new NBTTagList();

		if (this.chargeSlot != null) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setByte("Slot", (byte) 0);
			this.chargeSlot.writeToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		tags.setTag("Items", nbttaglist);
		
	}
	
	public GasMixture getMyGas() {
		GasMixture result = new GasMixture();
		
		for (int i = 0; i<4; i++) {
			if (this.tank[i] != null && this.tank[i].getFluidAmount() >= this.tankTrust[i]*this.totalTrust) {
				FluidStack transfer = this.tank[i].drain(this.tankTrust[i]*this.totalTrust, true);
				if (transfer != null)
					result.addGas(transfer);
			}
		}
		return result;
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (this.gasNetwork != null) {
			this.getNetwork().removeSource(this);
		}
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (this.gasNetwork != null) {
			this.getNetwork().removeSource(this);
		}
	}

	

	@Override
    public ItemStack decrStackSize(int slot, int amt) {
            ItemStack stack = getStackInSlot(slot);
            if (stack != null) {
                    if (stack.stackSize <= amt) {
                            setInventorySlotContents(slot, null);
                    } else {
                            stack = stack.splitStack(amt);
                            if (stack.stackSize == 0) {
                                    setInventorySlotContents(slot, null);
                            }
                    }
            }
            return stack;
    }

	@Override
	public String getInventoryName() {
		return "ss.tileentity.gasmixer.chargeSlot";
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
		return this.chargeSlot;
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
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int arg0, ItemStack arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return ((worldObj.getTileEntity(xCoord, yCoord, zCoord) == this)
				&& (entityplayer.getDistance((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D));

	}

	@Override
	public void openInventory() {
		if (worldObj == null) return;		
	}
	
	@Override
	public void closeInventory() {
		if (worldObj == null) return;		
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.chargeSlot = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                stack.stackSize = getInventoryStackLimit();
        }  
	}

	@Override
	public List<String> getNetworkedFields() {
		Vector<String> vector = new Vector<String>(3);
		vector.add("tank");
		vector.add("tankTrust");
		vector.add("totalTrust");
		return vector;
	}

}
