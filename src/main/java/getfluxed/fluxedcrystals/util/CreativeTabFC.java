package getfluxed.fluxedcrystals.util;

import getfluxed.fluxedcrystals.blocks.FCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collections;
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
        for (Item item : Item.REGISTRY)
        {
            if (item == null)
            {
                continue;
            }
            for (CreativeTabs tab : item.getCreativeTabs())
            {
                if (tab == this)
                {
                    item.getSubItems(item, this, p_78018_1_);
                }
            }
        }

        if (this.getRelevantEnchantmentTypes() != null)
        {
            this.addEnchantmentBooksToList(p_78018_1_, this.getRelevantEnchantmentTypes());
        }
        Collections.sort(p_78018_1_, (o1, o2) -> o1.getUnlocalizedName().compareTo(o2.getUnlocalizedName()));
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(FCBlocks.ghFrameGlass);
    }
}
