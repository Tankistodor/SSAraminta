package com.badday.ss.events;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.badday.ss.SS;
import com.badday.ss.core.atmos.FindNearestVentJob;
import com.badday.ss.core.utils.AirVentNet;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.entity.player.SSPlayerData;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

/**
 * Обработчик событий в мире Space
 * @author Cr0s
 */
public class SpaceEventHandler
{
	private ConcurrentHashMap<UUID, SSPlayerData> playerStatsMap = new ConcurrentHashMap<UUID, SSPlayerData>();
	
	//private HashMap<String, Integer> vacuumPlayers;
	//private HashMap<String, Integer> cloakPlayersTimers;
	private long lastTimer = 0;
	
	private FindNearestVentJob job;

	private final int CLOAK_CHECK_TIMEOUT_SEC = 5;

  private static final String PREFIX = EnumChatFormatting.GREEN + "[SSAraminta] " + EnumChatFormatting.RESET;

	public SpaceEventHandler() {
		//vacuumPlayers = new HashMap<String, Integer>();
		//cloakPlayersTimers = new HashMap<String, Integer>();
		this.lastTimer = 0;
	}

  @SubscribeEvent
  public void onDrawDebugText(RenderGameOverlayEvent.Text event) {
    if(Minecraft.getMinecraft().gameSettings.showDebugInfo) {
      event.left.add(null);
      event.left.add(PREFIX + "AirVentCount: " + AirVentNet.airvents.size());
    }
  }

	@SubscribeEvent
	public void addToVentNet(AirNetAddEvent event) {
		AirVentNet.registerAirVent(event.coords);
	}

