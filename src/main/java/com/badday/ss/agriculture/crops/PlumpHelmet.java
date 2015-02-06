package com.badday.ss.agriculture.crops;

import ic2.api.crops.ICropTile;
import net.minecraft.item.ItemStack;

import com.badday.ss.agriculture.Agriculture;

/**
 * Created by userad on 04/11/14.
 */
public class PlumpHelmet extends BaseCrop {
  @Override
  public String name() {
    return "Plump Helmet";
  }

  @Override
  public int tier() {
    return 4;
  }

  @Override
  public int stat(int n) {
    switch (n) {
      case 0:
        return 0;
      case 1:
        return 4;
      case 2:
        return 0;
      case 3:
        return 0;
      case 4:
        return 0;
    }
    return 0;
  }

  @Override
  public String[] attributes() {
    return new String[] { "Mushroom", "White", "Food"};
  }

  @Override
  public ItemStack getGain(ICropTile crop) {
    return new ItemStack(Agriculture.plumpHelmetItem, 1);
  }

  @Override
  public int maxSize() {
    return 3;
  }

  @Override
  public boolean canGrow(ICropTile crop) {
    return crop.getSize() < 3;
  }


}
