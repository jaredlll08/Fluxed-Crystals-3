package getfluxed.fluxedcrystals.tileentities.machine;

import getfluxed.fluxedcrystals.api.recipes.machines.RecipeMachineBase;
import getfluxed.fluxedcrystals.blocks.machines.BlockMachine;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.machines.MessageMachineBase;
import getfluxed.fluxedcrystals.tileentities.base.TileEnergyBase;
import getfluxed.fluxedcrystals.util.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by Jared on 5/31/2016.
 */
public abstract class TileEntityMachineBase extends TileEnergyBase implements ITickable, ISidedInventory {

    private static int[] slotsAll;
    public ItemStack[] items;
    public int itemCycleTime; // How long the current cycle has been running
    public int deviceCycleTime; // How long the machine will cycle
    public byte state;
    public int needCycleTime; // Based on everything how long should this
    // take?????
    private int currentTime = 0;
    private int totalTime = 0;
    // empty if not currently working on any valid recipe
    private String recipeIndex;
    private int prevEnergy;

    public TileEntityMachineBase(int energyCap, int invSize) {
        super(energyCap);
        items = new ItemStack[2];
        slotsAll = new int[invSize];
        for (int i = 0; i < invSize; i++) {
            slotsAll[i] = i;
        }
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getRecipeIndex() {
        return recipeIndex;
    }

    public void setRecipeIndex(String recipeIndex) {
        this.recipeIndex = recipeIndex;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public abstract int getEnergyUsed();

    public abstract RecipeMachineBase getRecipe(String index);

    public abstract HashMap<String, RecipeMachineBase> getRecipes();

    public abstract boolean isValidInput(ItemStack stack);

    public void update() {
        super.update();
        boolean canWork = false;
        boolean sendUpdate = false;
        // If we are still working then cycle the counter down 1
        if (this.deviceCycleTime > 0) {
            this.deviceCycleTime--;
            if (!worldObj.getBlockState(getPos()).getValue(BlockMachine.isActive)) {
                ((BlockMachine) worldObj.getBlockState(getPos()).getBlock()).setState(true, this.worldObj, getPos());
            }
        } else {
            if (worldObj.getBlockState(getPos()).getValue(BlockMachine.isActive)) {
                ((BlockMachine) worldObj.getBlockState(getPos()).getBlock()).setState(false, this.worldObj, getPos());
            }
        }

        if (!this.worldObj.isRemote) {
            if (prevEnergy != getEnergyStored()) {
                prevEnergy = getEnergyStored();
                sendUpdate = true;
            }
            RecipeMachineBase recipe = null;
            if (getStackInSlot(0) != null && !getRecipeIndex().isEmpty() && storage.getEnergyStored() > 0) {
                recipe = getRecipe(getRecipeIndex());
                if (recipe != null) {
                    if (storage.getEnergyStored() >= getEnergyUsed()) {
                        if (getStackInSlot(1) != null) {
                            if (NBTHelper.isInputEqual(recipe.getOutput(), getStackInSlot(1))) {
                                if (getStackInSlot(1).stackSize + recipe.getOutputAmount() < getStackInSlot(1).getMaxStackSize()) {
                                    if (getStackInSlot(0).stackSize >= recipe.getInputamount()) {
                                        canWork = true;
                                    }
                                }
                            }
                        } else {
                            if (getStackInSlot(0).stackSize >= recipe.getInputamount()) {
                                canWork = true;
                            }
                        }

                    }


                }

            }

            // Can we run a new item
            if (this.deviceCycleTime == 0 && canWork) {

                this.deviceCycleTime = 40;
                this.needCycleTime = 40;
                sendUpdate = true;

            }
            // Keep working if there is something in progress

            if (this.deviceCycleTime > 0 && canWork) {

                this.itemCycleTime++;
                if (this.itemCycleTime == 40) {

                    this.itemCycleTime = 0;
                    process();
                    storage.extractEnergy(getEnergyUsed(), false);
                    sendUpdate = true;

                }

            } else {

                this.itemCycleTime = 0;

            }

            // Last check
            if (this.deviceCycleTime > 0) {

                sendUpdate = true;

            }

        }

        if (sendUpdate) {

            this.markDirty();
            this.state = this.deviceCycleTime > 0 ? (byte) 1 : (byte) 0;
            getWorld().addBlockEvent(getPos(), this.getBlockType(), 1, this.state);
            PacketHandler.INSTANCE.sendToAllAround(new MessageMachineBase(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
            this.worldObj.notifyBlockOfStateChange(getPos(), getBlockType());

        }

    }

    @Override
    public void markDirty() {

        super.markDirty();

        PacketHandler.INSTANCE.sendToAllAround(new MessageMachineBase(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));

    }


    @Override
    public ItemStack decrStackSize(int i, int count) {
        ItemStack itemstack = getStackInSlot(i);

        if (itemstack != null) {
            if (itemstack.stackSize <= count) {
                setInventorySlotContents(i, null);
            } else {
                itemstack = itemstack.splitStack(count);

            }
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }


    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1) {

        return items[par1];
    }


    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack == null) {
            return false;
        }
        switch (slot) {
            default:
                return false;

            case 0:

                return isValidInput(stack);

            case 1:
                return false;

        }
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(getPos().getX() + 0.5f, getPos().getY() + 0.5f, getPos().getZ() + 0.5f) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }


    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {

        boolean changedItem;
        if (items[i] == null || itemstack == null) {
            changedItem = (items[i] == null) != (itemstack == null); // non-null
            // to
            // null,
            // or
            // vice
            // versa
        } else {
            changedItem = !items[i].isItemEqual(itemstack);
        }

        items[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }

        if (i == 0 && changedItem) {
            updateCurrentRecipe();
        }
    }

    public boolean addInventorySlotContents(int i, ItemStack itemstack) {
        if (items[i] != null) {

            if (items[i].isItemEqual(itemstack)) {
                items[i].stackSize += itemstack.stackSize;
            }
            if (items[i].stackSize > getInventoryStackLimit()) {
                items[i].stackSize = getInventoryStackLimit();
            }
        } else {
            setInventorySlotContents(i, itemstack);
        }
        return false;
    }

    /* NBT */
    @Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        readInventoryFromNBT(tags);
        currentTime = tags.getInteger("currentTime");
        String index = tags.getString("recipeIndex");
        if (index.equals("___null")) {
            index = "";
        }
        setRecipeIndex(index);
        deviceCycleTime = tags.getInteger("deviceCycleTime");
        itemCycleTime = tags.getInteger("itemCycleTime");
        needCycleTime = tags.getInteger("needCycleTime");
        updateCurrentRecipe();
    }

    public void readInventoryFromNBT(NBTTagCompound tags) {
        NBTTagList nbttaglist = tags.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int iter = 0; iter < nbttaglist.tagCount(); iter++) {
            NBTTagCompound tagList = nbttaglist.getCompoundTagAt(iter);
            byte slotID = tagList.getByte("Slot");
            if (slotID >= 0 && slotID < items.length) {
                items[slotID] = ItemStack.loadItemStackFromNBT(tagList);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        writeInventoryToNBT(tags);
        tags.setInteger("currentTime", currentTime);
        String index = getRecipeIndex();
        if (index == null || index.isEmpty()) {
            index = "___null";
        }
        tags.setString("recipeIndex", index);

        tags.setInteger("deviceCycleTime", deviceCycleTime);
        tags.setInteger("itemCycleTime", itemCycleTime);
        tags.setInteger("needCycleTime", needCycleTime);
        return tags;
    }

    private void writeInventoryToNBT(NBTTagCompound tags) {
        NBTTagList nbttaglist = new NBTTagList();
        for (int iter = 0; iter < items.length; iter++) {
            if (items[iter] != null) {
                NBTTagCompound tagList = new NBTTagCompound();
                tagList.setByte("Slot", (byte) iter);
                items[iter].writeToNBT(tagList);
                nbttaglist.appendTag(tagList);
            }
        }

        tags.setTag("Items", nbttaglist);

    }

    public boolean process() {
        if (!getRecipeIndex().isEmpty()) {
            RecipeMachineBase recipe = getRecipe(getRecipeIndex());
            if (recipe != null && getStackInSlot(0) != null && recipe.matchesExact(getStackInSlot(0))) {
                if (getStackInSlot(1) == null || NBTHelper.isInputEqual(getStackInSlot(1), recipe.getOutput())) {
                    decrStackSize(0, 1);
                    currentTime++;
                    if (currentTime >= recipe.getInputamount()) {
                        ItemStack out = recipe.getOutput().copy();
                        out.stackSize = recipe.getOutputAmount();
                        addInventorySlotContents(1, out);
                        currentTime = 0;
                    }
                }
                return true;
            }
        }
        currentTime = 0;
        return false;
    }


    public void updateCurrentRecipe() {
        setRecipeIndex("");
        ItemStack inputStack = getStackInSlot(0);
        if (inputStack != null && inputStack.stackSize > 0) {
            for (String id : getRecipes().keySet()) {
                RecipeMachineBase recipe = getRecipe(id);

                if (recipe != null && recipe.matchesExact(inputStack)) {
                    setRecipeIndex(id);
                    break;
                }
            }
        }
    }

    @Override
    public EnumSet<EnumFacing> getValidOutputs() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public EnumSet<EnumFacing> getValidInputs() {
        return EnumSet.allOf(EnumFacing.class);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return slotsAll;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (isItemValidForSlot(index, itemStackIn)) {

            for (String i : getRecipes().keySet()) {

                RecipeMachineBase recipe = getRecipe(i);

                if (recipe != null && NBTHelper.isInputEqual(recipe.getInput(), itemStackIn)) {
                    return true;
                }

            }

        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
        return slot != 0;
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }
}