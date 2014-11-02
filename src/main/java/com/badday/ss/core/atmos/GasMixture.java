package com.badday.ss.core.atmos;

import net.minecraftforge.fluids.Fluid;

public class GasMixture {

	private Fluid gas;
	private int ammount;
	
	public GasMixture(Fluid gas, int ammount) {
		super();
		this.gas = gas;
		this.ammount = ammount;
	}

	public Fluid getGas() {
		return this.gas;
	}
	
	public int getAmmount() {
		return this.ammount;
	}
	
	public void setAmmount(int newAmmount) {
		this.ammount = newAmmount;
	}
	
}