package com.badday.ss.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.badday.ss.entity.player.SSPlayerRoles;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSItemCards extends SSItem {

	private static String[] card_name = { "card_id", "card_gold", "card_silver", "card_data", "card_emag", "card_centcom", "card_f1", "card_f2" };

	public SSItemCards(String assets) {
		super();
		setUnlocalizedName(assets);
		setHasSubtypes(true);
		setMaxStackSize(1);
		this.textures = new IIcon[8];
		GameRegistry.registerItem(this, assets);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if (itemstack.getItemDamage() < card_name.length)
			return card_name[itemstack.getItemDamage()];
		else
			return card_name[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		for (int i = 0; i < card_name.length; i++)
			this.textures[i] = iconRegister.registerIcon("ss" + ":" + card_name[i]);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {

		// Add group cards
		for (SSPlayerRoles values : SSPlayerRoles.values()) {
			ItemStack itemStack = new ItemStack(this, 1, 0);
			NBTTagCompound tags = new NBTTagCompound();
			tags.setString("acl_group", values.toString());
			itemStack.setTagCompound(tags);
			itemList.add(itemStack);
		}

		// Add Master card
		/*ItemStack itemStack = new ItemStack(this, 1, 0);
		NBTTagCompound tags = new NBTTagCompound();
		tags.setString("acl_group", "MASTER_CARD");
		itemStack.setTagCompound(tags);
		itemList.add(itemStack);
		*/

		for (int i = 1; i < card_name.length; i++) {
			ItemStack itemStack = new ItemStack(this, 1, i);
			//NBTTagCompound tags = new NBTTagCompound();
			//tags.setString("acl_group", "test" + i);
			//itemStack.setTagCompound(tags);
			itemList.add(itemStack);
		}

	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	/* Tags and information about the tool */
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

		if (!stack.hasTagCompound())
			return;

		NBTTagCompound tags = stack.getTagCompound();
		if (tags.hasKey("acl_group")) {
			list.add("This card group/type: " + tags.getString("acl_group"));
		}
		if (tags.hasKey("acl_person")) {
			list.add("This card owner: " + tags.getString("acl_owner"));
		} else {
			list.add("This card owner: blank");
		}

	}

	/* Updating */

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {

	}

	public static String getCardAcl(ItemStack stack) {
		if (!stack.hasTagCompound())
			return "";

		NBTTagCompound tags = stack.getTagCompound();
		if (tags.hasKey("acl_group")) {
			return tags.getString("acl_group");
		}
		return "";

	}

}
