package getfluxed.fluxedcrystals.blocks.base;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Jared on 4/11/2016.
 */
public class BlockMultiblockComponent extends FCBlock implements ITileEntityProvider{


    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock){
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldIn.getTileEntity(pos);
        if(tile.getMaster() != null){

            //TODO remove this
//            worldIn.getBlockState(tile.getMaster()).getBlock().onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state){
//TODO remove this
        TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldIn.getTileEntity(pos);
        if(tile.getMaster() != null){
            System.out.println("before: "+tile.getMultiBlock().isActive());
            tile.getMultiBlock().setActive(false);
            System.out.println("after: "+tile.getMultiBlock().isActive());
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta){
        return new TileEntityMultiBlockComponent();
    }
}
