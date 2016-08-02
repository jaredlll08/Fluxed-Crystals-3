package getfluxed.fluxedcrystals.client.gui.crystalio;

import getfluxed.fluxedcrystals.client.gui.base.ContainerBase;
import getfluxed.fluxedcrystals.client.gui.slot.SlotTileDep;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityCrystalIO;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCrystalIO extends ContainerBase {
    public ContainerCrystalIO(InventoryPlayer invPlayer, TileEntityCrystalIO tile) {

        addSlotToContainer(new SlotTileDep(tile.getMasterTile().itemStackHandler, 0, 8, 8));
        addSlotToContainer(new SlotTileDep(tile.getMasterTile().itemStackHandler, 1, 152, 8) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        addSlotToContainer(new SlotTileDep(tile.getMasterTile().itemStackHandler, 2, 152, 26) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        addSlotToContainer(new SlotTileDep(tile.getMasterTile().itemStackHandler, 3, 152, 44) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
        addSlotToContainer(new SlotTileDep(tile.getMasterTile().itemStackHandler, 4, 152, 62) {
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
