package com.badday.ss.agriculture.items;

/**
 * Created by userad on 04/02/15.
 */
public class PlumpHelmetItem extends CropItem {
  public PlumpHelmetItem(int healAmount, float saturationModifier) {
    super(healAmount, saturationModifier);
  }

  @Override
  public String name() {
    return "PlumpHelmet";
  }
}
