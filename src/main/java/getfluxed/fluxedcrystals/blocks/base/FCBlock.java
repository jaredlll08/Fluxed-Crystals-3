package getfluxed.fluxedcrystals.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by Jared on 3/17/2016.
 */
public class FCBlock extends Block{


    public FCBlock() {
        super(Material.IRON);
        setHardness(1.5f);
    }

    public FCBlock(Material material) {
        super(material);
        setHardness(1.5f);
    }
}
