package getfluxed.fluxedcrystals.blocks.greenhouse;

import getfluxed.fluxedcrystals.blocks.base.FCBlock;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Jared on 3/19/2016.
 */
public class BlockSoilController extends FCBlock implements ITileEntityProvider {

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add("Not safe to build with!");
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntitySoilController) {
            ((TileEntitySoilController) te).checkMultiblock();
        }
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySoilController();
    }


}
