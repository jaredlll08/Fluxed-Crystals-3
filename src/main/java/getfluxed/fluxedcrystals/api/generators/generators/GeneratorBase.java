package getfluxed.fluxedcrystals.api.generators.generators;

import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageGenerator;
import getfluxed.fluxedcrystals.tileentities.base.TileEnergyBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.EnumSet;

public abstract class GeneratorBase extends TileEnergyBase implements ISidedInventory, ITickable {

    public ItemStack[] items;
    private int maxEnergy;
    public int generationTimer = -1;
    public int generationTimerDefault = -1;
    private boolean firstTicked = false;
    private int firstTickedTime = 40;


    public GeneratorBase(int cap, int inventorySize) {
        super(cap);
        maxEnergy = cap;
        items = new ItemStack[inventorySize];

    }

    public boolean isGenerating() {
        return generationTimer > -1;
    }

    @Override
    public void update() {
        super.update();
        if (!worldObj.isRemote) {
            PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
            if (generationTimerDefault < 0 && storage.getEnergyStored() < storage.getMaxEnergyStored()) {
                if (getStackInSlot(0) != null) {
                    if (canGenerateEnergy(getStackInSlot(0))) {
                        generationTimer = getGenerationTime(getStackInSlot(0));
                        generationTimerDefault = getGenerationTime(getStackInSlot(0));
                        decrStackSize(0, 1);
                        if (!worldObj.isRemote) {
                            markDirty();
                        }
                    }
                }
            }
            if (generationTimer < 0) {
                generationTimerDefault = -1;
                generationTimer = -1;
                if (!worldObj.isRemote) {
                    markDirty();
                }
            }
            if (generationTimerDefault > 0 && getEnergyStored() < getMaxStorage()) {
                generationTimer--;
                generateEnergy(worldObj, getPos(), generationTimer);
                if (!worldObj.isRemote) {
                    markDirty();
                }
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
    }

    public abstract void generateEnergy(World world, BlockPos pos, int timer);

    public abstract boolean canGenerateEnergy(ItemStack stack);

    public abstract int getGenerationTime(ItemStack stack);

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
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1) {

        return items[par1];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        items[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public ItemStack addInventorySlotContents(int i, ItemStack itemstack) {
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
        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        writeInventoryToNBT(nbt);
        nbt.setInteger("generationTimer", generationTimer);
        nbt.setInteger("generationTimerDefault", generationTimerDefault);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        readInventoryFromNBT(nbt);
        generationTimer = nbt.getInteger("generationTimer");
        generationTimerDefault = nbt.getInteger("generationTimerDefault");
    }

    public void readInventoryFromNBT(NBTTagCompound tags) {
        NBTTagList nbttaglist = tags.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int iter = 0; iter < nbttaglist.tagCount(); iter++) {
            NBTTagCompound tagList = (NBTTagCompound) nbttaglist.getCompoundTagAt(iter);
            byte slotID = tagList.getByte("Slot");
            if (slotID >= 0 && slotID < items.length) {
                items[slotID] = ItemStack.loadItemStackFromNBT(tagList);
            }
        }
    }

    public void writeInventoryToNBT(NBTTagCompound tags) {
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

    public void closeInventory() {

    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(getPos().getX() + 0.5f, getPos().getY() + 0.5f, getPos().getZ() + 0.5f) <= 64;
    }

    @Override
    public EnumSet<EnumFacing> getValidOutputs() {
        return EnumSet.allOf(EnumFacing.class);
    }

    @Override
    public EnumSet<EnumFacing> getValidInputs() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        if (getValidOutputs().contains(from)) {
            int ret = storage.extractEnergy(maxExtract, true);
            if (!simulate) {
                storage.extractEnergy(ret, false);
            }
            return ret;
        }
        return 0;
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        if (getValidInputs().contains(from)) {
            int ret = storage.receiveEnergy(maxReceive, true);
            if (!simulate) {
                storage.receiveEnergy(ret, false);
            }

            return ret;

        }
        return 0;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

}
