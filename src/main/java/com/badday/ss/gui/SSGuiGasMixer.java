package com.badday.ss.gui;

import ic2.core.IC2;
import ic2.core.network.NetworkManager;
import ic2.core.util.GuiTooltiphelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.badday.ss.blocks.SSTileEntityGasMixer;
import com.badday.ss.containers.SSContainerGasMixer;

public class SSGuiGasMixer extends GuiContainer {

	private static final ResourceLocation guiTexture = new ResourceLocation("ss", "textures/gui/GuiGasMixer.png");

	private SSTileEntityGasMixer tileEntity;
	private IInventory player;

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
		this.player = player;
		this.name = StatCollector.translateToLocal("gasMixer.gui.name");
	}

	@Override
	public void initGui()
	  {
	    super.initGui();
	    
	    int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
	    
	    this.buttonList.add(new GuiButton(0, x + 46, y + 49, 12, 20, "+"));
	    this.buttonList.add(new GuiButton(1, x + 46, y + 69, 12, 20, "-"));
	    
	    this.buttonList.add(new GuiButton(2, x + 83, y + 49, 12, 20, "+"));
	    this.buttonList.add(new GuiButton(3, x + 83, y + 69, 12, 20, "-"));
	    
	    this.buttonList.add(new GuiButton(4, x + 117, y + 49, 12, 20, "+"));
	    this.buttonList.add(new GuiButton(5, x + 117, y + 69, 12, 20, "-"));
	    
	    this.buttonList.add(new GuiButton(6, x + 154, y + 49, 12, 20, "+"));
	    this.buttonList.add(new GuiButton(7, x + 154, y + 69, 12, 20, "-"));
	    
	    this.buttonList.add(new GuiButton(8, x + 117, y + 6, 12, 20, "+"));
	    this.buttonList.add(new GuiButton(9, x + 154, y + 6, 12, 20, "-"));
	    
	    
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

		this.fontRendererObj.drawString(Byte.toString(this.tileEntity.tankTrust[0]), 34 - 4, 30, 4210752);
		this.fontRendererObj.drawString(Byte.toString(this.tileEntity.tankTrust[1]), 70 - 4, 30, 4210752);
		this.fontRendererObj.drawString(Byte.toString(this.tileEntity.tankTrust[2]), 106 - 4, 30, 4210752);
		this.fontRendererObj.drawString(Byte.toString(this.tileEntity.tankTrust[3]), 141 - 4, 30, 4210752);
		
		this.fontRendererObj.drawString(Byte.toString(this.tileEntity.totalTrust), 133 +6, 12, 4210752);
		
		GuiTooltiphelper.drawAreaTooltip(param1 - this.guiLeft, param2 - this.guiTop, "Total Gain Vent", 133 + 6, 12, 133 + 6 + 10, 133 + 6 + 10);
		
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
