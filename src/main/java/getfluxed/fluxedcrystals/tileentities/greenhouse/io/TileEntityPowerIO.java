package getfluxed.fluxedcrystals.tileentities.greenhouse.io;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Jared on 4/17/2016.
 */
public class TileEntityPowerIO extends TileEntityMultiBlockComponent {

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (getMasterTile() != null && capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER) {
            return getMasterTile().hasCapability(capability, facing);
        }
        return super.hasCapability(capability, facing);
    }


    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (getMasterTile() != null && (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER))
            return (T) getMasterTile().getCapability(capability, facing);
        return super.getCapability(capability, facing);
    }
}
