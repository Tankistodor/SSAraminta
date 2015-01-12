package com.badday.ss.blocks;

import ic2.core.IC2;
import ic2.core.network.NetworkManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.badday.ss.SS;
import com.badday.ss.SSConfig;
import com.badday.ss.api.ISSSealedBlock;
import com.badday.ss.core.utils.WorldUtils;
import com.badday.ss.entity.player.SSPlayerRoles;
import com.badday.ss.items.SSItemCards;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SSBlockAirlockFrameController extends BlockContainer implements ISSSealedBlock {

	public final static byte MT_UNCOMPLITE = 0;
	public final static byte MT_OPENED = 1;
	public final static byte MT_OFF = 2;
	public final static byte MT_ON = 3;
	public final static byte MT_LOCKED = 15;

	public IIcon accessOnIcon;
	public IIcon accessOffIcon;
	public IIcon accessLockedIcon;
	public IIcon accessOn1Icon;
	public IIcon accessOn2Icon;
	public IIcon accessOn3Icon;
	public IIcon accessOn4Icon;
	public IIcon accessOpenedIcon;

	public SSBlockAirlockFrameController(String asset) {
		super(Material.iron);
		this.setResistance(SSConfig.ssBayCasingResistance);
		this.setBlockName(asset);
		this.setBlockTextureName(SS.ASSET_PREFIX + asset);
		this.setBlockUnbreakable();
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(SS.ssTab);

		GameRegistry.registerTileEntity(SSTileEntityAirlockFrameController.class, asset);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)));
		this.accessOnIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_on");
		this.accessOffIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_off");
		this.accessLockedIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_locked");
		this.accessOpenedIcon = iconRegister.registerIcon(SS.ASSET_PREFIX + (this.getUnlocalizedName().substring(5)) + "_opened");
	}

	/**
	 * Show in inventory
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
			if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
				return this.blockIcon;
			return this.accessOnIcon;
	}
	
	public IIcon getIconFromMeta(int meta) {
		switch (meta) {
		case MT_OPENED:
			return this.accessOpenedIcon;
		case MT_OFF:
			return this.accessOffIcon;
		case MT_ON:
			return this.accessOnIcon;
		case MT_LOCKED:
			return this.accessLockedIcon;
		default:
			break;
		}
		return this.blockIcon;
	}
	

	/**
	 * render in world
	 */
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

		if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
			return this.blockIcon;

		SSTileEntityAirlockFrameController te = WorldUtils.get(world, x, y, z, SSTileEntityAirlockFrameController.class);

		if (te != null) {
			if ((side == ForgeDirection.EAST.ordinal() || side == ForgeDirection.WEST.ordinal()) && !te.getEW())
				return getIconFromMeta(te.getStatus());
			if ((side == ForgeDirection.NORTH.ordinal() || side == ForgeDirection.SOUTH.ordinal()) && te.getEW())
				return getIconFromMeta(te.getStatus());
			else
				return this.blockIcon;
		}
		return this.blockIcon;

	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new SSTileEntityAirlockFrameController();
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float a, float b, float c) {
		if (world.isRemote) {
			//SSTileEntityAirlockFrameController te = WorldUtils.get(world, x, y, z, SSTileEntityAirlockFrameController.class);
			//entityplayer.addChatMessage(new ChatComponentText("[SSAraminta]  CLI TE: " + te.getStatus()));
			return true;
		}

		SSTileEntityAirlockFrameController te = WorldUtils.get(world, x, y, z, SSTileEntityAirlockFrameController.class);
		if (te != null) {

			if (entityplayer.getCurrentEquippedItem() != null) {
				String itemName = entityplayer.getCurrentEquippedItem().getUnlocalizedName();
				System.out.println("itemName "+itemName);
				// Toggle EditMode
				if (itemName.equals("card_gold") && (te.getStatus() == MT_ON || te.getStatus() == MT_LOCKED)) {
					te.setEditMode(!te.getEditMode());
					
					if (te.getEditMode())
						te.setStatus(MT_LOCKED);
					else
						te.setStatus(MT_ON);
					
					((NetworkManager) IC2.network.get()).updateTileEntityField(te, "status");
					world.markBlockForUpdate(x, y, z);
					
					entityplayer.addChatMessage(new ChatComponentText("[SSAraminta] Set mode to " + te.getEditMode()));
				}
					
				if (itemName.equals("card_id") && te.getStatus() == MT_LOCKED && te.getEditMode()) {					
					if (entityplayer.getCurrentEquippedItem().getItem() instanceof SSItemCards) {
						String role = SSItemCards.getCardAcl(entityplayer.getCurrentEquippedItem());
							if (te.switchRole(SSPlayerRoles.valueOf(role)))
								entityplayer.addChatMessage(new ChatComponentText("[SSAraminta] Added role " + role));
							else
								entityplayer.addChatMessage(new ChatComponentText("[SSAraminta] Added remove " + role));
					}
				} else if (itemName.equals("item.ss.multitool")) {

					//te.state = 1;
					
					//((NetworkManager) IC2.network.get()).updateTileEntityField(te, "state");
					//world.markBlockForUpdate(x, y, z);
					
					//if (!entityplayer.isSneaking() && te.isPlayerHaveAccess(entityplayer)) {
					if (entityplayer.isSneaking()) {
						boolean right = false;
						// boolean right = te.checkStructure();
						entityplayer.addChatMessage(new ChatComponentText("[SSAraminta] SRV Airlock Structure: " + right + " getEW: " + te.getEW()));
						return true;
					} else {
						entityplayer.addChatMessage(new ChatComponentText("[SSAraminta] SRV status: " + te.getStatus()));
						if (te.getStatus() == MT_OPENED) {
							te.setStatus(MT_ON);
						} else {
							te.setStatus(MT_OPENED);
						}
						// world.markBlockForUpdate(x, y, z);

						((NetworkManager) IC2.network.get()).updateTileEntityField(te, "status");
						world.markBlockForUpdate(x, y, z);

						return true;
					}
				} else if (te.getStatus() == MT_OPENED) {
					entityplayer.openGui(SS.instance, 0, world, x, y, z);
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
		
		SSTileEntityAirlockFrameController tile = WorldUtils.get(world, x, y, z, SSTileEntityAirlockFrameController.class);
		
        if (tile != null && entityLiving instanceof EntityPlayer)
        {
            //tile.ownerName = ((EntityPlayer) entityLiving).getGameProfile().getName();
        	int facing = MathHelper.floor_double(((entityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        	tile.setEW((facing == 0 || facing == 2));
        	boolean right = tile.checkStructure();
        	if (!right && !world.isRemote) {
        		world.setBlockToAir(x, y, z);
        		world.func_147480_a(x,y,z,true);
        		if (!((EntityPlayer)entityLiving).capabilities.isCreativeMode)
        			dropBlockAsItem(world, x, y, z,new ItemStack(this));
        		return;
        	}
        		
        	System.out.println("Structure Airlock: "+ right + " getEW:" + tile.getEW());
        	tile.addAirlock();
        	tile.setStatus(MT_OPENED);
           	tile.markDirty();
           	
        }
        
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		SSTileEntityAirlockFrameController tile = WorldUtils.get(world,x,y,z,SSTileEntityAirlockFrameController.class);

		if (tile != null)
			tile.removeAirLock();
		
		super.breakBlock(world, x, y, z, block, par6);
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return false;
	}
	
	@Override
	public int damageDropped(int metadata) {
		return 0;
	}

}
