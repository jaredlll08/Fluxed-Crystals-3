package getfluxed.fluxedcrystals.blocks.greenhouse.monitor;

import getfluxed.fluxedcrystals.blocks.greenhouse.frame.BlockFrame;
import getfluxed.fluxedcrystals.tileentities.greenhouse.monitor.TileEntityPowerMonitor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Jared on 4/25/2016.
 */
public class BlockPowerMonitor extends BlockFrame {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);


    public BlockPowerMonitor() {

    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow
     * post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        IBlockState st = null;
        if (placer.getHorizontalFacing() == EnumFacing.EAST) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.EAST.getOpposite());
        } else if (placer.getHorizontalFacing() == EnumFacing.NORTH) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.NORTH.getOpposite());
        } else if (placer.getHorizontalFacing() == EnumFacing.SOUTH) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH.getOpposite());
        } else if (placer.getHorizontalFacing() == EnumFacing.WEST) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.WEST.getOpposite());
        }
        worldIn.setBlockState(pos, st);
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to
     * allow for adjustments to the IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState st = null;
        if (placer.getHorizontalFacing() == EnumFacing.EAST) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.EAST.getOpposite());
        } else if (placer.getHorizontalFacing() == EnumFacing.NORTH) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.NORTH.getOpposite());
        } else if (placer.getHorizontalFacing() == EnumFacing.SOUTH) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH.getOpposite());
        } else if (placer.getHorizontalFacing() == EnumFacing.WEST) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.WEST.getOpposite());
        }

        return st;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPowerMonitor();
    }

}
