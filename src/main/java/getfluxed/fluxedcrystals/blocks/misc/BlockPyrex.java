package getfluxed.fluxedcrystals.blocks.misc;

import getfluxed.fluxedcrystals.blocks.FCBlocks;
import getfluxed.fluxedcrystals.blocks.base.FCBlock;
import getfluxed.fluxedcrystals.blocks.glassjar.BlockBurner;
import getfluxed.fluxedcrystals.blocks.glassjar.BlockController;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jared.
 */
public class BlockPyrex extends FCBlock {

    List<Block> allowed = new ArrayList<>();

    public BlockPyrex() {
        super(Material.GLASS);

    }

    public void fillAllowed() {
        allowed.add(Blocks.WATER);
        allowed.add(Blocks.FLOWING_WATER);
        allowed.add(FCBlocks.blockBoilingWater);
        allowed.add(Blocks.AIR);
        allowed.add(FCBlocks.burner);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (allowed.isEmpty()) {
            fillAllowed();
        }
        BlockPos controller = pos;
        while (worldIn.getBlockState(controller.down()).getBlock() instanceof BlockPyrex && allowed.contains(worldIn.getBlockState(controller.offset(side.getOpposite()).down()).getBlock())) {
            controller = controller.down();

            if (worldIn.getBlockState(controller.offset(side.getOpposite())).getBlock() instanceof BlockBurner) {
                controller = ((BlockBurner) worldIn.getBlockState(controller.offset(side.getOpposite())).getBlock()).getController(worldIn, controller.offset(side.getOpposite()), new ArrayList<BlockPos>());
                break;
            }
        }
        if (controller == pos) {
            return false;
        }
        if (!(worldIn.getBlockState(controller).getBlock() instanceof BlockController)) {
            return false;
        }
        ((BlockController) worldIn.getBlockState(controller).getBlock()).setupGlassJar(worldIn, controller);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
}
