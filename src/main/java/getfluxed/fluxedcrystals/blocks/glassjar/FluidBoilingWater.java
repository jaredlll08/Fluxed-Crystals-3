package getfluxed.fluxedcrystals.blocks.glassjar;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import static getfluxed.fluxedcrystals.reference.Reference.MODID;


/**
 * Created by Jared.
 */
public class FluidBoilingWater extends Fluid {

    public FluidBoilingWater() {
        super("boiling_water", new ResourceLocation(MODID + ":blocks/boiling_water"), new ResourceLocation(MODID + ":blocks/boiling_water"));
        setUnlocalizedName("boiling_water");
    }
}
