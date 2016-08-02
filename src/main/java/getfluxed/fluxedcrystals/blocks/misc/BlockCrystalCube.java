package getfluxed.fluxedcrystals.blocks.misc;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

/**
 * Created by Jared on 7/15/2016.
 */
public class BlockCrystalCube extends Block {


    public BlockCrystalCube() {
        super(Material.GLASS);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
