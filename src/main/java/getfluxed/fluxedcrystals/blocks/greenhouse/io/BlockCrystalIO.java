package getfluxed.fluxedcrystals.blocks.greenhouse.io;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.blocks.base.BlockMultiblockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityCrystalIO;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Jared on 6/4/2016.
 */
public class BlockCrystalIO extends BlockMultiblockComponent {
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(FluxedCrystals.instance, 5, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCrystalIO();
    }
}
