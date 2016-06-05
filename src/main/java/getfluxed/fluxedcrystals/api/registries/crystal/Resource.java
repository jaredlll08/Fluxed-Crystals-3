package getfluxed.fluxedcrystals.api.registries.crystal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Jared on 4/30/2016.
 */
public class Resource<T> {

    private T stack;

    public Resource(T stack) {
        if (stack instanceof ItemStack || stack instanceof FluidStack) {
            this.stack = stack;
        } else {
            //NO-OP
        }
    }

    public boolean isItemStack() {
        return stack instanceof ItemStack;
    }

    public boolean isFluidStack() {
        return stack instanceof FluidStack;
    }
    public ItemStack getItemStack(){
        return ((ItemStack)stack).copy();
    }
    public FluidStack getFluidStack(){
        return (FluidStack)stack;
    }

    public <T> T getStack() {
        return (T) stack;
    }

}
