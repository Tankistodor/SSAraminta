package com.badday.ss.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.badday.ss.containers.SSContainerItemGasAnalyser;
import com.badday.ss.core.utils.WorldUtils;
import com.badday.ss.items.SSInventoryItem;

public class SSGuiItemGasAnalyser extends GuiContainer {

	private static final ResourceLocation guiTexture = new ResourceLocation("ss", "textures/gui/gasAnalyser.png");
	private IInventory player;
	public String name;
	private SSContainerItemGasAnalyser container;
	
	public SSGuiItemGasAnalyser(EntityPlayer player2, InventoryPlayer inventory, SSInventoryItem ssInventoryItem,
			SSContainerItemGasAnalyser ssContainerItemGasAnalyser) {
		super(ssContainerItemGasAnalyser);
		this.container = ssContainerItemGasAnalyser;
		this.xSize = 246;
		this.ySize = 152;		
		this.name = StatCollector.translateToLocal("gasAnalyser.gui.name");
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color
		//this.fontRendererObj.drawString(this.name, 65, 5, 4210752);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		int i = 0;
		for (String str : container.getAmtosInfo()) {
			this.fontRendererObj.drawStringWithShadow(str, 14, 14+(i*10), WorldUtils.to32BitColor(255,255,255,255));
			i++;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTexture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
	}

}
