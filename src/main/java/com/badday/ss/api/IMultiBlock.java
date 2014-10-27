package com.badday.ss.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.badday.ss.core.utils.BlockVec3;

public interface IMultiBlock
{
    /**
     * Called when activated
     */
    public boolean onActivated(EntityPlayer entityPlayer);

    /**
     * Called when this multiblock is created
     *
     * @param placedPosition - The position the block was placed at
     */
    public void onCreate(BlockVec3 placedPosition);

    /**
     * Called when one of the multiblocks of this block is destroyed
     *
     * @param callingBlock - The tile entity who called the onDestroy function
     */
    public void onDestroy(TileEntity callingBlock);
}
