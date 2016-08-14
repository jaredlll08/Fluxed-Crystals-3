package getfluxed.fluxedcrystals.client.gui.base;

import getfluxed.fluxedcrystals.client.gui.slot.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jared on 5/29/2016.
 */
public class ContainerBase extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    private void updateSlot(final Slot slot) {
        this.detectAndSendChanges();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int idx) {

        //TODO change to work with isItemValid ==false
        ItemStack itemStack = null;
        Slot clickSlot = (Slot) this.inventorySlots.get(idx);

        if (clickSlot instanceof SlotOutput) {
            return null;
        }

        if ((clickSlot != null) && (clickSlot.getHasStack())) {
            itemStack = clickSlot.getStack();

            if (itemStack == null) {
                return null;
            }
            List<Slot> selectedSlots = new ArrayList<Slot>();

            if (clickSlot.inventory instanceof InventoryPlayer) {
                for (int x = 0; x < this.inventorySlots.size(); x++) {
                    Slot advSlot = (Slot) this.inventorySlots.get(x);
                    if (advSlot.isItemValid(itemStack)) {
                        selectedSlots.add(advSlot);
                    }
                }
            } else {
                for (int x = 0; x < this.inventorySlots.size(); x++) {
                    Slot advSlot = (Slot) this.inventorySlots.get(x);

                    if ((advSlot.inventory instanceof InventoryPlayer)) {
                        if (advSlot.isItemValid(itemStack)) {
                            selectedSlots.add(advSlot);
                        }
                    }
                }
            }

            if (itemStack != null) {
                for (Slot d : selectedSlots) {
                    if ((d.isItemValid(itemStack)) && (itemStack != null)) {
                        if (d.getHasStack()) {
                            ItemStack t = d.getStack();

                            if ((itemStack != null) && (itemStack.isItemEqual(t))) {
                                int maxSize = t.getMaxStackSize();

                                if (maxSize > d.getSlotStackLimit()) {
                                    maxSize = d.getSlotStackLimit();
                                }

                                int placeAble = maxSize - t.stackSize;

                                if (itemStack.stackSize < placeAble) {
                                    placeAble = itemStack.stackSize;
                                }

                                t.stackSize += placeAble;
                                itemStack.stackSize -= placeAble;

                                if (itemStack.stackSize <= 0) {
                                    clickSlot.putStack(null);
                                    d.onSlotChanged();
                                    updateSlot(clickSlot);
                                    updateSlot(d);
                                    return null;
                                }

                                updateSlot(d);
                            }
                        } else {
                            int maxSize = itemStack.getMaxStackSize();

                            if (maxSize > d.getSlotStackLimit()) {
                                maxSize = d.getSlotStackLimit();
                            }

                            ItemStack tmp = itemStack.copy();

                            if (tmp.stackSize > maxSize) {
                                tmp.stackSize = maxSize;
                            }

                            itemStack.stackSize -= tmp.stackSize;
                            //Sets the stack
                            d.putStack(tmp);

                            if (itemStack.stackSize <= 0) {
                                clickSlot.putStack(null);
                                d.onSlotChanged();
                                updateSlot(clickSlot);
                                updateSlot(d);
                                return null;
                            }

                            updateSlot(d);
                        }
                    }
                }
            }

            clickSlot.putStack(itemStack != null ? itemStack.copy() : null);
        }
        updateSlot(clickSlot);
        return null;
    }


}
