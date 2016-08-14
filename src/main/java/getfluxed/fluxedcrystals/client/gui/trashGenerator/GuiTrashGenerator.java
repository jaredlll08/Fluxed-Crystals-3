package getfluxed.fluxedcrystals.client.gui.trashGenerator;

import getfluxed.fluxedcrystals.api.client.gui.GuiBase;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityTrashGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiTrashGenerator extends GuiBase {

    private TileEntityTrashGenerator tile;

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.modid, "textures/gui/trashGenerator.png");
    }

    public GuiTrashGenerator(InventoryPlayer invPlayer, TileEntityTrashGenerator tile2) {
        super(new ContainerTrashGenerator(invPlayer, tile2), tile2, invPlayer.player, "");
        this.tile = tile2;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);

        this.drawGeneratorProgress(81, 36, tile);
        this.drawPowerBar(tile.container);
    }
}
