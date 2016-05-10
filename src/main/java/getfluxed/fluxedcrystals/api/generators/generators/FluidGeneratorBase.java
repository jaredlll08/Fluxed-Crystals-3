package getfluxed.fluxedcrystals.api.generators.generators;

import getfluxed.fluxedcrystals.api.generators.Registry;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageGeneratorFluid;
import getfluxed.fluxedcrystals.tileentities.base.TileEnergyBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.EnumSet;


public abstract class FluidGeneratorBase extends TileEnergyBase implements IFluidTank, IFluidHandler, ITickable {

    protected TileEntity tile;
    public FluidTank tank;
    public int prevAmount;
    public int initPer;
    public int generationTimer = -1;
    public int generationTimerDefault = -1;

    public FluidGeneratorBase(int tankCapacity, int energyStorage) {
        super(energyStorage);
        tank = new FluidTank(tankCapacity);
        prevAmount = 0;
        initPer = 40;
    }

    public boolean isGenerating() {
        return generationTimer > -1;
    }

    public FluidGeneratorBase readFluidFromNBT(NBTTagCompound nbt) {
        this.tank = this.tank.readFromNBT(nbt.getCompoundTag("tank"));
        return this;
    }

    public NBTTagCompound writeFluidToNBT(NBTTagCompound nbt) {
        NBTTagCompound tank = new NBTTagCompound();
        this.tank.writeToNBT(tank);
        nbt.setTag("tank", tank);
        return nbt;
    }

    @Override
    public void update() {
        super.update();

        if (tank.getFluidAmount() >= 0 && !(tank.getFluidAmount() == prevAmount)) {
            prevAmount = tank.getFluidAmount();
            if (!worldObj.isRemote)
                markDirty();
        }
        if (initPer >= 0 && tank.getFluid() != null) {
            initPer--;
            if (initPer <= 0) {
                if (!worldObj.isRemote)
                    markDirty();
            }
        }
        if (generationTimerDefault < 0 && generationTimer < 0 && storage.getEnergyStored() < storage.getMaxEnergyStored()) {
            if (getFluid() != null) {

                if (Registry.LavaGenerator.canUse(getFluid().getFluid())) {
                    generationTimer = 300;
                    generationTimerDefault = 300;
                    drain(250, true);
                }

            }
        }
        if (generationTimerDefault >= 0 && getEnergyStored() < getMaxStorage()) {
            generationTimer--;
            generateEnergy(worldObj, getPos(), generationTimer);
        }
        if (generationTimer < 0 && generationTimerDefault >= 0) {
            generationTimer = -1;
            generationTimerDefault = -1;
        }
    }

    public abstract void generateEnergy(World world, BlockPos pos, int timer);

    public void setFluid(FluidStack fluid) {
        this.tank.setFluid(fluid);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public FluidStack getFluid() {
        return tank.getFluid();
    }

    @Override
    public int getFluidAmount() {
        if (tank.getFluid() == null) {
            return 0;
        }
        return tank.getFluidAmount();
    }

    @Override
    public int getCapacity() {
        return tank.getCapacity();
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null) {
            return 0;
        }

        if (!doFill) {
            if (tank.getFluid() == null) {
                return Math.min(capacity, resource.amount);
            }

            if (!tank.getFluid().isFluidEqual(resource)) {
                return 0;
            }

            return Math.min(capacity - tank.getFluid().amount, resource.amount);
        }

        if (tank.getFluid() == null) {
            tank.setFluid( new FluidStack(resource, Math.min(capacity, resource.amount)));
            if (tile != null) {
                FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(tank.getFluid(), tile.getWorld(), tile.getPos(), this, tank.getFluid().amount));
            }
            return tank.getFluid().amount;
        }

        if (!tank.getFluid().isFluidEqual(resource)) {
            return 0;
        }
        int filled = capacity - tank.getFluid().amount;

        if (resource.amount < filled) {
            tank.getFluid().amount += resource.amount;
            filled = resource.amount;
        } else {
            tank.getFluid().amount = capacity;
        }

        if (tile != null) {
            FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(tank.getFluid(), tile.getWorld(), tile.getPos(), this, filled));
        }
        return filled;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (tank.getFluid() == null) {
            return null;
        }

        int drained = maxDrain;
        if (tank.getFluid().amount < drained) {
            drained = tank.getFluid().amount;
        }

        FluidStack stack = new FluidStack(tank.getFluid(), drained);
        if (doDrain) {
            tank.getFluid().amount -= drained;
            if (tank.getFluid().amount <= 0) {
                tank.setFluid(null);
            }

            if (tile != null) {
                FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(tank.getFluid(), tile.getWorld(), tile.getPos(), this, drained));
            }
        }
        return stack;
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
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (resource == null) {
            return 0;
        }

        FluidStack resourceCopy = resource.copy();
        int totalUsed = 0;

        FluidStack liquid = tank.getFluid();
        if (liquid != null && liquid.amount > 0 && !liquid.isFluidEqual(resourceCopy)) {
            return 0;
        }

        while (resourceCopy.amount > 0) {
            int used = tank.fill(resourceCopy, doFill);
            resourceCopy.amount -= used;
            if (used > 0) {
                if (!worldObj.isRemote)
                    markDirty();
            }

            totalUsed += used;

        }

        return totalUsed;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxEmpty, boolean doDrain) {
        if (!worldObj.isRemote)
            markDirty();
        FluidStack output = tank.drain(maxEmpty, doDrain);

        return output;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (resource == null) {
            return null;
        }
        if (!resource.isFluidEqual(tank.getFluid())) {
            return null;
        }
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing direction) {
        FluidTank compositeTank = new FluidTank(tank.getCapacity());

        int capacity = tank.getCapacity();

        if (tank.getFluid() != null) {
            compositeTank.setFluid(tank.getFluid().copy());
        } else {
            return new FluidTankInfo[]{compositeTank.getInfo()};
        }

        FluidStack liquid = tank.getFluid();
        if (liquid == null || liquid.amount == 0) {

        } else {
            compositeTank.getFluid().amount += liquid.amount;
        }

        capacity += tank.getCapacity();

        compositeTank.setCapacity(capacity);
        return new FluidTankInfo[]{compositeTank.getInfo()};
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        Fluid tankFluid = getFluidType();
        return tankFluid == null || tankFluid == tank.getFluid().getFluid();
    }

    public Fluid getFluidType() {
        return tank.getFluid() != null ? tank.getFluid().getFluid() : null;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        Fluid tankFluid = getFluidType();
        return tankFluid != null && tankFluid == tank.getFluid().getFluid();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        PacketHandler.INSTANCE.sendToAllAround(new MessageGeneratorFluid(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
    }
}
