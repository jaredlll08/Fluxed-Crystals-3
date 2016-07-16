package getfluxed.fluxedcrystals.client.gui.crusher;

import getfluxed.fluxedcrystals.api.client.gui.GuiBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineCrusher;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GUICrusher extends GuiBase {

    private TileEntityMachineCrusher tile;

    public GUICrusher(InventoryPlayer invPlayer, TileEntityMachineCrusher tile2) {
        super(new ContainerCrusher(invPlayer, tile2), tile2, invPlayer.player, "Crusher");
        this.tile = tile2;

        this.title = true;
        this.outlines = true;
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.modid, "textures/gui/machineCrusher.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);

        this.drawPowerBar(tile.container);
        this.drawMachineProgress(tile);
    }

    @Override
    public boolean shouldOutline(ItemStack stack) {
        return RecipeRegistry.isCrusherInput(stack);
    }
}
