package getfluxed.fluxedcrystals.client.gui.crusher;

import getfluxed.fluxedcrystals.api.client.gui.GuiBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.client.gui.crystalio.ContainerCrystalIO;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineCrusher;
import getfluxed.fluxedcrystals.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GUICrusher extends GuiBase {

    private TileEntityMachineCrusher tile;

    public GUICrusher(InventoryPlayer invPlayer, TileEntityMachineCrusher tile2) {
        super(new ContainerCrusher(invPlayer, tile2), tile2, invPlayer.player, tile2.getDisplayName().getUnformattedText());
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

        this.drawPowerBar(tile);
        this.drawMachineProgress(tile);
    }

    @Override
    public boolean shouldOutline(ItemStack stack) {
        return RecipeRegistry.isCrusherInput(stack);
    }
}
