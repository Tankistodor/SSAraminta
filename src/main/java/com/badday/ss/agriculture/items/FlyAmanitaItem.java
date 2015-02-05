package com.badday.ss.agriculture.items;

public class FlyAmanitaItem extends CropItem {
  public FlyAmanitaItem(int healAmount, float saturationModifier) {
    super(healAmount, saturationModifier);
  }

  @Override
  public String name() {
    return "FlyAmanita";
  }
}
