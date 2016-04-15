package getfluxed.fluxedcrystals.blocks.greenhouse.io;

import getfluxed.fluxedcrystals.blocks.base.BlockMultiblockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityFluidIO;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Jared on 4/15/2016.
 */
public class BlockFluidIO extends BlockMultiblockComponent {


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluidIO();
    }
}
