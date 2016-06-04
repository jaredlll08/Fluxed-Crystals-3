package getfluxed.fluxedcrystals.data;

import getfluxed.fluxedcrystals.api.recipes.machines.RecipeCrusher;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeFurnace;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeSawmill;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.api.registries.crystal.Crystal;
import getfluxed.fluxedcrystals.items.FCItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

/**
 * Created by Jared on 5/25/2016.
 */
public class RecipeData {

    public static void init() {
        registerCrusherRecipes();
        registerFurnaceRecipes();
        registerSawmillRecipes();

    }

    private static void registerCrusherRecipes() {
        for (Map.Entry<String, Crystal> ent : CrystalRegistry.getCrystalMap().entrySet()) {
            if (ent.getValue().getResourceIn().isItemStack() && ent.getValue().getResourceOut().isItemStack()) {
                ItemStack output = new ItemStack(FCItems.crystalDust);
                if (output.getTagCompound() == null) {
                    output.setTagCompound(new NBTTagCompound());
                }
                output.getTagCompound().setString("crystalName", ent.getKey());
                RecipeRegistry.registerCrusherRecipe(ent.getKey(), new RecipeCrusher(ent.getValue().getResourceIn().getItemStack(), output, 1, 1));
            }
        }
    }

    private static void registerFurnaceRecipes() {
        for (Map.Entry<ItemStack, ItemStack> ent : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            RecipeRegistry.registerFurnaceRecipe(ent.getKey().getUnlocalizedName() + ":" + ent.getValue().getUnlocalizedName(), new RecipeFurnace(ent.getKey().copy(), ent.getValue().copy(), ent.getKey().stackSize, ent.getValue().stackSize));
        }
        for (Map.Entry<String, Crystal> ent : CrystalRegistry.getCrystalMap().entrySet()) {
            if (ent.getValue().getResourceIn().isItemStack() && ent.getValue().getResourceOut().isItemStack()) {
                ItemStack input = new ItemStack(FCItems.crystalDust);
                if (input.getTagCompound() == null) {
                    input.setTagCompound(new NBTTagCompound());
                }
                input.getTagCompound().setString("crystalName", ent.getKey());
                RecipeRegistry.registerFurnaceRecipe(input.getUnlocalizedName() + ":" + ent.getValue().getResourceOut().getItemStack().getUnlocalizedName(), new RecipeFurnace(input, ent.getValue().getResourceOut().getItemStack(), 1, 1));
            }
        }
    }

    private static void registerSawmillRecipes() {
        RecipeRegistry.registerSawmillRecipe(Blocks.LOG.getUnlocalizedName() + ":" + Blocks.PLANKS.getUnlocalizedName(), new RecipeSawmill(new ItemStack(Blocks.LOG), new ItemStack(Blocks.PLANKS), 1, 6));
    }
}
