package com.badday.ss.items;

import com.badday.ss.SSConfig;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemWelder extends SSItem {

	public SSItemWelder() {
		super();
		this.hasSubtypes = false;
		setUnlocalizedName("welder");
		setMaxStackSize(1);
	    setMaxDamage(64); //64 использования
	    GameRegistry.registerItem(this, "welder");
	}
	
	public boolean canTakeDamage(ItemStack stack, int amount) {
		return true;
	}
	
	public boolean onItemUseFirst(ItemStack itemstack,
			EntityPlayer entityPlayer, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ) {
		if (!canTakeDamage(itemstack, 1))
			return false;
		String rnd = String.valueOf(world.rand.nextInt(2)+1);
		world.playSoundEffect(x + 0.5f, y, z + 0.5f, "ss:welder"+rnd+"w", 4F, 1F);
		//System.out.println("[" + SS.MODNAME + "] sound " + "ss:welder"+rnd);
		Block b = world.getBlock(x, y, z);
		System.out.println(b.getUnlocalizedName());
		if ((b != null) && (b.getUnlocalizedName().equals("blockAlloyGlass"))) {
			world.setBlock(x, y, z, SSConfig.ssBlockGlassCasing, 0, 3);
			return true;
		}
		
		return false;

	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
		/*if (!canTakeDamage(stack, 1))
			return false;*/
		return super.onItemUse(stack, player, world, x, y, z, side, xOffset, yOffset, zOffset);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister
				.registerIcon("ss" + ":" + "welder");
	}
	
	/*@Override
	public String getItemDisplayName(ItemStack itemStack)
	  {
	    return "Welder";
	  }*/
}
