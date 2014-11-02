package com.badday.ss.core.atmos;

import java.util.List;

import net.minecraftforge.fluids.Fluid;


public class GasUtils {

	/**
	 * https://ru.wikipedia.org/wiki/%D0%9C%D0%BE%D0%BB%D1%8F%D1%80%D0%BD%D0%B0%D1%8F_%D0%BC%D0%B0%D1%81%D1%81%D0%B0
	 * @param fl
	 * @return
	 */
	public static int getMolV(Fluid fl) {
		String name = fl.getName(); 
		if (name.contains("hydrogen")) { // Водород H
			return 1; 
		} else if (name.contains("oxygen")) { // Кислород O
			return 16; 
		} else if (name.contains("nitrogen")) { // Азот N
			return 14; 
		} else if (name.contains("carbondioxide")) { // Углекислый гах CO2
			return 44; 
		} else if (name.contains("argon")) { // Аргон
			return 40;
		} else if (name.contains("helium")) { // Гелий
			return 4;
		} 
		return 38;
	}

	
	public static float getGasPressure(List<GasMixture> mix, int v, int t) {

		/*
		 * P · V = (m / μ) · R · T, где P — давление газа; V — его объём; T —
		 * температура; R — универсальная газовая постоянная, равная 8.314·10–3
		 * Дж · кг-моль / К; m — масса этого газа; μ — килограмм-моль, т. е.
		 * число килограммов вещества, численно равное его молекулярному весу (в
		 * 1 моле вещества всегда находится одинаковое число молекул, равное
		 * 6.025·1026 кг-моль–1 — число Авогадро).
		 * 
		 * 
		 * P = (m / u) * R * T / V;
		 * 
		 */
		float T = t + 273.15f;

		float M = 0;
		for (GasMixture gas : mix) {
			M += (gas.getAmmount()/getMolV(gas.getGas()));
		}

		if (v > 0) {
			return M * 8.31f * T / v;
		}
		
		return 0;
	}
	
}
