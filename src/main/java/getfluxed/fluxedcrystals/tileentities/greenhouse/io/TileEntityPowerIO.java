package getfluxed.fluxedcrystals.tileentities.greenhouse.io;

import cofh.api.energy.IEnergyHandler;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import net.minecraft.util.EnumFacing;

import java.util.EnumSet;

/**
 * Created by Jared on 4/17/2016.
 */
public class TileEntityPowerIO extends TileEntityMultiBlockComponent implements IEnergyHandler {


    public EnumSet<EnumFacing> getValidOutputs() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    public EnumSet<EnumFacing> getValidInputs() {
        return EnumSet.allOf(EnumFacing.class);
    }

    @Override
    public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {
        if (getWorld() != null && getMaster() != null && worldObj.getTileEntity(getMaster()) != null && getMultiBlock() != null && getMultiBlock().isActive()) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            if (getValidInputs().contains(facing)) {
                int ret = tile.getEnergyStorage().receiveEnergy(maxReceive, true);
                if (!simulate) {
                    tile.getEnergyStorage().receiveEnergy(ret, false);
                }
                return ret;
            }
        }
        return 0;
    }

    @Override
    public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate) {
        if (getWorld() != null && getMaster() != null && worldObj.getTileEntity(getMaster()) != null && getMultiBlock() != null && getMultiBlock().isActive()) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            if (getValidOutputs().contains(facing)) {
                int ret = tile.getEnergyStorage().extractEnergy(maxExtract, true);
                if (!simulate) {
                    tile.getEnergyStorage().extractEnergy(ret, false);
                }
                return ret;
            }
        }
        return 0;
    }

    @Override
    public int getEnergyStored(EnumFacing facing) {
        if (getWorld() != null && getMaster() != null && worldObj.getTileEntity(getMaster()) != null && getMultiBlock() != null && getMultiBlock().isActive()) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            return tile.getEnergyStorage().getEnergyStored();
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing facing) {
        if (getWorld() != null && getMaster() != null && worldObj.getTileEntity(getMaster()) != null && getMultiBlock() != null && getMultiBlock().isActive()) {
            TileEntitySoilController tile = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            return tile.getEnergyStorage().getMaxEnergyStored();
        }
        return 0;
    }

    @Override
    public final boolean canConnectEnergy(EnumFacing from) {
        return getValidInputs().contains(from) || getValidOutputs().contains(from);
    }
}
