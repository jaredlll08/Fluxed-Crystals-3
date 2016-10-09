package getfluxed.fluxedcrystals.blocks.glassjar;

import getfluxed.fluxedcrystals.blocks.FCBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;

import static getfluxed.fluxedcrystals.reference.Reference.MODID;

/**
 * Created by Jared.
 */
public class BlockBoilingWater extends BlockFluidClassic {


    public BlockBoilingWater() {
        super(FCBlocks.fluidBoilingWater, Material.LAVA);
        this.setQuantaPerBlock(8);
        setUnlocalizedName(MODID + ":boiling_water");
        FCBlocks.fluidBoilingWater.setBlock(this);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getBlockState().getBaseState().withProperty(LEVEL, meta);
    }
}
