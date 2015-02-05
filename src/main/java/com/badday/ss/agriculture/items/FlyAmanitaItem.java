package com.badday.ss.agriculture.items;

import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class FlyAmanitaItem extends CropItem {
  public FlyAmanitaItem(int healAmount, float saturationModifier) {
    super(healAmount, saturationModifier);
  }

  @Override
  public String name() {
    return "FlyAmanita";
  }

  @Override
  protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
    super.onFoodEaten(stack, world, player);
    if (!world.isRemote) {
      player.addPotionEffect(new PotionEffect(Potion.confusion.id, (IC2.random.nextInt(20) + 1) * 20, 1));
      player.addPotionEffect(new PotionEffect(Potion.weakness.id, (IC2.random.nextInt(30) + 1) * 20, 1));
    }
  }
}
