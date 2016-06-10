package getfluxed.fluxedcrystals.tileentities.machine;

import getfluxed.fluxedcrystals.api.client.gui.IOpenableGUI;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeMachineBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.client.gui.coalGenerator.ContainerCoalGenerator;
import getfluxed.fluxedcrystals.client.gui.coalGenerator.GUICoalGenerator;
import getfluxed.fluxedcrystals.client.gui.crusher.ContainerCrusher;
import getfluxed.fluxedcrystals.client.gui.crusher.GUICrusher;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityCoalGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by Jared on 5/3/2016.
 */
public class TileEntityMachineCrusher extends TileEntityMachineBase implements IOpenableGUI {


    public TileEntityMachineCrusher() {
        super(32000, 2);
    }

    @Override
    public int getEnergyUsed() {
        return 250;
    }

    @Override
    public RecipeMachineBase getRecipe(String index) {
        return RecipeRegistry.getCrusherRecipeByID(index);
    }

    @Override
    public HashMap<String, RecipeMachineBase> getRecipes() {
        return RecipeRegistry.getAllCrusherRecipes();
    }

    @Override
    public boolean isValidInput(ItemStack stack) {
        return RecipeRegistry.isCrusherInput(stack);
    }

    @Override
    public String getName() {
        return "Crusher";
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new GUICrusher(player.inventory, (TileEntityMachineCrusher) world.getTileEntity(pos));
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerCrusher(player.inventory, (TileEntityMachineCrusher) world.getTileEntity(pos));
    }
}
