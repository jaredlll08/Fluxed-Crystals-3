package getfluxed.fluxedcrystals.client.gui.coalGenerator;

import getfluxed.fluxedcrystals.api.generators.Registry;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityCoalGenerator;
import getfluxed.fluxedcrystals.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GUICoalGenerator extends GuiContainer {

    private TileEntityCoalGenerator tile;
    private InventoryPlayer invPlayer;

    public GUICoalGenerator(InventoryPlayer invPlayer, TileEntityCoalGenerator tile2) {
        super(new ContainerCoalGenerator(invPlayer, tile2));
        this.invPlayer = invPlayer;
        this.tile = tile2;
    }

    private static final ResourceLocation texture = new ResourceLocation(Reference.modid, "textures/gui/coalGenerator.png");


    public void initGui() {
        this.xSize = 176;
        this.ySize = 166;
        super.initGui();
    }

    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {

        GlStateManager.pushAttrib();
        GlStateManager.color(1, 1, 1, 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        int barHeight = (int) (((float) tile.generationTimer / tile.generationTimerDefault) * 16);
        System.out.println(barHeight);
        drawTexturedModalRect(82, 38 + barHeight, 176, barHeight, 16, barHeight + 16);
        int barWidth = (int) (((float) tile.getEnergyStored() / tile.getMaxStorage()) * 88);
        drawTexturedModalRect(44, 59, 44, 166, barWidth, 19);
        GlStateManager.popAttrib();

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            GL11.glPushMatrix();
            GlStateManager.pushAttrib();
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_FOG);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);

            for (int x = 0; x < 9; x++) {
                ItemStack stack = invPlayer.getStackInSlot(x);
                if (Registry.BasicCoalGenerator.containsItemStack(stack)) {
                    RenderUtils.drawRectNoFade(8 + 18 * x, 142, 280, 16, 16, 0f, 0.8f, 0, 2f, 1);
                } else if (stack != null)
                    RenderUtils.drawRectNoFade(8 + 18 * x, 142, 280, 16, 16, 0.8f, 0, 0, 2f, 1);
            }
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    ItemStack stack = invPlayer.getStackInSlot(x + y * 9 + 9);
                    if (Registry.BasicCoalGenerator.containsItemStack(stack)) {
                        RenderUtils.drawRectNoFade(8 + 18 * x, 84 + (y * 18), 280, 16, 16, 0f, 0.8f, 0f, 2f, 1);
                    } else if (stack != null)
                        RenderUtils.drawRectNoFade(8 + 18 * x, 84 + (y * 18), 280, 16, 16, 0.8f, 0, 0, 2f, 1);
                }
            }
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            GlStateManager.popAttrib();
            GL11.glPopMatrix();
        }

    }

    @Override
    public void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);


    }
}
