package getfluxed.fluxedcrystals.client.gui.trashGenerator;

import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityTrashGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTrashGenerator extends GuiContainer {

    private TileEntityTrashGenerator tile;

    public GuiTrashGenerator(InventoryPlayer invPlayer, TileEntityTrashGenerator tile2) {
        super(new ContainerTrashGenerator(invPlayer, tile2));

        this.tile = tile2;
    }

    private static final ResourceLocation texture = new ResourceLocation(Reference.modid, "textures/gui/trashGenerator.png");

    public void initGui() {
        xSize = 176;
        ySize = 166;
        super.initGui();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);


        int barWidth = (int) (((float) tile.getEnergyStored() / tile.getMaxStorage()) * 88);
        drawTexturedModalRect(44, 59, 44, 166, barWidth, 19);
        int barHeight = (int) (((float) tile.generationTimer / tile.generationTimerDefault) * 16);
        drawTexturedModalRect(81, 36 + barHeight, 176, barHeight, 16, barHeight + 16);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
