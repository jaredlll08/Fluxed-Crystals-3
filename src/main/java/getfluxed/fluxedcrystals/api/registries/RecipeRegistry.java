package getfluxed.fluxedcrystals.api.registries;

import getfluxed.fluxedcrystals.api.recipes.machines.RecipeCrusher;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeFurnace;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeSawmill;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jared on 5/13/2016.
 */
public class RecipeRegistry {
    private static HashMap<String, RecipeCrusher> crusherRecipes = new HashMap<String, RecipeCrusher>();
    private static HashMap<String, RecipeFurnace> furnaceRecipes = new HashMap<String, RecipeFurnace>();
    private static HashMap<String, RecipeSawmill> sawmillRecipes = new HashMap<String, RecipeSawmill>();


    public static void registerCrusherRecipe(String seedID, RecipeCrusher recipe) {
        if (!crusherRecipes.containsKey(seedID)) {
            crusherRecipes.put(seedID, recipe);
        }
    }
    public static void registerCrusherRecipe(RecipeCrusher recipe) {
        String id = String.format("%s;%s:%s;%s", recipe.getInput().getUnlocalizedName(), recipe.getInput().getItemDamage(), recipe.getOutput().getUnlocalizedName(), recipe.getOutput().getItemDamage());
        if (!crusherRecipes.containsKey(id)) {
            crusherRecipes.put(id, recipe);
        }
    }
    public static RecipeCrusher getCrusherRecipeByID(String seedID) {
        if (crusherRecipes.containsKey(seedID)) {
            return (RecipeCrusher) crusherRecipes.get(seedID);
        }
        return null;
    }

    public static HashMap<String, RecipeCrusher> getAllCrusherRecipes() {
        return crusherRecipes;
    }

    public static boolean isCrusherInput(ItemStack stack) {
        for (Map.Entry<String, RecipeCrusher> ent : getAllCrusherRecipes().entrySet()) {
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
    public static void registerFurnaceRecipe(RecipeFurnace recipe) {
        String id = String.format("%s;%s:%s;%s", recipe.getInput().getUnlocalizedName(), recipe.getInput().getItemDamage(), recipe.getOutput().getUnlocalizedName(), recipe.getOutput().getItemDamage());
        if (!furnaceRecipes.containsKey(id)) {
            furnaceRecipes.put(id, recipe);
        }
    }
    public static RecipeFurnace getFurnaceRecipeByID(String seedID) {
        if (furnaceRecipes.containsKey(seedID)) {
            return (RecipeFurnace) furnaceRecipes.get(seedID);
        }
        return null;
    }

    public static HashMap<String, RecipeFurnace> getAllFurnaceRecipes() {
        return furnaceRecipes;
    }

    public static boolean isFurnaceInput(ItemStack stack) {
        if (stack != null && stack.getItem() != null) {
            for (Map.Entry<String, RecipeFurnace> ent : getAllFurnaceRecipes().entrySet()) {
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
    public static void registerSawmillRecipe(RecipeSawmill recipe) {
        String id = String.format("%s;%s:%s;%s", recipe.getInput().getUnlocalizedName(), recipe.getInput().getItemDamage(), recipe.getOutput().getUnlocalizedName(), recipe.getOutput().getItemDamage());
        if (!sawmillRecipes.containsKey(id)) {
            sawmillRecipes.put(id, recipe);
        }
    }
    public static RecipeSawmill getSawmillRecipeByID(String seedID) {
        if (sawmillRecipes.containsKey(seedID)) {
            return (RecipeSawmill) sawmillRecipes.get(seedID);
        }
        return null;
    }

    public static HashMap<String, RecipeSawmill> getAllSawmillRecipes() {
        return sawmillRecipes;
    }

    public static boolean isSawmillInput(ItemStack stack) {
        for (Map.Entry<String, RecipeSawmill> ent : getAllSawmillRecipes().entrySet()) {
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
