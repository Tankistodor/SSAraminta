package com.badday.ss.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Гашение урона при падении с джетпаком или квантовыми бутсами
 * @author Cr0s
 */
public class AntiFallDamage
{
    private final int JETPACK_ID = 29954;
    private final int QUANTUM_BOOTS_ID = 29915;

    @SubscribeEvent
    public void livingFall(LivingFallEvent event)
    {
        EntityLivingBase entity = event.entityLiving;
        float distance = event.distance;

        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            int check = MathHelper.ceiling_float_int(distance - 3.0F);

            if (check > 0)   // Падение может нанести урон
            {
                // Проверяем наличие защиты
                if ((player.getCurrentArmor(0) != null && player.getCurrentArmor(0).getUnlocalizedName().equals("itemArmorQuantumBoots")) ||
                        (player.getCurrentArmor(2) != null && player.getCurrentArmor(2).getUnlocalizedName().equals("itemArmorJetpackElectric")) ||
                        (player.getCurrentArmor(2) != null && player.getCurrentArmor(2).getUnlocalizedName().equals("itemArmorJetpackElectric")))
                {
                    event.setCanceled(true); // Блокируем падение, если защита есть
                }
            }
        }
    }
}
