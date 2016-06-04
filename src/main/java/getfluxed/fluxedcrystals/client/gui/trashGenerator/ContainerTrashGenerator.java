package getfluxed.fluxedcrystals.client.gui.trashGenerator;

import getfluxed.fluxedcrystals.client.gui.base.ContainerBase;
import getfluxed.fluxedcrystals.client.gui.slot.SlotTileDep;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityTrashGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerTrashGenerator extends ContainerBase {
    public ContainerTrashGenerator(InventoryPlayer invPlayer, TileEntityTrashGenerator manager) {

        addSlotToContainer(new SlotTileDep(manager, 0, 80, 11));

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
