package com.badday.ss.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.blocks.SSTileEntityAirlockFrameController;
import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.blocks.SSTileEntityGasMixer;
import com.badday.ss.blocks.SSTileEntityScrubber;
import com.badday.ss.containers.SSContainerAirlockController;
import com.badday.ss.containers.SSContainerCabinet;
import com.badday.ss.containers.SSContainerGasMixer;
import com.badday.ss.containers.SSContainerItemGasAnalyser;
import com.badday.ss.containers.SSContainerScrubber;
import com.badday.ss.core.utils.WorldUtils;
import com.badday.ss.gui.SSGuiAirlockController;
import com.badday.ss.gui.SSGuiCabinet;
import com.badday.ss.gui.SSGuiGasMixer;
import com.badday.ss.gui.SSGuiItemGasAnalyser;
import com.badday.ss.gui.SSGuiScrubber;
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

			// 	http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571597-1-7-2-1-6-4-custom-inventories-in-items-and
			if (ID == 10) {
				SSInventoryItem inventoryItem = new SSInventoryItem(player.getHeldItem());
				return new SSGuiItemGasAnalyser(player, player.inventory,inventoryItem , new SSContainerItemGasAnalyser(player,player.inventory,inventoryItem));
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
	            } else if (tile instanceof SSTileEntityScrubber) {
	            	return new SSGuiScrubber(player.inventory, (SSTileEntityScrubber) tile);
	            } else if (tile instanceof SSTileEntityAirlockFrameController) {
	            	return new SSGuiAirlockController(player.inventory, (SSTileEntityAirlockFrameController) tile);
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
        
        //http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571597-1-7-2-1-6-4-custom-inventories-in-items-and
        if (ID == 10) {
			SSInventoryItem inventoryItem = new SSInventoryItem(player.getHeldItem());
			return new SSContainerItemGasAnalyser(player,player.inventory,inventoryItem);
		}
        
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null)
        {
        	if (tile instanceof SSTileEntityCabinet)
            {
        		return new SSContainerCabinet(player.inventory, ((SSTileEntityCabinet) tile), 184, 184); 
            } else if (tile instanceof SSTileEntityGasMixer) {
            	return new SSContainerGasMixer(player.inventory, ((SSTileEntityGasMixer) tile));
            } else if (tile instanceof SSTileEntityScrubber) {
            	return new SSContainerScrubber(player.inventory, ((SSTileEntityScrubber) tile));
            } else if (tile instanceof SSTileEntityAirlockFrameController) {
            	return new SSContainerAirlockController(player.inventory, ((SSTileEntityAirlockFrameController) tile));
            }
        }
                
        return null;
	}

}
