package getfluxed.fluxedcrystals.blocks.greenhouse.io;

import getfluxed.fluxedcrystals.blocks.base.BlockMultiblockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityFluidIO;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Created by Jared on 4/15/2016.
 */
public class BlockFluidIO extends BlockMultiblockComponent {

    public static ItemStack consumeItem(ItemStack stack) {
        if (stack.stackSize == 1) {
            if (stack.getItem().hasContainerItem(stack)) {
                return stack.getItem().getContainerItem(stack);
            } else {
                return null;
            }
        } else {
            stack.splitStack(1);

            return stack;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityFluidIO tile = (TileEntityFluidIO) worldIn.getTileEntity(pos);
        if (tile.getMaster() != null && !tile.getMaster().equals(new BlockPos(0, 0, 0)) && tile.getMultiBlock().isActive()) {
            TileEntitySoilController tank = (TileEntitySoilController) worldIn.getTileEntity(tile.getMaster());

            if (heldItem != null && tile instanceof IFluidHandler) {
                if (FluidUtil.interactWithTank(heldItem, playerIn, tile, side.getOpposite()))
                    return true;
            }

            if (heldItem == null)
                return false;
            return FluidContainerRegistry.isFilledContainer(heldItem) || heldItem.getItem() instanceof IFluidContainerItem;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluidIO();
    }
}
