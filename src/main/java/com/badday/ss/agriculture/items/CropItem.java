package com.badday.ss.agriculture.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemFood;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class CropItem extends ItemFood {

  public CropItem(int healAmount, float saturationModifier) {
    super(healAmount, saturationModifier, false);
    this.hasSubtypes = false;
    setUnlocalizedName("ss.crop_item." + name());
    setMaxStackSize(64);
  }

  public abstract String name();

  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister) {
    this.itemIcon = iconRegister
        .registerIcon("ss:crops/" + name());
  }


}
