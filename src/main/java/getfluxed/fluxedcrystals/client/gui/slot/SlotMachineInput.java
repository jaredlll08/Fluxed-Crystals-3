package getfluxed.fluxedcrystals.client.gui.slot;

import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Jared on 8/13/2016.
 */
public class SlotMachineInput extends SlotItemHandler {

    private TileEntityMachineBase tile;
    public SlotMachineInput(TileEntityMachineBase tile, int index, int xPosition, int yPosition) {
        super(tile.itemStackHandler, index, xPosition, yPosition);
        this.tile = tile;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack);
    }
}
