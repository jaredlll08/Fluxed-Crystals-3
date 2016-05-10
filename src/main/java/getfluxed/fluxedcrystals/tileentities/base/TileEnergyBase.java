package getfluxed.fluxedcrystals.tileentities.base;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.EnumSet;

public abstract class TileEnergyBase extends TileEntity implements IEnergyHandler, ITickable {
    public EnergyStorage storage;
    protected int capacity;

    public TileEnergyBase(int cap) {
        super();
        init(cap, cap);
    }

    public TileEnergyBase(int cap, int maxTransfer) {
        super();
        init(cap, maxTransfer);
    }

    public double getEnergyColor() {
        double energy = storage.getEnergyStored();
        double maxEnergy = storage.getMaxEnergyStored();
        return energy / maxEnergy;
    }

    private void init(int cap, int maxTransfer) {
        storage = new EnergyStorage(cap);
//        storage.setEnergyStored(0);
    }

    public abstract EnumSet<EnumFacing> getValidOutputs();

    public abstract EnumSet<EnumFacing> getValidInputs();

    @Override
    public void update() {
        pushEnergy();
    }

    protected void pushEnergy() {
        for (EnumFacing dir : getValidOutputs()) {
            TileEntity tile = worldObj.getTileEntity(getPos().offset(dir));
            if (tile instanceof IEnergyHandler) {
                IEnergyHandler ieh = (IEnergyHandler) tile;
                storage.extractEnergy(ieh.receiveEnergy(dir, storage.extractEnergy(getOutputSpeed(), true), false), false);
            }
        }
    }

	/* I/O Handling */

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
    public final boolean canConnectEnergy(EnumFacing from) {
        return getValidInputs().contains(from) || getValidOutputs().contains(from);
    }

	/* IEnergyHandler basic impl */

    @Override
    public final int getEnergyStored(EnumFacing from) {
        return getEnergyStored();
    }

    @Override
    public final int getMaxEnergyStored(EnumFacing from) {
        return getMaxStorage();
    }

	/* IWailaAdditionalInfo */

	/* getters & setters */

    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    public void setEnergyStored(int energy) {
        storage.setEnergyStored(energy);
    }

    public int getMaxStorage() {
        return storage.getMaxEnergyStored();
    }

    public void setMaxStorage(int storage) {
        this.storage.setCapacity(storage);
    }

    public int getOutputSpeed() {
        return storage.getMaxExtract();
    }

    public void setOutputSpeed(int outputSpeed) {
        this.storage.setMaxExtract(outputSpeed);
    }

    public int getMaxOutputSpeed() {
        return getOutputSpeed();
    }

    public int getInputSpeed() {
        return storage.getMaxReceive();
    }

    public void setInputSpeed(int inputSpeed) {
        this.storage.setMaxReceive(inputSpeed);
    }

	/* Read/Write NBT */

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound energy = new NBTTagCompound();
        energy.setInteger("Energy", storage.getEnergyStored());
        energy.setInteger("MaxEnergy", storage.getMaxEnergyStored());

        nbt.setTag("energy", energy);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound energy = nbt.getCompoundTag("energy");
        storage.setCapacity(energy.getInteger("MaxEnergy"));
        storage.setEnergyStored(energy.getInteger("Energy"));
    }
}
