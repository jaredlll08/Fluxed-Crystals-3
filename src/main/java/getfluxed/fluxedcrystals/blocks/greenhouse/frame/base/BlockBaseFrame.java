package getfluxed.fluxedcrystals.blocks.greenhouse.frame.base;

import getfluxed.fluxedcrystals.blocks.base.BlockMultiblockComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Jared on 3/26/2016.
 */
public class BlockBaseFrame extends BlockMultiblockComponent{

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add("Can be used as a frame");
    }
}
