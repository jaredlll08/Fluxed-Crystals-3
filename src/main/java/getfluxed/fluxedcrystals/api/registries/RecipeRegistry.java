package getfluxed.fluxedcrystals.api.registries;

import getfluxed.fluxedcrystals.api.recipes.machines.RecipeCrusher;

import java.util.HashMap;

/**
 * Created by Jared on 5/13/2016.
 */
public class RecipeRegistry {
    private static HashMap<String, RecipeCrusher> crusherRecipes = new HashMap<String, RecipeCrusher>();


    public static void registerCrusherrRecipe(String seedID, RecipeCrusher recipe) {

        if (!crusherRecipes.containsKey(seedID)) {

            crusherRecipes.put(seedID, recipe);

        }

    }

    public static RecipeCrusher getCrusherRecipeByID(String seedID) {

        if (crusherRecipes.containsKey(seedID)) {

            return crusherRecipes.get(seedID);

        }

        return null;

    }

    public static HashMap<String, RecipeCrusher> getAllCrusherRecipes() {

        return crusherRecipes;

    }
}
