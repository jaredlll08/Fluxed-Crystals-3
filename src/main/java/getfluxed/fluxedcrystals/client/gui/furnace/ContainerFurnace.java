package getfluxed.fluxedcrystals.client.gui.furnace;

import getfluxed.fluxedcrystals.client.gui.base.ContainerBase;
import getfluxed.fluxedcrystals.client.gui.slot.SlotTileDep;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFurnace extends ContainerBase {
    public ContainerFurnace(InventoryPlayer invPlayer, TileEntityMachineFurnace tile) {

        addSlotToContainer(new SlotTileDep(tile.itemStackHandler, 0, 44, 35));
        addSlotToContainer(new SlotTileDep(tile.itemStackHandler, 1, 116, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 142));
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, 84 + y * 18));
            }
        }

    }


}
