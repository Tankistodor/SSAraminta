package com.badday.ss.gui;

import org.lwjgl.opengl.GL11;

import ic2.core.IC2;
import ic2.core.network.NetworkManager;
import ic2.core.util.GuiTooltiphelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.badday.ss.blocks.SSTileEntityScrubber;
import com.badday.ss.containers.SSContainerScrubber;

public class SSGuiScrubber extends GuiContainer {
	
	private static final ResourceLocation guiTexture = new ResourceLocation("ss", "textures/gui/GuiScrubber.png");

	private SSTileEntityScrubber tileEntity;
	private IInventory player;
	
	public String name;

	public SSGuiScrubber(IInventory player, SSTileEntityScrubber tileEntity) {
		super(new SSContainerScrubber(player, tileEntity));
		this.ySize = 176;
		this.tileEntity = tileEntity;
		this.player = player;
		this.name = StatCollector.translateToLocal("gasScrubber.gui.name");
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
		this.fontRendererObj.drawString(this.name, 65, 5, 4210752);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		
		// Energy
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, "Energy: " + (this.tileEntity.getGuiChargeLevel(100)) + "%", 12, 50,
				12 + 7, 50 + 38);

		// Gas
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, tileEntity.getFluidTooltips(0), 25, 49, 43, 89);
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, tileEntity.getFluidTooltips(1), 61, 49, 43 + 36, 89);
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, tileEntity.getFluidTooltips(2), 97, 49, 43 + 36 + 36, 89);
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, tileEntity.getFluidTooltips(3), 133, 49, 43 + 36 + 36 + 36, 89);

	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTexture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		// Energy
		int scalef = tileEntity.getGuiChargeLevel(38);
		this.drawTexturedModalRect(x + 12, y + 50 + 38 - scalef, 220, 0, 7, scalef);
		if (tileEntity.getEnergy() > 0) {
			this.drawTexturedModalRect(x + 10, y + 37,208,0,12,10);
		}

		// Gas
		int scale = tileEntity.getScaled(0, 38);
		this.drawTexturedModalRect(x + 26, y + 50 + 38 - scale, 192, 0, 16, scale);
		
		scale = tileEntity.getScaled(1, 38);
		this.drawTexturedModalRect(x + 62, y + 50 + 38 - scale, 192, 0, 16, scale);
		
		scale = tileEntity.getScaled(2, 38);
		this.drawTexturedModalRect(x + 98, y + 50 + 38 - scale, 192, 0, 16, scale);

		scale = tileEntity.getScaled(3, 38);
		this.drawTexturedModalRect(x + 134, y + 50 + 38 - scale, 192, 0, 16, scale);
	}


}
