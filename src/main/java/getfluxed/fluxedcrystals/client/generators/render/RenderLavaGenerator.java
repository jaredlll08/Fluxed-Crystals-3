package getfluxed.fluxedcrystals.client.generators.render;

import getfluxed.fluxedcrystals.tileentities.generators.TileEntityLavaGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import java.util.Random;

public class RenderLavaGenerator extends TileEntitySpecialRenderer<TileEntityLavaGenerator> {
	private Random random = new Random();
	private Minecraft mc = Minecraft.getMinecraft();
	private final float size = 0.0625f;


	public void renderTile(TileEntityLavaGenerator tile, double x, double y, double z) {
		
	}

	@Override
	public void renderTileEntityAt(TileEntityLavaGenerator te, double x, double y, double z, float partialTicks, int destroyStage) {
		renderTile(te, x, y, z);
	}
}
