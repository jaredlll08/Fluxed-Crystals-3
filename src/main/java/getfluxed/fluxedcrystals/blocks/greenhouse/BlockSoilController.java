package getfluxed.fluxedcrystals.blocks.greenhouse;

import getfluxed.fluxedcrystals.blocks.base.FCBlock;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
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
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntitySoilController) {
            long check = ((TileEntitySoilController) te).checkMultiblock();
            if (check > 0) {
                ((TileEntitySoilController) te).container.setCapacity(check);
                if (((TileEntitySoilController) te).getMultiBlock().isActive()) {
                    ((TileEntitySoilController) te).tank.setCapacity(((TileEntitySoilController) te).getMultiBlock().getAirBlocks().size() * 16000);
                }
                if (!te.getWorld().isRemote) {
                    te.markDirty();
                }
            }
        }
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntitySoilController) {
            long check = ((TileEntitySoilController) te).checkMultiblock();
            if (check > -1) {
                ((TileEntitySoilController) te).container.setCapacity(check);
                if (((TileEntitySoilController) te).getMultiBlock().isActive()) {
                    ((TileEntitySoilController) te).tank.setCapacity(((TileEntitySoilController) te).getMultiBlock().getAirBlocks().size() * 16000);
                }
                if (!te.getWorld().isRemote) {
                    ((TileEntitySoilController) te).markDirty();
                }
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySoilController();
    }


}
