package com.badday.ss.entity.player;

import java.lang.ref.WeakReference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.badday.ss.api.IPlayerData;
import com.badday.ss.core.utils.BlockVec3;

public class SSPlayerData implements IPlayerData,IExtendedEntityProperties {

	private static final String SS_PLAYER_PROP = "SSPlayerData";
	
	public WeakReference<EntityPlayerMP> player;
	
	public BlockVec3 playerNearestVent = BlockVec3.INVALID_VECTOR;
	private SSPlayerRoles playerRole = SSPlayerRoles.ASSISTENT;
	
	public SSPlayerData(EntityPlayerMP player) {
		this.player = new WeakReference<EntityPlayerMP>(player);
	}
	
	@Override
	public void init(Entity player, World world) {
		
	}
	
	public static void register(EntityPlayerMP player)
    {
        player.registerExtendedProperties(SSPlayerData.SS_PLAYER_PROP, new SSPlayerData(player));
    }
	
	@Override
	public void loadNBTData(NBTTagCompound tags) {
		try {
			this.playerRole = SSPlayerRoles.valueOf(tags.getString("playerRole"));
		} catch (Exception e) {
			this.playerRole = SSPlayerRoles.ASSISTENT;
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound tags) {
		
		tags.setString("playerRole", this.playerRole.toString());
	}
	
	public static SSPlayerData get(EntityPlayerMP player)
    {
        return (SSPlayerData) player.getExtendedProperties(SSPlayerData.SS_PLAYER_PROP);
    }

	public void copyFrom(SSPlayerData oldData, boolean keepInv)
    {
        /*if (keepInv)
        {
            this.extendedInventory.copyInventory(oldData.extendedInventory);
        }

        this.spaceStationDimensionID = oldData.spaceStationDimensionID;
        this.unlockedSchematics = oldData.unlockedSchematics;
        this.receivedSoundWarning = oldData.receivedSoundWarning;
        this.openedSpaceRaceManager = oldData.openedSpaceRaceManager;
        this.spaceRaceInviteTeamID = oldData.spaceRaceInviteTeamID;
        */
		
		//this.playerNearestVent = oldData.playerNearestVent;
    }

	public void setPlayerRole(SSPlayerRoles role) {
		this.playerRole = role;
	}
	
	public SSPlayerRoles getPlayerRole() {
		return this.playerRole;
	}
	
}
