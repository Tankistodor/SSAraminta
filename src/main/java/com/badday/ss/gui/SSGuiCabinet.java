package com.badday.ss.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.containers.SSContainerCabinet;


public class SSGuiCabinet extends GuiContainer {
	
	private final int ROWLENGHT = 9;
	
    public enum ResourceList {
        IRON(new ResourceLocation("ironchest", "textures/gui/ironcontainer.png"));
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

    private SSGuiCabinet(GUI type, IInventory player, IInventory chest)
    {
        super(type.makeContainer(player, chest));
        this.type = type;
        this.xSize = type.xSize;
        this.ySize = type.ySize;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // new "bind tex"
        this.mc.getTextureManager().bindTexture(type.guiResourceList.location);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}

