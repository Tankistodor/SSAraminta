package com.badday.ss;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.badday.ss.agriculture.util.AgricultureConfig;
import com.badday.ss.blocks.SSBlockAirVent;
import com.badday.ss.blocks.SSBlockAirlockDoor;
import com.badday.ss.blocks.SSBlockAirlockDoorAir;
import com.badday.ss.blocks.SSBlockAirlockFrame;
import com.badday.ss.blocks.SSBlockAirlockFrameController;
import com.badday.ss.blocks.SSBlockCabinet;
import com.badday.ss.blocks.SSBlockCardEncoder;
import com.badday.ss.blocks.SSBlockGasMixer;
import com.badday.ss.blocks.SSBlockGasPipe;
import com.badday.ss.blocks.SSBlockGasPipeCasing;
import com.badday.ss.blocks.SSBlockGlassCasing;
import com.badday.ss.blocks.SSBlockIC2CableCasing;
import com.badday.ss.blocks.SSBlockMultiFake;
import com.badday.ss.blocks.SSBlockScrubber;
import com.badday.ss.blocks.SSBlockWallCasing;
import com.badday.ss.blocks.SSBlockWallCasingRaw;
import com.badday.ss.blocks.SSFuidTextrures;
import com.badday.ss.blocks.SSTileEntityAirVent;
import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.blocks.SSTileEntityGasMixer;
import com.badday.ss.blocks.SSTileEntityMultiFake;
import com.badday.ss.blocks.SSTileEntityScrubber;
import com.badday.ss.core.utils.SSSettings;
import com.badday.ss.items.SSItemBlockCabinet;
import com.badday.ss.items.SSItemBlockGasPipe;
import com.badday.ss.items.SSItemBlockGasPipeCasing;
import com.badday.ss.items.SSItemBlockIC2CableCasing;
import com.badday.ss.items.SSItemCards;
import com.badday.ss.items.SSItemDoorDisassembly;
import com.badday.ss.items.SSItemGasAnalyzer;
import com.badday.ss.items.SSItemHidden;
import com.badday.ss.items.SSItemMetaBlockWallCasing;
import com.badday.ss.items.SSItemMetaBlockWallCasingRaw;
import com.badday.ss.items.SSItemMultitool;
import com.badday.ss.items.SSItemScrewdriver;
import com.badday.ss.items.SSItemWelder;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class SSConfig {

	// Blocks
	public static Block ssBlockWallCasingRaw; // metal rods
	public static Block ssBlockWallCasingC; // metal rods
	public static Block ssBlockGlassCasing;
	public static Block ssBlockAir;
	public static Block ssBlockAirVent;
	public static Block ssBlockCabinet;
	public static Block ssBlockGasPipe;
	public static Block ssBlockGasPipeCasing;
	public static Block ssBlockGasMixer;
	public static Block ssBlockScrubber;
	public static Block fakeBlock;
	
	public static Block ssBlockIC2CableCasing;
	
	public static Block ssBlockAirLockDoor;
	public static Block ssBlockAirLockFrameController;
	public static Block ssBlockAirLockFrame;
	public static Block ssBlockCardEncoder;
	
	
	// Items
	public static Item ssScrewDriver;
	public static Item ssWelder;
	public static Item ssMultitool;
	public static Item ssGasAnalyser;
	public static Item ssDoorDisassembly;
	public static Item ssCardEmag;
		
	
	//	
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

	public static final String[] ssGasPipeCasing_unlocalizedName = { "blockGasPipeCasingBlack", "blockGasPipeCasingRed", "blockGasPipeCasingGreen",
		"blockGasPipeCasingBrown", "blockGasPipeCasingBlue", "blockGasPipeCasingPurple", "blockGasPipeCasingCyan", "blockGasPipeCasingLightGray", "blockGasPipeCasingGray",
		"blockGasPipeCasingPink", "blockGasPipeCasingLime", "blockGasPipeCasingYellow", "blockGasPipeCasingLightBlue", "blockGasPipeCasingMagenta",
		"blockGasPipeCasingOrange", "blockGasPipeCasingWhite" };
	
	public static final String[] ssIC2CableCasing_unlocalizedName = { "blockIC2CableCasingBlack", "blockIC2CableCasingRed", "blockIC2CableCasingGreen",
		"blockIC2CableCasingBrown", "blockIC2CableCasingBlue", "blockIC2CableCasingPurple", "blockIC2CableCasingCyan", "blockIC2CableCasingLightGray", "blockIC2CableCasingGray",
		"blockIC2CableCasingPink", "blockIC2CableCasingLime", "blockIC2CableCasingYellow", "blockIC2CableCasingLightBlue", "blockIC2CableCasingMagenta",
		"blockIC2CableCasingOrange", "blockIC2CableCasingWhite" };
	
	public static final String[] ssCabinet_unlocalizedName = { "cabinetGray", "cabinetSecure", "cabinetHydroponic", "cabinetElectric", "cabinetMining", "cabinetWhite",
			"cabinetO2", "cabinetMedical", "cabinetFire", "cabinetHazard", "cabinetIndustry" };

	// TODO: Добавить
	public static Block ssBlockAirDetector;
	
	public static Block ssBlockDoor2Gate;
	public static Block ssBlockProtolathe;

	
	
	public static int ssAirVentBlockArea = 2048;
	/**
	 * max distance for pathfinder check airvent
	 */
	public static int ssPathfinderMaxDistance = 32; 
	
	public static int ssBayCasingResistance = 330;
	
	public static int ssCabinetSize =  36;

	/**
	 * Render planet 0 - none
	 */
	public static int ssRenderPlanet = 1;
	
	public static boolean ssEnableSpace= true;
	public static boolean ssEnableIsland = true;
	
	/**
	 * Default station temperature in C
	 */
	public static int ssDefaultTemperature = 24; 
	public static float ssGasPressureConst = 10.0f;
	public static float ssGasUsagesInMM = 1.0f; // 1.0f = 1000 miliBuckets per 100 block; 
	
	public static int cabinetRenderId;
	public static int gasPipeRenderId;

	static Configuration config;
	
	
	public static boolean cachedIC2Loaded = false;
	public static boolean cachedIC2LoadedValue = false;

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

    LoadAgriculture();
		
		if (config != null) {
			config.save();
		}
		
	}

  private static void LoadAgriculture() {
    AgricultureConfig.ChanterelleMeal = SSSettings.getIntFor(config, "agriculture", "ChanterelleMeal", 1);
    AgricultureConfig.DestroyingAngelMeal = SSSettings.getIntFor(config, "agriculture", "DestroyingAngelMeal", 1);
    AgricultureConfig.FlyAmanitaMeal = SSSettings.getIntFor(config, "agriculture", "FlyAmanitaMeal", 1);
    AgricultureConfig.GlowshroomMeal = SSSettings.getIntFor(config, "agriculture", "GlowshroomMeal", 1);
    AgricultureConfig.LibertyCapMeal = SSSettings.getIntFor(config, "agriculture", "LibertyCapMeal", 1);
    AgricultureConfig.PlumpHelmetMeal = SSSettings.getIntFor(config, "agriculture", "PlumpHelmetMeal", 1);

    AgricultureConfig.ChanterelleSaturation = SSSettings.getFloatFor(config, "agriculture", "ChanterelleSaturation", 0.5F);
    AgricultureConfig.DestroyingAngelSaturation = SSSettings.getFloatFor(config, "agriculture", "DestroyingAngelSaturation", 0.5F);
    AgricultureConfig.FlyAmanitaSaturation = SSSettings.getFloatFor(config, "agriculture", "FlyAmanitaSaturation", 0.5F);
    AgricultureConfig.GlowshroomSaturation = SSSettings.getFloatFor(config, "agriculture", "GlowshroomSaturation", 0.5F);
    AgricultureConfig.LibertyCapSaturation = SSSettings.getFloatFor(config, "agriculture", "LibertyCapSaturation", 0.5F);
    AgricultureConfig.PlumpHelmetSaturation = SSSettings.getFloatFor(config, "agriculture", "PlumpHelmetSaturation", 0.5F);

    AgricultureConfig.GlowshroomsToGlowstone = SSSettings.getIntFor(config, "agriculture_recipes", "GlowshroomsToGlowstone", 5);

  }

  private static void LoadGlobal(File file) {
		System.out.println("[" + SS.MODNAME + "] Loading Global config");
		SS.Debug = SSSettings.getBooleanFor(config, "global", "debug", SS.Debug, "Debug mode");
		ssAirVentBlockArea = SSSettings.getIntFor(config, "global", "airVentMaxAreaSize",ssAirVentBlockArea,"Max area in bloks for Air Vent");
		ssPathfinderMaxDistance = SSSettings.getIntFor(config, "global", "PathfinderMaxDistance",ssPathfinderMaxDistance,"Max distance for pathfinder check");
		ssDefaultTemperature = SSSettings.getIntFor(config, "global", "DefaultTemperature",ssDefaultTemperature,"Default station temperature");
		ssBayCasingResistance = SSSettings.getIntFor(config, "global", "WallCasingResistance",ssBayCasingResistance,"Default explosion resistance block (330)");

		ssEnableSpace = SSSettings.getBooleanFor(config, "global", "EnableSpace",ssEnableSpace,"Enable Space Dimension");
		ssEnableIsland = SSSettings.getBooleanFor(config, "global", "EnableIsland",ssEnableIsland,"Enable Island Dimension");
		
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
		ssGasAnalyser = new SSItemGasAnalyzer("gasAnalyser");
		ssDoorDisassembly = new SSItemDoorDisassembly();
		
		ssCardEmag = new SSItemCards("cards");
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
		
		ssBlockGasPipe = new SSBlockGasPipe("blockGasPipe");
		GameRegistry.registerBlock(ssBlockGasPipe, SSItemBlockGasPipe.class, "blockGasPipe");
		ssBlockGasPipeCasing = new SSBlockGasPipeCasing("blockGasPipeCasing");
		GameRegistry.registerBlock(ssBlockGasPipeCasing, SSItemBlockGasPipeCasing.class, "blockGasPipeCasing");
		ssBlockIC2CableCasing = new SSBlockIC2CableCasing("blockIC2CableCasing");
		GameRegistry.registerBlock(ssBlockIC2CableCasing,SSItemBlockIC2CableCasing.class,"blockIC2CableCasing");

		ssBlockAirVent = new SSBlockAirVent("ssBlockAirVent");
		GameRegistry.registerBlock(ssBlockAirVent, "ssBlockAirVent");
		GameRegistry.registerTileEntity(SSTileEntityAirVent.class, "ssBlockAirVent");
		
		ssBlockGasMixer = new SSBlockGasMixer("ssBlockGasMixer");
		GameRegistry.registerBlock(ssBlockGasMixer, "ssBlockGasMixer");
		GameRegistry.registerTileEntity(SSTileEntityGasMixer.class, "ssBlockGasMixer");
		
		ssBlockScrubber = new SSBlockScrubber("ssBlockScrubber");
		GameRegistry.registerBlock(ssBlockScrubber, "ssBlockScrubber");
		GameRegistry.registerTileEntity(SSTileEntityScrubber.class, "ssBlockScrubber");
		
		//ssBlockBayDoor = new SSBlockBayDoor("bayDoor",true);
		//GameRegistry.registerBlock(ssBlockBayDoor, "bayDoor");
		
		ssBlockAirLockDoor = new SSBlockAirlockDoor("bayDoor2");
		GameRegistry.registerBlock(ssBlockAirLockDoor, "bayDoor2");
		
		ssBlockAir = new SSBlockAirlockDoorAir("blockAir");
		GameRegistry.registerBlock(ssBlockAir, "blockAir");
		
		ssBlockAirLockFrame = new SSBlockAirlockFrame("blockAirLockFrame");
		GameRegistry.registerBlock(ssBlockAirLockFrame, "blockAirLockFrame");
		
		ssBlockAirLockFrameController = new SSBlockAirlockFrameController("blockAirLockFrameController");
		GameRegistry.registerBlock(ssBlockAirLockFrameController, "blockAirLockFrameController");
		
		ssBlockCardEncoder = new SSBlockCardEncoder("blockCardEncoder");
		GameRegistry.registerBlock(ssBlockCardEncoder, "blockCardEncoder");
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

	
	/**
     * Checks using the FML loader too see if IC2 is loaded
     */
    public static boolean isIndustrialCraft2Loaded()
    {
        
		if (!cachedIC2Loaded)
        {
            cachedIC2Loaded = true;
            cachedIC2LoadedValue = Loader.isModLoaded("IC2");
        }

        return cachedIC2LoadedValue;
    }
}
