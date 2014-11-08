package com.badday.ss.gui;

import org.lwjgl.opengl.GL11;

import com.badday.ss.blocks.SSTileEntityGasMixer;
import com.badday.ss.containers.SSContainerGasMixer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class SSGuiGasMixer extends GuiContainer {

	private static final ResourceLocation guiTexture = new ResourceLocation("ss", "textures/gui/GuiGasMixer.png");
	
	public SSGuiGasMixer(IInventory player, SSTileEntityGasMixer tileEntity, int xSize, int ySize) {
		super(new SSContainerGasMixer(player,tileEntity, ySize, ySize));
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
