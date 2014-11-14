package com.badday.ss.items;

import com.badday.ss.SS;

import ic2.api.item.ElectricItem;
import ic2.api.item.IBoxable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemGasAnalyzer extends SSItemBaseElectric implements IBoxable {

	public SSItemGasAnalyzer(String name) {
		super(name, 100, 1, 1);
		this.hasSubtypes = false;
		setUnlocalizedName(name);
	    GameRegistry.registerItem(this, name);
	}
	
	public boolean canTakeDamage(ItemStack stack, int amount) {
		return ElectricItem.manager.getCharge(stack) >= amount;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister
				.registerIcon("ss" + ":" + "gasAnalyser");
	}

	@Override
	public boolean canBeStoredInToolbox(ItemStack itemstack) {
		return true;
	}
	

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {

		if (entityplayer.isSneaking()) {
			return itemstack;
		}

		if (!canTakeDamage(itemstack, 1))
			return itemstack;

		if (!ElectricItem.manager.canUse(itemstack, 1.0D))
			return itemstack;
		

		//FIXME: ava.lang.NullPointerException: Ticking memory connection when open gui
		//if (!ElectricItem.manager.use(itemstack, 1, entityplayer)) {
			//return itemstack;
		//}
		

		if (!world.isRemote) {
			entityplayer.openGui(SS.instance, 10, world, entityplayer.serverPosX, entityplayer.serverPosY, entityplayer.serverPosZ);
		} else {
			world.playSoundEffect(entityplayer.serverPosX + 0.5f, entityplayer.serverPosY, entityplayer.serverPosZ + 0.5f, "ss:gasAnalyser", 4F, 1F);
		}

		
		return itemstack;

	}
}
