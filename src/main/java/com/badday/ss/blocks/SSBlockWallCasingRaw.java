package com.badday.ss.blocks;

import ic2.core.util.StackUtil;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockWallCasingRaw extends SSMetaBlock {

	public SSBlockWallCasingRaw() {
		super("blockWallCasingRaw", Material.rock);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		disableStats();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		this.icons = new IIcon[SSConfig.ssWallCasingRaw_unlocalizedName.length];
		for (int i = 0; i < SSConfig.ssWallCasingRaw_unlocalizedName.length; i++) {
			this.icons[i] = icon.registerIcon(SS.ASSET_PREFIX
					+ SSConfig.ssWallCasingRaw_unlocalizedName[i]);
			this.metaID = i; 
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName().substring(5);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs tab, List list) {
		for (int i = 0; i < SSConfig.ssWallCasingRaw_unlocalizedName.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	/* 
	 * public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
	 * {
	 * }
	 */

	 public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float a, float b, float c)
	 {
	    if (world.isRemote) return false;
	    if (entityplayer.getCurrentEquippedItem() != null) {

	    	if (SS.Debug) System.out.println("["+SS.MODNAME+"] act " + entityplayer.getCurrentEquippedItem().getUnlocalizedName());

	    	/*
	    	 * Tier  MetaID  Монтаж     Демонтаж  
	    	 * 1     0       отвертка   ключ
	    	 * 2     1       резина     отвертка
	    	 * 3     2       отвертка   горелка
	    	 * 4     3       пластина   отвертка
	    	 * 5     4       ключ       горелка
	    	 * 6     5       горелка    ключ
	    	 * cas   0                  горелка
	    	 */
	    	
	    	int metaData = world.getBlockMetadata(x, y, z);
	    	if (metaData == 0) {
	    		if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("item.screwdriver")) {
	    			world.setBlockMetadataWithNotify(x, y, z, 1, 3);
	    			entityplayer.getCurrentEquippedItem().damageItem(1,entityplayer);
	    			return true;
	    		} else if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("ic2.itemToolWrench")) {
	    			ItemStack itemStack = new ItemStack(SSConfig.ssBlockWallCasingRaw,1,0);
	    			world.setBlockToAir(x, y, z);
	    			StackUtil.dropAsEntity(world, x, y, z, itemStack);
	    			//TODO Sound
	    			return true;
	    		}
	    	} else if (metaData == 1) {
	    		if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("ic2.itemRubber")) {
	    			--entityplayer.getCurrentEquippedItem().stackSize;
	    			world.setBlockMetadataWithNotify(x, y, z, 2, 3);
	    			return true;
	    		} else if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("item.screwdriver")) {
	    			world.setBlockMetadataWithNotify(x, y, z, 0, 3);
	    			entityplayer.getCurrentEquippedItem().damageItem(1,entityplayer);
	    			return true;
	    		}
	    	} else if (metaData == 2) {
	    		if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("item.screwdriver")) {
	    			world.setBlockMetadataWithNotify(x, y, z, 3, 3);
	    			return true;
	    		} else if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("item.welder")) {
	    			world.setBlockMetadataWithNotify(x, y, z, 1, 3);
	    			entityplayer.getCurrentEquippedItem().damageItem(1,entityplayer);
	    			return true;
	    		}
	    	} else if (metaData == 3) {
	    		if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("ic2.itemPlateIron")) {
	    			--entityplayer.getCurrentEquippedItem().stackSize;
	    			world.setBlockMetadataWithNotify(x, y, z, 4, 3);
	    			return true;
	    		} else if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("item.screwdriver")) {
	    			world.setBlockMetadataWithNotify(x, y, z, 2, 3);
	    			entityplayer.getCurrentEquippedItem().damageItem(1,entityplayer);
	    			return true;
	    		}
	    	} else if (metaData == 4) {
	    		if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("ic2.itemToolWrench") || entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("item.GT_Wrench_Bronze")) {
	    			world.setBlockMetadataWithNotify(x, y, z, 5, 3);
	    			return true;
	    		} else if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("item.welder")) {
	    			world.setBlockMetadataWithNotify(x, y, z, 3, 3);
	    			entityplayer.getCurrentEquippedItem().damageItem(1,entityplayer);
	    			return true;
	    		}
	    	} else if (metaData == 5) {
	    		if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("item.welder")) {
	    			world.setBlock(x, y, z, SSConfig.ssBlockWallCasingC, 0, 3);
	    			return true;
	    		} else if (entityplayer.getCurrentEquippedItem().getUnlocalizedName().equals("ic2.itemToolWrench")) {
	    			world.setBlockMetadataWithNotify(x, y, z, 4, 3);
	    			entityplayer.getCurrentEquippedItem().damageItem(1,entityplayer);
	    			return true;
	    		}
	    	}
	    	
	    	
	    	/*
	    	 * // if (IC2.platform.isRendering())
			// IC2.audioManager.playOnce(entityPlayer, PositionSpec.Hand,
			// "Tools/wrench.ogg", true, IC2.audioManager.defaultVolume);

	    	 */
	    }
		
		return false;
	 }


}
