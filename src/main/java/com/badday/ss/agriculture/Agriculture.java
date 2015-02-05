package com.badday.ss.agriculture;


import com.badday.ss.agriculture.items.*;
import com.badday.ss.agriculture.util.AgricultureConfig;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.crops.Crops;
import com.badday.ss.agriculture.crops.*;
import net.minecraft.item.Item;

public class Agriculture {

  public static Item flyAmanitaItem;
  public static Item chanterelleItem;
  public static Item destroyingAngelItem;
  public static Item glowshroomItem;
  public static Item libertyCapItem;
  public static Item plumpHelmetItem;


  public static void init() {

    flyAmanitaItem = new FlyAmanitaItem(AgricultureConfig.FlyAmanitaMeal, AgricultureConfig.FlyAmanitaSaturation);
    chanterelleItem = new ChanterelleItem(AgricultureConfig.ChanterelleMeal, AgricultureConfig.ChanterelleSaturation);
    destroyingAngelItem = new DestroyingAngelItem(AgricultureConfig.DestroyingAngelMeal, AgricultureConfig.DestroyingAngelSaturation);
    glowshroomItem = new GlowshroomItem(AgricultureConfig.GlowshroomMeal, AgricultureConfig.GlowshroomSaturation);
    libertyCapItem = new LibertyCapItem(AgricultureConfig.LibertyCapMeal, AgricultureConfig.LibertyCapSaturation);
    plumpHelmetItem = new PlumpHelmetItem(AgricultureConfig.PlumpHelmetMeal, AgricultureConfig.PlumpHelmetSaturation);


    Crops.instance.registerCrop(new FlyAmanita(), 100);
    Crops.instance.registerCrop(new Chanterelle(), 101);
    Crops.instance.registerCrop(new DestroyingAngel(), 102);
    Crops.instance.registerCrop(new Glowshroom(),103);
    Crops.instance.registerCrop(new LibertyCap(),104);
    Crops.instance.registerCrop(new PlumpHelmet(),105);


    GameRegistry.registerItem(flyAmanitaItem, "ss.agriculture.fly_amanita");
    GameRegistry.registerItem(chanterelleItem, "ss.agriculture.chanterelle");
    GameRegistry.registerItem(destroyingAngelItem, "ss.agriculture.destroying_angel");
    GameRegistry.registerItem(glowshroomItem, "ss.agriculture.glowshroom");
    GameRegistry.registerItem(libertyCapItem, "ss.agriculture.libertycap");
    GameRegistry.registerItem(plumpHelmetItem, "ss.agriculture.plumphelmet");
  }
}
