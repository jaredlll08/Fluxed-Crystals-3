package getfluxed.fluxedcrystals.tileentities.machine;

import getfluxed.fluxedcrystals.api.recipes.machines.RecipeMachineBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

/**
 * Created by Jared on 5/31/2016.
 */
public class TileEntityMachineSawmill extends TileEntityMachineBase {
    public TileEntityMachineSawmill() {
        super(32000, 2);
    }

    @Override
    public int getEnergyUsed() {
        return 250;
    }

    @Override
    public RecipeMachineBase getRecipe(String index) {
        return RecipeRegistry.getSawmillRecipeByID(index);
    }

    @Override
    public HashMap<String, RecipeMachineBase> getRecipes() {
        return RecipeRegistry.getAllSawmillRecipes();
    }

    @Override
    public boolean isValidInput(ItemStack stack) {
        return RecipeRegistry.isSawmillInput(stack);
    }

    @Override
    public String getName() {
        return "Sawmill";
    }
}
