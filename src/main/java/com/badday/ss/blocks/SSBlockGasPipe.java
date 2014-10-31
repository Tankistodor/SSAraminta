package com.badday.ss.blocks;

import ic2.core.util.Vector3;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.core.atmos.SSGasNetwork;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockGasPipe  extends BlockContainer {
	
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

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new SSTileEntityGasPipe(meta);
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
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
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
	}	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item par1, CreativeTabs tab, List list) {
		for (int i = 0; i < SSConfig.ssGasPipe_unlocalizedName.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        world.func_147479_m(x, y, z);
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		return true;
    	//TODO: Make color fill
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
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        TileEntity[] connectable = new TileEntity[6];

        if (tileEntity != null)
        {
 
            connectable = SSGasNetwork.getAdjacentOxygenConnections(tileEntity);
 
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
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity)
    {
        this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
        super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null)
        {
            TileEntity[] connectable = SSGasNetwork.getAdjacentOxygenConnections(tileEntity);


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
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
	
}
