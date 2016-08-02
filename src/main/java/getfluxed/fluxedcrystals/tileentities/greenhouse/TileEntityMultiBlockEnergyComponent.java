package getfluxed.fluxedcrystals.tileentities.greenhouse;

import net.darkhax.tesla.api.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

import java.util.EnumSet;

/**
 * Created by Jared on 5/7/2016.
 */
public abstract class TileEntityMultiBlockEnergyComponent extends TileEntityMultiBlockComponent implements ITickable {

    public BaseTeslaContainer container;
    protected int capacity;

    public TileEntityMultiBlockEnergyComponent(int cap) {
        super();
        init(cap);
    }

    public double getEnergyColor() {
        double energy = container.getStoredPower();
        double maxEnergy = container.getCapacity();
        return energy / maxEnergy;
    }

    private void init(int cap) {
        container = new BaseTeslaContainer(cap, 250, 250);
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
            if (tile.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, dir.getOpposite()) || tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, dir.getOpposite())) {
                BaseTeslaContainer cont = (BaseTeslaContainer) tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, dir.getOpposite());
                container.takePower(cont.givePower(container.takePower(container.getOutputRate(), true), false), false);
                tile.markDirty();
            }
        }
    }


    public final boolean canConnectEnergy(EnumFacing from) {
        return getValidInputs().contains(from) || getValidOutputs().contains(from);
    }


	/* Read/Write NBT */

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound energy = new NBTTagCompound();
        nbt.setTag("tesla", container.serializeNBT());
        nbt.setTag("energy", energy);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound energy = nbt.getCompoundTag("energy");
        container.deserializeNBT(nbt.getCompoundTag("tesla"));
    }


    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return (T) this.container;
        return super.getCapability(capability, facing);
    }
}
