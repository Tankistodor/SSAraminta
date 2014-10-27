package com.badday.ss.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.containers.SSContainerCabinet;


public class SSGuiCabinet extends GuiContainer {
	
	private final int ROWLENGHT = 9;
	private static final ResourceLocation guiTexture = new ResourceLocation("cabinet", "textures/gui/cabinet.png");
	private final SSTileEntityCabinet cabinet;
	
	private static final int xSize = 184;
	private static final int ySize = 202;
	
	/*
    public enum ResourceList {
        IRON(new ResourceLocation("cabinet", "textures/gui/cabinet.png"));
        public final ResourceLocation location;
        private ResourceList(ResourceLocation loc) {
            this.location = loc;
        }
    }
    public enum GUI {
        IRON(184, 202, ResourceList.IRON, 1);

        private int xSize;
        private int ySize;
        private ResourceList guiResourceList;
        private int mainType;

        private GUI(int xSize, int ySize, ResourceList guiResourceList, int mainType)
        {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiResourceList = guiResourceList;
            this.mainType = mainType;

        }

        protected Container makeContainer(IInventory player, IInventory chest)
        {
            return new SSContainerCabinet(player, chest, mainType, xSize, ySize);
        }

        public static SSGuiCabinet buildGUI(int type, IInventory playerInventory, SSTileEntityCabinet chestInventory)
        {
            return new SSGuiCabinet(values()[chestInventory.getType()], playerInventory, chestInventory);
        }
    }

    public int getRowLength()
    {
        return ROWLENGHT;
    }

    private GUI type;

*/
	
    public SSGuiCabinet(IInventory player, SSTileEntityCabinet chest)
    {
        super(new SSContainerCabinet(player,chest,0,xSize,ySize));
        cabinet = chest;
        this.allowUserInput = false;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
    
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

