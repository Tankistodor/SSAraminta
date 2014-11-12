package com.badday.ss.core.atmos;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.blocks.SSTileEntityAirVent;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.events.RebuildNetworkEvent;


public class GasUtils {

	public static ConcurrentHashMap<String,RebuildNetworkEvent> pointToRebuild = new ConcurrentHashMap<String,RebuildNetworkEvent>();
	
	public static void registeredEventRebuildGasNetwork(RebuildNetworkEvent point) {
		String index = point.coords.x + ":" + point.coords.y + ":" + point.coords.z;
		System.out.println("Added event to rebuld net on "+index);
		pointToRebuild.put(index, point);
	}
	
	public static void removeAirVent(RebuildNetworkEvent point) {
	    String index = point.coords.x + ":" + point.coords.y + ":" + point.coords.z;
	    pointToRebuild.remove(index);
	}
	

	/**
	 * Event called from SSTickHandlerServer
	 */
	public static void rebuildGasNetworkEvent() {
		for (RebuildNetworkEvent point : pointToRebuild.values()) {
			System.out.println("Start to rebuld net on "+point.coords.toString());
			
			SSGasNetwork network = new SSGasNetwork(point.world);
			
			GasPathfinder finder = new GasPathfinder(point.world, point.coords);
			List<BlockVec3> results = finder.exploreNetwork();
			
			network.setPipes(finder.getPipes());
			network.setVents(finder.getVents());
			network.setSources(finder.getSources());
			
			if (SS.Debug) {
				System.out.println("Network "+network.toString() + " reBuilded at " + point.coords.toString());
				network.printDebugInfo();
			}
			removeAirVent(point);
		}
		
	}
	
	
	/**
	 * https://ru.wikipedia.org/wiki/%D0%9C%D0%BE%D0%BB%D1%8F%D1%80%D0%BD%D0%B0%D1%8F_%D0%BC%D0%B0%D1%81%D1%81%D0%B0
	 * @param fl
	 * @return
	 */
	public static int getMolV(FluidStack fl) {
		String name = fl.getUnlocalizedName(); 
		if (name.contains("hydrogen")) { // Водород H
			return 1; 
		} else if (name.contains("fluid.oxygen")) { // Кислород O
			return 16; 
		} else if (name.contains("fluid.nitrogen")) { // Азот N
			return 14; 
		} else if (name.contains("fluid.carbondioxide")) { // Углекислый гах CO2
			return 44; 
		} else if (name.contains("fluid.argon")) { // Аргон
			return 40;
		} else if (name.contains("fluid.helium")) { // Гелий
			return 4;
		} 
		return 38;
	}
	
	public static float getGasPressure(GasMixture mix, int v, int t) {

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
		for (FluidTank tank : mix.mixtureTank) {
			M += (tank.getFluidAmount()/getMolV(tank.getFluid()));
		}

		if (v > 0) {
			return M * 8.31f * T / v;
		}
		
		return 0;
	}
	
	
	/**
	 * Return all block, who can connect to GasNetwork. MOVE TO GasUtils
	 * @param w
	 * @param position
	 * @return
	 */
	public static BlockVec3[] getAdjacentAll(World w, BlockVec3 position) {

		BlockVec3[] adjacentConnections = new BlockVec3[ForgeDirection.VALID_DIRECTIONS.length];

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			Block block = position.getBlockOnSide(w, direction.ordinal());
			TileEntity src = position.getTileEntityOnSide(w, direction.ordinal());
			
			//GasMixer
			if (direction.equals(ForgeDirection.DOWN) && src instanceof IGasNetworkSource ) {
				adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
			}
			
			//Vent
			if (src instanceof IGasNetworkVent) {
				int meta = position.clone().modifyPositionFromSide(direction).getBlockMetadata(w);				
				if ((meta == 0 || meta == 2) && direction.equals(ForgeDirection.DOWN)) {
					adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
				} else if ((meta == 1 || meta == 3) && direction.equals(ForgeDirection.UP)) {
					adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
				}
			}
						
			//Pipe
			if (block.equals(SSConfig.ssBlockGasPipe))
				adjacentConnections[direction.ordinal()] = position.clone().modifyPositionFromSide(direction);
			
		}
		return adjacentConnections;
	}
	
	/**
	 * Return count valid connections
	 * @param w
	 * @param position
	 * @return
	 */
	public static int getAdjacentAllCount(World w, BlockVec3 position) {

		int validConnections = 0;

		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			Block block = position.getBlockOnSide(w, direction.ordinal());
			TileEntity src = position.getTileEntityOnSide(w, direction.ordinal());
			
			//GasMixer
			if (direction.equals(ForgeDirection.DOWN) && src instanceof IGasNetworkSource ) {
				validConnections++;
			}
			
			//Vent
			if (src instanceof IGasNetworkVent) {
				int meta = position.clone().modifyPositionFromSide(direction).getBlockMetadata(w);				
				if (meta == 0 && direction.equals(ForgeDirection.DOWN)) {
					validConnections++;
				} else if (meta == 1 && direction.equals(ForgeDirection.UP)) {
					validConnections++;
				}
			}
						
			//Pipe
			if (block.equals(SSConfig.ssBlockGasPipe))
				validConnections++;
			
		}
		return validConnections;
	}
	
	public static boolean checkAtmos(World w, SSTileEntityAirVent vent, GasMixture tank) {
		
		float pressure = vent.getPressure();
		
		if (pressure < 9 || pressure > 11) return false;
		
		int part = tank.getPercentOfGas("fluid.oxygen");
		if ((part < 18) || (part > 30)) return false;
		
		part = tank.getPercentOfGas("fluid.carbondioxide");
		if ((part < 70) || (part > 80)) return false;

		return true;
	}
}


