package getfluxed.fluxedcrystals.client.gui.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotTileDep extends SlotItemHandler {


    public SlotTileDep(ItemStackHandler inventory, int number, int x, int y) {
        super(inventory, number, x, y);
    }

    public boolean isItemValid(ItemStack stack) {
        return inventory.isItemValidForSlot(this.slotNumber, stack);
    }

}
