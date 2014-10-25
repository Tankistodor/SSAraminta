package com.badday.ss.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SSItemBlockCabinet  extends SSItemBlock {

	public SSItemBlockCabinet(Block block) {
		super(block);
		setMaxDamage(0);
	}
	
	@Override
    public int getMetadata(int i)
    {
        return 0;
    }
}
