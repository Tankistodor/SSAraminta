package com.badday.ss.agriculture.crops;

import ic2.api.crops.ICropTile;
import ic2.core.IC2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Created by userad on 04/11/14.
 */
public class DestroyingAngel extends BaseCrop {
  @Override
  public String name() {
    return "Destroying Angel";
  }

  @Override
  public int tier() {
    return 1;
  }

  @Override
  public int stat(int n) {
    switch (n) {
      case 0:
        return 0;
      case 1:
        return 4;
      case 2:
        return 4;
      case 3:
        return 0;
      case 4:
        return 3;
    }
    return 0;
  }

  @Override
  public String[] attributes() {
    return new String[] { "Mushroom", "Poison", "White", "Food", "Toxic"};
  }

  @Override
  public int getOptimalHavestSize(ICropTile crop) {
    return 0;
  }

  @Override
  public boolean canBeHarvested(ICropTile crop) {
    return false;
  }

  @Override
  public ItemStack getGain(ICropTile crop) {
    return null;
  }

  //We are toxic!
  @Override
  public boolean onEntityCollision(ICropTile crop, Entity entity)
  {
    if (entity instanceof EntityPlayer) {
      if ((((EntityPlayer) entity).isSneaking()) && (IC2.random.nextInt(50) != 0)) {
        return super.onEntityCollision(crop, entity);
      }
    }
    if ((entity instanceof EntityLivingBase)) {
      ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.poison.id, (IC2.random.nextInt(10) + 5) * 20, 0));
      return super.onEntityCollision(crop, entity);
    }
    return false;
  }
}
