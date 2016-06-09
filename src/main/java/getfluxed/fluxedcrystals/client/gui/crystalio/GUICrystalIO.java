package getfluxed.fluxedcrystals.client.gui.crystalio;

import getfluxed.fluxedcrystals.api.client.gui.GuiBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.client.gui.trashGenerator.ContainerTrashGenerator;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityCrystalIO;
import getfluxed.fluxedcrystals.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GUICrystalIO extends GuiBase {

    private TileEntityCrystalIO tile;

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.modid, "textures/gui/ioCrystal.png");
    }

    public GUICrystalIO(InventoryPlayer invPlayer, TileEntityCrystalIO tile2) {
        super(new ContainerCrystalIO(invPlayer, tile2), tile2, invPlayer.player, tile2.getDisplayName().getUnformattedText());
        this.tile = tile2;

        this.title = true;
        this.outlines = true;
    }

    @Override
    public boolean shouldOutline(ItemStack stack) {
        return RecipeRegistry.isCrusherInput(stack);
    }

}
