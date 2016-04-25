package getfluxed.fluxedcrystals.client.greenhouse.monitor;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.monitor.TileEntityPowerMonitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Jared on 4/25/2016.
 */
public class RenderPowerMonitor extends TileEntitySpecialRenderer<TileEntityPowerMonitor> {


    @Override
    public void renderTileEntityAt(TileEntityPowerMonitor te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te != null) {
            GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_TEXTURE_BIT);
            GlStateManager.pushAttrib();
            System.out.println("o");
            GlStateManager.pushMatrix();
            float f3;
            int meta = te == null ? 0 : te.getBlockMetadata();
            if (meta == 2) {
                f3 = 180.0F;
            } else if (meta == 4) {
                f3 = 90.0F;
            } else if (meta == 5) {
                f3 = -90.0F;
            } else {
                f3 = 0.0F;
            }

            GlStateManager.translate((float) x + 0.5F, (float) y + 5.75F, (float) z + 0.5F);
            GlStateManager.rotate(-f3, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.2500F, -0.4375F);

            if (te != null) {
                FontRenderer fontrenderer = this.getFontRenderer();
//            GlStateManager.depthMask(false);
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
        float f3;
        float factor = 2.0f;
        int currenty = 7;
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, 0.5F, 0.07F);
        f3 = 0.0075F;
        GlStateManager.scale(f3 * factor, -f3 * factor, f3);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.disableLighting();
        Minecraft minecraft = Minecraft.getMinecraft();
        TileEntitySoilController master = (TileEntitySoilController) tileEntity.getWorld().getTileEntity(tileEntity.getMaster());
        fontrenderer.drawString(master.getEnergyStorage().getEnergyStored() + " " + master.getEnergyStorage().getMaxEnergyStored(), 4, 1, 0xFFFFFF);
        GlStateManager.popMatrix();
    }

}
