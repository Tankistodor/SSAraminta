package com.badday.ss.blocks;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.badday.ss.SSConfig;
import com.badday.ss.core.utils.BlockVec3;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class SSTileEntityAirlockFrameController extends SSTileEntityAirlockFrame implements INetworkUpdateListener, INetworkClientTileEntityEventListener, INetworkDataProvider, IEnergySink {

	
	/**
	 * false - n/s airLock
	 * true - e/w airLock
	 */
	private boolean sideEW = false; 
	private byte side = 0;
	
	private byte status = 0; // 0 - uncomplite; 1 - off; 1 - on

	// Energy
	public double energy = 0.0D;
	public int maxEnergy = 5000;
	private boolean addedToEnergyNet = false;
	public float guiChargeLevel;
	
	public void addAirlock() {
		if (this.status > 0) {
			ForgeDirection dir = ForgeDirection.getOrientation(this.side);

			for (int i = 1; i < 3; i++) {
				this.worldObj.setBlock(this.xCoord + (dir.offsetX * i), this.yCoord, this.zCoord + (dir.offsetZ * i), SSConfig.ssBlockAirLockDoor);
				this.worldObj.setBlock(this.xCoord + (dir.offsetX * i), this.yCoord - 1, this.zCoord + (dir.offsetZ * i), SSConfig.ssBlockAirLockDoor);
			}
		}
	}

	/**
	 * Remove airlock (block break)
	 */
	public void removeAirLock() {
		if (this.status > 0) {
			this.setStatus((byte) 0);
			ForgeDirection dir = ForgeDirection.getOrientation(this.side);

			for (int i = 1; i < 3; i++) {
				this.worldObj.setBlock(this.xCoord + (dir.offsetX * i), this.yCoord, this.zCoord + (dir.offsetZ * i), Blocks.air);
				this.worldObj.setBlock(this.xCoord + (dir.offsetX * i), this.yCoord - 1, this.zCoord + (dir.offsetZ * i), Blocks.air);
			}
		}
		
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
	
	public int getStatus() {
		return this.status;
	}
	
	public void setStatus(byte status) {
		this.status = status;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		setEW(nbt.getBoolean("sideEW"));
		setSide(nbt.getByte("side"));
		setStatus(nbt.getByte("status"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("sideEW", this.sideEW);
		nbt.setByte("side", this.side);
		nbt.setByte("status", this.status);
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
	
	/*
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		this.readFromNBT(packet.func_148857_g());
	}
	*/
	
	@Override
	public void onNetworkUpdate(String field) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkEvent(EntityPlayer player, int event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getNetworkedFields() {
		Vector<String> vector = new Vector<String>(3);
		vector.add("sideEW");
		vector.add("side");
		vector.add("status");
		vector.add("energy");
		vector.add("guiChargeLevel");
		return vector;
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
		return 0;
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
			return false;
		}
		
		for (BlockVec3 vec : frame) {
			TileEntity t = vec.getTileEntity(this.worldObj);
			if (t instanceof SSTileEntityAirlockFrame && t != this) {
				((SSTileEntityAirlockFrame) t).setMainBlock(this);
			}
		}
		this.status = 1;
		return true;
	}

}
