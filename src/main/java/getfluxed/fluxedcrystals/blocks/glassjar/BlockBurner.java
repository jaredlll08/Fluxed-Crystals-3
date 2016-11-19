package getfluxed.fluxedcrystals.blocks.glassjar;

import getfluxed.fluxedcrystals.blocks.base.FCBlock;
import getfluxed.fluxedcrystals.tileentities.glassjar.TileEntityBurner;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Jared.
 */
public class BlockBurner extends FCBlock implements ITileEntityProvider {


    public BlockPos getController(World world, BlockPos pos, List<BlockPos> burners) {
        BlockPos controller = null;
        burners.add(pos);
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (controller == null && !burners.contains(pos.offset(facing))) {
                if (world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockController) {
                    controller = pos.offset(facing);
                } else {
                    if (world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockBurner) {
                        controller = (((BlockBurner) world.getBlockState(pos.offset(facing)).getBlock()).getController(world, pos.offset(facing), burners));
                    }
                }
            }
        }
        return controller;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBurner();
    }
}
