package getfluxed.fluxedcrystals.client.greenhouse.monitor;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.monitor.TileEntityPowerMonitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

/**
 * Created by Jared on 4/25/2016.
 */
public class RenderPowerMonitor extends TileEntitySpecialRenderer<TileEntityPowerMonitor> {


    @Override
    public void renderTileEntityAt(TileEntityPowerMonitor te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te != null && te.getMaster() != null && !te.getMaster().equals(new BlockPos(0, 0, 0)) && te.getMultiBlock() !=null&& te.getMultiBlock().isActive()) {
//            GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_TEXTURE_BIT);
            GlStateManager.pushAttrib();
            GlStateManager.pushMatrix();
            float f3 = 0;
            int meta = te == null ? 0 : te.getBlockMetadata();
            float xOff = 0;
            float zOff = 0;
            switch (meta) {
                case 2 /*north*/:
                    f3 = 180.0F;
                    xOff = 1f - 0.03f;
                    zOff = -0.01f;
                    break;

                case 5 /*east*/:
                    f3 = -90.0F;
                    xOff = 1.01f;
                    zOff = 1 - 0.03f;
                    break;

                case 3 /*south*/:
                    f3 = 0;
                    xOff = 0.03f;
                    zOff = 1.01f;
                    break;

                case 4 /*west*/:
                    f3 = 90.0F;
                    xOff = -0.01f;
                    zOff = 0.03f;
                    break;


            }


            GlStateManager.translate((float) x + xOff, (float) y + 0.75 - 0.06F, (float) z + zOff);
            GlStateManager.rotate(-f3, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.2500F, 0);

            if (te != null) {
                FontRenderer fontrenderer = this.getFontRenderer();
                GlStateManager.depthMask(true);
                GlStateManager.disableLighting();
                renderText(fontrenderer, te);
            }

            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);

            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
//        GL11.glPopAttrib();
        }
    }


    private void renderText(FontRenderer fontrenderer, TileEntityPowerMonitor tileEntity) {
        float size;
        float factor = 2.0f;
        int currenty = 7;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.5F, 0);
        size = 0.00450F;
        GlStateManager.scale(size * factor, -size * factor, size);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.disableLighting();
        Minecraft minecraft = Minecraft.getMinecraft();
        TileEntitySoilController master = (TileEntitySoilController) tileEntity.getWorld().getTileEntity(tileEntity.getMaster());
        fontrenderer.drawString("Stored: " + getEnergyText(master.getEnergyStorage().getEnergyStored()), 4, 1, 0xFFFFFF);
        fontrenderer.drawString("Max: " + getEnergyText(master.getEnergyStorage().getMaxEnergyStored()), 4, 12, 0xFFFFFF);
        GlStateManager.popMatrix();
    }

    private String getEnergyText(int energy) {
        StringBuilder str = new StringBuilder();
        int rf = energy;
        if (energy > 999 && energy < 1000000) {
            str.append(Math.round((rf / 1000.0) * 100.0) / 100.0).append("K RF");
        } else if (energy > 999999 && energy < 1000000000) {
            str.append(Math.round((rf / 1000000.0) * 100.0) / 100.0).append("M RF");
        } else if (energy > 99999999 && energy < Integer.MAX_VALUE) {
            str.append(Math.round((rf / 1000000000.0) * 100.0) / 100.0).append("B RF");
        } else if (energy == Integer.MAX_VALUE) {
            str.append("PGM? Max RF");
        } else {
            str.append(rf).append(" RF");
        }

        return str.toString();
    }

}
