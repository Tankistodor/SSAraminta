/**
 * 
 */
package com.badday.ss.events;

import java.util.EnumSet;

import com.badday.ss.world.space.SpaceCloudRenderer;
import com.badday.ss.world.space.SpaceProvider;
import com.badday.ss.world.space.SpaceSkyRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * @author KolesnikovAK
 *
 */
public class SSTickHandlerClient {
	
	
	private static long tickCount;

	/* (non-Javadoc)
	 * @see cpw.mods.fml.common.ITickHandler#tickStart(java.util.EnumSet, java.lang.Object[])
	 */
	//Called when the client ticks. 
	 @SubscribeEvent
	 public void onClientTick(TickEvent.ClientTickEvent event) {
			final Minecraft minecraft = FMLClientHandler.instance().getClient();
			final WorldClient world = minecraft.theWorld;
			
			if (SSTickHandlerClient.tickCount >= Long.MAX_VALUE)
			{
				SSTickHandlerClient.tickCount = 0;
			}
			
			SSTickHandlerClient.tickCount++;
			
			if (SSTickHandlerClient.tickCount % 20 == 0)
			{
			
				
				if (world != null && world.provider instanceof SpaceProvider)
				{
					if (world.provider.getSkyRenderer() == null)
					{
						world.provider.setSkyRenderer(new SpaceSkyRenderer());
					}

					if (world.provider.getCloudRenderer() == null)
					{
						world.provider.setCloudRenderer(new SpaceCloudRenderer());
					}
				}

				
				
			}
	}
}
