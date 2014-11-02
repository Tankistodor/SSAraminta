package com.badday.ss;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.badday.ss.api.SSAPI;
import com.badday.ss.blocks.SSBlockAirVent;
import com.badday.ss.blocks.SSBlockCabinet;
import com.badday.ss.blocks.SSBlockGasPipe;
import com.badday.ss.blocks.SSBlockGlassCasing;
import com.badday.ss.blocks.SSBlockMultiFake;
import com.badday.ss.blocks.SSBlockWallCasing;
import com.badday.ss.blocks.SSBlockWallCasingRaw;
import com.badday.ss.blocks.SSFuidTextrures;
import com.badday.ss.blocks.SSTileEntityAirVent;
import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.blocks.SSTileEntityMultiFake;
import com.badday.ss.core.utils.SSSettings;
import com.badday.ss.items.SSItemBlockCabinet;
import com.badday.ss.items.SSItemBlockGasPipe;
import com.badday.ss.items.SSItemHidden;
import com.badday.ss.items.SSItemMetaBlockWallCasing;
import com.badday.ss.items.SSItemMetaBlockWallCasingRaw;
import com.badday.ss.items.SSItemMultitool;
import com.badday.ss.items.SSItemScrewdriver;
import com.badday.ss.items.SSItemWelder;

import cpw.mods.fml.common.registry.GameRegistry;

public class SSConfig {

	// Blocks
	public static Block ssBlockWallCasingRaw; // metal rods
	public static Block ssBlockWallCasingC; // metal rods
	public static Block ssBlockGlassCasing;
	public static Block ssBlockAir;
	public static Block ssBlockGas;
	public static Block ssBlockAirVent;
	public static Block ssBlockCabinet;
	public static Block ssBlockGasPipe;
	public static Block fakeBlock;
	//public static Block ssBlockAirGenerator;
	
	public static Fluid fluidMethaneGas;
	public static Fluid fluidOxygenGas;
	public static Fluid fluidNitrogenGas;
	public static Fluid fluidLiquidMethane;
	public static Fluid fluidLiquidOxygen;
	public static Fluid fluidLiquidNitrogen;
	public static Fluid fluidLiquidArgon;
	public static Fluid fluidAtmosphericGases;
	public static Fluid fluidHelium;
	public static Fluid fluidHydrogen;
	public static Fluid fluidCarbonDioxide;
	

	public static final String[] ssWallCasingRaw_unlocalizedName = { "blockWallCasing1", "blockWallCasing2", "blockWallCasing3", "blockWallCasing4",
			"blockWallCasing5", "blockWallCasing6" };

	public static final String[] ssWallCasingC_unlocalizedName = { "blockWallCasingBlack", "blockWallCasingRed", "blockWallCasingGreen",
			"blockWallCasingBrown", "blockWallCasingBlue", "blockWallCasingPurple", "blockWallCasingCyan", "blockWallCasingLightGray", "blockWallCasingGray",
			"blockWallCasingPink", "blockWallCasingLime", "blockWallCasingYellow", "blockWallCasingLightBlue", "blockWallCasingMagenta",
			"blockWallCasingOrange", "blockWallCasingWhite" };

	public static final String[] ssGasPipe_unlocalizedName = { "blockGasPipeBlack", "blockGasPipeRed", "blockGasPipeGreen",
		"blockGasPipeBrown", "blockGasPipeBlue", "blockGasPipePurple", "blockGasPipeCyan", "blockGasPipeLightGray", "blockGasPipeGray",
		"blockGasPipePink", "blockGasPipeLime", "blockGasPipeYellow", "blockGasPipeLightBlue", "blockGasPipeMagenta",
		"blockGasPipeOrange", "blockGasPipeWhite" };
	
	public static final String[] ssCabinet_unlocalizedName = { "cabinetGray", "cabinetSecure", "cabinetHydroponic", "cabinetElectric", "cabinetMining", "cabinetWhite",
			"cabinetO2", "cabinetMedical", "cabinetFire", "cabinetHazard", "cabinetIndustry" };

	// TODO: Добавить
	public static Block ssBlockAirDetector;
	public static Block ssBlockDoorGate;
	public static Block ssBlockDoor2Gate;
	public static Block ssBlockProtolathe;

	// Items
	public static Item ssScrewDriver;
	public static Item ssWelder;
	public static Item ssMultitool;
	
	public static int ssAirVentBlockArea = 2048;
	/**
	 * max distance for pathfinder check airvent
	 */
	public static int ssPathfinderMaxDistance = 64; 
	
	public static int ssCabinetSize =  36;
	
	/**
	 * Default station temperature in C
	 */
	public static int ssDefaultTemperature = 24; 
	
	public static int cabinetRenderId;
	public static int gasPipeRenderId;

	static Configuration config;

	public static void Load(File file) {
		
		try {
			config = new Configuration(file);
			config.load();
		} catch (Exception var1) {
			System.out.println("[" + SS.MODNAME + "] Error while trying to access item configuration!");
			config = null;
		}
		
		LoadGlobal(file);
		LoadItems(file);
		LoadBlocks(file);
		
		if (config != null) {
			config.save();
		}
		
	}

	private static void LoadGlobal(File file) {
		System.out.println("[" + SS.MODNAME + "] Loading Global config");
		SS.Debug = SSSettings.getBooleanFor(config, "global", "debug", SS.Debug, "Debug mode");
		ssAirVentBlockArea = SSSettings.getIntFor(config, "global", "airVentMaxAreaSize",ssAirVentBlockArea,"Max area in bloks for Air Vent");
		ssPathfinderMaxDistance = SSSettings.getIntFor(config, "global", "PathfinderMaxDistance",ssPathfinderMaxDistance,"Max distance for pathfinder check");
		ssDefaultTemperature = SSSettings.getIntFor(config, "global", "DefaultTemperature",ssDefaultTemperature,"Default station temperature");
		
	}

