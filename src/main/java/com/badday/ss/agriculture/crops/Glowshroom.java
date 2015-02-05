package com.badday.ss.agriculture.crops;

import com.badday.ss.agriculture.Agriculture;
import com.badday.ss.agriculture.items.FlyAmanitaItem;
import com.badday.ss.agriculture.items.GlowshroomItem;
import ic2.api.crops.ICropTile;
import net.minecraft.item.ItemStack;

/**
 * Created by userad on 04/11/14.
 */
public class Glowshroom extends BaseCrop {
  @Override
  public String name() {
    return "Glowshroom";
  }

  @Override
  public int tier() {
    return 2;
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
        return 4;
      case 4:
        return 2;
    }
    return 0;
  }

  @Override
  public String[] attributes() {
    return new String[] { "Mushroom", "Green", "White", "Food", "Glowing"};
  }

  @Override
  public ItemStack getGain(ICropTile crop) {
    return new ItemStack(Agriculture.glowshroomItem, 1);
  }

  @Override
  public int maxSize() {
    return 3;
  }

  @Override
  public boolean canGrow(ICropTile crop) {
    return crop.getSize() < 3;
  }

  @Override
  public int getEmittedLight(ICropTile crop) {
    return crop.getSize() * 2;
  }

}
