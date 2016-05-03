package getfluxed.fluxedcrystals.blocks.machines;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

/**
 * Created by Jared on 5/3/2016.
 */
public abstract class BlockMachine extends BlockContainer {
    protected BlockMachine() {
        super(Material.iron);
    }
}
