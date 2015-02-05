package com.badday.ss.agriculture.items;

/**
 * Created by userad on 04/02/15.
 */
public class LibertyCapItem extends CropItem {
  public LibertyCapItem(int healAmount, float saturationModifier) {
    super(healAmount, saturationModifier);
  }

  @Override
  public String name() {
    return "LibertyCap";
  }
}
