package com.badday.ss.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SS;
import com.badday.ss.core.utils.WorldUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockCardEncoder extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon iconFront;
	
	public SSBlockCardEncoder(String assetName) {
		super(Material.iron);
		this.setResistance(1000.0F);
		this.setHardness(330.0F);
		this.setBlockTextureName(SS.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
		disableStats();
		
		GameRegistry.registerTileEntity(SSTileEntityCardEncoder.class, assetName);
	}
	
	/**
	 * Overridden by {@link #createTileEntity(World, int)}
	 */
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new SSTileEntityCardEncoder();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.iconFront = par1IconRegister.registerIcon(SS.ASSET_PREFIX +"blockCardEncoderFront");
		this.blockIcon = par1IconRegister.registerIcon(SS.ASSET_PREFIX +"blockCardEncoderSide"); 
	}

	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {

		if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
			return this.blockIcon;

		if (side == ForgeDirection.SOUTH.ordinal())
			return this.iconFront;

		return this.blockIcon;
	}
	
	/**
	 * render in world
	 */
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
	
		SSTileEntityCardEncoder te = WorldUtils.get(world, x, y, z, SSTileEntityCardEncoder.class);
		if (te != null) {
			if (side == te.getSide())
				return this.iconFront;
		} else {
			System.out.println("CE no TE");
		}
		return this.blockIcon;
	}
	
	//It's not a normal block, so you need this too.
	@Override
    public boolean renderAsNormalBlock() {
            return false;
    }
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

		SSTileEntityCardEncoder tile = WorldUtils.get(world, x, y, z, SSTileEntityCardEncoder.class);

		if (tile != null && entityLiving instanceof EntityPlayer) {

			int facingDirection = MathHelper.floor_double(((entityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
			int newFacing = 0;
			if (facingDirection == 0) {
				newFacing = 2;
			}
			if (facingDirection == 1) {
				newFacing = 5;
			}
			if (facingDirection == 2) {
				newFacing = 3;
			}
			if (facingDirection == 3) {
				newFacing = 4;
			}
			System.out.println(newFacing+", "+facingDirection);
			tile.setSide(newFacing);
		}

	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		super.breakBlock(world, x, y, z, block, par6);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return false;
	}
	
	@Override
	public int damageDropped(int metadata) {
		return 0;
	}
	
	
}
