package com.badday.ss.agriculture.crops;

import com.badday.ss.agriculture.Agriculture;
import ic2.api.crops.ICropTile;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class Diamonda extends BaseCrop {
  @Override
  public String name() {
    return "Diamonda";
  }

  @Override
  public int tier() {
    return 8;
  }

  @Override
  public int stat(int n) {
    switch (n) {
      case 0:
        return 2;
      case 1:
        return 0;
      case 2:
        return 0;
      case 3:
        return 2;
      case 4:
        return 0;
    }
    return 0;
  }

  @Override
  public String[] attributes() {
    return new String[] { "Diamonds", "Leaves", "Reed" };
  }

  @Override
  public int getrootslength(ICropTile crop) {
    return 3;
  }

  @Override
  public int maxSize() {
    return 4;
  }

  @Override
  public boolean canGrow(ICropTile crop) {
    if (crop.getSize() < 3) return true;
    if ((crop.getSize() == 3) && ((crop.isBlockBelow(Blocks.diamond_ore)) || (crop.isBlockBelow(Blocks.diamond_block)))) return true;
    return false;
  }

  @Override
  public int growthDuration(ICropTile crop) {
    if (crop.getSize() == 3) {
      return 2200;
    }
    return 750;
  }

  @Override
  public byte getSizeAfterHarvest(ICropTile crop) {
    return 2;
  }

  @Override
  public ItemStack getGain(ICropTile crop) {
    return new ItemStack(Agriculture.diamondFruitItem, 1);
  }
}
