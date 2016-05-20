package getfluxed.fluxedcrystals.api.recipes.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by Jared on 5/13/2016.
 */
public class RecipeCrusher {

    private ItemStack input;
    private ItemStack output;
    private int inputAmount;
    private int outputAmount;

    public RecipeCrusher(ItemStack input, ItemStack output, int inputAmount, int outputAmount) {
        this.input = input;
        this.output = output;
        this.inputAmount = inputAmount;
        this.outputAmount = outputAmount;
    }

    public int getOutputAmount() {
        return outputAmount;
    }

    public boolean matches(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int id : ids) {
            String name = OreDictionary.getOreName(id);
            if (matches(name)) {
                return true;
            }
        }
        return stack != null && OreDictionary.itemMatches(stack, input, false);
    }

    private boolean matches(String oreDict) {
        List<ItemStack> stacks = OreDictionary.getOres(oreDict);
        for (ItemStack stack : stacks) {
            if (OreDictionary.itemMatches(stack, input, false)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesExact(ItemStack stack) {
        return stack != null && input.isItemEqual(stack);
    }

    public ItemStack getInput() {
        // stack.stackSize = getInputamount();
        return input.copy();
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public int getInputamount() {
        return inputAmount;
    }
}
