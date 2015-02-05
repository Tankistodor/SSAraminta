package com.badday.ss;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import com.badday.ss.agriculture.Agriculture;
import com.badday.ss.api.SSAPI;
import com.badday.ss.core.utils.commands.SSCommandIslandTp;
import com.badday.ss.core.utils.commands.SSCommandPathfinder;
import com.badday.ss.core.utils.commands.SSCommandSetRole;
import com.badday.ss.core.utils.commands.SSCommandSpaceTp;
import com.badday.ss.events.AntiFallDamage;
import com.badday.ss.events.GuiHandler;
import com.badday.ss.events.SSPacketHandler;
import com.badday.ss.events.SSTickHandlerClient;
import com.badday.ss.events.SSTickHandlerServer;
import com.badday.ss.events.SoundHandler;
import com.badday.ss.events.SpaceEventHandler;
import com.badday.ss.world.island.BiomeIsland;
import com.badday.ss.world.island.IslandProvider;
import com.badday.ss.world.space.BiomeSpace;
import com.badday.ss.world.space.SpaceProvider;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "SS", name = "SS", version = "2.201", dependencies = "required-after:IC2; after:gregtech_addon; after:AppliedEnergistics; after:AdvancedSolarPanel; after:AtomicScience; after:ICBM|Explosion; after:MFFS; after:GraviSuite")

public class SS {
	
	@SidedProxy(clientSide="com.badday.ss.client.ClientProxy", serverSide="com.badday.ss.CommonProxy")
	public static CommonProxy proxy;

	public final static int WORLD_LIMIT_BLOCKS = 10000;
	public static Set<Integer> SpaceHelmets;
	public static Item[] IC2_Air;
	
	private static final String MODID = "SS";
	public static final String MODNAME = MODID;


	
	public static String ASSET_PREFIX = "ss:";
	public static CreativeTabs ssTab;
	
	public static boolean Debug = true;
	
	public static BiomeGenBase spaceBiome;
	private int spaceProviderID;
	public int spaceDimID;	
	
	public static BiomeGenBase islandBiome;
	public static int islandProviderID;
	public int islandDimID;	
	

	@Instance(SS.MODID)
	public static SS instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		SSConfig.Load(new File(event.getModConfigurationDirectory(), SS.MODNAME+"/"+SS.MODNAME+".cfg"));
		
		if (FMLCommonHandler.instance().getSide().isClient()) {
			System.out
			.println("["+SS.MODNAME+"] Registering sounds event handler...");
			MinecraftForge.EVENT_BUS.register(new SoundHandler());
		}
		
		SSPacketHandler.INSTANCE.ordinal();
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		
		SS.ssTab = new SSCreativeTab("SS");
		
		SSConfig.RegisterItems();
		SSConfig.RegisterBlocks();
		SSConfig.RegisterGases();
	
		// DIMENSIONS
		
		spaceBiome = (new BiomeSpace(23)).setColor(0).setDisableRain().setBiomeName("Space");
		this.spaceProviderID = 14;
		DimensionManager.registerProviderType(spaceProviderID, SpaceProvider.class, true);
		this.spaceDimID = DimensionManager.getNextFreeDimId();
		DimensionManager.registerDimension(spaceDimID,this.spaceProviderID);

		
		islandBiome = (new BiomeIsland(24)).setColor(0).setDisableRain().setBiomeName("Island Sky");
		this.islandProviderID = 15;
		DimensionManager.registerProviderType(islandProviderID, IslandProvider.class, true);
		this.islandDimID = DimensionManager.getNextFreeDimId();
		DimensionManager.registerDimension(islandDimID,this.islandProviderID);
		
		//DimensionManager.shouldLoadSpawn(spaceDimID);
		
		initData();
		
		MinecraftForge.EVENT_BUS.register(new AntiFallDamage());
		
		MinecraftForge.EVENT_BUS.register(new SpaceEventHandler());

		if (FMLCommonHandler.instance().getSide().isClient()) {
			
			/*int renderIdBreathableAir = RenderingRegistry
					.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new SSBlockAirNormalRender(
					renderIdBreathableAir));*/
			
		}
		proxy.init(event);
		
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(SSTickHandlerServer.instance);
		FMLCommonHandler.instance().bus().register(SSTickHandlerServer.instance);

		/** Регистрируем рендеринг космоса */
		if (FMLCommonHandler.instance().getSide().isClient()) {
			SSTickHandlerClient tickHandlerClient = new SSTickHandlerClient();
			FMLCommonHandler.instance().bus().register(tickHandlerClient);
			MinecraftForge.EVENT_BUS.register(tickHandlerClient);
		}

		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new GuiHandler());
	}
	
	@EventHandler
	public void serverInit(FMLServerStartedEvent event)
	{
		
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new SSCommandSpaceTp());
		event.registerServerCommand(new SSCommandIslandTp());
		event.registerServerCommand(new SSCommandPathfinder());
		event.registerServerCommand(new SSCommandSetRole());
		
		if (Loader.isModLoaded("PlayerAPI"))
        {
            //ClientPlayerAPI.register(Constants.MOD_ID_CORE, GCPlayerBaseSP.class);
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        }
		
	}
	
	public static int getEntityId() {
		return EntityRegistry.findGlobalUniqueEntityId();
	}
	
	
	public static void LogMSG(String msg) {
		System.out.println("["+SS.MODNAME+"] " + msg);
	}
	
	public static boolean isServer() {
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
	
	public void initData(){

    SSAPI.softBlocks.add(Blocks.snow);
    SSAPI.softBlocks.add(Blocks.vine);
    SSAPI.softBlocks.add(Blocks.fire);
    SSAPI.softBlocks.add(Blocks.air);


		SpaceHelmets = new HashSet<Integer>();
		Item ss = GameRegistry.findItem("IC2", "itemArmorHazmatHelmet");
		SpaceHelmets.add(Item.getIdFromItem(ss));

		////SpaceHelmets.add(Item.getIdFromItem(OreDictionary.getOres("itemArmorHazmatHelmet").get(0).getItem()));
		//SpaceHelmets.add(Item.getItem("quantumHelmet").itemID);
		////SpaceHelmets.add(Item.getIdFromItem(OreDictionary.getOres("quantumHelmet").get(0).getItem()));
		
		//IC2_Air = new int[] {Items.getItem("airCell").itemID, Items.getItem("airCell").getItemDamage()};
		ss = GameRegistry.findItem("IC2", "itemArmorHazmatHelmet");
		IC2_Air = new Item[] {GameRegistry.findItem("IC2", "itemCellAir")};

    Agriculture.init();
	}
}
