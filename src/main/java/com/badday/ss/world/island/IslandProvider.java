package com.badday.ss.world.island;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IslandProvider  extends WorldProvider {
		
	float gravity = 0.058F;
	
	public IslandProvider() {
		this.worldChunkMgr = new IslandChunkManager();
		this.hasNoSky = false;
		this.isHellWorld = false;
	}
	
	@Override
	/** tells Minecraft to use our new Terrain Generator */
	public IChunkProvider createChunkGenerator() {
		return new IslandGenerator(this.worldObj, this.worldObj.getSeed());
	}
	
	@Override
	/** tells Minecraft to use our new WorldChunkManager **/
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new IslandChunkManager();
		//this.dimensionId = SS.islandProviderID;
	}

	@Override
	public String getDimensionName() {
		return "Island";
	}

	@Override
	public void updateWeather()
	{
		this.worldObj.getWorldInfo().setRainTime(0);
		this.worldObj.getWorldInfo().setRaining(false);
		this.worldObj.getWorldInfo().setThunderTime(0);
		this.worldObj.getWorldInfo().setThundering(false);
	}
	
	@Override
	public String getWelcomeMessage()
	{
		return "Entering Sky Island";
	}

	@Override
	public String getDepartMessage()
	{
		return "Leaving Sky Island";
	}

	public float getGravity()
	{
		return this.gravity;
	}

	@Override
	public int getHeight()
	{
		return 800;
	}
	
	/*
	@Override
    public boolean isSurfaceWorld()
    {
        return false;
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 1;
    }

    @Override
    public double getHorizon()
    {
        return 1;
    }*/
	
	
	@Override
    public double getHorizon()
    {
        return 44.0D;
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 44;
    }

    @Override
    public boolean isSurfaceWorld()
    {
        return true;
    }
	

    @Override
    public boolean canSnowAt(int x, int y, int z,boolean f)
    {
        return false;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z)
    {
        return SS.islandBiome;
    }

    @Override
    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful)
    {
        super.setAllowedSpawnTypes(false, false);
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3)
    {
    	final int var4 = (int) (par1 % 24000L);
		float var5 = (var4 + par3) / 24000.0F - 0.25F;

		if (var5 < 0.0F)
		{
			++var5;
		}

		if (var5 > 1.0F)
		{
			--var5;
		}

		final float var6 = var5;
		var5 = 1.0F - (float) ((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return var5;
        //return 0F;
    }
    
    
    /*@Override
    protected void generateLightBrightnessTable()
    {
    	float var1 = 12.0F;

        for (int var2 = 0; var2 <= 15; ++var2)
        {
            float var3 = 1.0F - (float)var2 / 15.0F;
            this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
        }
    }*/
    
    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
    {
   		return worldObj.getSkyColorBody(cameraEntity, partialTicks);
    }

    @Override
	@SideOnly(Side.CLIENT)
	public Vec3 getFogColor(float par1, float par2)
	{
		 float f2 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

	        if (f2 < 0.0F)
	        {
	            f2 = 0.0F;
	        }

	        if (f2 > 1.0F)
	        {
	            f2 = 1.0F;
	        }

	        float f3 = 0.7529412F;
	        float f4 = 0.84705883F;
	        float f5 = 1.0F;
	        f3 *= f2 * 0.94F + 0.06F;
	        f4 *= f2 * 0.94F + 0.06F;
	        f5 *= f2 * 0.91F + 0.09F;
	        return Vec3.createVectorHelper((double)f3, (double)f4, (double)f5);
	}

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isSkyColored()
    {
    	return true;
    }
    
    @Override
    public boolean getWorldHasVoidParticles()
    {
        return false;
    }

    @Override
    public boolean isDaytime()
    {
        return true;
    }

    @Override
    public boolean canDoLightning(Chunk chunk)
    {
        return false;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return false;
    }
    
    @Override
    public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
    {
        return false;
    }
    
    @Override
    public int getRespawnDimension(EntityPlayerMP player)
    {
        return SS.instance.islandDimID; // respawn on Earth
    }
    
    
    @Override
    public ChunkCoordinates getEntrancePortalLocation()
    {
        return null;
    }
    
    @Override
    public boolean canRespawnHere()
    {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public float getStarBrightness(float par1)
    {
    	/*final float var2 = this.worldObj.getCelestialAngle(par1);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float) Math.PI * 2.0F) * 2.0F + 0.25F);

		if (var3 < 0.0F)
		{
			var3 = 0.0F;
		}

		if (var3 > 1.0F)
		{
			var3 = 1.0F;
		}

		return var3 * var3 * 0.5F + 0.3F; */
        return 1.0F;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public String getSaveFolder()
    {
        return (dimensionId == 0 ? null : "SS/Island" + dimensionId);
    }
    
    
    
}
