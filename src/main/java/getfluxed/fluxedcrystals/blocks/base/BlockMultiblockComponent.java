package getfluxed.fluxedcrystals.blocks.base;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Jared on 4/11/2016.
 */
public abstract class BlockMultiblockComponent extends Block implements ITileEntityProvider {

    public BlockMultiblockComponent() {
        super(Material.IRON);
        setHardness(1.5f);
    }

//    @Override
//    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
//        TileEntity te = world.getTileEntity(pos);
//        if (te instanceof TileEntitySoilController) {
//            ((TileEntitySoilController) te).checkMultiblock();
//            int check = ((TileEntitySoilController) te).checkMultiblock();
//            if (check > 0) {
//                ((TileEntitySoilController) te).setMaxStorage(check);
//                if (((TileEntitySoilController) te).getMultiBlock().isActive()) {
//                    ((TileEntitySoilController) te).tank.setCapacity(((TileEntitySoilController) te).getMultiBlock().getAirBlocks().size() * 16000);
//                }
//                if (!te.getWorld().isRemote) {
//                    te.markDirty();
//                }
//            }
//        }
//    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent)worldIn.getTileEntity(pos);
        if(tile.getMasterTile() !=null){
            tile.getMasterTile().getMultiBlock().setActive(false);
            tile.markDirty();
        }
        super.breakBlock(worldIn, pos, state);
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMultiBlockComponent();
    }

}
