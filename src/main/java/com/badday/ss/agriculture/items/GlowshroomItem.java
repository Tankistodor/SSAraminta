package com.badday.ss.agriculture.items;

import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

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

  @Override
  protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
    super.onFoodEaten(stack, world, player);
    if (!world.isRemote) {
      player.addPotionEffect(new PotionEffect(Potion.blindness.id, (IC2.random.nextInt(20) + 1) * 20, 1));
    }
  }
}
