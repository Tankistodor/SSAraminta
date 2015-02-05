package com.badday.ss.agriculture.items;

/**
 * Created by userad on 04/02/15.
 */
public class GlowshroomItem extends CropItem {
  public GlowshroomItem(int healAmount, float saturationModifier) {
    super(healAmount, saturationModifier);
  }

  @Override
  public String name() {
    return "Glowshroom";
  }
}
