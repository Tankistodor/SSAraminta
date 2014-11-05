package com.badday.ss.agriculture;

import ic2.api.crops.Crops;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.badday.ss.agriculture.crops.Amanita;

/**
 * Created by userad on 04/11/14.
 */
public class Agriculture {
  public static void init() {
    Crops.instance.registerCrop(new Amanita(), 100);
    Crops.instance.registerBaseSeed(new ItemStack(Blocks.cobblestone, 1), 100, 1,1,1,1);
  }
}
