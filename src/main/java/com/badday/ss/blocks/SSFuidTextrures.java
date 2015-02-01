package com.badday.ss.blocks;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSFuidTextrures
{
    public static void init()
    {
        MinecraftForge.EVENT_BUS.register(new SSFuidTextrures());
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0)
        {
            SSConfig.fluidMethaneGas.setIcons(event.map.registerIcon(SS.ASSET_PREFIX + "fluids/MethaneGas"));
            SSConfig.fluidAtmosphericGases.setIcons(event.map.registerIcon(SS.ASSET_PREFIX + "fluids/AtmosphericGases"));
            SSConfig.fluidLiquidMethane.setIcons(event.map.registerIcon(SS.ASSET_PREFIX + "fluids/LiquidMethane"));
            SSConfig.fluidLiquidOxygen.setIcons(event.map.registerIcon(SS.ASSET_PREFIX + "fluids/LiquidOxygen"));
            SSConfig.fluidOxygenGas.setIcons(event.map.registerIcon(SS.ASSET_PREFIX + "fluids/OxygenGas"));
            SSConfig.fluidLiquidNitrogen.setIcons(event.map.registerIcon(SS.ASSET_PREFIX + "fluids/LiquidNitrogen"));
            SSConfig.fluidLiquidArgon.setIcons(event.map.registerIcon(SS.ASSET_PREFIX + "fluids/LiquidArgon"));
            SSConfig.fluidNitrogenGas.setIcons(event.map.registerIcon(SS.ASSET_PREFIX + "fluids/NitrogenGas"));
            FluidRegistry.getFluid("hydrogen").setIcons(event.map.registerIcon(SS.ASSET_PREFIX + "fluids/HydrogenGas"));
        }
    }

}
