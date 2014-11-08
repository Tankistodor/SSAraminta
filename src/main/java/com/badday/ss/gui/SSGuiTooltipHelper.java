package com.badday.ss.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public final class SSGuiTooltipHelper
{

  public static void drawAreaTooltip(int x, int y, String tooltip, int minX, int minY, int maxX, int maxY, boolean drawborder)
  {
    drawAreaTooltip(x, y, tooltip, minX, minY, maxX, maxY, 0, 0, drawborder);
  }

  public static void drawAreaTooltip(int x, int y, String tooltip, int minX, int minY, int maxX, int maxY) {
    drawAreaTooltip(x, y, tooltip, minX, minY, maxX, maxY, 0, 0, true);
  }

  public static void drawAreaTooltip(int x, int y, String tooltip, int minX, int minY, int maxX, int maxY, int yoffset, int xoffset) {
    drawAreaTooltip(x, y, tooltip, minX, minY, maxX, maxY, yoffset, xoffset, true);
  }

  public static void drawAreaTooltip(int x, int y, String tooltip, int minX, int minY, int maxX, int maxY, int yoffset, int xoffset, boolean drawborder) {
    if ((x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY))
      drawTooltip(x, y, yoffset, xoffset, tooltip, drawborder, 0);
  }

  public static void drawTooltip(int x, int y, int yoffset, int xoffset, String tooltip, boolean drawborder, int width)
  {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    if (width == 0) x -= fontRenderer.getStringWidth(tooltip) / 2;
    y -= 12;

    x += xoffset;
    y += yoffset;

    if (width == 0) {
      width = fontRenderer.getStringWidth(tooltip);
    }
    width += 8;
    int height = 8;

    int backgroundColor = 255;
    int borderColor = -1162167553;

    GL11.glPushAttrib(16704);

    RenderHelper.disableStandardItemLighting();
    GL11.glDisable(2929);
    GL11.glDisable(3553);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);

    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();

    drawRectangle(tessellator, x - 3, y - 4, x + width + 3, y - 3, backgroundColor);
    drawRectangle(tessellator, x - 3, y + height + 3, x + width + 3, y + height + 4, backgroundColor);
    drawRectangle(tessellator, x - 3, y - 3, x + width + 3, y + height + 3, backgroundColor);
    drawRectangle(tessellator, x - 4, y - 3, x - 3, y + height + 3, backgroundColor);
    drawRectangle(tessellator, x + width + 3, y - 3, x + width + 4, y + height + 3, backgroundColor);
    if (drawborder)
    {
      drawRectangle(tessellator, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, borderColor);
      drawRectangle(tessellator, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, borderColor);
      drawRectangle(tessellator, x - 3, y - 3, x + width + 3, y - 3 + 1, borderColor);
      drawRectangle(tessellator, x - 3, y + height + 2, x + width + 3, y + height + 3, borderColor);
    }
    tessellator.draw();

    GL11.glEnable(3553);

    fontRenderer.drawStringWithShadow(tooltip, x + 4, y, -2);

    GL11.glPopAttrib();
  }

  private static void drawRectangle(Tessellator tessellator, int x1, int y1, int x2, int y2, int color) {
    tessellator.setColorRGBA(color >>> 24 & 0xFF, color >>> 16 & 0xFF, color >>> 8 & 0xFF, color & 0xFF);

    tessellator.addVertex(x2, y1, 300.0D);
    tessellator.addVertex(x1, y1, 300.0D);
    tessellator.addVertex(x1, y2, 300.0D);
    tessellator.addVertex(x2, y2, 300.0D);
  }
}