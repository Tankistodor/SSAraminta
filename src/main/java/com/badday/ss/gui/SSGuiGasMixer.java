package com.badday.ss.gui;

import ic2.core.util.GuiTooltiphelper;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.badday.ss.blocks.SSTileEntityGasMixer;
import com.badday.ss.containers.SSContainerGasMixer;

public class SSGuiGasMixer extends GuiContainer {

	private static final ResourceLocation guiTexture = new ResourceLocation("ss", "textures/gui/GuiGasMixer.png");

	private SSTileEntityGasMixer tileEntity;

	private GuiButton buttonTank1p;
	private GuiButton buttonTank1m;
	private GuiButton buttonTank2p;
	private GuiButton buttonTank2m;
	private GuiButton buttonTank3p;
	private GuiButton buttonTank3m;
	private GuiButton buttonTank4p;
	private GuiButton buttonTank4m;

	private GuiButton buttonTankP;
	private GuiButton buttonTankM;

	public String name;

	public SSGuiGasMixer(IInventory player, SSTileEntityGasMixer tileEntity) {
		super(new SSContainerGasMixer(player, tileEntity));
		this.ySize = 176;
		this.tileEntity = tileEntity;
		this.name = StatCollector.translateToLocal("gasMixer.gui.name");
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color
		this.fontRendererObj.drawString(this.name, 65, 5, 4210752);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, tileEntity.getFluidTooltips(0), 25, 49, 43, 89);
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, tileEntity.getFluidTooltips(1), 61, 49, 43 + 36, 89);
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, tileEntity.getFluidTooltips(2), 97, 49, 43 + 36 + 36, 89);
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, tileEntity.getFluidTooltips(3), 133, 49, 43 + 36 + 36 + 36, 89);

		// fontRenderer.drawString("Tiny", 8, 6, 4210752);
		// draws "Inventory" or your regional equivalent
		// fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"),
		// 8, ySize - 96 + 2, 4210752);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTexture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

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
