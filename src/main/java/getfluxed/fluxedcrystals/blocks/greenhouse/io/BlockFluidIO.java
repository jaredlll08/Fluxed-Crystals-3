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
import net.minecraftforge.fluids.FluidUtil;

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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityFluidIO tile = (TileEntityFluidIO) world.getTileEntity(pos);
        if (tile.getMaster() != null && !tile.getMaster().equals(new BlockPos(0, 0, 0)) && tile.getMultiBlock() != null && tile.getMultiBlock().isActive()) {
            TileEntitySoilController tank = (TileEntitySoilController) world.getTileEntity(tile.getMaster());
            ItemStack input = player.getHeldItem(hand);
            if (input != null) {
                if (FluidUtil.interactWithFluidHandler(input, tank.tank, player)) {
                    if (!world.isRemote)
                        tank.markDirty();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluidIO();
    }
}
