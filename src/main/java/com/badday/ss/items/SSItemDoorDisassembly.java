package com.badday.ss.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemDoorDisassembly extends SSItem {
	
	public SSItemDoorDisassembly() {
		super();
		this.hasSubtypes = false;
		setUnlocalizedName("doorDisassembly");
		setMaxStackSize(1);
	    setMaxDamage(64); //64 использования
	    GameRegistry.registerItem(this, "doorDisassembly");
	}
	
	public boolean canTakeDamage(ItemStack stack, int amount) {
		return true;
	}
	
	public boolean onItemUseFirst(ItemStack itemstack,
			EntityPlayer entityPlayer, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ) {
		if (!canTakeDamage(itemstack, 1))
			return false;
		world.playSoundEffect(x + 0.5f, y, z + 0.5f, "ss:screwdriver1w", 4F, 1F);
		return false;

	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
		return super.onItemUse(stack, player, world, x, y, z, side, xOffset, yOffset, zOffset);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister
				.registerIcon("ss" + ":" + "doorDisassembly");
	}

}
