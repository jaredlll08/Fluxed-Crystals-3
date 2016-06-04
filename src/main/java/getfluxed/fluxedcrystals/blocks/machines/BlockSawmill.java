package getfluxed.fluxedcrystals.blocks.machines;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineSawmill;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Jared on 5/31/2016.
 */
public class BlockSawmill extends BlockMachine {


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMachineSawmill();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(FluxedCrystals.instance, 4, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

}
