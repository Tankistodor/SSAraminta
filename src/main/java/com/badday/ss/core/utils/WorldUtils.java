package com.badday.ss.core.utils;

import java.util.Iterator;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.core.atmos.SSFindSealedBay;
import com.badday.ss.world.space.SpaceProvider;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	
	@Deprecated
	public static SSFindSealedBay getGasPressure(World world, int x, int y, int z) {
		/*TileEntity tt = world.getTileEntity(x, y, z);
		if (tt instanceof SSTileEntityGasBlock) {
			SSTileEntityGasBlock t = (SSTileEntityGasBlock) tt;
			if (t != null) {
				if (t.getTileEntityAirVent() != null) {
					SSTileEntityAirVent vent = t.getTileEntityAirVent();
					if (vent.findSealedBay != null)
						return vent.findSealedBay;
				}
			}
		}*/
		return null;
	}
	
	/** Returns whether the TileEntity at the position is an instance of tileClass. */
	public static <T> boolean is(IBlockAccess world, int x, int y, int z, Class<T> tileClass) {
		return tileClass.isInstance(world.getTileEntity(x, y, z));
	}
	/** Returns the TileEntity at the position if it's an instance of tileClass, null if not. */
	public static <T> T get(IBlockAccess world, int x, int y, int z, Class<T> tileClass) {
		TileEntity t = world.getTileEntity(x, y, z);
		return (tileClass.isInstance(t) ? (T)t : null);
	}
	
	@SideOnly(Side.CLIENT)
    public static EntityClientPlayerMP getPlayerBaseClientFromPlayer(EntityPlayer player, boolean ignoreCase)
    {
        EntityClientPlayerMP clientPlayer = FMLClientHandler.instance().getClientPlayerEntity();

        if (clientPlayer == null && player != null)
        {
        	System.out.println("Warning: Could not find player base client instance for player " + player.getGameProfile().getName());
        }

        return clientPlayer;
    }
	
	public static EntityPlayerMP getPlayerBaseServerFromPlayer(EntityPlayer player, boolean ignoreCase)
    {
        if (player == null)
        {
            return null;
        }

        if (player instanceof EntityPlayerMP)
        {
            return (EntityPlayerMP) player;
        }

        return getPlayerBaseServerFromPlayerUsername(player.getCommandSenderName(), ignoreCase);
    }

	public static EntityPlayerMP getPlayerBaseServerFromPlayerUsername(String username, boolean ignoreCase)
    {
        MinecraftServer server = MinecraftServer.getServer();

        if (server != null)
        {
                Iterator iterator = server.getConfigurationManager().playerEntityList.iterator();
                EntityPlayerMP entityplayermp;

                do
                {
                    if (!iterator.hasNext())
                    {
                        return null;
                    }

                    entityplayermp = (EntityPlayerMP) iterator.next();
                }
                while (!entityplayermp.getCommandSenderName().equalsIgnoreCase(username));

                return entityplayermp;
            
        }

        System.out.println("Warning: Could not find player base server instance for player " + username);

        return null;
    }
	
	public static int to32BitColor(int a, int r, int g, int b)
    {
        a = a << 24;
        r = r << 16;
        g = g << 8;

        return a | r | g | b;
    }
	
}
