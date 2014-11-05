package com.badday.ss.agriculture;

import com.badday.ss.agriculture.crops.*;
import ic2.api.crops.Crops;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by userad on 04/11/14.
 */
public class Agriculture {
  public static void init() {
    Crops.instance.registerCrop(new FlyAmanita(), 100);
    Crops.instance.registerCrop(new Chanterelle(), 101);
    Crops.instance.registerCrop(new DestroyingAngel(), 102);
    Crops.instance.registerCrop(new Glowshroom(),103);
    Crops.instance.registerCrop(new LibertyCap(),104);
    Crops.instance.registerCrop(new PlumpHelmet(),105);

    Crops.instance.registerBaseSeed(new ItemStack(Blocks.cobblestone, 1), 100, 1, 1, 1, 1);
  }
}
