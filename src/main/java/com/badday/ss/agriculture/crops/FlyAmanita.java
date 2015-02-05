package com.badday.ss.agriculture.crops;

import com.badday.ss.agriculture.Agriculture;
import com.badday.ss.agriculture.items.FlyAmanitaItem;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by userad on 04/11/14.
 */
public class FlyAmanita extends BaseCrop {
  @Override
  public String name() {
    return "Fly Amanita";
  }

  @Override
  public int tier() {
    return 1;
  }

  /* - 0: Chemistry (Industrial uses based on chemical plant components)
  * - 1: Consumable (Food, potion ingredients, stuff meant to be eaten or similarly used)
  * - 2: Defensive (Plants with defense capabilities (damaging, explosive, chemical) or special abilities in general)
  * - 3: Colorful (How colorful/aesthetically/beautiful is the plant, like dye-plants or plants without actual effects)
  * - 4: Weed (Is this plant weed-like and rather unwanted/quick-spreading? Rare super-breed plants should have low values here)
  */

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
        return 2;
    }
    return 0;
  }

  @Override
  public String[] attributes() {
    return new String[] { "Mushroom", "Poison", "Red", "Food"};
  }

  @Override
  public int getOptimalHavestSize(ICropTile crop) {
    return 0;
  }

  @Override
  public ItemStack getGain(ICropTile crop) {
    return new ItemStack(Agriculture.flyAmanitaItem, 1);
  }
}
