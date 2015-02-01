package com.badday.ss.world.island;

import net.minecraft.world.biome.BiomeGenBase;

public class BiomeIsland extends BiomeGenBase
{
	
	public static final BiomeGenBase biom = new BiomeIsland(105).setBiomeName("Island Sky");
	
    public BiomeIsland(int par1)
    {
        super(par1);
        
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        
        this.setBiomeName("Island Sky");
        
        this.theBiomeDecorator.treesPerChunk = 0;
        //this.temperature = 1F;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.theBiomeDecorator.grassPerChunk = 0;
        //this.biomeName = "Island";
        this.rainfall = 0F;
    }

    @Override
    public float getSpawningChance()
    {
        return 0;
    }

    @Override
    public boolean canSpawnLightningBolt()
    {
        return false;
    }

    @Override
    public boolean getEnableSnow()
    {
        return false;
    }
}
