package getfluxed.fluxedcrystals.client.gui.furnace;

import getfluxed.fluxedcrystals.api.client.gui.GuiBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GUIFurnace extends GuiBase {

    private TileEntityMachineFurnace tile;

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.modid, "textures/gui/machineFurnace.png");
    }

    public GUIFurnace(InventoryPlayer invPlayer, TileEntityMachineFurnace tile2) {
        super(new ContainerFurnace(invPlayer, tile2), tile2, invPlayer.player, "Furnace");
        this.tile = tile2;
        this.title = true;
        this.outlines = true;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);
        drawMachineProgress(tile.itemCycleTime, tile.needCycleTime);
        drawPowerBar(tile.container);
    }

    @Override
    public boolean shouldOutline(ItemStack stack) {
        return RecipeRegistry.isFurnaceInput(stack);
    }
}
