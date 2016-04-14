package getfluxed.fluxedcrystals.blocks.greenhouse.powerframes;

import getfluxed.fluxedcrystals.blocks.base.BlockMultiblockComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jared on 3/19/2016.
 */
public class BlockFrame extends BlockMultiblockComponent{
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.TRANSLUCENT;
    }

    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

}
