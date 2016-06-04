package getfluxed.fluxedcrystals.client.gui.crusher;

import getfluxed.fluxedcrystals.client.gui.base.ContainerBase;
import getfluxed.fluxedcrystals.client.gui.slot.SlotTileDep;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineCrusher;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerCrusher extends ContainerBase {
    public ContainerCrusher(InventoryPlayer invPlayer, TileEntityMachineCrusher tile) {

        addSlotToContainer(new SlotTileDep(tile, 0, 44, 35));
        addSlotToContainer(new SlotTileDep(tile, 1, 116, 35));
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
