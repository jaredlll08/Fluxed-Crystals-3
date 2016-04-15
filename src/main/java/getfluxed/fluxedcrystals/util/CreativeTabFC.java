package getfluxed.fluxedcrystals.util;

import getfluxed.fluxedcrystals.blocks.FCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Jared on 4/15/2016.
 */
public class CreativeTabFC extends CreativeTabs {


    public CreativeTabFC() {
        super("fc.tab.name");
    }

    @Override
    public void displayAllRelevantItems(List<ItemStack> p_78018_1_) {
        super.displayAllRelevantItems(p_78018_1_);
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(FCBlocks.ghFrameGlass);
    }
}
