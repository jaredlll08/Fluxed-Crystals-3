package getfluxed.fluxedcrystals.client.gui.sawmill;

import getfluxed.fluxedcrystals.api.client.gui.GuiBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineSawmill;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GUISawmill extends GuiBase {

    private TileEntityMachineSawmill tile;

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/machineSawmill.png");

    }

    public GUISawmill(InventoryPlayer invPlayer, TileEntityMachineSawmill tile2) {
        super(new ContainerSawmill(invPlayer, tile2), tile2, invPlayer.player, "Sawmill");
        this.tile = tile2;
        this.title = true;
        this.outlines = true;
    }

    private static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/machineSawmill.png");

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);

        drawMachineProgress(tile);
        drawPowerBar(tile.container);
    }

    @Override
    public boolean shouldOutline(ItemStack stack) {
        return RecipeRegistry.isSawmillInput(stack);
    }
}
