package com.badday.ss;

import java.io.File;

import com.badday.ss.api.SSAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.badday.ss.blocks.SSBlockAirVent;
import com.badday.ss.blocks.SSBlockGas;
import com.badday.ss.blocks.SSBlockGlassCasing;
import com.badday.ss.blocks.SSBlockWallCasing;
import com.badday.ss.blocks.SSBlockWallCasingRaw;
import com.badday.ss.blocks.SSFuidTextrures;
import com.badday.ss.blocks.SSTileEntityAirVent;
import com.badday.ss.blocks.SSTileEntityGasBlock;
import com.badday.ss.core.utils.SSSettings;
import com.badday.ss.items.SSItemBlock;
import com.badday.ss.items.SSItemMetaBlockWallCasing;
import com.badday.ss.items.SSItemMetaBlockWallCasingRaw;
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
	//public static Block ssBlockAirGenerator;
	

	public static Fluid fluidMethaneGas;
	public static Fluid fluidOxygenGas;
	public static Fluid fluidNitrogenGas;
	public static Fluid fluidLiquidMethane;
	public static Fluid fluidLiquidOxygen;
	public static Fluid fluidLiquidNitrogen;
	public static Fluid fluidLiquidArgon;
	public static Fluid fluidAtmosphericGases;

	public static final String[] ssWallCasingRaw_unlocalizedName = { "blockWallCasing1", "blockWallCasing2", "blockWallCasing3", "blockWallCasing4",
			"blockWallCasing5", "blockWallCasing6" };

	public static final String[] ssWallCasingC_unlocalizedName = { "blockWallCasingBlack", "blockWallCasingRed", "blockWallCasingGreen",
			"blockWallCasingBrown", "blockWallCasingBlue", "blockWallCasingPurple", "blockWallCasingCyan", "blockWallCasingLightGray", "blockWallCasingGray",
			"blockWallCasingPink", "blockWallCasingLime", "blockWallCasingYellow", "blockWallCasingLightBlue", "blockWallCasingMagenta",
			"blockWallCasingOrange", "blockWallCasingWhite" };

	public static final String[] ssWallCasingC_name = { "Wall Casing Black", "Wall Casing Red", "Wall Casing Green", "Wall Casing Brown", "Wall Casing Blue",
			"Wall Casing Purple", "Wall Casing Cyan", "Wall Casing LightGray", "Wall Casing Gray", "Wall Casing Pink", "Wall Casing Lime",
			"Wall Casing Yellow", "Wall Casing LightBlue", "Wall Casing Magenta", "Wall Casing Orange", "Wall Casing White" };

	// TODO: Добавить

	
	public static Block ssBlockAirDetector;
	public static Block ssBlockDoorGate;
	public static Block ssBlockDoor2Gate;
	public static Block ssBlockProtolathe;

	// Items
	public static Item ssScrewDriver;
	public static Item ssWelder;
	
	
	public static int ssAirVentBlockArea = 2048;

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
		
		/*
		// AIR GENERATOR
		ssBlockAirGenerator = (new BlockAirGenerator(0, Material.rock)).setHardness(0.5F).setStepSound(Block.soundTypeMetal);
		GameRegistry.registerBlock(ssBlockAirGenerator, "ssBlockAirGenerator");
		GameRegistry.registerTileEntity(SSTileEntityAirGenerator.class, "ssBlockAirGenerator");

		// AIR BLOCK
		ssBlockAir = new SSBlockAirNormal("airBlock"); // normalAir
		ssBlockGas = new SSBlockAirNormal("gasBlock"); // normalAir

		GameRegistry.registerBlock(ssBlockAir, SSItemBlock.class, ssBlockAir.getUnlocalizedName());
		GameRegistry.registerBlock(ssBlockGas, SSItemBlock.class, ssBlockGas.getUnlocalizedName());*/
		
		
		ssBlockGas = new SSBlockGas("ssBlockGas"); // gasBlock
		GameRegistry.registerBlock(ssBlockGas, SSItemBlock.class, ssBlockGas.getUnlocalizedName());
		GameRegistry.registerTileEntity(SSTileEntityGasBlock.class, "ssBlockGas");

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
		FluidRegistry.registerFluid(new Fluid("methane").setDensity(1).setViscosity(11).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("atmosphericgases").setDensity(1).setViscosity(13).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("liquidmethane").setDensity(450).setViscosity(120).setTemperature(109));
		// Data source for liquid methane:
		// http://science.nasa.gov/science-news/science-at-nasa/2005/25feb_titan2/
		FluidRegistry.registerFluid(new Fluid("liquidoxygen").setDensity(1141).setViscosity(140).setTemperature(90));
		FluidRegistry.registerFluid(new Fluid("oxygen").setDensity(1).setViscosity(13).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("liquidnitrogen").setDensity(808).setViscosity(130).setTemperature(90));
		FluidRegistry.registerFluid(new Fluid("nitrogen").setDensity(1).setViscosity(12).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("carbondioxide").setDensity(2).setViscosity(20).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("hydrogen").setDensity(1).setViscosity(1).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("argon").setDensity(1).setViscosity(4).setGaseous(true));
		FluidRegistry.registerFluid(new Fluid("liquidargon").setDensity(900).setViscosity(100).setTemperature(87));
		FluidRegistry.registerFluid(new Fluid("helium").setDensity(1).setViscosity(1).setGaseous(true));
		fluidMethaneGas = FluidRegistry.getFluid("methane");
		fluidAtmosphericGases = FluidRegistry.getFluid("atmosphericgases");
		fluidLiquidMethane = FluidRegistry.getFluid("liquidmethane");
		fluidLiquidOxygen = FluidRegistry.getFluid("liquidoxygen");
		fluidOxygenGas = FluidRegistry.getFluid("oxygen");
		fluidLiquidNitrogen = FluidRegistry.getFluid("liquidnitrogen");
		fluidLiquidArgon = FluidRegistry.getFluid("liquidargon");
		fluidNitrogenGas = FluidRegistry.getFluid("nitrogen");
	}

}
