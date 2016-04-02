package getfluxed.fluxedcrystals.blocks.greenhouse.powerframes;

import getfluxed.fluxedcrystals.blocks.base.FCBlock;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityFrame;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Jared on 3/19/2016.
 */
public class BlockFrame extends FCBlock implements ITileEntityProvider{

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFrame();
    }

}
