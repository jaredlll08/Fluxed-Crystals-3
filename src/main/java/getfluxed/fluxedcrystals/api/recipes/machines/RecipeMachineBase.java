package getfluxed.fluxedcrystals.api.recipes.machines;

import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by Jared on 5/31/2016.
 */
public class RecipeMachineBase {

    private ItemStack input;
    private ItemStack output;
    private int inputAmount;
    private int outputAmount;

    public RecipeMachineBase(ItemStack input, ItemStack output, int inputAmount, int outputAmount) {
        this.input = input;
        this.input.setItemDamage(input.getItemDamage());
        this.output = output;
        this.output.setItemDamage(output.getItemDamage());
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
        System.out.println(">>>");
        System.out.println(RecipeRegistry.compareStacks(input, stack));
        System.out.println(NBTHelper.isInputEqual(input, stack));
        System.out.println("<<<");
        return NBTHelper.isInputEqual(input, stack);
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public int getInputamount() {
        return inputAmount;
    }

    @Override
    public String toString() {
        return "RecipeMachineBase{" +
                "input=" + input +
                ", output=" + output +
                ", inputAmount=" + inputAmount +
                ", outputAmount=" + outputAmount +
                '}';
    }
}