	@SubscribeEvent
	public void removeFromVentNet(AirNetRemoveEvent event) {
		AirVentNet.removeAirVent(event.coords);
	}

	
	@SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            this.onPlayerLogin((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerLoggedOutEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            this.onPlayerLogout((EntityPlayerMP) event.player);
        }
    }
    
    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            this.onPlayerRespawn((EntityPlayerMP) event.player);
        }
    }
    
    
    private void onPlayerLogin(EntityPlayerMP player)
    {
    	SSPlayerData oldData = this.playerStatsMap.remove(player.getPersistentID());
        if (oldData != null)
        {
            oldData.saveNBTData(player.getEntityData());
        }

        SSPlayerData stats = SSPlayerData.get(player);

        //GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_GET_CELESTIAL_BODY_LIST, new Object[] { }), player);
    }

    private void onPlayerLogout(EntityPlayerMP player)
    {

    }
    
    private void onPlayerRespawn(EntityPlayerMP player)
    {
    	SSPlayerData oldData = this.playerStatsMap.remove(player.getPersistentID());
    	SSPlayerData stats = SSPlayerData.get(player);

        if (oldData != null)
        {
            stats.copyFrom(oldData, false);
        }

       // stats.player = new WeakReference<EntityPlayerMP>(player);
    }
	
     
    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayerMP && SSPlayerData.get((EntityPlayerMP) event.entity) == null)
        {
            SSPlayerData.register((EntityPlayerMP) event.entity);
        }
    }
	
	
	@SubscribeEvent
	public void livingUpdate(LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.entityLiving;
		
		// Instant kill if entity exceeds world's limit
		if (Math.abs(MathHelper.floor_double(entity.posX)) > SS.WORLD_LIMIT_BLOCKS || Math.abs(MathHelper.floor_double(entity.posZ)) > SS.WORLD_LIMIT_BLOCKS)
		{
			if (entity instanceof EntityPlayerMP)
			{
				if (((EntityPlayerMP)entity).capabilities.isCreativeMode)
				{
					return;
				}
			}

			entity.attackEntityFrom(DamageSource.outOfWorld, 9000);
			return;
		}
		
		//Ok. get pressure
		int distance = 0;
		if (entity.worldObj.provider.dimensionId == SS.instance.spaceDimID && !entity.worldObj.isRemote) {
			
			this.lastTimer++;
			
			if (this.lastTimer % 60 == 0) { // 160 - for 1 player

				long time1 = System.nanoTime();
				
				//int distance = FindNearestVent.getDistance(entity.worldObj, new BlockVec3(entity));
				
				if (job == null) {
					job = new FindNearestVentJob(entity.worldObj, new BlockVec3(entity));
					job.run();
				} else {
					if (!job.isAlive()) {
						if (entity instanceof EntityPlayerMP)
						{
							distance = job.getDistance();
							SSPlayerData data = SSPlayerData.get((EntityPlayerMP) entity);
							data.playerNearestVent = job.getNearestVent();
						}
					}
					
					if (!job.getValidAtmos()) {
						entity.attackEntityFrom(DamageSource.drown, 1);
					}
				}
				
				if (job.isDone()) {
					job = new FindNearestVentJob(entity.worldObj, new BlockVec3(entity));
					job.run();
				}
				
				if(SS.Debug) {
					long time2 = System.nanoTime();
					System.out.println("[" + SS.MODNAME + "] Distance is: " + distance + " vent: " + job.getNearestVent() + " time: " + (time2 - time1) / 1000000.0D + "ms");
				}
				
				
			}
		}
		
		/*if (entity.worldObj.provider.dimensionId == SS.instance.spaceDimID) {
			this.lastTimer++;
			if (this.lastTimer > 40) {
				this.lastTimer = 0;
				long time1 = System.nanoTime();
				Pathfinding p = new Pathfinding(entity.worldObj, new BlockVec3(entity), new BlockVec3(0,0,0));
				p.iterate(1024);
				long time2 = System.nanoTime();
				if (SS.Debug) {
					System.out.println("[" + SS.MODNAME + "] Pathfinding work complite. Result: "+p.isDone());
					System.out.println("[" + SS.MODNAME + "] Pathfinding work time: "+(time2 - time1) / 1000000.0D + "ms");
				}
			}
		}*/

		// If player in vaccum, check and start consuming air cells
		/*
		if (entity.worldObj.provider.dimensionId == SS.instance.spaceDimID)
		{
			boolean inVacuum = isEntityInVacuum(entity);

			// Damage entity if in vacuum without protection
			if (inVacuum)
			{
				if (entity instanceof EntityPlayerMP)
				{

					if (((EntityPlayerMP)entity).getCurrentArmor(3) != null && SS.SpaceHelmets.contains(((EntityPlayerMP)entity).getCurrentArmor(3)))
					{
						Integer airValue = vacuumPlayers.get(((EntityPlayerMP)entity).getDisplayName());

						if (airValue == null)
						{
							vacuumPlayers.put(((EntityPlayerMP)entity).getDisplayName(), 300);
							airValue = 300;
						}

						if (airValue <= 0)
						{
							if (consumeO2(((EntityPlayerMP)entity).inventory.mainInventory))
							{
								setPlayerAirValue(entity, 300);
							}
							else
							{
								setPlayerAirValue(entity, 0);
								entity.attackEntityFrom(DamageSource.drown, 1);
							}
						}
						else
						{
							setPlayerAirValue(entity, airValue - 1);
						}
					}
					else
					{
						entity.attackEntityFrom(DamageSource.drown, 1);
					}

					// If player falling down, teleport on earth
					if (entity.posY < -10.0D)
					{
						((EntityPlayerMP)entity).mcServer.getConfigurationManager().transferPlayerToDimension(((EntityPlayerMP) entity), 0, new SpaceTeleporter(DimensionManager.getWorld(SS.instance.spaceDimID), 0, MathHelper.floor_double(entity.posX), 250, MathHelper.floor_double(entity.posZ)));
						((EntityPlayerMP)entity).setFire(30);
						((EntityPlayerMP)entity).setPositionAndUpdate(entity.posX, 250D, entity.posZ);
					}
				}
				else
				{
					entity.attackEntityFrom(DamageSource.drown, 1);
				}
			}
		}*/
		
		
	}

}
