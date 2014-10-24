package com.badday.ss.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.badday.ss.SS;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author KolesnikovAK
 * 
 *         Seal gases on bay
 * 
 */
public class SSBlockAirVent extends BlockContainer {

	private IIcon[] iconBuffer;
	private int ICON_INACTIVE_SIDE = 0, ICON_BOTTOM = 1, ICON_SIDE_ACTIVATED = 2;

	public SSBlockAirVent(String assetName) {
		super(Material.rock);
		this.setResistance(1000.0F);
		this.setHardness(330.0F);
        this.setBlockTextureName(SS.ASSET_PREFIX + assetName);
        this.setBlockName(assetName);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
		disableStats();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBuffer = new IIcon[3];
		iconBuffer[ICON_INACTIVE_SIDE] = par1IconRegister.registerIcon("ss:airgenSideInactive");
		iconBuffer[ICON_BOTTOM] = par1IconRegister.registerIcon("ss:contBottom");
		iconBuffer[ICON_SIDE_ACTIVATED] = par1IconRegister.registerIcon("ss:airgenSideActive");
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		if (side == 0 || side == 1) {
			if (metadata == 0) // Inactive state
			{
				return iconBuffer[ICON_INACTIVE_SIDE];
			} else if (metadata == 1) {
				return iconBuffer[ICON_SIDE_ACTIVATED];
			}
		}

		return iconBuffer[ICON_BOTTOM];
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new SSTileEntityAirVent(world,meta);
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
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            return false;
        }

        SSTileEntityAirGenerator gen = (SSTileEntityAirGenerator)par1World.getTileEntity(par2, par3, par4);

        if (gen != null)
        {
            //par5EntityPlayer.addChatMessage("[AirGen] Energy level: " + gen.getCurrentEnergyValue() + " Eu");
        	par5EntityPlayer.addChatComponentMessage(new ChatComponentText("[AirVent]"));
        }

        return true;
    }
    
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        TileEntity te = par1World.getTileEntity(par2, par3, par4);

        if (te != null)
        {
            te.invalidate();
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

}
