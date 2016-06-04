package getfluxed.fluxedcrystals.client.gui.base;

import getfluxed.fluxedcrystals.util.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
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
        ItemStack itemStack = null;
        Slot slot = this.inventorySlots.get(idx);

        if ((slot != null) && (slot.getHasStack())) {
            itemStack = slot.getStack();

            if (itemStack == null) {
                return null;
            }

            List<Slot> selectedSlots = new ArrayList();

            for (int x = 0; x < this.inventorySlots.size(); x++) {
                Slot advSlot = this.inventorySlots.get(x);
                if (advSlot.isItemValid(itemStack)) {
                    selectedSlots.add(advSlot);
                }
            }

            if ((selectedSlots.isEmpty())) {
                if (itemStack != null) {
                    for (int x = 0; x < this.inventorySlots.size(); x++) {
                        Slot advSlot = this.inventorySlots.get(x);
                        ItemStack dest = advSlot.getStack();
                        if (dest == null) {
                            advSlot.putStack(itemStack != null ? itemStack.copy() : null);
                            advSlot.onSlotChanged();
                            updateSlot(advSlot);
                            return null;
                        }
                    }
                }
            }

            if (itemStack != null) {
                for (Slot d : selectedSlots) {
                    if ((d.isItemValid(itemStack)) && (itemStack != null)) {
                        if (d.getHasStack()) {
                            ItemStack t = d.getStack();

                            if ((itemStack != null) && (NBTHelper.isStackEqual(itemStack, t))) {
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
                                    slot.putStack(null);
                                    d.onSlotChanged();
                                    updateSlot(slot);
                                    updateSlot(d);
                                    return null;
                                }

                                updateSlot(d);
                            }
                        }
                    }
                }

                for (Slot d : selectedSlots) {
                    if ((d.isItemValid(itemStack)) && (itemStack != null)) {
                        if (d.getHasStack()) {
                            ItemStack t = d.getStack();
                            if ((itemStack != null) && (NBTHelper.isStackEqual(itemStack, t))) {
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
                                    slot.putStack(null);
                                    d.onSlotChanged();
                                    updateSlot(slot);
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
                            d.putStack(tmp);

                            if (itemStack.stackSize <= 0) {
                                slot.putStack(null);
                                d.onSlotChanged();
                                updateSlot(slot);
                                updateSlot(d);
                                return null;
                            }

                            updateSlot(d);
                        }
                    }
                }
            }

            slot.putStack(itemStack != null ? itemStack.copy() : null);
        }
        updateSlot(slot);
        return null;
    }
}
