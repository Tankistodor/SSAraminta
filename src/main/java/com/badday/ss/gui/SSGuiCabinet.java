package com.badday.ss.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.badday.ss.SS;
import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.containers.SSContainerCabinet;


public class SSGuiCabinet extends GuiContainer {
	
	private static final ResourceLocation guiTexture = new ResourceLocation("ss", "textures/gui/cabinet.png");
	private final SSTileEntityCabinet cabinet;
	
	private static int xSize = 176;//184
	private static int ySize = 176;
	
    public SSGuiCabinet(IInventory player, SSTileEntityCabinet chest)
    {
        super(new SSContainerCabinet(player,chest,xSize,ySize));
        this.xSize = xSize;
        this.ySize = ySize;
        cabinet = chest;
        this.allowUserInput = false;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
    	super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // new "bind tex"
        this.mc.getTextureManager().bindTexture(guiTexture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}

