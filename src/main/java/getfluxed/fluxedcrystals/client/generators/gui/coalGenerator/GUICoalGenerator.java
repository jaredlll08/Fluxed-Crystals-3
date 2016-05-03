package getfluxed.fluxedcrystals.client.generators.gui.coalGenerator;

import getfluxed.fluxedcrystals.api.generators.Registry;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityCoalGenerator;
import getfluxed.fluxedcrystals.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
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
        drawGuiContainerForegroundLayer(0,0);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        int barHeight = (int) (((float) tile.generationTimer / tile.generationTimerDefault) * 13);
        drawTexturedModalRect(80, 33, 176, 0, 16, 13 - barHeight);
        int barWidth = (int) (((float) tile.getEnergyStored() / tile.getMaxStorage()) * 89);
        drawTexturedModalRect(43, 50, 0, 166, barWidth, 18);


        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            for (int x = 0; x < 9; x++) {
                ItemStack stack = invPlayer.getStackInSlot(x);
                if (Registry.BasicCoalGenerator.containsItemStack(stack)) {
                    RenderUtils.drawRect(8 + 18 * x, 142, 16, 16, 0f, 0.8f, 0, 0.3f);
                } else if (stack != null)
                    RenderUtils.drawRect(8 + 18 * x, 142, 16, 16, 0.8f, 0, 0, 1);
            }
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    ItemStack stack = invPlayer.getStackInSlot(x + y * 9 + 9);
                    if (Registry.BasicCoalGenerator.containsItemStack(stack)) {
                        RenderUtils.drawRect(8 + 18 * x, 84 + (y * 18), 16, 16, 0f, 0.8f, 0f, 1f);
                    } else if (stack != null)
                        RenderUtils.drawRect(8 + 18 * x, 84 + (y * 18), 16, 16, 0.8f, 0, 0, 1f);
                }
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glPopAttrib();

    }

    @Override
    public void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);


    }
}
