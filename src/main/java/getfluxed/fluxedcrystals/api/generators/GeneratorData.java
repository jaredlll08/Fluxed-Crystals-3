package getfluxed.fluxedcrystals.api.generators;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;


public class GeneratorData {

	public static void init() {
		registerBasicCoalGeneratorItems();
		registerTrashGeneratorItems();
		registerLavaGeneratorFluid();
	}

	private static void registerLavaGeneratorFluid() {
		Registry.LavaGenerator.addLavaGeneratorFluid(new FluidStack(FluidRegistry.LAVA, 250));
	}

	private static void registerTrashGeneratorItems() {
	}

	private static void registerBasicCoalGeneratorItems() {
//		Registry.BasicCoalGenerator.basicCoalGenerator.add(new MutablePair<ItemStack, Integer>(new ItemStack(Items.coal), 600));
		Registry.BasicCoalGenerator.addBasicCoalGeneratorItem(new ItemStack(Items.coal), 60);
		Registry.BasicCoalGenerator.addBasicCoalGeneratorItem(new ItemStack(Items.coal, 1, 1), 60);
		for (ItemStack stack : OreDictionary.getOres("blockCoal")) {
			Registry.BasicCoalGenerator.addBasicCoalGeneratorItem(stack, 5400);
		}
	}
}
