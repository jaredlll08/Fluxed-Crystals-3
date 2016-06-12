package getfluxed.fluxedcrystals.client.gui.crystalio;

import getfluxed.fluxedcrystals.api.client.gui.GuiBase;
import getfluxed.fluxedcrystals.api.crystals.ICrystalInfoProvider;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityCrystalIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

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
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        if (title)
            this.fontRendererObj.drawString(this.tile.getDisplayName().getUnformattedText(), 38, 6, 0xa0a0a0);

        GlStateManager.pushAttrib();
        GlStateManager.color(1, 1, 1, 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(getTexture());
        GlStateManager.popAttrib();

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && outlines)
            drawOutlines();
    }
    @Override
    public boolean shouldOutline(ItemStack stack) {
        return stack.getItem() instanceof ICrystalInfoProvider;
    }

}
