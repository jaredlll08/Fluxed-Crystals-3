package getfluxed.fluxedcrystals.blocks.machines;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jared on 5/3/2016.
 */
public abstract class BlockMachine extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool isActive = PropertyBool.create("active");

    protected BlockMachine() {
        super(Material.iron);
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, isActive});
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to
     * allow for adjustments to the IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(isActive, false);
    }

    /**
     * Possibly modify the given BlockState before rendering it on an Entity
     * (Minecarts, Endermen, ...)
     */
    @SideOnly(Side.CLIENT)
    public IBlockState getStateForEntityRender(IBlockState state) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(isActive, false);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        boolean active = false;
        if ((meta & 15 >> 2) == 1) {
            active = true;
        }
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(isActive, active);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | (state.getValue(FACING)).getHorizontalIndex();
        if (state.getValue(isActive)) {
            i = i | 1 << 2;
        } else {
            i = i | 0 << 2;
        }
        return i;
    }

}
