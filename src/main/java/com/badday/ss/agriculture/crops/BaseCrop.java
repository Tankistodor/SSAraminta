package com.badday.ss.agriculture.crops;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.crops.CropCard;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * Created by userad on 04/11/14.
 */
public abstract class BaseCrop extends CropCard {

  @Override
  @SideOnly(Side.CLIENT)
  public void registerSprites(IIconRegister iconRegister) {
    textures = new IIcon[maxSize()];

    for (int i = 1; i <= textures.length; i++) {
      textures[i-1] = iconRegister.registerIcon("ss:crops/blockCrop."+name()+"."+i);
    }
  }
}
