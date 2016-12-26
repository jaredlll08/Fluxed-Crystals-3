package com.blamejared.fluxedcrystals.client.render.tile;

import com.blamejared.fluxedcrystals.client.model.pylon.*;
import com.blamejared.fluxedcrystals.reference.Reference;
import com.blamejared.fluxedcrystals.tileentities.crystal.TileEntityCrystalPylon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class RenderTileEntityCrystalPylon extends TileEntitySpecialRenderer<TileEntityCrystalPylon> {
	
	
	private ModelPylonCube modelPylonCube;
	private ModelPylonOuterCubes modelPylonOuterCubes;
	
	public RenderTileEntityCrystalPylon() {
		this.modelPylonCube = new ModelPylonCube();
		this.modelPylonOuterCubes = new ModelPylonOuterCubes();
	}
	
	@Override
	public void renderTileEntityAt(TileEntityCrystalPylon te, double x, double y, double z, float partialTicks, int destroyStage) {
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GL11.glTranslated(x, y, z);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		GlStateManager.color(1, 1, 1, 1);
		GL11.glTranslated(0.5, 0.5 + te.floatAm, 0.5);
		GL11.glRotatef(te.angle, 0, 1, 0);
		renderBlockModel(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()), true);
		GL11.glRotatef(-te.angle, 0, 1, 0);
		GL11.glTranslated(-0.5, -0.5 - te.floatAm, -0.5);
		
		
		GL11.glTranslated(0.5, -0.5 + te.floatAm, 0.5);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/models/crystal.png"));
		GL11.glRotatef(-te.angle, 0, 1, 0);
		modelPylonOuterCubes.render(0.0675F);
		GL11.glRotatef(te.angle, 0, 1, 0);
		
		
		modelPylonCube.render(0.0675F);
		GL11.glTranslated(-0.5, 0.5 - te.floatAm, -0.5);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
	
	public static void renderBlockModel(World world, BlockPos pos, IBlockState state, boolean translateToOrigin) {
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		if(translateToOrigin) {
			wr.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
		}
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		BlockModelShapes modelShapes = blockrendererdispatcher.getBlockModelShapes();
		IBakedModel ibakedmodel = modelShapes.getModelForState(state);
		final IBlockAccess worldWrapper = world;
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