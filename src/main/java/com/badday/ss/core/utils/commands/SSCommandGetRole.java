package com.badday.ss.core.utils.commands;

import java.util.Arrays;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import com.badday.ss.entity.player.SSPlayerData;
import com.badday.ss.entity.player.SSPlayerRoles;

public class SSCommandGetRole extends CommandBase{

	@Override
	public String getCommandName() {
		return "setRole";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/setRole ["+Arrays.toString(SSPlayerRoles.values())+"]";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		 	EntityPlayerMP player = (EntityPlayerMP)icommandsender;
	        MinecraftServer server = MinecraftServer.getServer();

	        if (astring.length == 1 || astring[0] != null)
	        {
	        	SSPlayerData pdata = SSPlayerData.get(player);
	        	try {
	        		pdata.setPlayerRole(SSPlayerRoles.valueOf(astring[0]));
	        		player.addChatMessage(new ChatComponentText("[SSAraminta] Successfully set role " + SSPlayerRoles.valueOf(astring[0])));
	        	} catch (Exception e) {
	        		
	        	}
        		//notifyAdmins()
	                //notifyAdmins(icommandsender, "/space: teleporting player " + astring[0] + " to space", new Object[0]);
	                //player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[0]);
	                //player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(astring[0]);
	        }

	        if (player == null)
	        {
	            return;
	        }
	}
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

}
