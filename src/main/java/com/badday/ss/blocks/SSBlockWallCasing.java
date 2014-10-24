package com.badday.ss.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSSealedBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockWallCasing extends SSMetaBlock implements ISSSealedBlock {

	public SSBlockWallCasing() {
		super("blockWallCasing", Material.rock);
		this.setBlockUnbreakable();
		this.setResistance(330.0F);
		this.setStepSound(soundTypeMetal);
		disableStats();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		this.icons = new IIcon[SSConfig.ssWallCasingC_unlocalizedName.length];
		for (int i = 0; i < SSConfig.ssWallCasingC_unlocalizedName.length; i++) {
			
			if (SS.Debug)
				System.out.println("[" + SS.MODNAME + "] SSWallCasing.java try to registerBlockIcon "
						+ SSConfig.ssWallCasingC_unlocalizedName[i] + " " + i);
			
			icons[i] = icon.registerIcon(SS.ASSET_PREFIX
					+ SSConfig.ssWallCasingC_unlocalizedName[i]);
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return true;
	}

	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName().substring(5);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item par1, CreativeTabs tab, List list) {
		for (int i = 0; i < SSConfig.ssWallCasingC_name.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return true;
		if (entityplayer.getCurrentEquippedItem() != null) {

			if (SS.Debug)
				System.out.println("["
						+ SS.MODNAME
						+ "] act "
						+ entityplayer.getCurrentEquippedItem()
								.getUnlocalizedName());

			/*
			 * Tier MetaID Монтаж Демонтаж 1 0 отвертка
			 * ключ 2 1 резина отвертка 3 2 отвертка
			 * горелка 4 3 пластина отвертка 5 4 ключ
			 * горелка 6 5 горелка ключ cas 0 горелка
			 */

			String itemName = entityplayer.getCurrentEquippedItem()
					.getUnlocalizedName();

			if (itemName.equals("item.welder")) {
				world.setBlock(x, y, z, SSConfig.ssBlockWallCasingRaw, 5, 3);
				return true;
			}

			if (itemName.equals("ic2.itemToolPainterBlack")) {
				world.setBlockMetadataWithNotify(x, y, z, 0, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterRed")) {
				world.setBlockMetadataWithNotify(x, y, z, 1, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterGreen")) {
				world.setBlockMetadataWithNotify(x, y, z, 2, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterBrown")) {
				world.setBlockMetadataWithNotify(x, y, z, 3, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterBlue")) {
				world.setBlockMetadataWithNotify(x, y, z, 4, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterPurple")) {
				world.setBlockMetadataWithNotify(x, y, z, 5, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterCyan")) {
				world.setBlockMetadataWithNotify(x, y, z, 6, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterLightGrey")) {
				world.setBlockMetadataWithNotify(x, y, z, 7, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterDarkGrey")) {
				world.setBlockMetadataWithNotify(x, y, z, 8, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterPink")) {
				world.setBlockMetadataWithNotify(x, y, z, 9, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterLime")) {
				world.setBlockMetadataWithNotify(x, y, z, 10, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterYellow")) {
				world.setBlockMetadataWithNotify(x, y, z, 11, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterCloud")) {
				world.setBlockMetadataWithNotify(x, y, z, 12, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterMagenta")) {
				world.setBlockMetadataWithNotify(x, y, z, 13, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterOrange")) {
				world.setBlockMetadataWithNotify(x, y, z, 14, 3);
				return true;
			} else if (itemName.equals("ic2.itemToolPainterWhite")) {
				world.setBlockMetadataWithNotify(x, y, z, 15, 3);
				return true;
			}

		}
		return false;
	}

}
