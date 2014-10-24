package com.badday.ss.world.space;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import com.badday.ss.SS;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpaceProvider  extends WorldProvider {
		
	float gravity = 0.058F;
	
	public SpaceProvider() {
			this.worldChunkMgr = new WorldChunkManagerHell(SS.spaceBiome, 0.0F);
	        //this.worldChunkMgr = new WorldChunkManagerHell(SS.spaceBiome, 0.0F, 0.0F);
	        this.hasNoSky = false;
	}
	

	@Override
	public String getDimensionName() {
		return "Space";
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
		return "Entering Space Sector 13";
	}

	@Override
	public String getDepartMessage()
	{
		return "Leaving Space Sector 13";
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
    }

    @Override
    public boolean canSnowAt(int x, int y, int z,boolean f)
    {
        return false;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z)
    {
        return SS.spaceBiome;
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

    @Override
    protected void generateLightBrightnessTable()
    {
    	float var1 = 0.0F;

        for (int var2 = 0; var2 <= 15; ++var2)
        {
            float var3 = 1.0F - (float)var2 / 15.0F;
            this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
        }
    }
    
    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
    {
        return Vec3.createVectorHelper(0F, 0F, 0F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vec3 getFogColor(float par1, float par2)
    {
    	return Vec3.createVectorHelper(0F, 0F, 0F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isSkyColored()
    {
        return false;
    }
    
    @Override
    public IChunkProvider createChunkGenerator()
    {
        return new SpaceGenerator(worldObj, 45);
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
        return SS.instance.spaceDimID; // respawn on Earth
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
        return (dimensionId == 0 ? null : "SS/Space" + dimensionId);
    }
    
    
    
}
