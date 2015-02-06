package com.badday.ss.agriculture;


import com.badday.ss.agriculture.crops.*;
import com.badday.ss.agriculture.items.*;
import ic2.api.crops.Crops;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import ic2.core.Ic2Items;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.badday.ss.agriculture.util.AgricultureConfig;

import cpw.mods.fml.common.registry.GameRegistry;

public class Agriculture {

  public static Item flyAmanitaItem;
  public static Item chanterelleItem;
  public static Item destroyingAngelItem;
  public static Item glowshroomItem;
  public static Item libertyCapItem;
  public static Item plumpHelmetItem;
  public static Item diamondFruitItem;


  public static void init() {

    flyAmanitaItem = new FlyAmanitaItem(AgricultureConfig.FlyAmanitaMeal, AgricultureConfig.FlyAmanitaSaturation);
    chanterelleItem = new ChanterelleItem(AgricultureConfig.ChanterelleMeal, AgricultureConfig.ChanterelleSaturation);
    destroyingAngelItem = new DestroyingAngelItem(AgricultureConfig.DestroyingAngelMeal, AgricultureConfig.DestroyingAngelSaturation);
    glowshroomItem = new GlowshroomItem(AgricultureConfig.GlowshroomMeal, AgricultureConfig.GlowshroomSaturation);
    libertyCapItem = new LibertyCapItem(AgricultureConfig.LibertyCapMeal, AgricultureConfig.LibertyCapSaturation);
    plumpHelmetItem = new PlumpHelmetItem(AgricultureConfig.PlumpHelmetMeal, AgricultureConfig.PlumpHelmetSaturation);
    diamondFruitItem = new DiamondFruit(AgricultureConfig.DiamondFruitMeal, AgricultureConfig.DiamondFruitSaturation);

    Crops.instance.registerCrop(new FlyAmanita(), 100);
    Crops.instance.registerCrop(new Chanterelle(), 101);
    Crops.instance.registerCrop(new DestroyingAngel(), 102);
    Crops.instance.registerCrop(new Glowshroom(),103);
    Crops.instance.registerCrop(new LibertyCap(),104);
    Crops.instance.registerCrop(new PlumpHelmet(),105);

    //Growable diamonds
    Crops.instance.registerCrop(new Diamonda(), 106);

    GameRegistry.registerItem(flyAmanitaItem, "ss.agriculture.fly_amanita");
    GameRegistry.registerItem(chanterelleItem, "ss.agriculture.chanterelle");
    GameRegistry.registerItem(destroyingAngelItem, "ss.agriculture.destroying_angel");
    GameRegistry.registerItem(glowshroomItem, "ss.agriculture.glowshroom");
    GameRegistry.registerItem(libertyCapItem, "ss.agriculture.libertycap");
    GameRegistry.registerItem(plumpHelmetItem, "ss.agriculture.plumphelmet");
    GameRegistry.registerItem(diamondFruitItem, "ss.agriculture.diamond_fruit");

    Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(flyAmanitaItem), AgricultureConfig.CropsToBiomass), null, Ic2Items.biochaff);
    Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(chanterelleItem), AgricultureConfig.CropsToBiomass), null, Ic2Items.biochaff);
    Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(destroyingAngelItem), AgricultureConfig.CropsToBiomass), null, Ic2Items.biochaff);
    Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(glowshroomItem), AgricultureConfig.CropsToBiomass), null, Ic2Items.biochaff);
    Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(libertyCapItem), AgricultureConfig.CropsToBiomass), null, Ic2Items.biochaff);
    Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(plumpHelmetItem), AgricultureConfig.CropsToBiomass), null, Ic2Items.biochaff);


    Recipes.extractor.addRecipe(new RecipeInputItemStack(new ItemStack(glowshroomItem), AgricultureConfig.GlowshroomsToGlowstone), null, new ItemStack(Items.glowstone_dust, 1));

    GameRegistry.addSmelting(diamondFruitItem, new ItemStack(Items.diamond), 10f);
  }
}
