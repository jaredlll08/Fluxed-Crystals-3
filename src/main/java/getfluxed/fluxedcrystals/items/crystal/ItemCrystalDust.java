package getfluxed.fluxedcrystals.items.crystal;

import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.items.base.FCItem;
import getfluxed.fluxedcrystals.util.NBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

/**
 * Created by Jared on 4/30/2016.
 */
public class ItemCrystalDust extends FCItem {


    public ItemCrystalDust() {
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (Crystal c : CrystalRegistry.getCrystalMap().values()) {
            ItemStack s = new ItemStack(itemIn);
            NBTHelper.setString(s, "crystalName", c.getName());
            subItems.add(s);
        }

    }


    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        Crystal c = CrystalRegistry.getCrystal(NBTHelper.getString(stack, "crystalName"));
        if (c != null) {
            tooltip.add(c.getName());
        } else {
            tooltip.add(TextFormatting.RED + "INVALID");
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Crystal c = CrystalRegistry.getCrystal(NBTHelper.getString(stack, "crystalName"));
        if (c == null) {
            return TextFormatting.RED + "INVALID";
        }
        return String.format(I18n.translateToLocal(getUnlocalizedName() + ".name"), c.getName());
    }
}
