package getfluxed.fluxedcrystals.api.crystals;

import net.minecraft.item.ItemStack;

/**
 * Created by Jared on 6/4/2016.
 */
public interface ICrystalInfoProvider {

    CrystalInfo getCrystalInfo(ItemStack stack);
}
