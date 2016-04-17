package getfluxed.fluxedcrystals.blocks.greenhouse.io;

import getfluxed.fluxedcrystals.blocks.base.FCBlock;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityPowerIO;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Jared on 4/17/2016.
 */
public class BlockPowerIO extends FCBlock {

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityPowerIO();
    }
}
