package com.badday.ss.core.atmos;

import java.util.ArrayList;
import java.util.List;

import com.badday.ss.SSConfig;

import net.minecraftforge.fluids.Fluid;
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
		
		if (this.capacity - this.getTotalAmount() < resource.amount) 
			return 0;
		
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
		int cap = 0;
		
		for (FluidTank tank: mixtureTank) {
			if (tank != null && tank.getFluidAmount()>0 && tank.getCapacity()>0) {
				res += tank.getFluid().getUnlocalizedName()+": "+tank.getFluidAmount()+" "+Float.toString(tank.getFluidAmount()*100.0f / tank.getCapacity())+"% ";
				cap = tank.getCapacity();
			}
		}
		
		return "Cap: "+cap+" "+res;
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
		for (FluidTank tank: mixtureTank) {
			tank.setCapacity(this.capacity);
		}
		
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
	
	public int getPercentOfGas(String name) {
		for (FluidTank tank: mixtureTank) {
			if (tank != null && tank.getFluid() != null && tank.getFluid().getUnlocalizedName().equals(name)) {
				return tank.getFluidAmount() * 100 / this.getCapacity();
			}
		}
		return 0;
	}

	public int getPercentOfGas(FluidTank fluidtank) {
		for (FluidTank tank: mixtureTank) {
			if (tank != null && tank.getFluid() != null && tank.getFluid().fluidID == fluidtank.getFluid().fluidID) {
				return tank.getFluidAmount() * 100 / this.getCapacity();
			}
		}
		return 0;
	}
	
	/**
	 * Emulate gas consumption - Dispel drainGas into fillGas
	 * @param drainGas
	 * @param fillGas
	 * @param amount
	 */
	public void dispel(String drainGas, String fillGas, int amount) {
		FluidStack o2 = null;
		int co2 = 0;
		for (FluidTank tank: mixtureTank) {
			if (amount > 0 && tank != null && tank.getFluid() != null && tank.getFluid().getUnlocalizedName().equals(drainGas)) {
				o2 = tank.drain(amount, true);
			}
		}
		if (o2 != null)
			addGas(new FluidStack(SSConfig.fluidCarbonDioxide,o2.amount));
		
	}

	/**
	 * Emulate work scrubber
	 * @param drainGas
	 * @param amount
	 */
	public void dispel(String drainGas, int amount) {
		for (FluidTank tank: mixtureTank) {
			if (amount > 0 && tank != null && tank.getFluid() != null && tank.getFluid().getUnlocalizedName().equals(drainGas)) {
				tank.drain(amount, true);
			}
		}
	}
	
}
