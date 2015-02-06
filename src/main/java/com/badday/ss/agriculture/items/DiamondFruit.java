package com.badday.ss.agriculture.items;

import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Created by userad on 06/02/15.
 */
public class DiamondFruit extends CropItem {
  public DiamondFruit(int healAmount, float saturationModifier) {
    super(healAmount, saturationModifier);
  }

  @Override
  public String name() {
    return "DiamondFruit";
  }

  @Override
  protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
    super.onFoodEaten(stack, world, player);
    if (!world.isRemote) {
      player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, (IC2.random.nextInt(10) + 1) * 40, 2));
      player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, (IC2.random.nextInt(10) + 1) * 40, 2));
      player.addPotionEffect(new PotionEffect(Potion.jump.id, (IC2.random.nextInt(10) + 1) * 40, 2));
      player.addPotionEffect(new PotionEffect(Potion.nightVision.id, (IC2.random.nextInt(10) + 1) * 20, 1));
    }
  }
}
