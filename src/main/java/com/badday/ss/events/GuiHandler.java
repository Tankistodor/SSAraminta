package com.badday.ss.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.blocks.SSTileEntityGasMixer;
import com.badday.ss.containers.SSContainerCabinet;
import com.badday.ss.containers.SSContainerGasMixer;
import com.badday.ss.core.utils.WorldUtils;
import com.badday.ss.gui.SSGuiCabinet;
import com.badday.ss.gui.SSGuiGasMixer;
import com.badday.ss.gui.SSGuiIDs;
import com.badday.ss.gui.SSGuiItemGasAnalyser;
import com.badday.ss.items.SSInventoryItem;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			
			if (ID == 10) {
				//return new SSGuiItemGasAnalyser(player, player.inventory, new SSInventoryItem(player.getHeldItem()));
				//http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571597-1-7-2-1-6-4-custom-inventories-in-items-and
			}
			
			if (!world.blockExists(x, y, z)) {
				if (SS.Debug) System.out.println("["+SS.MODNAME+"] Block not exists");
				return null;
			}

			TileEntity tile = world.getTileEntity(x, y, z);
			
			if (tile != null)
	        {
	            if (tile instanceof SSTileEntityCabinet)
	            {
	            	return new SSGuiCabinet(player.inventory, (SSTileEntityCabinet) tile);
	            } else if (tile instanceof SSTileEntityGasMixer) {
	            	return new SSGuiGasMixer(player.inventory, (SSTileEntityGasMixer) tile);
	            }
	            
	        }
		
			/*if (ID == SSGuiIDs.GUI_ID_CABINET) {
				return new SSGuiCabinet(player.inventory, (SSTileEntityCabinet) tile);
			}*/
			
			
		}
		
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		 
		EntityPlayerMP playerBase = WorldUtils.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase == null)
        {
            player.addChatMessage(new ChatComponentText("[" + SS.MODNAME + "] player instance null server-side. This is a bug."));
            return null;
        }
        
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null)
        {
        	if (tile instanceof SSTileEntityCabinet)
            {
        		return new SSContainerCabinet(player.inventory, ((SSTileEntityCabinet) tile), 184, 184); 
            } else if (tile instanceof SSTileEntityGasMixer) {
            	return new SSContainerGasMixer(player.inventory, ((SSTileEntityGasMixer) tile));
            }
        }
                
        return null;
	}

}
