package getfluxed.fluxedcrystals.client.gui.furnace;

import getfluxed.fluxedcrystals.api.client.gui.GuiBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GUIFurnace extends GuiBase {

    private TileEntityMachineFurnace tile;

    public GUIFurnace(InventoryPlayer invPlayer, TileEntityMachineFurnace tile2) {
        super(new ContainerFurnace(invPlayer, tile2), tile2, invPlayer.player, texture, tile2.getDisplayName().getUnformattedText());
        this.tile = tile2;
        this.title = true;
        this.outlines = true;
    }

    private static final ResourceLocation texture = new ResourceLocation(Reference.modid, "textures/gui/machineFurnace.png");

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);

        drawMachineProgress(tile.itemCycleTime, tile.needCycleTime);
        drawPowerBar(tile);
    }

    @Override
    public boolean shouldOutline(ItemStack stack) {
        return RecipeRegistry.isFurnaceInput(stack);
    }
}
