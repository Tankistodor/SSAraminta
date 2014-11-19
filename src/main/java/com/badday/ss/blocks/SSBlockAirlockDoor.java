package com.badday.ss.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSSealedBlock;
import com.badday.ss.core.utils.BlockVec3;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockAirlockDoor extends Block implements ISSSealedBlock {

	public IIcon sideIcon;
	public IIcon topLeftIcon;
	public IIcon topRightIcon;
	public IIcon bottomLeftIcon;
	public IIcon bottomRightIcon;
	
	public SSBlockAirlockDoor(String asset) {
		super(Material.iron);
		asset = "bayDoor";
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)));
		
		this.blockIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)));
		this.sideIcon= iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_side");
		this.topLeftIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_topLeft");
		this.bottomLeftIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_bottomLeft");
		this.topRightIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_topRight");
		this.bottomRightIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5))+"_bottomRight");
	}
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		return this.blockIcon;
	}
	
	
	@Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {

		// return bottomLeftIcon;
		boolean sDown = false;
		boolean sUp = false;
		boolean sLeft = false;
		boolean sRight = false;
		
		for (final ForgeDirection orientation : ForgeDirection.values()) {
			if (orientation != ForgeDirection.UNKNOWN) {
				final BlockVec3 vector = new BlockVec3(x, y, z);
				vector.modifyPositionFromSide(orientation);
				Block connection = vector.getBlock(world);

				if (connection != null && connection.equals(SSConfig.ssBlockAirLockDoor)) {
					
					if (orientation.offsetY == -1) {
						sUp = true;
					} else if (orientation.offsetY == 1) {
						sDown = true;
					}

					if (orientation.offsetX == 1) {
						if (side == 3)
							sLeft = true;
						else if (side == 2)
							sRight = true;
					} else if (orientation.offsetX == -1) {
						if (side == 3)
							sRight = true;
						else if (side == 2)
							sLeft = true;
					} else if (orientation.offsetZ == 1) {
						if (side == 4)
							sLeft = true;
						else if (side == 5)
							sRight = true;
					} else if (orientation.offsetZ == -1) {
						if (side == 4)
							sRight = true;
						else if (side == 5)
							sLeft = true;
					}
				
				}
			}
		}
		
		if (sUp && sLeft) return this.topLeftIcon;
		if (sUp && sRight) return this.topRightIcon;
		if (sDown && sLeft) return this.bottomLeftIcon;
		if (sDown && sRight) return this.bottomRightIcon;
		return this.sideIcon;
	
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
        Block sealID = SSConfig.ssBlockAirLockDoor;

        Block idXMin = world.getBlock(x - 1, y, z);
        Block idXMax = world.getBlock(x + 1, y, z);

        if (idXMin != frameID && idXMax != frameID && idXMin != sealID && idXMax != sealID)
        {
        	// on X axis
            var5 = 0.25F;
            var6 = 0.5F;
            this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
        }
        else
        {
        	// on X axis
        	
        	/*// Horisontal
            int adjacentCount = 0;

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            {
                if (dir != ForgeDirection.UP && dir != ForgeDirection.DOWN)
                {
                    BlockVec3 thisVec = new BlockVec3(x, y, z);
                    thisVec = thisVec.modifyPositionFromSide(dir);
                    Block blockID = thisVec.getBlock(world);

                    if (blockID == SSConfig.ssBlockAirLockFrame || blockID == SSConfig.ssBlockAirLockDoor)
                    {
                        adjacentCount++;
                    }
                }
            }

            if (adjacentCount == 4)
            {
                this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 0.75F, 1.0F);
            }
            else*/
            {
                var5 = 0.5F;
                var6 = 0.25F;
                this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
            }
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return true;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
	
}
