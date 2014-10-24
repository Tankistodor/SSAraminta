package com.badday.ss.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSGasBlock;

public class SSBlockAirNormal extends Block implements ISSGasBlock {

	private final int TICK_RATE = 200;
	
	public SSBlockAirNormal(String assetName) {
		super(Material.circuits);
		this.setResistance(1000.0F);
        this.setHardness(0.0F);
        this.setBlockTextureName(SS.ASSET_PREFIX + assetName);
        this.setBlockName(assetName);
        this.setStepSound(new SoundType("sand", 0.0F, 1.0F));
        this.setLightLevel(0.0F);
	}

	
	@Override
    public boolean canReplace(World world, int x, int y, int z, int side, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
    {
        return true;
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public int getMobilityFlag()
    {
        return 1;
    }

    @Override
    public Item getItemDropped(int var1, Random var2, int var3)
    {
        return Item.getItemFromBlock(Blocks.air);
    }
    
    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
    
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
    
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        final Block block = par1IBlockAccess.getBlock(par2, par3, par4);
        if (block == this || block == SSConfig.ssBlockAir)
        {
            return false;
        }
        else
        {
            return block instanceof BlockAir && par5 >= 0 && par5 <= 5;
        }
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block idBroken)
    {
        /*if (Blocks.air != idBroken && idBroken != GCBlocks.brightAir)
        //Do nothing if an air neighbour was replaced (probably because replacing with breatheableAir)
        //but do a check if replacing breatheableAir as that could be dividing a sealed space
        {
            OxygenPressureProtocol.onEdgeBlockUpdated(world, new BlockVec3(x, y, z));
        }*/
    }
    
    // TANKE CHANGES
    
    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World par1World)
    {
        return TICK_RATE;
    }
    
    @Override
    public void onBlockAdded(World par1World, int x, int y, int z)
    {
        if (par1World.provider.dimensionId == SS.instance.spaceDimID)
        {
            par1World.scheduleBlockUpdate(x, y, z, this, this.tickRate(par1World)*4);
            if (SS.Debug) System.out.println("["+SS.MODNAME+"] onBlockAdded sheduler at " + x + "," + y + "," + z);
        }
        else
        {
            par1World.setBlockToAir(x, y, z);
        }
    }
    
    /**
     * Returns if this block is collidable. Args: x, y, z
     */
    @Override
    public boolean isCollidable()
    {
        return false;
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int x, int y, int z, Random par5Random)
    {
        int concentration = par1World.getBlockMetadata(x, y, z);
        boolean isInSpaceWorld = par1World.provider.dimensionId == SS.instance.spaceDimID;

        // Remove air block to vacuum block
        if (concentration <= 0 || !isInSpaceWorld)
        {
            //System.out.println("Killing air block");
            par1World.setBlock(x, y, z, Blocks.air, 0, 2); // replace our air block to vacuum block
        }
        else
        {
            System.out.println("Conc: current " + concentration + " new: " + (concentration - 1) + " to spread: " + (concentration - 2));
            // Try to spread the air
            spreadAirBlock(par1World, x, y, z, concentration);
        }

        par1World.scheduleBlockUpdate(x, y, z, this, this.TICK_RATE); //20
    }

    
    /*@Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par1IBlockAccess.getBlock(par2, par3, par4) == this)
        {
            return false;
        }
        else
        {
            final Block block = par1IBlockAccess.getBlock(par2, par3, par4);
            boolean var6 = false;
            
            if (block != null)
            {
                var6 = !block.isOpaqueCube();
            }

            final boolean var7 = block == Blocks.air;

            if ((var6 || var7) && par5 == 3 && !var6)
            {
                return true;
            }
            else if ((var6 || var7) && par5 == 4 && !var6)
            {
                return true;
            }
            else if ((var6 || var7) && par5 == 5 && !var6)
            {
                return true;
            }
            else if ((var6 || var7) && par5 == 2 && !var6)
            {
                return true;
            }
            else if ((var6 || var7) && par5 == 0 && !var6)
            {
                return true;
            }
            else if ((var6 || var7) && par5 == 1 && !var6)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }*/
    
    private void spreadAirBlock(World worldObj, int x, int y, int z, int concentration)
    {
        if (concentration <= 0)
        {
            return;
        }

        int mid_concentration;
        int block_count = 1;
        //final int K = 128;
        mid_concentration = worldObj.getBlockMetadata(x, y, z);// * K;

        // Count air in adjacent blocks
        if (worldObj.isAirBlock(x + 1, y, z))
        {
            block_count++;
            mid_concentration += worldObj.getBlockMetadata(x + 1, y, z);// * K;
        }

        if (worldObj.isAirBlock(x - 1, y, z))
        {
            block_count++;
            mid_concentration += worldObj.getBlockMetadata(x - 1, y, z);// * K;
        }

        if (worldObj.isAirBlock(x, y + 1, z))
        {
            block_count++;
            mid_concentration += worldObj.getBlockMetadata(x, y + 1, z);// * K;
        }

        if (worldObj.isAirBlock(x, y - 1, z))
        {
            block_count++;
            mid_concentration += worldObj.getBlockMetadata(x, y - 1, z);// * K;
        }

        if (worldObj.isAirBlock(x, y, z + 1))
        {
            block_count++;
            mid_concentration += worldObj.getBlockMetadata(x, y, z + 1);// * K;
        }

        if (worldObj.isAirBlock(x, y, z - 1))
        {
            block_count++;
            mid_concentration += worldObj.getBlockMetadata(x, y, z - 1);// * K;
        }

        mid_concentration = (int) Math.floor(mid_concentration * 1.0f / block_count);
        setNewAirBlockWithConcentration(worldObj, x, y, z, mid_concentration);// / K);

        // Check and setup air to adjacent blocks
        if (worldObj.isAirBlock(x + 1, y, z) && (mid_concentration > worldObj.getBlockMetadata(x + 1, y, z)))// * K))
        {
           setNewAirBlockWithConcentration(worldObj, x + 1, y, z, mid_concentration);// / K);
        }

        if (worldObj.isAirBlock(x - 1, y, z) && (mid_concentration > worldObj.getBlockMetadata(x - 1, y, z)))// * K))
        {
           setNewAirBlockWithConcentration(worldObj, x - 1, y, z, mid_concentration);// / K);
        }

        if (worldObj.isAirBlock(x, y + 1, z) && (mid_concentration > worldObj.getBlockMetadata(x, y + 1, z)))// * K))
        {
           setNewAirBlockWithConcentration(worldObj, x, y + 1, z, mid_concentration);// / K);
        }

        if (worldObj.isAirBlock(x, y - 1, z) && (mid_concentration > worldObj.getBlockMetadata(x, y - 1, z)))// * K))
        {
           setNewAirBlockWithConcentration(worldObj, x, y - 1, z, mid_concentration);//  / K);
        }

        if (worldObj.isAirBlock(x, y, z + 1) && (mid_concentration > worldObj.getBlockMetadata(x, y, z + 1)))// * K))
        {
           setNewAirBlockWithConcentration(worldObj, x, y, z + 1, mid_concentration);// / K);
        }

        if (worldObj.isAirBlock(x, y, z - 1) && (mid_concentration > worldObj.getBlockMetadata(x, y, z - 1)))// * K))
        {
           setNewAirBlockWithConcentration(worldObj, x, y, z - 1, mid_concentration);// / K);
        }
    }
    
    private void setNewAirBlockWithConcentration(World worldObj, int x, int y, int z, int concentration)
    {
        worldObj.setBlock(x, y, z, this, concentration, 2);
    }

}
