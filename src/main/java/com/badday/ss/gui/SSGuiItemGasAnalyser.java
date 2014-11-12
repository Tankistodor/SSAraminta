package com.badday.ss.gui;

import com.badday.ss.containers.SSContainerItemGasAnalyser;
import com.badday.ss.items.SSInventoryItem;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

public class SSGuiItemGasAnalyser extends GuiContainer {

	private IInventory player;
	
	public String name;
	
	public SSGuiItemGasAnalyser(EntityPlayer player, InventoryPlayer playerInventory, SSContainerItemGasAnalyser conteiner) {
		super(conteiner);
		this.ySize = 238;
		this.name = StatCollector.translateToLocal("gasAnalyser.gui.name");
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
