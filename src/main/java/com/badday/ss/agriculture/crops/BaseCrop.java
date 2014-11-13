package com.badday.ss.agriculture.crops;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * Created by userad on 04/11/14.
 */
public abstract class BaseCrop extends CropCard {

  @Override
  public String discoveredBy() {
    return "UserAd";
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerSprites(IIconRegister iconRegister) {
    textures = new IIcon[maxSize()];

    for (int i = 1; i <= textures.length; i++) {
      textures[i-1] = iconRegister.registerIcon("ss:crops/blockCrop."+name()+"."+i);
    }
  }

  @Override
  public int maxSize() {
    return 4;
  }

  @Override
  public boolean canGrow(ICropTile crop) {
    return crop.getSize() < 4;
  }

  @Override
  public int getOptimalHavestSize(ICropTile crop) {
    return maxSize();
  }


}
