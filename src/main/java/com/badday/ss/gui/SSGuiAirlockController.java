package com.badday.ss.gui;

import org.lwjgl.opengl.GL11;

import ic2.core.IC2;
import ic2.core.network.NetworkManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.badday.ss.blocks.SSTileEntityAirlockFrameController;
import com.badday.ss.containers.SSContainerAirlockController;

public class SSGuiAirlockController extends GuiContainer {
	
	private static final ResourceLocation guiTexture = new ResourceLocation("ss", "textures/gui/GuiAirlockController.png");

	private SSTileEntityAirlockFrameController tileEntity;
	private IInventory player;

	public String name;

	public SSGuiAirlockController(IInventory player, SSTileEntityAirlockFrameController tileEntity) {
		super(new SSContainerAirlockController(player, tileEntity));
		this.ySize = 176;
		this.tileEntity = tileEntity;
		this.player = player;
		this.name = StatCollector.translateToLocal("airlockController.gui.name");
	}
	
	@Override
	public void initGui()
	  {
	    super.initGui();
	    
	    int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
	  }
	
	@Override
	protected void actionPerformed(GuiButton guibutton)
	  {
	    super.actionPerformed(guibutton);
	    ((NetworkManager)IC2.network.get()).initiateClientTileEntityEvent(this.tileEntity, guibutton.id);
	  }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color
		this.fontRendererObj.drawString(this.name, 35, 5, 4210752);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
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
