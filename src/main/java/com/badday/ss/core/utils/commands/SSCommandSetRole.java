package com.badday.ss.core.utils.commands;

import java.util.Arrays;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import com.badday.ss.entity.player.SSPlayerData;
import com.badday.ss.entity.player.SSPlayerRoles;

public class SSCommandSetRole extends CommandBase {

	@Override
	public String getCommandName() {
		return "getRole";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/getRole [" + Arrays.toString(SSPlayerRoles.values()) + "]";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		EntityPlayerMP player = (EntityPlayerMP) icommandsender;
		MinecraftServer server = MinecraftServer.getServer();

		SSPlayerData pdata = SSPlayerData.get(player);
		try {
			SSPlayerRoles role = pdata.getPlayerRole();
			player.addChatMessage(new ChatComponentText("[SSAraminta] You role " + role));
		} catch (Exception e) {

		}

		if (player == null) {
			return;
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 1;
	}

}
