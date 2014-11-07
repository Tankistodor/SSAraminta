package com.badday.ss.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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

public class SSTileEntityGasMixer extends TileEntity implements IGasNetworkSource, IFluidHandler {
	
	public FluidTank[] tank = new FluidTank[4];
	public int[] tankTrust = new int[4];  // Регуряторы напора
	public int 	totalTrust = 0;
	public int pressure[] = new int[4]; // Регуряторы напора

	public int renderOffset[] = new int[4];
	
	public IGasNetwork gasNetwork;
	
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
	
	@Override
	public void readFromNBT(NBTTagCompound tags) {
		super.readFromNBT(tags);
		for (int i = 0; i < 4; i++) {
		 if (tags.getBoolean("hasFluid"+i))
	        {
                tank[i].setFluid(FluidRegistry.getFluidStack(tags.getString("fluidName"+i), tags.getInteger("amount"+i)));
	        }
	        else
	            tank[i].setFluid(null);
		}
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
	}



	/**
	 * Collect some gases from SourceVent for added in vent
	 */
	@Override
	public float nipGas() {
		
		float resultMixerPressure = 0;
		int localTrust = 0;
		for (int i = 0; i<4; i++) {
			if (this.tank[i] != null && this.tank[i].getFluidAmount() >= this.tankTrust[i]) {
				resultMixerPressure += this.tankTrust[i];
			}
		}
		
		return resultMixerPressure*this.totalTrust/20;
		
	}
	
	public GasMixture getMyGas() {
		GasMixture result = new GasMixture();
		
		for (int i = 0; i<4; i++) {
			if (this.tank[i] != null && this.tank[i].getFluidAmount() >= this.tankTrust[i]*this.totalTrust) {
				FluidStack transfer = this.tank[i].drain(this.tankTrust[i]*this.totalTrust, true);
				//FluidStack transfer = this.tank[i].drain(1, true);
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

}
