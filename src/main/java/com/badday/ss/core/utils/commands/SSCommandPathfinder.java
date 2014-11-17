package com.badday.ss.core.utils.commands;

import com.badday.ss.SS;
import com.badday.ss.core.atmos.Pathfinding;
import com.badday.ss.core.utils.BlockVec3;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

public class SSCommandPathfinder extends CommandBase
{
    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandName()
    {
        return "pathcheck";
    }


    //TODO: real pathfinding checks should to be in separate thread
    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        EntityPlayerMP player = (EntityPlayerMP)icommandsender;
        MinecraftServer server = MinecraftServer.getServer();

        if (astring.length >= 3)
        {
        	if (player.worldObj.provider.dimensionId == SS.instance.spaceDimID) {
            long time1 = System.nanoTime();
            int x = Integer.parseInt(astring[0]);
            int y = Integer.parseInt(astring[1]);
            int z = Integer.parseInt(astring[2]);
            Pathfinding p = new Pathfinding(player.worldObj, new BlockVec3(player), new BlockVec3(x, y, z));
            long time2 = System.nanoTime();
            while (!p.isDone()) {
              time2 = System.nanoTime();
              p.iterate(1024);
              if ((time2 - time1) > 500 * 1000000.0D) {
                System.out.println("[" + SS.MODNAME + "] Pathfinding timeout");
                break;
              }
            }

            System.out.println("[" + SS.MODNAME + "] Pathfinding work to " + x + " " + y + "" + z + " complite. Result: " + p.isDone() + " Path: " + p.getResult().size());
            System.out.println("[" + SS.MODNAME + "] Pathfinding work time: " + (time2 - time1) / 1000000.0D + "ms");

            if (astring.length == 4)
              if (p.isDone()) {
                for (BlockVec3 coord : p.getResult()) {
                  player.worldObj.setBlock(coord.x, coord.y, coord.z, Blocks.redstone_block);
                }
              }
            }

        }

        if (player == null)
        {
            return;
        }
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/pathcheck x y z";
    }
}
