package com.badday.ss.world.island;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

//import micdoodle8.mods.galacticraft.api.vector.Vector3;

//import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;

public class IslandGenerator extends ChunkProviderGenerate implements IChunkProvider {
	
	
	private Random rand;
    private BiomeGenBase[] biomesForGeneration = new BiomeGenBase[1];

    /**
     * Reference to the World object.
     */
    private World worldObj;

    public IslandGenerator(World par1World, long par2)
    {
        super(par1World, par2, false);
        rand = new Random();
        this.worldObj = par1World;
    }
    
	@Override
	public boolean unloadQueuedChunks()
	{
		return false;
	}

	@Override
	public int getLoadedChunkCount()
	{
		return 0;
	}

	@Override
	public boolean saveChunks(boolean var1, IProgressUpdate var2)
	{
		return true;
	}

	@Override
	public boolean canSave()
	{
		return true;
	}
    
	@Override
    public Chunk provideChunk(int par1, int par2)
    {
        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
        final Block[] ids = new Block[32768];
        Arrays.fill(ids, Blocks.air);
        final byte[] meta = new byte[32768];

        final Chunk var4 = new Chunk(this.worldObj, ids, meta, par1, par2);

        final byte[] biomesArray = var4.getBiomeArray();
        for (int i = 0; i < biomesArray.length; ++i)
        {
            biomesArray[i] = (byte) BiomeIsland.biom.biomeID;
        }


        var4.generateSkylightMap();
        return var4;
    }
	
    /*@Override
	public Chunk provideChunk(int par1, int par2)
	{
		this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
		final Block[] blocks = new Block[32768*2];
		final byte[] meta = new byte[32768*2];

		final Chunk var4 = new Chunk(this.worldObj, blocks, meta, par1, par2);
		
		var4.generateSkylightMap();
		return var4;
	}*/
    
	@Override
	public boolean chunkExists(int par1, int par2)
	{
		return true;
	}

	@Override
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
	{
		BlockSand.fallInstantly = true;
		final int k = par2 * 16;
		final int l = par3 * 16;
		this.rand.setSeed(this.worldObj.getSeed());
		final long i1 = this.rand.nextLong() / 2L * 2L + 1L;
		final long j1 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed(par2 * i1 + par3 * j1 ^ this.worldObj.getSeed());
		if (k == 0 && l == 0)
		{
			//new WorldGenSpaceStation().generate(this.worldObj, this.rand, k - 10, 62, l - 3);
		}
		BlockSand.fallInstantly = false;
	}
	
    
	@Override
	public String makeString()
	{
		return "IslandLevelSource";
	}
	
	private int getIndex(int x, int y, int z)
    {
        return (x * 16 + z) * 256 + y;
    }
	
	@Override
    public void replaceBlocksForBiome(int par1, int par2, Block[] arrayOfIDs, byte[] arrayOfMeta, BiomeGenBase[] par4ArrayOfBiomeGenBase)
    {
        final int var5 = 20;
        final float var6 = 0.03125F;
        
        for (int var8 = 0; var8 < 16; ++var8)
        {
            for (int var9 = 0; var9 < 16; ++var9)
            {

                for (int var16 = 17 - 1; var16 >= 0; --var16)
                {
                    final int index = this.getIndex(var8, var16, var9);

                    
                        final Block var18 = arrayOfIDs[index];

                        if (Blocks.lava == var18)
                        {
                        	arrayOfIDs[index] = Blocks.air;
                        }

                }
            }
        }
    }
	

}
