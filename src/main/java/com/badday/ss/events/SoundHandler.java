package com.badday.ss.events;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.SoundLoadEvent;

import com.badday.ss.SS;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SoundHandler
{
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onSoundLoad(SoundLoadEvent event)
    {
        try
        {
            System.out.println("[" + SS.MODNAME + "] Registering sound files...");
            //TODO: Fix sound
            /*event.manager.addSound("ss:welder1w.ogg");
            event.manager.addSound("ss:welder2w.ogg");
            event.manager.addSound("ss:screwdriver1w.ogg");
            event.manager.addSound("ss:screwdriver2w.ogg");*/
        }
        catch (Exception e)
        {
            System.err.println("Failed to register sound: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}