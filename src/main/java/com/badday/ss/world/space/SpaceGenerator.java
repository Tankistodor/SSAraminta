package com.badday.ss.world.space;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;

public class SpaceGenerator extends ChunkProviderGenerate implements IChunkProvider {
	
	
	private Random rand;
    private BiomeGenBase[] biomesForGeneration = new BiomeGenBase[1];

    /**
     * Reference to the World object.
     */
    private World worldObj;

    public SpaceGenerator(World par1World, long par2)
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
		final Block[] blocks = new Block[32768*2];
		final byte[] meta = new byte[32768*2];

		final Chunk var4 = new Chunk(this.worldObj, blocks, meta, par1, par2);
		
		var4.generateSkylightMap();
		return var4;
	}
    
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
			//this.worldObj.setBlock(k, 64, l, 41, 0, 3);

			//final TileEntity var8 = this.worldObj.getBlockTileEntity(k, 64, l);

			/*if (var8 instanceof IMultiBlock)
			{
				((IMultiBlock) var8).onCreate(new Vector3(k, 64, l));
			}*/

			new WorldGenSpaceStation().generate(this.worldObj, this.rand, k - 10, 62, l - 3);
		}
		BlockSand.fallInstantly = false;
	}
    
	@Override
	public String makeString()
	{
		return "OrbitLevelSource";
	}

}
