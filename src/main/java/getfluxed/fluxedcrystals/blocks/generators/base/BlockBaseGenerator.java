package getfluxed.fluxedcrystals.blocks.generators.base;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

abstract class BlockBaseGenerator extends BlockContainer {

    protected BlockBaseGenerator() {
        super(Material.IRON);
    }

}
