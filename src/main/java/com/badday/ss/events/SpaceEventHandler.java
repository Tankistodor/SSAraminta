package com.badday.ss.events;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.core.atmos.FindNearestVentJob;
import com.badday.ss.core.atmos.GasUtils;
import com.badday.ss.core.utils.AirVentNet;
import com.badday.ss.core.utils.BlockVec3;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Обработчик событий в мире Space
 * @author Cr0s
 */
public class SpaceEventHandler
{
	private HashMap<String, Integer> vacuumPlayers;
	private HashMap<String, Integer> cloakPlayersTimers;
	private long lastTimer = 0;
	
	private FindNearestVentJob job;

	private final int CLOAK_CHECK_TIMEOUT_SEC = 5;

  private static final String PREFIX = EnumChatFormatting.GREEN + "[SSAraminta] " + EnumChatFormatting.RESET;

	public SpaceEventHandler() {
		vacuumPlayers = new HashMap<String, Integer>();
		cloakPlayersTimers = new HashMap<String, Integer>();
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
		
		/*if (entity instanceof EntityPlayerMP) { 
			updatePlayerCloakState(entity);
		}*/
		
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
						distance = job.getDistance();
						GasUtils.playerNearestVent = job.getNearestVent();
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
					System.out.println("[" + SS.MODNAME + "] Distance is: " + distance + " time: " + (time2 - time1) / 1000000.0D + "ms");
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

	/*private void updatePlayerCloakState(EntityLivingBase entity) {
		// Make sure for elapsed time is second after last update
		if (System.currentTimeMillis() - this.lastTimer > 1000)
			lastTimer = System.currentTimeMillis();
		else 
			return;
		
		try {
			EntityPlayerMP p = (EntityPlayerMP)entity;
			Integer cloakTicks = this.cloakPlayersTimers.get(p.username);
			
			if (cloakTicks == null) {
				this.cloakPlayersTimers.remove(p.username);
				this.cloakPlayersTimers.put(p.username, 0);
				
				return;
			}
			
			if (cloakTicks >= CLOAK_CHECK_TIMEOUT_SEC) {
				this.cloakPlayersTimers.remove(p.username);
				this.cloakPlayersTimers.put(p.username, 0);
				
				List<CloakedArea> cloaks = WarpDrive.instance.cloaks.getCloaksForPoint(p.worldObj.provider.dimensionId, MathHelper.floor_double(p.posX), MathHelper.floor_double(p.posY), MathHelper.floor_double(p.posZ), false);
				if (cloaks.size() != 0) {
					//System.out.println("[Cloak] Player inside " + cloaks.size() + " cloaked areas");
					for (CloakedArea area : cloaks) {
						//System.out.println("[Cloak] Frequency: " + area.frequency + ". In: " + area.isPlayerInArea(p) + ", W: " + area.isPlayerWithinArea(p));
						if (!area.isPlayerInArea(p) && area.isPlayerWithinArea(p)) {
							WarpDrive.instance.cloaks.playerEnteringCloakedArea(area, p);
						}
					}
				} else {
					//System.out.println("[Cloak] Player is not inside any cloak fields. Check, which field player may left...");
					WarpDrive.instance.cloaks.checkPlayerLeavedArea(p);
				}
			} else {
				this.cloakPlayersTimers.remove(p.username);
				this.cloakPlayersTimers.put(p.username, cloakTicks + 1);			
			}
		} catch (Exception e) { e.printStackTrace(); }
	}*/
	
	private void setPlayerAirValue(EntityLivingBase entity, Integer air)
	{
		vacuumPlayers.remove(((EntityPlayerMP)entity).getDisplayName());
		vacuumPlayers.put(((EntityPlayerMP)entity).getDisplayName(), air);
	}

	/**
	 * Проверка, находится ли Entity в открытом космосе
	 * @param e
	 * @return
	 */
	private boolean isEntityInVacuum(Entity e)
	{
		int x = MathHelper.floor_double(e.posX);
		int y = MathHelper.floor_double(e.posY);
		int z = MathHelper.floor_double(e.posZ);
		Block id1 = e.worldObj.getBlock(x, y, z);
		Block id2 = e.worldObj.getBlock(x, y + 1, z);

		if (id1 == SSConfig.ssBlockAir || id2 == SSConfig.ssBlockAir)
			return false;
		return true;
	}

	private boolean consumeO2(ItemStack[] i)
	{
		for (int j = 0; j < i.length; ++j)
			//if (i[j] != null && i[j].equals(SS.IC2_Air[0]) && i[j].getItemDamage() == SS.IC2_Air[1])
			if (i[j] != null && i[j].equals(SS.IC2_Air[0]))
			{
				if (--i[j].stackSize <= 0)
					i[j] = null;
				return true;
			}
		return false;
	}
}
