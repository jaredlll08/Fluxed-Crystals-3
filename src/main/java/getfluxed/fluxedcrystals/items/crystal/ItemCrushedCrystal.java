package getfluxed.fluxedcrystals.items.crystal;

import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Map;

/**
 * Created by Jared on 6/25/2016.
 */
public class ItemCrushedCrystal extends Item {

    public ItemCrushedCrystal() {
        setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (Map.Entry<String, Crystal> c : CrystalRegistry.getCrystalMap().entrySet()) {
            for (int i = 0; i < 4; i++) {
                ItemStack stack = new ItemStack(itemIn, 1, i);
                if(stack.getTagCompound() ==null){
                    stack.setTagCompound(new NBTTagCompound());
                }
                stack.getTagCompound().setString("crystalName", c.getKey());
                subItems.add(stack);
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "crushed_crystal_" + stack.getItemDamage();
    }

}
