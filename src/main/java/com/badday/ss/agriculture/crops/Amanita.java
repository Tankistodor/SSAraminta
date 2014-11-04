package com.badday.ss.agriculture.crops;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by userad on 04/11/14.
 */
public class Amanita extends BaseCrop {
  @Override
  public String name() {
    return "Fly Amanita";
  }

  @Override
  public int tier() {
    return 0;
  }

  @Override
  public int stat(int n) {
    return 0;
  }

  @Override
  public String[] attributes() {
    return new String[] { "Mushroom", "Poison"};
  }

  @Override
  public int maxSize() {
    return 4;
  }

  @Override
  public boolean canGrow(ICropTile crop) {
    return crop.getSize() < 4;
  }

  @Override
  public int getOptimalHavestSize(ICropTile crop) {
    return 0;
  }

  @Override
  public boolean canBeHarvested(ICropTile crop) {
    return crop.getSize() == 4;
  }

  @Override
  public ItemStack getGain(ICropTile crop) {
    return new ItemStack(Blocks.red_mushroom);
  }
}
