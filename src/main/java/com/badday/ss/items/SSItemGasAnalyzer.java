package com.badday.ss.items;

import com.badday.ss.SS;

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
		super(name, 100000.0D, 128.0D, 1);
		this.hasSubtypes = false;
		setUnlocalizedName(name);
		setMaxStackSize(1);
	    setMaxDamage(64); //64 использования
	    GameRegistry.registerItem(this, name);
	}
	
	public boolean canTakeDamage(ItemStack stack, int amount) {
		return true;
	}
	
	public boolean onItemUseFirst(ItemStack itemstack,
			EntityPlayer entityPlayer, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ) {
		if (!canTakeDamage(itemstack, 1))
			return false;
		
		world.playSoundEffect(x + 0.5f, y, z + 0.5f, "ss:gasAnalyser", 4F, 1F);
		//System.out.println("[" + SS.MODNAME + "] sound " + "ss:welder"+rnd);
		return false;

	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
		return super.onItemUse(stack, player, world, x, y, z, side, xOffset, yOffset, zOffset);
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
	
	/*@Override
	public String getItemDisplayName(ItemStack itemStack)
	  {
	    return "Welder";
	  }*/
	
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		
		if (entityplayer.isSneaking()) {
            return itemstack;
		}
		
		if (!world.isRemote) {
			entityplayer.openGui(SS.instance, 100, world, entityplayer.serverPosX, entityplayer.serverPosY, entityplayer.serverPosZ);
		} else {
			
		}
		
		return itemstack;
		
	}
}
