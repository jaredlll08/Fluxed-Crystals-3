package getfluxed.fluxedcrystals.client.gui.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotTileDep extends SlotItemHandler {


    public SlotTileDep(IItemHandler inventory, int number, int x, int y) {
        super(inventory, number, x, y);
    }

    public boolean isItemValid(ItemStack stack) {
        return inventory.isItemValidForSlot(this.slotNumber, stack);
    }

}
