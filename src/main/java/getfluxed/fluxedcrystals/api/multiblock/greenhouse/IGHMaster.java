package getfluxed.fluxedcrystals.api.multiblock.greenhouse;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Jared on 7/26/2016.
 */
public interface IGHMaster<T> extends IGHComponent {
    void activate(World world, BlockPos pos, EnumFacing facing);
}
