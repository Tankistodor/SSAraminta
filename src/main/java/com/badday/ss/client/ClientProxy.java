package com.badday.ss.client;

import com.badday.ss.CommonProxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

	public void load() {
		super.load();

		// RenderingRegistry.registerEntityRenderingHandler(SSFakeEntityPlayer.class,new
		// SSRenderPlayer(new ModelPlayerMale(0.0f),new
		// ModelPlayerMale(1.0f),new ModelPlayerMale(0.5f)));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		/*int renderIdBreathableAir = RenderingRegistry
				.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new SSBlockAirNormalRender(
				renderIdBreathableAir));*/

	}
}
