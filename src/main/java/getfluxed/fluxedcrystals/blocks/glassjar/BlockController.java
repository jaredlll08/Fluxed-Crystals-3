package getfluxed.fluxedcrystals.blocks.glassjar;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.blocks.base.FCBlock;
import getfluxed.fluxedcrystals.tileentities.glassjar.TileEntityController;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Jared.
 */
public class BlockController extends FCBlock implements ITileEntityProvider {


    public void setupGlassJar(World world, BlockPos pos) {
        FluxedCrystals.logger.info("Setting up glass jar at! " + pos);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityController();
    }
}
