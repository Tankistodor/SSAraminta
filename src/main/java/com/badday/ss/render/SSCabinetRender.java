package com.badday.ss.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.badday.ss.SSConfig;
import com.badday.ss.blocks.SSTileEntityCabinet;
import com.badday.ss.core.utils.DirectionUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SSCabinetRender extends TileEntitySpecialRenderer {
	
	public SSCabinetRender() {
		super();
		loadTextures();
	}

	private SSCabinetModel cabinetModel = new SSCabinetModel();
	public static ResourceLocation[] textures;//      = new ResourceLocation("ss:textures/model/cabinet.png");
	public static final ResourceLocation textureCabinetGray      = new ResourceLocation("ss:textures/model/cabinetGray.png");
		
	public static void loadTextures() {
		textures = new ResourceLocation[SSConfig.ssCabinet_unlocalizedName.length];
		for (int i = 0; i < SSConfig.ssCabinet_unlocalizedName.length; i++) {
			textures[i] = new ResourceLocation("ss:textures/model/"+SSConfig.ssCabinet_unlocalizedName[i]+".png");
		}
	}
	
	public void renderTileEntityAt(SSTileEntityCabinet cabinetTE, double x, double y, double z, float partialTicks) {
		
		float scale = 1.0F / 16;
		
		//boolean large = cabinetTE.isConnected();
		boolean large = true;
		//if (large && !locker.isMain()) return;
		
		int index = (cabinetTE.mirror ? 1 : 0);
		SSCabinetModel model = cabinetModel;
		//bindTexture(locker.getResource());
		/*if (cabinetTE.getType() == 2) {
			bindTexture(textureLockerLarge);
		}*/
		bindTexture(textures[cabinetTE.getType()]);
		
		
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
		float angle = cabinetTE.prevLidAngle + (cabinetTE.lidAngle - cabinetTE.prevLidAngle) * partialTicks;
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		angle = angle * 90;
		
		GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
		int rotation = DirectionUtils.getRotation(cabinetTE.getOrientation());
		GL11.glRotatef(-rotation, 0.0F, 1.0F, 0.0F);
		
			GL11.glPushMatrix();
			GL11.glScalef(scale, scale, scale);
			
			model.renderAll(cabinetTE.mirror, angle);
			
			GL11.glPopMatrix();
		
			/**
		if (cabinetTE.canHaveLock()) {
			if (angle > 0) {
				double seven = 7 / 16.0D;
				GL11.glTranslated((cabinetTE.mirror ? seven : -seven), 0, seven);
				GL11.glRotatef((cabinetTE.mirror ? angle : -angle), 0, 1, 0);
				GL11.glTranslated((cabinetTE.mirror ? -seven : seven), 0, -seven);
			}
			LockAttachment a = cabinetTE.lockAttachment;
			GL11.glTranslated(0.5 - a.getX(), 0.5 - a.getY(), 0.5 - a.getZ());
			a.getRenderer().render(a, partialTicks);
		}*/
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float par8) {
		renderTileEntityAt((SSTileEntityCabinet)entity, x, y, z, par8);
	}
	
}

