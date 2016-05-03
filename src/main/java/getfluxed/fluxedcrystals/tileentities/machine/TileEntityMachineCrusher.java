package getfluxed.fluxedcrystals.tileentities.machine;

import getfluxed.fluxedcrystals.tileentities.base.TileEnergyBase;
import net.minecraft.util.EnumFacing;

import java.util.EnumSet;

/**
 * Created by Jared on 5/3/2016.
 */
public class TileEntityMachineCrusher extends TileEnergyBase {
    public TileEntityMachineCrusher() {
        super(32000);
    }

    @Override
    public EnumSet<EnumFacing> getValidOutputs() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public EnumSet<EnumFacing> getValidInputs() {
        return EnumSet.allOf(EnumFacing.class);
    }
}
