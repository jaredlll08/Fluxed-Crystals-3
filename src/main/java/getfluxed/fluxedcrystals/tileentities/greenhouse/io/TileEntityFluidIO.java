package getfluxed.fluxedcrystals.tileentities.greenhouse.io;

import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageFluid;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

/**
 * Created by Jared on 4/15/2016.
 */
public class TileEntityFluidIO extends TileEntityMultiBlockComponent implements IFluidHandler {


    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (canFill(from, resource.getFluid())) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            FluidStack resourceCopy = resource.copy();
            int totalUsed = 0;

            FluidStack liquid = tile.tank.getFluid();
            if (liquid != null && liquid.amount > 0 && !liquid.isFluidEqual(resourceCopy)) {
                return 0;
            }

            while (resourceCopy.amount > 0 && resourceCopy.amount + tile.tank.getFluidAmount() < tile.tank.getCapacity()) {
                int used = tile.tank.fill(resourceCopy, doFill);
                resourceCopy.amount -= used;
                if (used > 0) {
                    if (!worldObj.isRemote)
                        PacketHandler.INSTANCE.sendToAllAround(new MessageFluid(tile), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), getMaster().getX(), getMaster().getY(), getMaster().getZ(), 128d));
                }

                totalUsed += used;

            }
            return totalUsed;
        }

        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxEmpty, boolean doDrain) {
        FluidStack output = null;
        if (getMaster() != null && getMultiBlock().isActive()) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            output = tile.tank.drain(maxEmpty, doDrain);
            if (!worldObj.isRemote)
                PacketHandler.INSTANCE.sendToAllAround(new MessageFluid(tile), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), getMaster().getX(), getMaster().getY(), getMaster().getZ(), 128d));

            return output;
        }
        return output;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (canDrain(from, resource.getFluid())) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            if (!resource.isFluidEqual(tile.tank.getFluid())) {
                return null;
            }
            return drain(from, resource.amount, doDrain);
        }
        return resource;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing direction) {
        if (getMaster() != null && getMultiBlock().isActive()) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            FluidTank compositeTank = new FluidTank(tile.tank.getCapacity());

            int capacity = tile.tank.getCapacity();

            if (tile.tank.getFluid() != null) {
                compositeTank.setFluid(tile.tank.getFluid().copy());
            } else {
                return new FluidTankInfo[]{compositeTank.getInfo()};
            }

            FluidStack liquid = tile.tank.getFluid();
            if (liquid == null || liquid.amount == 0) {

            } else {
                compositeTank.getFluid().amount += liquid.amount;
            }

            capacity += tile.tank.getCapacity();

            compositeTank.setCapacity(capacity);
            return new FluidTankInfo[]{compositeTank.getInfo()};
        }
        return new FluidTankInfo[]{};
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        if (getWorld() != null && getMaster() != null && worldObj.getTileEntity(getMaster()) != null && getMultiBlock() != null && getMultiBlock().isActive()) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            Fluid tankFluid = getFluidType();
            return tankFluid == null || (tankFluid == fluid && tile.tank.getFluidAmount() < tile.tank.getCapacity());
        }
        return false;
    }

    public Fluid getFluidType() {
        if (getWorld() != null && getMaster() != null && worldObj.getTileEntity(getMaster()) != null && getMultiBlock() != null && getMultiBlock().isActive()) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            return tile.tank.getFluid() != null ? tile.tank.getFluid().getFluid() : null;
        }
        return null;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        if (getWorld() != null && getMaster() != null && worldObj.getTileEntity(getMaster()) != null && getMultiBlock() != null && getMultiBlock().isActive()) {
            Fluid tankFluid = getFluidType();
            return tankFluid != null && tankFluid == fluid;
        }
        return false;
    }

    // we send all our info to the client on load

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());

    }
}
