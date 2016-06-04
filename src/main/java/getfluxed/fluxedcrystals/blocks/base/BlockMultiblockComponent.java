package getfluxed.fluxedcrystals.blocks.base;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Jared on 4/11/2016.
 */
public class BlockMultiblockComponent extends Block implements ITileEntityProvider {

    public BlockMultiblockComponent() {
        super(Material.IRON);
        setHardness(1.5f);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) world.getTileEntity(pos);
        if (tile.getMaster() != null) {

            //TODO remove this
//            worldIn.getBlockState(tile.getMaster()).getBlock().onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
//TODO remove this
        TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldIn.getTileEntity(pos);
        if (tile.getMaster() != null && tile.getMultiBlock() != null) {
            tile.getMultiBlock().setActive(false);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMultiBlockComponent();
    }
}