	public static void LoadItems(File configFile) {
		// ssScrewDriverID = SSSettings.getIntFor(config, "item",
		// "itemScrewDriverID",ssScrewDriverID);
		// ssWelderID = SSSettings.getIntFor(config, "item",
		// "itemWelderID",ssWelderID);
	}

	public static void LoadBlocks(File configFile) {

	}

	public static void RegisterItems() {
		ssScrewDriver = new SSItemScrewdriver();
		ssWelder = new SSItemWelder();
		ssMultitool = new SSItemMultitool();
	}

	public static void RegisterBlocks() {
		System.out.println("[" + SS.MODNAME + "] Registering Blocks");

		// RAW WALL CASING
		ssBlockWallCasingRaw = new SSBlockWallCasingRaw();
		GameRegistry.registerBlock(ssBlockWallCasingRaw, SSItemMetaBlockWallCasingRaw.class, "blockWallCasing1");

		for (int i = 0; i < ssWallCasingRaw_unlocalizedName.length; i++) {
			ItemStack multiBlockStack = new ItemStack(ssBlockWallCasingRaw, 1, i);
		}

		// COMPLITE WALL CASING
		ssBlockWallCasingC = new SSBlockWallCasing();
		GameRegistry.registerBlock(ssBlockWallCasingC, SSItemMetaBlockWallCasing.class, "blockWallCasingBlack");

		for (int i = 0; i < ssWallCasingC_unlocalizedName.length; i++) {
			ItemStack multiBlockStack = new ItemStack(ssBlockWallCasingC, 1, i);
		}

		ssBlockGlassCasing = new SSBlockGlassCasing("blockGlassCasing");
		GameRegistry.registerBlock(ssBlockGlassCasing, "blockGlassCasing");
		
		fakeBlock = new SSBlockMultiFake("fakeBlock");
		GameRegistry.registerBlock(fakeBlock, SSItemHidden.class, fakeBlock.getUnlocalizedName());
		GameRegistry.registerTileEntity(SSTileEntityMultiFake.class, "fakeBlock");
		
		ssBlockCabinet = new SSBlockCabinet("grayCabinet");
		GameRegistry.registerBlock(ssBlockCabinet, SSItemBlockCabinet.class, "grayCabinet");
		GameRegistry.registerTileEntity(SSTileEntityCabinet.class, "grayCabinet");
		
		ssBlockGasPipe = new SSBlockGasPipe("blockGasPipeBlack");
		GameRegistry.registerBlock(ssBlockGasPipe, SSItemBlockGasPipe.class, "blockGasPipeBlack");
		//GameRegistry.registerTileEntity(SSTileEntityGasPipe.class, "blockGasPipeBlack");

        SSAPI.softBlocks.add(ssBlockGas);

		ssBlockAirVent = new SSBlockAirVent("ssBlockAirVent");
		GameRegistry.registerBlock(ssBlockAirVent, "ssBlockAirVent");
		GameRegistry.registerTileEntity(SSTileEntityAirVent.class, "ssBlockAirVent");
	}

	/**
	 * Density - плотность Viscosity - вязкость Gaseous - true - газ
	 */
	public static void RegisterGases() {
		SSFuidTextrures.init();
		FluidRegistry.registerFluid(new Fluid("liquidmethane").setDensity(450).setViscosity(120).setTemperature(109));
		// Data source for liquid methane:
		// http://science.nasa.gov/science-news/science-at-nasa/2005/25feb_titan2/
		FluidRegistry.registerFluid(new Fluid("liquidoxygen").setDensity(1141).setViscosity(140).setTemperature(90));
		FluidRegistry.registerFluid(new Fluid("liquidnitrogen").setDensity(808).setViscosity(130).setTemperature(90));
		FluidRegistry.registerFluid(new Fluid("liquidargon").setDensity(900).setViscosity(100).setTemperature(87));

		FluidRegistry.registerFluid(new Fluid("methane").setDensity(1).setViscosity(11).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("atmosphericgases").setDensity(1).setViscosity(13).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("oxygen").setDensity(1).setViscosity(13).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("nitrogen").setDensity(1).setViscosity(12).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("carbondioxide").setDensity(2).setViscosity(20).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("hydrogen").setDensity(1).setViscosity(1).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("argon").setDensity(1).setViscosity(4).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("helium").setDensity(1).setViscosity(1).setGaseous(true));
		
		fluidMethaneGas = FluidRegistry.getFluid("methane");
		fluidAtmosphericGases = FluidRegistry.getFluid("atmosphericgases");
		fluidLiquidMethane = FluidRegistry.getFluid("liquidmethane");
		fluidLiquidOxygen = FluidRegistry.getFluid("liquidoxygen");
		fluidOxygenGas = FluidRegistry.getFluid("oxygen");
		fluidLiquidNitrogen = FluidRegistry.getFluid("liquidnitrogen");
		fluidLiquidArgon = FluidRegistry.getFluid("liquidargon");
		fluidNitrogenGas = FluidRegistry.getFluid("nitrogen");
		fluidHelium = FluidRegistry.getFluid("helium");
		fluidHydrogen = FluidRegistry.getFluid("hydrogen");
		fluidCarbonDioxide = FluidRegistry.getFluid("carbondioxide");
	}

}
