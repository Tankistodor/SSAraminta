package com.badday.ss.blocks;

import ic2.core.util.Vector3;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.IGasNetworkElement;
import com.badday.ss.api.IGasNetworkSource;
import com.badday.ss.api.IGasNetworkVent;
import com.badday.ss.core.atmos.GasUtils;
import com.badday.ss.core.atmos.SSGasNetwork;
import com.badday.ss.core.utils.BlockVec3;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockGasPipe  extends Block implements IGasNetworkElement{
	
	private IIcon[] icons = new IIcon[16];
	
	public Vector3 minVector = new Vector3(0.3, 0.3, 0.3);
	public Vector3 maxVector = new Vector3(0.7, 0.7, 0.7);

	public SSBlockGasPipe(String asset) {
		super(Material.iron);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);
		this.isBlockContainer = true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType() {
		return SSConfig.gasPipeRenderId;
	}
	
	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName().substring(5);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		this.icons = new IIcon[SSConfig.ssGasPipe_unlocalizedName.length];
		for (int i = 0; i < SSConfig.ssGasPipe_unlocalizedName.length; i++) {
			
			if (SS.Debug)
				System.out.println("[" + SS.MODNAME + "] try to registerBlockIcon "
						+ SSConfig.ssGasPipe_unlocalizedName[i] + " " + i);
			
			icons[i] = icon.registerIcon(SS.ASSET_PREFIX
					+ SSConfig.ssGasPipe_unlocalizedName[i]);
		}
		this.blockIcon = this.icons[SSConfig.ssGasPipe_unlocalizedName.length-1];
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta >= this.icons.length) {
			if (SS.Debug)
				System.out.println("[" + SS.MODNAME + "] try getIcon Icons.length: "
						+ this.icons.length + " meta: "+ meta);
			return null;
		}
		try {
			return this.icons[meta];
		} catch (Exception e) {
			SS.LogMSG("Error BlockID: " + this.getUnlocalizedName() + " Meta: " + meta);

		}
		return null;
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		if (!world.isRemote && GasUtils.getAdjacentAllCount(world, new BlockVec3(x,y,z))>1) { //FIXME - Called 2 times ? Why?
			// Rebild network
			SSGasNetwork net = new SSGasNetwork(world);
			net.rebuildNetwork(world, new BlockVec3(x,y,z));
		}
		world.markBlockForUpdate(x, y, z);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (GasUtils.getAdjacentAllCount(world, new BlockVec3(x,y,z)) > 1) {
			// Rebild network
			for (BlockVec3 node : GasUtils.getAdjacentAll(world, new BlockVec3(x,y,z))) {
				if (node != null) {
					SSGasNetwork net = new SSGasNetwork(world);
					net.rebuildNetwork(world, node,new BlockVec3(x,y,z));
				}
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
	
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        world.func_147479_m(x, y, z);
        
        /*if (block instanceof SSBlockAirVent) {
        	TileEntity tileEntity = world.getTileEntity(x, y, z);
        	if (tileEntity instanceof IGasNetworkSource || tileEntity instanceof IGasNetworkVent) {
        		SSGasNetwork gasNetwork = new SSGasNetwork(world);
    			gasNetwork.rebuildNetworkFromVent(world, new BlockVec3(x,y,z));
        	}
        }*/
    }
    
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote)
			return true;

		if (entityplayer.getCurrentEquippedItem() != null) {
			String itemName = entityplayer.getCurrentEquippedItem().getUnlocalizedName();
			if (itemName.equals("item.ss.multitool")) {
				SSGasNetwork net = new SSGasNetwork(world);
				net.rebuildNetwork(world, new BlockVec3(x,y,z));
				if (SS.Debug) { 
					net.printDebugInfo();
				}
			}
 		}
		return false;
	}
    
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this
     * box can change after the pool has been cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z)
    {
        BlockVec3[] connectable = new BlockVec3[6];

            connectable = GasUtils.getAdjacentAll(Minecraft.getMinecraft().theWorld, new BlockVec3(x,y,z));
 
            float minX = (float) this.minVector.x;
            float minY = (float) this.minVector.y;
            float minZ = (float) this.minVector.z;
            float maxX = (float) this.maxVector.x;
            float maxY = (float) this.maxVector.y;
            float maxZ = (float) this.maxVector.z;

            if (connectable[0] != null)
            {
                minY = 0.0F;
            }

            if (connectable[1] != null)
            {
                maxY = 1.0F;
            }

            if (connectable[2] != null)
            {
                minZ = 0.0F;
            }

            if (connectable[3] != null)
            {
                maxZ = 1.0F;
            }

            if (connectable[4] != null)
            {
                minX = 0.0F;
            }

            if (connectable[5] != null)
            {
                maxX = 1.0F;
            }

            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity)
    {
        this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
        super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);

            BlockVec3[] connectable = GasUtils.getAdjacentAll(world,new BlockVec3(x,y,z));


            if (connectable[4] != null)
            {
                this.setBlockBounds(0, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
            }

            if (connectable[5] != null)
            {
                this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, 1, (float) this.maxVector.y, (float) this.maxVector.z);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
            }

            if (connectable[0] != null)
            {
                this.setBlockBounds((float) this.minVector.x, 0, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
            }

            if (connectable[1] != null)
            {
                this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, 1, (float) this.maxVector.z);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
            }

            if (connectable[2] != null)
            {
                this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, 0, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
            }

            if (connectable[3] != null)
            {
                this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, 1);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
	
    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
}
