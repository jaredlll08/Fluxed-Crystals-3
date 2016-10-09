package getfluxed.fluxedcrystals.api.generators.generators;

import getfluxed.fluxedcrystals.api.nbt.TileEntityBase;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageGenerator;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
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

public abstract class GeneratorBase extends TileEntityBase implements ITickable {
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

    protected boolean pushEnergy() {
        for (EnumFacing dir : EnumFacing.VALUES) {
            TileEntity tile = worldObj.getTileEntity(getPos().offset(dir));
            if (tile != null)
                if (tile.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, dir.getOpposite()) || tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, dir.getOpposite())) {
                    BaseTeslaContainer cont = (BaseTeslaContainer) tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, dir.getOpposite());
                    container.takePower(cont.givePower(container.takePower(container.getOutputRate(), true), false), false);
                    if (!worldObj.isRemote) {
                        tile.markDirty();
                        markDirty();
                        return true;
                    }
                }
        }
        return false;
    }

    @Override
    public void update() {
        boolean sendUpdate = false;
//            PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));

        if (generationTimerDefault < 0 && this.container.getStoredPower() < this.container.getCapacity()) {
            if (itemStackHandler.getStackInSlot(0) != null) {
                if (canGenerateEnergy(itemStackHandler.getStackInSlot(0))) {
                    generationTimer = getGenerationTime(itemStackHandler.getStackInSlot(0));
                    generationTimerDefault = getGenerationTime(itemStackHandler.getStackInSlot(0));
                    itemStackHandler.extractItem(0, 1, false);
                    if (!worldObj.isRemote)
                        sendUpdate = true;

                }
            }
        }
        if (generationTimer < 0) {
            generationTimerDefault = -1;
            generationTimer = -1;
            if (!worldObj.isRemote)
                sendUpdate = true;
        }
        if (pushEnergy()) {
            sendUpdate = true;
        }
        if (generationTimerDefault > 0 && this.container.getStoredPower() < this.container.getCapacity()) {
            generationTimer--;
            generateEnergy(worldObj, getPos(), generationTimer);
            if (!worldObj.isRemote)
                sendUpdate = true;
        }

        if (!worldObj.isRemote) {
            if (sendUpdate) {
                this.markDirty();
                PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
                this.worldObj.notifyBlockOfStateChange(getPos(), getBlockType());
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

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
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
