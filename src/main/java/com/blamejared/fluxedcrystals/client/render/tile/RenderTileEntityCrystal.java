package com.blamejared.fluxedcrystals.client.render.tile;

import com.blamejared.fluxedcrystals.tileentities.crystal.TileEntityCrystal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderTileEntityCrystal extends TileEntitySpecialRenderer<TileEntityCrystal> {
	
	public RenderTileEntityCrystal() {
	}
	
	@Override
	public void renderTileEntityAt(TileEntityCrystal te, double x, double y, double z, float partialTicks, int destroyStage) {
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GL11.glTranslated(x, y, z);
		
		GlStateManager.disableLighting();
		GL11.glTranslated(0.5, 0, 0.5);
		
		if(te.isActivated()) {
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 768);
			GL11.glRotatef(te.angle, 0, 1, 0);
			renderBlockModel(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()), true);
			GL11.glRotatef(-te.angle, 0, 1, 0);
			GlStateManager.blendFunc(770, 771);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GL11.glTranslated(-0.5, 0, -0.5);
		} else {
		
		/* Configuration tweaks */
			float BEAM_START_DISTANCE = 1F;
			float BEAM_END_DISTANCE = 1.2f;
			float MAX_OPACITY = 192;
			
			RenderHelper.disableStandardItemLighting();
			float f2 = 0.0F;
			float progress = 1 - te.getActivatingTime() / 100f;
			if(progress > 0.4F) {
				f2 = (progress - 0.4F) / 0.8F;
			}
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 768);
			GL11.glRotatef(te.angle, 0, 1, 0);
			GL11.glScalef(progress, progress, progress);
			GL11.glColor4f(1, 1, 1, 0.2f);
			renderBlockModel(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()), true);
			GL11.glScalef(1, 1, 1);
			GL11.glRotatef(te.angle, 0, -1, 0);
			GlStateManager.blendFunc(770, 771);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
			//			GL11.glTranslated(-0.5, 0, -0.5);
			
			//				/* Shift down a bit */
			//			GL11.glTranslatef(0f, 0.5f, 0);
		/* Rotate opposite direction at 20% speed */
			GL11.glRotatef(35 * -0.2f % 360, 0.5f, 0.5f, 0.5f);
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, 1);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDepthMask(false);
			Random RANDOM = new Random(te.getPos().toLong());//.setSeed(432L);
			for(int i = 0; i < (progress * progress) * 60; ++i) {
				GL11.glRotatef(RANDOM.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(RANDOM.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(RANDOM.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(RANDOM.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(RANDOM.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(RANDOM.nextFloat() * 360.0F + (progress * 90.0F), 0.0F, 0.0F, 1.0F);
				VertexBuffer vert = Tessellator.getInstance().getBuffer();
				vert.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
				float f3 = RANDOM.nextFloat() * BEAM_END_DISTANCE + 1.5F + f2 * 2F;
				float f4 = RANDOM.nextFloat() * BEAM_START_DISTANCE + 1.0F + f2 * 2.0F;
				vert.pos(0, 0, 0).color(100, 100, 255, (int) (MAX_OPACITY * (1.0F - f2))).endVertex();
				vert.pos(-0.866D * f4, f3, -0.5F * f4).color(65, 0, 199, 0).endVertex();
				vert.pos(0.866D * f4, f3, -0.5F * f4).color(65, 0, 199, 0).endVertex();
				vert.pos(0.0D, f3, 1.0F * f4).color(65, 0, 199, 0).endVertex();
				vert.pos(-0.866D * f4, f3, -0.5F * f4).color(65, 0, 199, 0).endVertex();
				Tessellator.getInstance().draw();
				GL11.glRotatef(-(RANDOM.nextFloat() * 360.0F), 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-(RANDOM.nextFloat() * 360.0F), 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-(RANDOM.nextFloat() * 360.0F), 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-(RANDOM.nextFloat() * 360.0F), 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-(RANDOM.nextFloat() * 360.0F), 0.0F, 1.0F, 0.0F);
			}
			
			
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			RenderHelper.enableStandardItemLighting();
		}
		
		
		GL11.glTranslated(-x, -y, -z);
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
	
	public static void renderBlockModel(World world, BlockPos pos, IBlockState state, boolean translateToOrigin) {
		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		if(translateToOrigin) {
			wr.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
		}
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		BlockModelShapes modelShapes = blockrendererdispatcher.getBlockModelShapes();
		IBakedModel ibakedmodel = modelShapes.getModelForState(state);
		final IBlockAccess worldWrapper = world;
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		for(BlockRenderLayer layer : BlockRenderLayer.values()) {
			if(state.getBlock().canRenderInLayer(state, layer)) {
				ForgeHooksClient.setRenderLayer(layer);
				blockrendererdispatcher.getBlockModelRenderer().renderModel(worldWrapper, ibakedmodel, state, pos, wr, false);
			}
		}
		ForgeHooksClient.setRenderLayer(null);
		if(translateToOrigin) {
			wr.setTranslation(0, 0, 0);
		}
		Tessellator.getInstance().draw();
	}
}
