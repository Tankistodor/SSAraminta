package com.badday.ss.blocks;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.IMultiBlock;
import com.badday.ss.containers.SSContainerCabinet;
import com.badday.ss.core.utils.BlockVec3;
import com.badday.ss.events.SSPacketHandler;
import com.badday.ss.gui.SSGuiIDs;

public class SSTileEntityCabinet extends TileEntity implements IInventory,IMultiBlock {

	private int ticksSinceSync = -1;
	public float prevLidAngle;
	public float lidAngle;
	private int numUsingPlayers;
	private int type;
	public ItemStack[] chestContents;
	//private ItemStack[] topStacks;
	private int facing;
	private boolean inventoryTouched;
	private boolean hadStuff;
	public boolean mirror = false;

	public SSTileEntityCabinet() {
		//this(SSGuiIDs.GUI_ID_CABINET);
		this(0);
	}

	public SSTileEntityCabinet(int type) {
		super();
		this.type = type;
		this.chestContents = new ItemStack[getSizeInventory()];
	}
	
	public ItemStack[] getContents() {
		return chestContents;
	}
	
	@Override
    public void updateEntity()
    {
        super.updateEntity();
        // Resynchronize clients with the server state
        if (worldObj != null && !this.worldObj.isRemote && this.numUsingPlayers != 0 && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0)
        {
            this.numUsingPlayers = 0;
            float var1 = 5.0F;
            @SuppressWarnings("unchecked")
            //List<EntityPlayer> var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB((double)((float)this.xCoord - var1), (double)((float)this.yCoord - var1), (double)((float)this.zCoord - var1), (double)((float)(this.xCoord + 1) + var1), (double)((float)(this.yCoord + 1) + var1), (double)((float)(this.zCoord + 1) + var1)));
            List<EntityPlayer> var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(
            		(double)((float)this.xCoord - var1), 
            		(double)((float)this.yCoord - var1), 
            		(double)((float)this.zCoord - var1), 
            		(double)((float)(this.xCoord + 1) + var1), 
            		(double)((float)(this.yCoord + 1) + var1), 
            		(double)((float)(this.zCoord + 1) + var1)));
            Iterator<EntityPlayer> var3 = var2.iterator();

            while (var3.hasNext())
            {
                EntityPlayer var4 = var3.next();

                if (var4.openContainer instanceof SSContainerCabinet)
                {
                    ++this.numUsingPlayers;
                }
            }
        }

