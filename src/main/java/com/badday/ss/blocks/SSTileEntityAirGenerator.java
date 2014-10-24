package com.badday.ss.blocks;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;

import cpw.mods.fml.common.FMLCommonHandler;

public class SSTileEntityAirGenerator extends TileEntity implements IEnergySink
{
    public boolean addedToEnergyNet = false;

    private final int EU_PER_AIRBLOCK = 10; // 10
    private final int MAX_ENERGY_VALUE = 36 * EU_PER_AIRBLOCK; //36
    private int currentEnergyValue = 0;

    private int cooldownTicks = 0;
    private final float AIR_POLLUTION_INTERVAL = 4; // 4 seconds

    private final int START_CONCENTRATION_VALUE = 15;

    @Override
    public void updateEntity()
    {
        if (!SS.isServer())
        {
            return;
        }

        if (!addedToEnergyNet && !this.tileEntityInvalid)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            addedToEnergyNet = true;
        }

        // Air generator works only in spaces
        if (worldObj.provider.dimensionId != SS.instance.spaceDimID)
        {
            return;
        }

        if (addedToEnergyNet && currentEnergyValue > EU_PER_AIRBLOCK)
        {
            if (cooldownTicks++ > AIR_POLLUTION_INTERVAL * 20)
            {
                cooldownTicks = 0;
                worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2); // set enabled texture
                releaseAir();
            }
        }
        else
        {
            if (cooldownTicks++ > 20)
            {
                worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2); // set disabled texture
                cooldownTicks = 0;
            }
        }
    }

    private void releaseAir()
    {
        if (worldObj.isAirBlock(xCoord + 1, yCoord, zCoord) && (currentEnergyValue - EU_PER_AIRBLOCK >= 0))
        {
            worldObj.setBlock(xCoord + 1, yCoord, zCoord, SSConfig.ssBlockAir, START_CONCENTRATION_VALUE, 2);
            currentEnergyValue -= EU_PER_AIRBLOCK;
        }

        if (worldObj.isAirBlock(xCoord - 1, yCoord, zCoord) && (currentEnergyValue - EU_PER_AIRBLOCK >= 0))
        {
            worldObj.setBlock(xCoord - 1, yCoord, zCoord, SSConfig.ssBlockAir, START_CONCENTRATION_VALUE, 2);
            currentEnergyValue -= EU_PER_AIRBLOCK;
        }

        if (worldObj.isAirBlock(xCoord, yCoord + 1, zCoord) && (currentEnergyValue - EU_PER_AIRBLOCK >= 0))
        {
            worldObj.setBlock(xCoord, yCoord + 1, zCoord, SSConfig.ssBlockAir, START_CONCENTRATION_VALUE, 2);
            currentEnergyValue -= EU_PER_AIRBLOCK;
        }

        if (worldObj.isAirBlock(xCoord, yCoord - 1, zCoord) && (currentEnergyValue - EU_PER_AIRBLOCK >= 0))
        {
            worldObj.setBlock(xCoord, yCoord - 1, zCoord, SSConfig.ssBlockAir, START_CONCENTRATION_VALUE, 2);
            currentEnergyValue -= EU_PER_AIRBLOCK;
        }

        if (worldObj.isAirBlock(xCoord, yCoord, zCoord + 1) && (currentEnergyValue - EU_PER_AIRBLOCK >= 0))
        {
            worldObj.setBlock(xCoord, yCoord, zCoord + 1, SSConfig.ssBlockAir, START_CONCENTRATION_VALUE, 2);
            currentEnergyValue -= EU_PER_AIRBLOCK;
        }

        if (worldObj.isAirBlock(xCoord, yCoord, zCoord - 1) && (currentEnergyValue - EU_PER_AIRBLOCK >= 0))
        {
            worldObj.setBlock(xCoord, yCoord, zCoord - 1, SSConfig.ssBlockAir, START_CONCENTRATION_VALUE, 2);
            currentEnergyValue -= EU_PER_AIRBLOCK;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.currentEnergyValue = tag.getInteger("energy");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("energy", this.getCurrentEnergyValue());
    }
    
    /**
     * IEnergySink methods implementation
     */
	@Override
	public double getDemandedEnergy() {
		// TODO Auto-generated method stub
		return (MAX_ENERGY_VALUE - currentEnergyValue);
	}

	@Override
	public int getSinkTier() {
		// TODO Auto-generated method stub
		return 3;
	}    
    
    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        double leftover = 0;
        currentEnergyValue += Math.round(amount);

        if (getCurrentEnergyValue() > MAX_ENERGY_VALUE)
        {
            leftover = (getCurrentEnergyValue() - MAX_ENERGY_VALUE);
            currentEnergyValue = MAX_ENERGY_VALUE;
        }

        return leftover;
    }

    /*@Override
    public int getMaxSafeInput()
    {
        return Integer.MAX_VALUE;
    }*/

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
    {
        return true;
    }

    /**
     * @return the currentEnergyValue
     */
    public int getCurrentEnergyValue()
    {
        return currentEnergyValue;
    }

    @Override
    public void onChunkUnload()
    {
        if (addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnergyNet = false;
        }
    }

    @Override
    public void invalidate()
    {
        if (addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnergyNet = false;
        }

        super.invalidate();
    }

}
