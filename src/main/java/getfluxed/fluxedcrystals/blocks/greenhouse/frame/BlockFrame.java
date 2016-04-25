package getfluxed.fluxedcrystals.blocks.greenhouse.frame;

import getfluxed.fluxedcrystals.blocks.base.BlockMultiblockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jared on 3/19/2016.
 */
public class BlockFrame extends BlockMultiblockComponent {
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        if (!blockState.equals(iblockstate)) {
            return true;
        }

        if (block instanceof BlockFrame) {
            return false;
        }

        return block instanceof BlockFrame ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

}