        if (worldObj != null && !worldObj.isRemote && ticksSinceSync < 0)
        {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, SSConfig.ssBlockCabinet, 3, ((numUsingPlayers << 3) & 0xF8) | (facing & 0x7));
        }
        if (!worldObj.isRemote && inventoryTouched)
        {
            inventoryTouched = false;
        }

        this.ticksSinceSync++;
        prevLidAngle = lidAngle;
        float f = 0.1F;
        if (numUsingPlayers > 0 && lidAngle == 0.0F)
        {
            double d = (double) xCoord + 0.5D;
            double d1 = (double) zCoord + 0.5D;
            worldObj.playSoundEffect(d, (double) yCoord + 0.5D, d1, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }
        if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F)
        {
            float f1 = lidAngle;
            if (numUsingPlayers > 0)
            {
                lidAngle += f;
            }
            else
            {
                lidAngle -= f;
            }
            if (lidAngle > 1.0F)
            {
                lidAngle = 1.0F;
            }
            float f2 = 0.5F;
            if (lidAngle < f2 && f1 >= f2)
            {
                double d2 = (double) xCoord + 0.5D;
                double d3 = (double) zCoord + 0.5D;
                worldObj.playSoundEffect(d2, (double) yCoord + 0.5D, d3, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }
            if (lidAngle < 0.0F)
            {
                lidAngle = 0.0F;
            }
        }
    }
	
	public int getFacing() {
		return this.facing;
	}
	
	public void setFacing(int facing2)
    {
        this.facing = facing2;
    }

	public int getType() {
		return type;
	}
	
	public void setType(int t) {
		this.type = t;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (chestContents[i] != null) {
			if (chestContents[i].stackSize <= j) {
				ItemStack itemstack = chestContents[i];
				chestContents[i] = null;
				markDirty();
				return itemstack;
			}
			ItemStack itemstack1 = chestContents[i].splitStack(j);
			if (chestContents[i].stackSize == 0) {
				chestContents[i] = null;
			}
			markDirty();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public String getInventoryName() {
			return "gray";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public int getSizeInventory() {
		return SSConfig.ssCabinetSize;
	}

	@Override
	public ItemStack getStackInSlot(int arg0) {
		inventoryTouched = true;
		return chestContents[arg0];
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack var2 = this.chestContents[par1];
            this.chestContents[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

	@Override
    public boolean hasCustomInventoryName()
    {
        return false; // Как в IronChest
    }

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		//System.out.println("Try to put in cabinet in slot "+slot+" Item "+item.toString());
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj == null) {
			return true;
		}
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
	}

    @Override
    public void openInventory()
    {
        if (worldObj == null) return;
        numUsingPlayers++;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, SSConfig.ssBlockCabinet, 1, numUsingPlayers);
    }
    
    @Override
    public void closeInventory()
    {
        if (worldObj == null) return;
        numUsingPlayers--;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, SSConfig.ssBlockCabinet, 1, numUsingPlayers);
    }

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		chestContents[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		chestContents = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < chestContents.length) {
				chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		facing = nbttagcompound.getByte("facing");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < chestContents.length; i++) {
			if (chestContents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				chestContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
		nbttagcompound.setByte("facing", (byte) facing);
	}
	
	@Override
    public boolean receiveClientEvent(int i, int j)
    {
        if (i == 1)
        {
            numUsingPlayers = j;
        }
        else if (i == 2)
        {
            facing = (byte) j;
        }
        else if (i == 3)
        {
            facing = (byte) (j & 0x7);
            numUsingPlayers = (j & 0xF8) >> 3;
        }
        return true;
    }

	@Override
    public Packet getDescriptionPacket()
    {
        return SSPacketHandler.getPacket(this);
    }
	
	
	 public void handlePacketData(int typeData, int[] intData)
	    {
		 /*
	        SSTileEntityCabinet chest = this;
	        if (this.type.ordinal() != typeData)
	        {
	            chest = updateFromMetadata(typeData);
	        }
	        if (IronChestType.values()[typeData].isTransparent() && intData != null)
	        {
	            int pos = 0;
	            if (intData.length < chest.topStacks.length * 3)
	            {
	                return;
	            }
	            for (int i = 0; i < chest.topStacks.length; i++)
	            {
	                if (intData[pos + 2] != 0)
	                {
	                    Item it = Item.getItemById(intData[pos]);
	                    ItemStack is = new ItemStack(it, intData[pos + 2], intData[pos + 1]);
	                    chest.topStacks[i] = is;
	                }
	                else
	                {
	                    chest.topStacks[i] = null;
	                }
	                pos += 3;
	            }
	        } */
	    }

	    public int[] buildIntDataList() // SEE HERE
	    {
	        return null;
	    }
	    
	    void rotateAround(ForgeDirection axis)
	    {
	        setFacing((byte)ForgeDirection.getOrientation(facing).getRotation(axis).ordinal());
	        worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, SSConfig.ssBlockCabinet, 2, getFacing());
	    }

	    public void wasPlaced(EntityLivingBase entityliving, ItemStack itemStack)
	    {
	    }

	    public void removeAdornments()
	    {

	    }
	    
	    public ForgeDirection getOrientation() { return ForgeDirection.getOrientation(facing); }

	    @Override
	    public boolean onActivated(EntityPlayer entityPlayer)
	    {
	        return this.getBlockType().onBlockActivated(this.worldObj, this.xCoord, this.yCoord, this.zCoord, entityPlayer, 0, this.xCoord, this.yCoord, this.zCoord);
	    }

		@Override
		public void onCreate(BlockVec3 placedPosition) {
			final BlockVec3 vecStrut = new BlockVec3(placedPosition.x, placedPosition.y + 1, placedPosition.z);
			((SSBlockMultiFake) SSConfig.fakeBlock).makeFakeBlock(this.worldObj, vecStrut, placedPosition, 0);
		}

		@Override
		public void onDestroy(TileEntity callingBlock) {
			final BlockVec3 thisBlock = new BlockVec3(this);
			this.worldObj.setBlockToAir(thisBlock.x , thisBlock.y + 1,thisBlock.z);
			this.worldObj.func_147480_a(thisBlock.x, thisBlock.y, thisBlock.z, true);
			
		}
	    
	    
	    
}
