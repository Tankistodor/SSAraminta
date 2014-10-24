package com.badday.ss.core.utils;

import com.badday.ss.SS;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

public class SpaceTpCommand extends CommandBase
{
    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandName()
    {
        return "space";
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
        //player.travelToDimension(SS.instance.spaceDimID);
        
        WorldServer to = server.worldServerForDimension(SS.instance.spaceDimID);
        //SpaceTeleporter teleporter = new SpaceTeleporter(to, 0, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
        SpaceTeleporter teleporter = new SpaceTeleporter(to, 0, 0, 66, 0);
        server.getConfigurationManager().transferPlayerToDimension(player, SS.instance.spaceDimID, teleporter);
        player.setPosition(0, 70, 0);
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/space [hyper|<player>]";
    }
}
