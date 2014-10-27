package com.badday.ss.client;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.badday.ss.CommonProxy;
import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.render.SSCabinetRender;
import com.badday.ss.render.SSRenderHandlers;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	public static final Map<Class<? extends TileEntity>, SSRenderHandlers> renderingHandlers =
			new HashMap<Class<? extends TileEntity>, SSRenderHandlers>();

	public void load() {
		super.load();

		// RenderingRegistry.registerEntityRenderingHandler(SSFakeEntityPlayer.class,new
		// SSRenderPlayer(new ModelPlayerMale(0.0f),new
		// ModelPlayerMale(1.0f),new ModelPlayerMale(0.5f)));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		/*
		 * int renderIdBreathableAir = RenderingRegistry
		 * .getNextAvailableRenderId();
		 * RenderingRegistry.registerBlockHandler(new SSBlockAirNormalRender(
		 * renderIdBreathableAir));
		 */
		//SSCabinetRender.loadTextures();
		SSConfig.cabinetRenderId = registerTileEntityRenderer(SSTileEntityCabinet.class, new SSCabinetRender());
		if (SS.Debug) System.out.println("[" + SS.MODNAME + "] render handler " + SSConfig.cabinetRenderId);

	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

	public static int registerTileEntityRenderer(Class<? extends TileEntity> tileEntityClass, TileEntitySpecialRenderer renderer, boolean render3dInInventory,
			float rotation, float scale, float yOffset) {
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, renderer);
		SSRenderHandlers renderingHandler = new SSRenderHandlers(tileEntityClass, renderer, render3dInInventory, rotation, scale,
				yOffset);
		RenderingRegistry.registerBlockHandler(renderingHandler);
		renderingHandlers.put(tileEntityClass, renderingHandler);
		return renderingHandler.getRenderId();
	}

	public static int registerTileEntityRenderer(Class<? extends TileEntity> tileEntityClass, TileEntitySpecialRenderer renderer) {
		return registerTileEntityRenderer(tileEntityClass, renderer, true, 90, 1, 0);
	}

}
