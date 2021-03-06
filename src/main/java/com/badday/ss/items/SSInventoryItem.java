package com.badday.ss.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class SSInventoryItem implements IInventory {
	private String name = "Inventory Item";

	/** Defining your inventory size this way is handy */
	public static final int INV_SIZE = 8;

	/**
	 * Inventory's size must be same as number of slots you add to the Container
	 * class
	 */
	private ItemStack[] inventory = new ItemStack[INV_SIZE];

	/** Provides NBT Tag Compound to reference */
	private final ItemStack invItem;

	/**
	 * @param itemstack
	 *            - the ItemStack to which this inventory belongs
	 */
	public SSInventoryItem(ItemStack stack) {
		this.invItem = stack;
		// Just in case the itemstack doesn't yet have an NBT Tag Compound:
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		// note that it's okay to use stack instead of invItem right there
		// both reference the same memory location, so whatever you change using
		// either reference will change in the other

		// Read the inventory contents from NBT
		readFromNBT(stack.getTagCompound());
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize > amount) {
				stack = stack.splitStack(amount);
				// 1.7.2: change to markDirty()
				markDirty();
			} else {
				setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		this.inventory[slot] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		// 1.7.2 change to markDirty()
		markDirty();
	}

	// 1.7.2 change to getInventoryName
	@Override
	public String getInventoryName() {
		return name;
	}

	// 1.7.2 change to hasCustomInventoryName
	@Override
	public boolean hasCustomInventoryName() {
		return name.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	// 1.7.2 change to markDirty
	@Override
	public void markDirty() {
		for (int i = 0; i < getSizeInventory(); ++i) {
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0)
				inventory[i] = null;
		}
		// be sure to write to NBT when the inventory changes!
		writeToNBT(invItem.getTagCompound());
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// this will close the inventory if the player tries to move
		// the item that opened it, but you need to return this method
		// from the Container's canInteractWith method
		// an alternative would be to override the slotClick method and
		// prevent the current item slot from being clicked
		return player.getHeldItem() == invItem;
	}

	// 1.7.2 change to openInventory
	@Override
	public void openInventory() {
	}

	// 1.7.2 change to closeInventory
	@Override
	public void closeInventory() {
	}

	/**
	 * This method doesn't seem to do what it claims to do, as items can still
	 * be left-clicked and placed in the inventory even when this returns false
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		// Don't want to be able to store the inventory item within itself
		// Bad things will happen, like losing your inventory
		// Actually, this needs a custom Slot to work
		// return !(itemstack.getItem() instanceof ItemStore);
		// FIXME: !!!
		return true;
	}

	/**
	 * A custom method to read our inventory from an ItemStack's NBT compound
	 */
	public void readFromNBT(NBTTagCompound tagcompound) {
		// Gets the custom taglist we wrote to this compound, if any
		// 1.7.2 change to compound.getTagList("ItemInventory",
		// Constants.NBT.TAG_COMPOUND);
		NBTTagList items = tagcompound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < items.tagCount(); ++i) {
			// 1.7.2 change to items.getCompoundTagAt(i)
			NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);
			byte slot = item.getByte("Slot");

			// Just double-checking that the saved slot index is within our
			// inventory array bounds
			if (slot >= 0 && slot < getSizeInventory()) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(item);
			}
		}
	}

	/**
	 * A custom method to write our inventory to an ItemStack's NBT compound
	 */
	public void writeToNBT(NBTTagCompound tagcompound) {
		// Create a new NBT Tag List to store itemstacks as NBT Tags
		NBTTagList items = new NBTTagList();

		for (int i = 0; i < getSizeInventory(); ++i) {
			// Only write stacks that contain items
			if (getStackInSlot(i) != null) {
				// Make a new NBT Tag Compound to write the itemstack and slot
				// index to
				NBTTagCompound item = new NBTTagCompound();
				item.setInteger("Slot", i);
				// Writes the itemstack in slot(i) to the Tag Compound we just
				// made
				getStackInSlot(i).writeToNBT(item);

				// add the tag compound to our tag list
				items.appendTag(item);
			}
		}
		// Add the TagList to the ItemStack's Tag Compound with the name
		// "ItemInventory"
		tagcompound.setTag("ItemInventory", items);
	}

}
