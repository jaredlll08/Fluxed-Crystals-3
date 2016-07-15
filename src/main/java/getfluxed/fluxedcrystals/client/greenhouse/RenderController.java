package getfluxed.fluxedcrystals.client.greenhouse;

import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.util.TextureUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by Jared on 4/14/2016.
 */
public class RenderController extends TileEntitySpecialRenderer<TileEntitySoilController> {
    ModelCube cube;
    ResourceLocation cubeLocation = new ResourceLocation(Reference.modid + ":textures/models/cube.png");

    public RenderController(ModelCube cube) {
        this.cube = cube;
    }

    @Override
    public void renderTileEntityAt(TileEntitySoilController te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te != null) {
            if (te.isGrowing()) {
                BlockPos pos = te.getPos();
                Crystal crystal = te.getCrystalInfo();
                GL11.glTranslated(x, y, z);
                Minecraft.getMinecraft().getTextureManager().bindTexture(cubeLocation);
                GL11.glPushMatrix();
                GlStateManager.disableLighting();
                Random rand = new Random(crystal.hashCode());
                int stage = (int) ((te.getCurrentGrowth() / (crystal.getGrowthTimePerBlock()*te.getMultiBlock().getAirBlocks().size())) * 10);
                for (double i = 0; i < stage; i++) {
                    for (BlockPos bp : te.getMultiBlock().getAirBlocks()) {
                        bp = bp.up();
                        GL11.glColor4d((rand.nextBoolean() ? crystal.getColourObj().getRed() : crystal.getColourObj().brighter().getRed()) / 255.0, (rand.nextBoolean() ? crystal.getColourObj().getGreen() : crystal.getColourObj().brighter().getGreen()) / 255.0, (rand.nextBoolean() ? crystal.getColourObj().getBlue() : crystal.getColourObj().brighter().getBlue()) / 255.0 / 255.0, 1);
                        double nX = rand.nextDouble() + 0.5;
                        double nY = 0.8 + rand.nextDouble();
                        double nZ = rand.nextDouble() + 0.5;
                        GL11.glTranslated(bp.subtract(pos).getX() + nX, bp.subtract(pos).getY() - nY, bp.subtract(pos).getZ() + nZ);
                        cube.render(0.0675f/2);
                        GL11.glTranslated(-bp.subtract(pos).getX() - nX, -bp.subtract(pos).getY() + nY, -bp.subtract(pos).getZ() - nZ);
                    }
                }
                GlStateManager.enableLighting();
                GL11.glPopMatrix();
                GL11.glTranslated(-x, -y, -z);
            }

            if (te.getMultiBlock().isActive() && te.tank.getFluid() != null) {
                float d = TextureUtils.FLUID_OFFSET;

                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();
                BlockPos min = te.getMultiBlock().getAirBlocks().get(0).subtract(te.getPos());
                BlockPos max = te.getMultiBlock().getAirBlocks().get(te.getMultiBlock().getAirBlocks().size() - 1).subtract(te.getPos());

                double currentLiquid = te.tank.getFluidAmount();
                double maxLiquid = te.tank.getCapacity();
                double height = ((currentLiquid / maxLiquid)) * (min.getY() * max.getY());

//                TextureUtils.renderFluidCuboid(te.tank.getFluid(), te.getPos(), x, y, z, 2, 3, 4, max.getY()  height - d, max.getY() - d);
//                System.out.println(te.getMultiBlock().getAirBlocks().get(0));
//                System.out.println(te.getPos());
                TextureUtils.renderStackedFluidCuboid(te.tank.getFluid(), x, y, z, min.west(5), min, max, min.down().getY(), min.down().getY() + height);


                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
        }
    }
}
