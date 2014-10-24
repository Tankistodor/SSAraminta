package com.badday.ss.core.utils;

import com.badday.ss.world.space.SpaceProvider;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class WorldUtils {

	public static double getGravityForEntity(EntityLivingBase eLiving)
	{
		if (eLiving.worldObj.provider instanceof SpaceProvider)
		{
			final SpaceProvider customProvider = (SpaceProvider) eLiving.worldObj.provider;

			return 0.08D - customProvider.getGravity(); 
		}
		else
		{
			return 0.08D;
		}
	}
	
	
	public static double getItemGravity(EntityItem e)
	{
		return 0.03999999910593033D - 0.05999999910593033D/1.75D; /*
		if (e.worldObj.provider instanceof SSWorldProviderSpace)
		{
			final SSWorldProviderSpace customProvider = (SSWorldProviderSpace) e.worldObj.provider;
			//return 0.03999999910593033D - (customProvider instanceof IOrbitDimension ? 0.05999999910593033D : customProvider.getGravity()) / 1.75D;
			return 0.03999999910593033D - 0.05999999910593033D/1.75D;
		}
		else
		{
			return 0.03999999910593033D;
		}*/
	}

	public static double getItemGravity2(EntityItem e)
	{
		if (e.worldObj.provider instanceof SpaceProvider)
		{
			return 1.0D;
		}
		else
		{
			return 0.9800000190734863D;
		}
	}
	
}
