package com.badday.ss.events;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.core.utils.WorldUtils;
import com.badday.ss.gui.SSGuiCabinet;
import com.badday.ss.gui.SSGuiIDs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			return this.getClientGuiElement(ID, player, world, new BlockVec3(x, y, z));
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	private Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockVec3 position) {
		EntityClientPlayerMP playerClient = WorldUtils.getPlayerBaseClientFromPlayer(player, false);
		
		TileEntity tile = world.getTileEntity(position.x, position.y, position.z);
		
		if (ID == SSGuiIDs.GUI_ID_CABINET) {
			return new SSGuiCabinet(player.inventory, (SSTileEntityCabinet) tile);
		}
		
		if (tile != null)
        {
            if (tile instanceof SSTileEntityCabinet)
            {
            	return new SSGuiCabinet(player.inventory, (SSTileEntityCabinet) tile);
            }
        }
		return position;
	}

	@Override
	public Object getServerGuiElement(int arg0, EntityPlayer arg1, World arg2, int arg3, int arg4, int arg5) {
		 
		return null;
	}

}
