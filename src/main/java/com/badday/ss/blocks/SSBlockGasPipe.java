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
import com.badday.ss.core.atmos.SSGasNetwork;
import com.badday.ss.core.utils.BlockVec3;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockGasPipe  extends Block {
	
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
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		world.markBlockForUpdate(i, j, k);
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
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        world.func_147479_m(x, y, z);
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
			/*if (itemName.equals("ic2.itemToolPainterBlack")) {
				tileEntity.setColor((byte) 0);
			} else if (itemName.equals("ic2.itemToolPainterRed")) {
				tileEntity.setColor((byte) 1);
			} else if (itemName.equals("ic2.itemToolPainterGreen")) {
				tileEntity.setColor((byte) 2);
			} else if (itemName.equals("ic2.itemToolPainterBrown")) {
				tileEntity.setColor((byte) 3);
			} else if (itemName.equals("ic2.itemToolPainterBlue")) {
				tileEntity.setColor((byte) 4);
			} else if (itemName.equals("ic2.itemToolPainterPurple")) {
				tileEntity.setColor((byte) 5);
			} else if (itemName.equals("ic2.itemToolPainterCyan")) {
				tileEntity.setColor((byte) 6);
			} else if (itemName.equals("ic2.itemToolPainterLightGrey")) {
				tileEntity.setColor((byte) 7);
			} else if (itemName.equals("ic2.itemToolPainterDarkGrey")) {
				tileEntity.setColor((byte) 8);
			} else if (itemName.equals("ic2.itemToolPainterPink")) {
				tileEntity.setColor((byte) 9);
			} else if (itemName.equals("ic2.itemToolPainterLime")) {
				tileEntity.setColor((byte) 10);
			} else if (itemName.equals("ic2.itemToolPainterYellow")) {
				tileEntity.setColor((byte) 11);
			} else if (itemName.equals("ic2.itemToolPainterCloud")) {
				tileEntity.setColor((byte) 12);
			} else if (itemName.equals("ic2.itemToolPainterMagenta")) {
				tileEntity.setColor((byte) 13);
			} else if (itemName.equals("ic2.itemToolPainterOrange")) {
				tileEntity.setColor((byte) 14);
			} else if (itemName.equals("ic2.itemToolPainterWhite")) {
				tileEntity.setColor((byte) 15);
			}
			
			BlockVec3 tileVec = new BlockVec3(tileEntity);
            for (final ForgeDirection dir : ForgeDirection.values())
            {
                final TileEntity tileAt = tileVec.getTileEntityOnSide(tileEntity.getWorldObj(), dir);

                if (tileAt != null && tileAt instanceof SSTileEntityGasPipe)
                {
                    ((SSTileEntityGasPipe) tileAt).onAdjacentColorChanged(dir);
                }
            }
			
		}*/

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

            connectable = SSGasNetwork.getAdjacentAll(Minecraft.getMinecraft().theWorld, new BlockVec3(x,y,z));
 
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

            BlockVec3[] connectable = SSGasNetwork.getAdjacentAll(world,new BlockVec3(x,y,z));


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
