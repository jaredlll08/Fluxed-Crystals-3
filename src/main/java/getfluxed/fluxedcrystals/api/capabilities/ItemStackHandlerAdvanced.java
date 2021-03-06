package getfluxed.fluxedcrystals.api.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jared on 7/10/2016.
 */
public class ItemStackHandlerAdvanced implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {
    private List<ItemStack> stacks;

    public ItemStackHandlerAdvanced() {
        stacks = new ArrayList<>();
    }


    public void addStack(ItemStack stack) {
        this.stacks.add(stack);
    }

    @Override
    @Deprecated
    public void setStackInSlot(int slot, ItemStack stack) {
        validateSlotIndex(slot);
        if (ItemStack.areItemStacksEqual(this.stacks.get(slot), stack))
            return;
        this.stacks.set(slot, stack);
        onContentsChanged(slot);
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack == null || stack.stackSize == 0)
            return null;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        int limit = getStackLimit(slot, stack);

        if (existing != null) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.stackSize;
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.stackSize > limit;

        if (!simulate) {
            if (existing == null) {
                this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.stackSize += reachedLimit ? limit : stack.stackSize;
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return null;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        if (existing == null)
            return null;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.stackSize <= toExtract) {
            if (!simulate) {
                this.stacks.set(slot, null);
                onContentsChanged(slot);
            }
            return existing;
        } else {
            if (!simulate) {
                this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.stackSize - toExtract));
                onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    protected int getStackLimit(int slot, ItemStack stack) {
        return stack.getMaxStackSize();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i) != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stacks.get(i).writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.stacks = new ArrayList<>();
        for (int i = 0; i < (nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.size()); i++) {
            stacks.add(null);
        }
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < this.stacks.size()) {
                stacks.set(slot, ItemStack.loadItemStackFromNBT(itemTags));
            }
        }
        onLoad();
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }

    protected void onLoad() {

    }

    protected void onContentsChanged(int slot) {

    }

    public List<ItemStack> getStacks() {
        return stacks;
    }


    public void setStacks(List<ItemStack> stacks) {
        this.stacks = stacks;
    }
}
