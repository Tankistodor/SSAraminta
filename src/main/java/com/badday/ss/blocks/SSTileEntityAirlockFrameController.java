package com.badday.ss.blocks;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.network.NetworkManager;
import ic2.core.IC2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SSConfig;
import com.badday.ss.api.ITEAccess;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.entity.player.SSPlayerData;
import com.badday.ss.entity.player.SSPlayerRoles;
import com.badday.ss.items.SSItemCards;

public class SSTileEntityAirlockFrameController extends TileEntity implements ITEAccess, IInventory, ISidedInventory, INetworkUpdateListener,
		INetworkClientTileEntityEventListener, INetworkDataProvider, IEnergySink {

	
	/**
	 * false - n/s airLock
	 * true - e/w airLock
	 */
	private boolean sideEW = false; 
	private boolean constuctionFrameRight = false;
	private boolean editMode = false;
	private byte side = 0;
	
	// 0 - uncomplite;
	// 1 - off; 
	//  - on
	private byte status = 0; 

	// Energy
	public double energy = 0.0D;
	public int maxEnergy = 10000;
	private boolean addedToEnergyNet = false;
		
	public long ticks = 0;
	public int state = 0;
	public int inc = 0;
	
	private int numUsingPlayers;
	
	private HashSet<SSPlayerRoles> acl = new HashSet<SSPlayerRoles>();
	
	/**
	 * Discharge Slot
	 */
	private ItemStack[] containingItems = new ItemStack[1];
	
	public SSTileEntityAirlockFrameController() {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updateEntity() {
		super.updateEntity();

		boolean foundPlayer = false;

		if (!this.worldObj.isRemote && this.ticks % 10 == 0 && (this.status == SSBlockAirlockFrameController.MT_ON || this.status == SSBlockAirlockFrameController.MT_OFF)) {

			if (this.maxEnergy - this.energy >= 1.0D) {

				if (this.getDischargeSlot() != null) {
					double amount = ElectricItem.manager.discharge(this.getDischargeSlot(), 1, 1, true, true, false);
					if (amount > 0.0D) {
						this.energy += amount;

					markDirty();
					}
				}
			}
		}
		
		if (!this.worldObj.isRemote && this.ticks % 10 == 0) {
			if (this.energy <= 0 && this.status == SSBlockAirlockFrameController.MT_ON) {
				this.energy = 0;
				setStatus(SSBlockAirlockFrameController.MT_OFF);
				((NetworkManager) IC2.network.get()).updateTileEntityField(this, "status");
				//worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
				//markDirty();
			} else if (this.energy > 0 && this.status == SSBlockAirlockFrameController.MT_OFF) {
				setStatus(SSBlockAirlockFrameController.MT_ON);
				((NetworkManager) IC2.network.get()).updateTileEntityField(this, "status");
				//worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
				//markDirty();
			}
		}
		
		if (!this.worldObj.isRemote && this.ticks % 10 == 0 && this.status == SSBlockAirlockFrameController.MT_ON) {
		
			int distance = 3;
			
			BlockVec3 thisPos = new BlockVec3(this);
			BlockVec3 minPos = new BlockVec3(this).translate(-distance, -distance, -distance);
			BlockVec3 maxPos = new BlockVec3(this).translate(distance, distance, distance);
			AxisAlignedBB matchingRegion = AxisAlignedBB.getBoundingBox(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z);
			List playersWithin = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, matchingRegion);

			for (int i = 0; i < playersWithin.size(); i++) {
				Object o = playersWithin.get(i);

				if (o instanceof EntityPlayer) {
					if (this.acl.size() > 0) {
						try {
							EntityPlayer player = (EntityPlayer) o;
							ItemStack itemstack = player.getCurrentEquippedItem();
							if (isCardHaveAccess(SSItemCards.getCardAcl(itemstack))) {
								foundPlayer = true;
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					} else {
						foundPlayer = false;
					}

				}
			}

			// 0 - closed. scan
			// 1 - opening
			// 2 - open
			// 3 - closing
			//
			
			if (state == 0 && foundPlayer) {
				((NetworkManager) IC2.network.get()).updateTileEntityField(this, "state");
				this.energy -= 1;
				state = 1;
			}

			if (state == 2 && !foundPlayer) {
				((NetworkManager) IC2.network.get()).updateTileEntityField(this, "state");
				state = 3;
			}

			if (state == 1 && inc < 33)
				inc++;

			if (state == 3 && inc > 1)
				inc--;

			if (state == 1 && inc == 32)
				state = 2;

			if (state == 3 && inc == 0)
				state = 0;

			if (state == 1)
				openDoor();

			if (state == 3)
				closeDoor();

		}

		if (this.ticks >= Long.MAX_VALUE) {
			this.ticks = 1;
		}

		this.ticks++;

	}
	
	public boolean hasTileEntity(int metadata) {
		return true;
	}
	
	public List<BlockVec3> getDoorBlock() {
		List<BlockVec3> res = new ArrayList<BlockVec3>();
		
		if (getConstuctionFrameRight()) {
			ForgeDirection dir = ForgeDirection.getOrientation(this.side);

			for (int i = 1; i < 3; i++) {
				res.add(new BlockVec3(this.xCoord + (dir.offsetX * i), this.yCoord, this.zCoord + (dir.offsetZ * i)));
				res.add(new BlockVec3(this.xCoord + (dir.offsetX * i), this.yCoord - 1, this.zCoord + (dir.offsetZ * i)));
			}
		}
		return res;
	}
	
	public void openDoor() {
		
		this.worldObj.playSoundEffect(this.xCoord + 0.5f, this.yCoord, this.zCoord + 0.5f, "ss:airlock", 4F, 1F);
		
		setAirLockToAir();
		
		this.state = 2;
		return;
	}
	
	public void closeDoor() {
		addAirlock();	
		this.state = 0;
	}
	
	public void addAirlock() {
		for (BlockVec3 block : getDoorBlock()) {
			block.setBlock(this.worldObj, SSConfig.ssBlockAirLockDoor);
		}
	}

	/**
	 * Remove airlock (block break)
	 */
	public void removeAirLock() {
		for (BlockVec3 block : getDoorBlock()) {
			block.setBlock(this.worldObj, Blocks.air);
		}
	}

	public void setAirLockToAir() {
		for (BlockVec3 block : getDoorBlock()) {
			block.setBlock(this.worldObj, SSConfig.ssBlockAir);
		}
	}
	
	public boolean getEditMode() {
		return this.editMode;
	}
	
	public void setEditMode(boolean mode) {
		this.editMode = mode;
	}
	
	public int getSide() {
		return this.side;
	}
	
	public void setSide(byte side) {
		this.side = side;
	}
	
	public boolean getEW() {
		
		return this.sideEW;
	}
	
	public void setEW(boolean ew) {
		this.sideEW = ew;
	}
	
	public byte getStatus() {
		return this.status;
	}
	
	public void setStatus(byte status) {
		//if (status != SSBlockAirlockFrameController.MT_OPENED) this.closeDoor();
		this.status = status;
	}

	public boolean getConstuctionFrameRight() {
		return this.constuctionFrameRight;
	}
	
	public void setConstuctionFrameRight(boolean boolean1) {
		this.constuctionFrameRight = boolean1;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		setEW(nbt.getBoolean("sideEW"));
		setConstuctionFrameRight(nbt.getBoolean("constuctionFrameRight"));
		setSide(nbt.getByte("side"));
		setStatus(nbt.getByte("status"));
		int i = nbt.getInteger("roleCount");
		for (int j = 1; j<=i; j++) {
			addRole(SSPlayerRoles.valueOf(nbt.getString("role"+j)));
		}
		
		final NBTTagList var2 = nbt.getTagList("Items", 10);
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length) {
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
		
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("sideEW", this.sideEW);
		nbt.setBoolean("constuctionFrameRight", this.constuctionFrameRight);
		nbt.setByte("side", this.side);
		nbt.setByte("status", this.status);
		nbt.setInteger("roleCount", this.acl.size());
		int i = 0;
		for (SSPlayerRoles r : this.acl) {
			i++;
			nbt.setString("role"+i, r.toString());
		}
		
		final NBTTagList list = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3) {
			if (this.containingItems[var3] != null) {
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				list.appendTag(var4);
			}
		}

		nbt.setTag("Items", list);
		
	}

	@Override
	public void validate() {
		if (!this.worldObj.isRemote) {
		      MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

		      this.addedToEnergyNet = true;
		    }
		super.validate();
	}
	
	@Override
	public void invalidate() {
		if ((!this.worldObj.isRemote) && (this.addedToEnergyNet)) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			this.addedToEnergyNet = false;
		}
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		if ((!this.worldObj.isRemote) && (this.addedToEnergyNet)) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			this.addedToEnergyNet = false;
		}
		super.onChunkUnload();
	}

	@Override
	public List<String> getNetworkedFields() {
		Vector<String> vector = new Vector<String>();
		vector.add("sideEW");
		vector.add("side");
		vector.add("status");
		vector.add("energy");
		vector.add("constuctionFrameRight");
		return vector;
	}
	
	@Override
	public void onNetworkUpdate(String field) {
		if (field.equals("status")) 
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		
	}

	@Override
	public void onNetworkEvent(EntityPlayer player, int event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getDemandedEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSinkTier() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean checkStructure() {
		List<BlockVec3> frame = new ArrayList<BlockVec3>();
		int sx = 0; int sz = 0;
		int x = 0; int z = 0;
		
		this.status = 0;
		
		// Find Direction
		if (this.worldObj.getBlock(xCoord+1, yCoord+1, zCoord) == SSConfig.ssBlockAirLockFrame) {
			sx= 1; x = 3; this.side = 5;
		} else if (this.worldObj.getBlock(xCoord-1, yCoord+1, zCoord) == SSConfig.ssBlockAirLockFrame) {
			sx= -1; x = -3; this.side = 4;
	    } else if (this.worldObj.getBlock(xCoord, yCoord+1, zCoord+1) == SSConfig.ssBlockAirLockFrame) {
			sz= 1; z = 3; this.side = 3;
	    } else if (this.worldObj.getBlock(xCoord, yCoord+1, zCoord-1) == SSConfig.ssBlockAirLockFrame) {
			sz= -1; z = -3; this.side = 2;
	    }
		
		
		// VERT
		if (sx != 0 || sz != 0) {
			// Check vertical side
			if (
					(this.worldObj.getBlock(xCoord, yCoord-2, zCoord) != SSConfig.ssBlockAirLockFrame) ||
					(this.worldObj.getBlock(xCoord, yCoord-1, zCoord) != SSConfig.ssBlockAirLockFrame) ||
					(this.worldObj.getBlock(xCoord, yCoord+1, zCoord) != SSConfig.ssBlockAirLockFrame)
				)
				return false;

			if (
					(this.worldObj.getBlock(xCoord+x, yCoord-2, zCoord+z) != SSConfig.ssBlockAirLockFrame) ||
					(this.worldObj.getBlock(xCoord+x, yCoord, zCoord+z) != SSConfig.ssBlockAirLockFrame) ||
					(this.worldObj.getBlock(xCoord+x, yCoord-1, zCoord+z) != SSConfig.ssBlockAirLockFrame) ||
					(this.worldObj.getBlock(xCoord+x, yCoord+1, zCoord+z) != SSConfig.ssBlockAirLockFrame)
				)
				return false;

			frame.add(new BlockVec3(xCoord, yCoord-2, zCoord));
			frame.add(new BlockVec3(xCoord, yCoord-1, zCoord));
			frame.add(new BlockVec3(xCoord, yCoord+1, zCoord));
			
			frame.add(new BlockVec3(xCoord+x, yCoord, zCoord+z));
			frame.add(new BlockVec3(xCoord+x, yCoord+1, zCoord+z));
			frame.add(new BlockVec3(xCoord+x, yCoord-1, zCoord+z));
			frame.add(new BlockVec3(xCoord+x, yCoord-2, zCoord+z));
			
			// Check horisontal side
			for (int i = 1; i < 3; i++) {
				if ((this.worldObj.getBlock(xCoord+(sx*i), yCoord-2, zCoord+(sz*i)) != SSConfig.ssBlockAirLockFrame) ||
						(this.worldObj.getBlock(xCoord+(sx*i), yCoord+1, zCoord+(sz*i)) != SSConfig.ssBlockAirLockFrame)) 
					return false;
				frame.add(new BlockVec3(xCoord+(sx*i), yCoord-2, zCoord+(sz*i)));
				frame.add(new BlockVec3(xCoord+(sx*i), yCoord+1, zCoord+(sz*i)));
			}
		} else {
			setConstuctionFrameRight(false);
			setStatus(SSBlockAirlockFrameController.MT_UNCOMPLITE);
			return false;
			
		}
		
		for (BlockVec3 vec : frame) {
			TileEntity t = vec.getTileEntity(this.worldObj);
			if (t instanceof SSTileEntityAirlockFrame && t != this) {
				((SSTileEntityAirlockFrame) t).setMainBlock(new BlockVec3(this.xCoord,this.yCoord,this.zCoord));
			}
		}
		setConstuctionFrameRight(true);
		setStatus(SSBlockAirlockFrameController.MT_OPENED);
		return true;
	}

	@Override
	public void openInventory() {
		if (worldObj == null) return;	
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, SSConfig.ssBlockAirLockFrameController, 1, numUsingPlayers);
	}
	
	@Override
	public void closeInventory() {
		if (worldObj == null) return;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, SSConfig.ssBlockAirLockFrameController, 1, numUsingPlayers);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.containingItems[i] != null) {
			if (containingItems[i].stackSize <= j) {
				ItemStack itemstack = containingItems[i];
				containingItems[i] = null;
				markDirty();
				return itemstack;
			}
			ItemStack itemstack1 = containingItems[i].splitStack(j);
			if (containingItems[i].stackSize == 0) {
				containingItems[i] = null;
			}
			markDirty();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public String getInventoryName() {
		return "inventoryAirlockController";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.containingItems[slot];
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
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {

		if (itemstack.getItem() instanceof ic2.api.item.ISpecialElectricItem) {
			return true;
		}
		return false;
	}


	@Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { };
    }
	
	@Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }
	
	@Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        return false;
    }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return ((worldObj.getTileEntity(xCoord, yCoord, zCoord) == this)
				&& (entityplayer.getDistance((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D));
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.containingItems[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}

	@Override
	public boolean isPlayerHaveAccess(Entity player) {
		if (player instanceof EntityPlayerMP) {
			SSPlayerData pData = SSPlayerData.get((EntityPlayerMP) player);
			if (this.acl.contains(pData.getPlayerRole())) return true;
		}
		return false;
	}

	public boolean isCardHaveAccess(String str) {
		
		try {
			
			if (this.acl.contains(SSPlayerRoles.valueOf(str))) {
				return true;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} 
	
		return false;
	}
	
	@Override
	public void addRole(SSPlayerRoles role) {
		this.acl.add(role);
		this.markDirty();
	}

	@Override
	public void removeRole(SSPlayerRoles role) {
		if (this.acl.contains(role)) {
			this.acl.remove(role);
			this.markDirty();
		}
	}

	@Override
	public boolean switchRole(SSPlayerRoles role) {
		if (this.acl.contains(role)) {
			this.acl.remove(role);
			this.markDirty();
			return false;
		} else {
			this.acl.add(role);
			this.markDirty();
			return true;
		}
	}

	public ItemStack getDischargeSlot() {
		if (this.containingItems != null && this.containingItems.length > 0) {
			return this.containingItems[0];
		} else {
			this.containingItems = new ItemStack[1];
		}
		return this.containingItems[0];
	}
	
	public void setDischargeSlot(ItemStack slot) {
		if (this.containingItems != null && this.containingItems.length > 0) {
			this.containingItems[0] = slot;
		} else {
			this.containingItems = new ItemStack[1];
			this.containingItems[0] = slot;
		}
 	}
	
}
