package getfluxed.fluxedcrystals.client.gui.coalGenerator;

import getfluxed.fluxedcrystals.api.client.gui.GuiBase;
import getfluxed.fluxedcrystals.api.generators.Registry;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.client.gui.crusher.ContainerCrusher;
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

public class GUICoalGenerator extends GuiBase {

    private TileEntityCoalGenerator tile;

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.modid, "textures/gui/coalGenerator.png");
    }

    public GUICoalGenerator(InventoryPlayer invPlayer, TileEntityCoalGenerator tile2) {
        super(new ContainerCoalGenerator(invPlayer, tile2), tile2, invPlayer.player, "");
        this.tile = tile2;

        this.outlines = true;
    }

    @Override
    public boolean shouldOutline(ItemStack stack) {
        return Registry.BasicCoalGenerator.containsItemStack(stack);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);

        this.drawGeneratorProgress(82, 38, tile);
        this.drawPowerBar(tile.container);
    }
}
