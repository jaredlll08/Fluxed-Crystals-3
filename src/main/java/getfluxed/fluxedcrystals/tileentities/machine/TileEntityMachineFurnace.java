package getfluxed.fluxedcrystals.tileentities.machine;

import getfluxed.fluxedcrystals.api.recipes.machines.RecipeFurnace;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

/**
 * Created by Jared on 5/25/2016.
 */
public class TileEntityMachineFurnace extends TileEntityMachineBase<RecipeFurnace>  {
    public TileEntityMachineFurnace() {
        super(32000, 2);
    }

    @Override
    public int getEnergyUsed() {
        return 250;
    }

    @Override
    public RecipeFurnace getRecipe(String index) {
        return RecipeRegistry.getFurnaceRecipeByID(index);
    }

    @Override
    public HashMap<String, RecipeFurnace> getRecipes() {
        return RecipeRegistry.getAllFurnaceRecipes();
    }

    @Override
    public boolean isValidInput(ItemStack stack) {
        return RecipeRegistry.isFurnaceInput(stack);
    }

}
