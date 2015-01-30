package com.badday.ss.core.utils.commands;

import com.badday.ss.SS;
import com.badday.ss.core.utils.SpaceTeleporter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

public class SSCommandIslandTp extends CommandBase
{
    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandName()
    {
        return "island";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        EntityPlayerMP player = (EntityPlayerMP)icommandsender;
        MinecraftServer server = MinecraftServer.getServer();

        if (astring.length >= 1)
        {
        		//notifyAdmins()
                //notifyAdmins(icommandsender, "/space: teleporting player " + astring[0] + " to space", new Object[0]);
                //player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[0]);
                //player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[0]);
        }

        if (player == null)
        {
            return;
        }

        player.setPosition(0, 70, 0);
        
        WorldServer to = server.worldServerForDimension(SS.instance.islandDimID);
        SpaceTeleporter teleporter = new SpaceTeleporter(to, 0, 0, 66, 0);
        server.getConfigurationManager().transferPlayerToDimension(player, SS.instance.islandDimID, teleporter);
        player.setPosition(0, 70, 0);
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/island [hyper|<player>]";
    }
}
