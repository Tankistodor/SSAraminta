package com.badday.ss.api;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Created by userad on 25/10/14.
 */
public final class SSAPI {
  public static final Set<Block> softBlocks = new HashSet<Block>();


  public static boolean isSoftBlock(World world, int x, int y, int z) {
    Block block = world.getBlock(x, y,z);
    return !(block instanceof ISSSealedBlock);
    /*return block == null
        || block.isAir(world, x, y, z)
        || SSAPI.softBlocks.contains(block)
        || block.isReplaceable(world, x, y, z);
        */
  }
}
