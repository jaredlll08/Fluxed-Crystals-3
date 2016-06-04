package getfluxed.fluxedcrystals.api.registries;

import getfluxed.fluxedcrystals.api.recipes.machines.RecipeCrusher;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeFurnace;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeMachineBase;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeSawmill;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jared on 5/13/2016.
 */
public class RecipeRegistry {
    private static HashMap<String, RecipeMachineBase> crusherRecipes = new HashMap<String, RecipeMachineBase>();
    private static HashMap<String, RecipeMachineBase> furnaceRecipes = new HashMap<String, RecipeMachineBase>();
    private static HashMap<String, RecipeMachineBase> sawmillRecipes = new HashMap<String, RecipeMachineBase>();


    public static void registerCrusherRecipe(String seedID, RecipeCrusher recipe) {
        if (!crusherRecipes.containsKey(seedID)) {
            crusherRecipes.put(seedID, recipe);
        }
    }

    public static RecipeCrusher getCrusherRecipeByID(String seedID) {
        if (crusherRecipes.containsKey(seedID)) {
            return (RecipeCrusher) crusherRecipes.get(seedID);
        }
        return null;
    }

    public static HashMap<String, RecipeMachineBase> getAllCrusherRecipes() {
        return crusherRecipes;
    }

    public static boolean isCrusherInput(ItemStack stack) {
        for (Map.Entry<String, RecipeMachineBase> ent : getAllCrusherRecipes().entrySet()) {
            if (ent.getValue().getInput().isItemEqual(stack)) {
                return true;
            }
        }
        return false;
    }

    public static void registerFurnaceRecipe(String seedID, RecipeFurnace recipe) {
        if (!furnaceRecipes.containsKey(seedID)) {
            furnaceRecipes.put(seedID, recipe);
        }
    }

    public static RecipeFurnace getFurnaceRecipeByID(String seedID) {
        if (furnaceRecipes.containsKey(seedID)) {
            return (RecipeFurnace) furnaceRecipes.get(seedID);
        }
        return null;
    }

    public static HashMap<String, RecipeMachineBase> getAllFurnaceRecipes() {
        return furnaceRecipes;
    }

    public static boolean isFurnaceInput(ItemStack stack) {
        if (stack != null && stack.getItem() != null) {
            for (Map.Entry<String, RecipeMachineBase> ent : getAllFurnaceRecipes().entrySet()) {
                if (compareStacks(ent.getValue().getInput(), stack))
                    return true;
            }
        }

        return false;
    }


    public static void registerSawmillRecipe(String seedID, RecipeSawmill recipe) {
        if (!sawmillRecipes.containsKey(seedID)) {
            sawmillRecipes.put(seedID, recipe);
        }
    }

    public static RecipeSawmill getSawmillRecipeByID(String seedID) {
        if (sawmillRecipes.containsKey(seedID)) {
            return (RecipeSawmill) sawmillRecipes.get(seedID);
        }
        return null;
    }

    public static HashMap<String, RecipeMachineBase> getAllSawmillRecipes() {
        return sawmillRecipes;
    }

    public static boolean isSawmillInput(ItemStack stack) {
        for (Map.Entry<String, RecipeMachineBase> ent : getAllSawmillRecipes().entrySet()) {
            if (ent.getValue().getInput().isItemEqual(stack)) {
                return true;
            }
        }
        return false;
    }


    public static boolean compareStacks(ItemStack input, ItemStack output) {
        return output.getItem() == input.getItem() && (input.getMetadata() == 32767 || output.getMetadata() == input.getMetadata());
    }

}
