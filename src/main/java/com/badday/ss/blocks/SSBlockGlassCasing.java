package com.badday.ss.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSSealedBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockGlassCasing extends SSMetaBlock implements ISSSealedBlock {

	public SSBlockGlassCasing(String assetName) {
		super(assetName, Material.glass);
		this.setBlockUnbreakable();
        this.setBlockTextureName(SS.ASSET_PREFIX + assetName);
        this.setBlockName(assetName);
		this.setResistance(330.0F);
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
		disableStats();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		this.icons = new IIcon[1];
		icons[0] = icon.registerIcon(SS.ASSET_PREFIX
					+ this.getUnlocalizedName());
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	public String getUnlocalizedName() {
		return "blockGlassCasing";
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return false;
		if (entityplayer.getCurrentEquippedItem() != null) {

			if (SS.Debug && false)
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
				Block bb = Block.getBlockFromName("IC2:blockAlloyGlass");
				world.setBlock(x, y, z, bb, 0, 3);
				return true;
			}
		}
		return false;
	}
	
}
