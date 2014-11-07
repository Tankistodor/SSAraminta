package com.badday.ss.core.atmos;

import java.util.ArrayList;
import java.util.List;

import com.badday.ss.SSConfig;

import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class GasMixture {
	
	public int capacity = FluidContainerRegistry.BUCKET_VOLUME * 10;
	public List<FluidTank> mixtureTank = new ArrayList<FluidTank>();

	//new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
	public int addGas(FluidStack resource) {
		
		int amount = 0;
		boolean fluidExists = false;
		
		for (FluidTank tank: mixtureTank) {
			if (tank.getFluid() != null && tank.getFluid().isFluidEqual(resource)) {
				amount = tank.fill(resource, true);
				fluidExists = true;
			}
		}
		
		if (!fluidExists) {
			FluidTank newTank = new FluidTank(this.capacity);
			amount = newTank.fill(resource, true);
			mixtureTank.add(newTank);
		}
		
		return amount;
	}

	@Override
	public String toString() {
		
		String res = "";
		
		for (FluidTank tank: mixtureTank) {
			if (tank != null) {
				res += tank.getFluid().getUnlocalizedName()+": "+tank.getCapacity()+"/"+tank.getFluidAmount()+" ";
			}
		}
		
		return res;
	}
	
	public int getTotalAmount() {
		int res = 0;
		for (FluidTank tank: mixtureTank) {
			if (tank != null) {
				res += tank.getFluidAmount();
			}
		}
		return res;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public void setCapacity(int value) {
		this.capacity = value;
	}

	public void fill(GasMixture gas) {
		for (FluidTank tank: gas.mixtureTank) {
			this.addGas(tank.getFluid());
		}
	}
	
	public float getPressure() {
		// SIMPLE
		if (this.getCapacity() > 0) {
			float t = (float) this.getTotalAmount()/this.getCapacity();
			return t*10.13f;
		} else {
			return 0;
		}
		// TODO: MAKE MORE HARD 
		//return GasUtils.getGasPressure(this,this.capacity,SSConfig.ssDefaultTemperature);
	}
	
}
