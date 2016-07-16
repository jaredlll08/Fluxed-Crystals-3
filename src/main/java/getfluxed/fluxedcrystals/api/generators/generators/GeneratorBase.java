package getfluxed.fluxedcrystals.api.generators.generators;

import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageGenerator;
import net.darkhax.tesla.api.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public abstract class GeneratorBase extends TileEntity implements ITickable {
    public ItemStackHandler itemStackHandler;
    public int generationTimer = -1;
    public int generationTimerDefault = -1;
    private boolean firstTicked = false;
    private int firstTickedTime = 40;

    public BaseTeslaContainer container;


    public GeneratorBase(int cap, int inventorySize) {
        container = new BaseTeslaContainer(cap, 250, 250);
        itemStackHandler = new ItemStackHandler(inventorySize);
    }

    public boolean isGenerating() {
        return generationTimer > -1;
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
            for (EnumFacing face : EnumFacing.values()) {
                if (worldObj.getTileEntity(pos.offset(face)) != null) {
                    if (worldObj.getTileEntity(pos.offset(face)).hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, face.getOpposite())) {
                        BaseTeslaContainer con = (BaseTeslaContainer) worldObj.getTileEntity(pos.offset(face)).getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, face.getOpposite());
                        if (container.getStoredPower() >= 250 && con.getCapacity() - con.getStoredPower() >= 250) {
                            con.givePower(250, false);
                            container.takePower(250, false);
                            markDirty();
                            worldObj.getTileEntity(pos.offset(face)).markDirty();
                        }
                    }
                }
            }
            if (generationTimerDefault < 0 && this.container.getStoredPower() < this.container.getCapacity()) {
                if (itemStackHandler.getStackInSlot(0) != null) {
                    if (canGenerateEnergy(itemStackHandler.getStackInSlot(0))) {
                        generationTimer = getGenerationTime(itemStackHandler.getStackInSlot(0));
                        generationTimerDefault = getGenerationTime(itemStackHandler.getStackInSlot(0));
                        itemStackHandler.extractItem(0, 1, false);
                        markDirty();

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
            if (generationTimerDefault > 0 && this.container.getStoredPower() < this.container.getCapacity()) {
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("items", itemStackHandler.serializeNBT());
        nbt.setInteger("generationTimer", generationTimer);
        nbt.setInteger("generationTimerDefault", generationTimerDefault);
        nbt.setTag("TeslaContainer", this.container.serializeNBT());
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.itemStackHandler.deserializeNBT(nbt.getCompoundTag("items"));
        generationTimer = nbt.getInteger("generationTimer");
        generationTimerDefault = nbt.getInteger("generationTimerDefault");
        this.container = new BaseTeslaContainer(nbt.getCompoundTag("TeslaContainer"));
    }




    /*public EnumSet<EnumFacing> getValidOutputs() {
        return EnumSet.allOf(EnumFacing.class);
    }

    public EnumSet<EnumFacing> getValidInputs() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        if (getValidOutputs().contains(from)) {
            long ret = this.container.takePower(maxExtract, true);
            if (!simulate) {
                this.container.takePower(ret, false);
            }
            return ret;
        }
        return 0;
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        if (getValidInputs().contains(from)) {
            int ret = this.container.receiveEnergy(maxReceive, true);
            if (!simulate) {
                this.container.receiveEnergy(ret, false);
            }

            return ret;

        }
        return 0;
    }*/

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

        if (capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return (T) this.container;
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.itemStackHandler;
        }

        return super.getCapability(capability, facing);
    }


    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return true;
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
}
