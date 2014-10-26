package com.badday.ss.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.badday.ss.SS;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class SSMetaBlock extends Block {

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;
	
	protected int metaID;

	public SSMetaBlock(String internamName, Material mat) {
		super(mat);
		this.setBlockName(internamName);
		this.setCreativeTab(SS.ssTab);
	}	

    	

	/*@Override
	@SideOnly(Side.CLIENT)
	public IIcon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z,
			int side) {
		int meta = iBlockAccess.getBlockMetadata(x, y, z);

		return getIcon(side, meta);
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta >= this.icons.length) {
			if (SS.Debug)
				System.out.println("[" + SS.MODNAME + "] SSMetaBlock.java try getIcon Icons.length: "
						+ this.icons.length + " meta: "+ meta);
			return null;
		}
		try {
			return this.icons[meta];
		} catch (Exception e) {
			SS.LogMSG("Error BlockID: " + this.getUnlocalizedName() + " Meta: " + meta);

		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
	}
	
	
	@Override
	public int damageDropped (int metadata) {
		return metadata;
	}
	
	/*protected String getTextureName(int index)
	  {
	    Item item = Item.getItemFromBlock(this);

	    if (!item.getHasSubtypes()) {
	      if (index == 0) {
	        return getUnlocalizedName();
	      }
	      return null;
	    }
	    ItemStack itemStack = new ItemStack(this, 1, index);
	    String ret = item.getUnlocalizedName(itemStack);

	    if (ret == null) {
	      return null;
	    }
	    return ret.replace("item", "block");
	  }*/

	@Override
	public int getRenderBlockPass() {
		return 1;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

	@Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return SS.ssTab;
    }
	
}
