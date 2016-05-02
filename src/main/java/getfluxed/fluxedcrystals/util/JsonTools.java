package getfluxed.fluxedcrystals.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import getfluxed.fluxedcrystals.api.registries.crystal.Resource;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class JsonTools {

    private static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    public static Object parseStringIntoRecipeItem(String string) {
        return parseStringIntoRecipeItem(string, false);
    }

    public static Object parseStringIntoRecipeItem(String string, boolean forceItemStack) {
        if ("null".equals(string)) {
            return null;
        } else if (OreDictionary.getOres(string).isEmpty()) {
            ItemStack stack;
            String[] info = string.split(";");
            Object temp;
            int damage = Short.MAX_VALUE;
            temp = Item.itemRegistry.getObject(new ResourceLocation(info[0]));
            if (info.length > 1) {
                damage = Integer.parseInt(info[1]);
            }

            if (temp instanceof Item) {
                stack = new ItemStack((Item) temp, 1, damage);
            } else if (temp instanceof Block) {
                stack = new ItemStack((Block) temp, 1, damage);
            } else {
                if (!(temp instanceof ItemStack)) {
                    throw new IllegalArgumentException(string + " is not a vaild string. Strings should be either an oredict name, or in the format objectname;damage (damage is optional)");
                }

                stack = ((ItemStack) temp).copy();
                stack.setItemDamage(damage);
            }

            return stack;
        } else {
            return forceItemStack ? OreDictionary.getOres(string).get(0).copy() : string;
        }
    }

    public static ItemStack parseStringIntoItemStack(String string) {
        int size = 1;
        int idx = string.indexOf(35);
        if (idx != -1) {
            String stack = string.substring(idx + 1);

            try {
                size = Integer.parseInt(stack);
            } catch (NumberFormatException var5) {
                throw new IllegalArgumentException(stack + " is not a valid stack size");
            }

            string = string.substring(0, idx);
        }

        ItemStack stack1 = (ItemStack) parseStringIntoRecipeItem(string, true);
        if (stack1 != null) {
            stack1.stackSize = MathHelper.clamp_int(size, 1, stack1.getMaxStackSize());
        }
        return stack1;
    }

    public static String getStringForItemStack(ItemStack stack, boolean damage, boolean size) {
        if (stack == null) {
            return null;
        } else {
            String base = Item.itemRegistry.getNameForObject(stack.getItem()).toString();
            if (damage) {
                base = base + ";" + stack.getItemDamage();
            }

            if (size) {
                base = base + "#" + stack.stackSize;
            }

            return base;
        }
    }
    public static Resource getResource(String str) {
        Resource res = null;

        if (!str.contains(":")) {
            System.out.println("is Fluid");
            if (FluidRegistry.getFluid(str) != null) {
                System.out.println("found fluid");
                res = new Resource(new FluidStack(FluidRegistry.getFluid(str), 1));
            }
        } else {
            res = new Resource(parseStringIntoItemStack(str));
        }
        return res;
    }

}
