package getfluxed.fluxedcrystals.items.crystal;

import getfluxed.fluxedcrystals.api.crystals.CrystalInfo;
import getfluxed.fluxedcrystals.api.crystals.ICrystalInfoProvider;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.api.registries.crystal.Crystal;
import getfluxed.fluxedcrystals.config.Config;
import getfluxed.fluxedcrystals.items.base.FCItem;
import getfluxed.fluxedcrystals.util.NBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Jared on 4/30/2016.
 */
public class ItemCrystalSolution extends FCItem implements ICrystalInfoProvider {


    public ItemCrystalSolution() {
        setHasSubtypes(true);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        Config.registerJsons();
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

    }


    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (Crystal c : CrystalRegistry.getCrystalMap().values()) {
            ItemStack s = new ItemStack(itemIn);
            NBTHelper.setString(s, "crystalName", c.getName());
            CrystalInfo info = new CrystalInfo(c.getName(), new AxisAlignedBB(2, 2, 2, 2, 2, 2), 100, 20, 20);
            NBTTagCompound tag = new NBTTagCompound();
            info.writeToNBT(tag);
            s.getTagCompound().setTag("crystalTag", tag);
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

    @Override
    public CrystalInfo getCrystalInfo(ItemStack stack) {
//        return new CrystalInfo("name", new AxisAlignedBB(2, 2, 2, 2, 2, 2), 100, 2, 2);
        return CrystalInfo.readFromNBT(stack.getSubCompound("crystalTag", true));
    }
}
