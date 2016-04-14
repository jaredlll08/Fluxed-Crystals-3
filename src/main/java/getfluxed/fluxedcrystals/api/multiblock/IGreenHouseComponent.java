package getfluxed.fluxedcrystals.api.multiblock;

import net.minecraft.util.math.BlockPos;

/**
 * Created by Jared on 3/19/2016.
 */
public interface IGreenHouseComponent {

    boolean isMaster();
    BlockPos getMaster();
    MultiBlock getMultiBlock();
    void setMaster(BlockPos pos);
    void breakBlock();



}
