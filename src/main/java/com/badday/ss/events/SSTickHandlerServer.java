package com.badday.ss.events;

import com.badday.ss.core.atmos.GasUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;

public class SSTickHandlerServer {

	public final static SSTickHandlerServer instance = new SSTickHandlerServer();
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase == Phase.START) {
		}

		else if (event.phase == Phase.END) {
			GasUtils.rebuildGasNetworkEvent();	
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ServerTickEvent event) {
		/*if (event.type == Type.SERVER && event.side == Side.SERVER
				&& event.phase == Phase.END) {
			System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");	
		}*/
	}

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		
		/*if (event.phase == Phase.START) {
			final WorldServer world = (WorldServer) event.world;

			CopyOnWriteArrayList<ScheduledBlockChange> changeList = SSTickHandlerServer.scheduledBlockChanges.get(world.provider.dimensionId);

			if (changeList != null && !changeList.isEmpty()) {
				for (ScheduledBlockChange change : changeList) {
					if (change != null) {
						BlockVec3 changePosition = change.getChangePosition();
						if (changePosition != null) {
							world.setBlock(changePosition.x, changePosition.y, changePosition.z, change.getChangeID(), change.getChangeMeta(), 2);
						}
					}
				}

				changeList.clear();
				SSTickHandlerServer.scheduledBlockChanges.remove(world.provider.dimensionId);
			}
		}		else if (event.phase == Phase.END)
        {
            final WorldServer world = (WorldServer) event.world;

            List<BlockVec3> edgesList = SSTickHandlerServer.edgeChecks.get(world.provider.dimensionId);
            final HashSet<BlockVec3> checkedThisTick = new HashSet();

            if (edgesList != null && !edgesList.isEmpty())
            {
                List<BlockVec3> edgesListCopy = new ArrayList();
                edgesListCopy.addAll(edgesList);
                for (BlockVec3 edgeBlock : edgesListCopy)
                {
                    if (edgeBlock != null && !checkedThisTick.contains(edgeBlock))
                    {
                        if (SSTickHandlerServer.scheduledForChange(world.provider.dimensionId, edgeBlock))
                        {
                            continue;
                        }

                        ThreadFindSeal done = new ThreadFindSeal(world, edgeBlock, 2000, new ArrayList<TileEntityOxygenSealer>());
                        checkedThisTick.addAll(done.checked);
                    }
                }

                SSTickHandlerServer.edgeChecks.remove(world.provider.dimensionId);
            }
        }*/
	}

}
