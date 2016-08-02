package getfluxed.fluxedcrystals.tileentities.greenhouse.io;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

/**
 * Created by Jared on 4/15/2016.
 */
public class TileEntityFluidIO extends TileEntityMultiBlockComponent implements IFluidHandler {

//    @Nullable
//    @Override
//    public SPacketUpdateTileEntity getUpdatePacket() {
//        NBTTagCompound tag = new NBTTagCompound();
//        writeToNBT(tag);
//        return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
//    }
//
//    @Override
//    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
//        super.onDataPacket(net, pkt);
//        readFromNBT(pkt.getNbtCompound());
//    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return getMasterTile().tank.getTankProperties();
    }


    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return getMasterTile().tank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return getMasterTile().tank.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return getMasterTile().tank.drain(maxDrain, doDrain);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) getMasterTile().tank;
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
}
