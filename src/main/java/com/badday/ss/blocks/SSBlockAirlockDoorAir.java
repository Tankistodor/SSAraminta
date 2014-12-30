package com.badday.ss.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSSealedBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockAirlockDoorAir extends Block implements ISSSealedBlock {

	public SSBlockAirlockDoorAir(String asset) {
		super(Material.iron);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + "blank");
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setResistance(SSConfig.ssBayCasingResistance * 100);
		this.setCreativeTab(SS.ssTab);
	}

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }
	
	  @Override
	    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	    {
	        float var5;
	        float var6;

	        Block frameID = SSConfig.ssBlockAirLockFrame;
	        Block sealID = SSConfig.ssBlockAir;

	        Block idXMin = world.getBlock(x - 1, y, z);
	        Block idXMax = world.getBlock(x + 1, y, z);
	        
	        Boolean top = (world.getBlock(x, y + 1, z) == frameID);

	        if (idXMin != frameID && idXMax != frameID && idXMin != sealID && idXMax != sealID)
	        {
	        	// on X axis
	            var5 = 0.25F;
	            var6 = 0.5F;
	            if (top) {
	            	this.setBlockBounds(0.5F - var5, 1.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
	            } else {
	            	this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 0.0F, 0.5F + var6);
	            }
	        }
	        else
	        {
	                var5 = 0.5F;
	                var6 = 0.25F;
	                if (top) {
	                	this.setBlockBounds(0.5F - var5, 1.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);	
	                } else {
	                	this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 0.0F, 0.5F + var6);
	                }
	                
	        }
	    }
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}
}
