package getfluxed.fluxedcrystals.tileentities.machine;

import getfluxed.fluxedcrystals.api.client.gui.IOpenableGUI;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeMachineBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.client.gui.coalGenerator.ContainerCoalGenerator;
import getfluxed.fluxedcrystals.client.gui.coalGenerator.GUICoalGenerator;
import getfluxed.fluxedcrystals.client.gui.sawmill.ContainerSawmill;
import getfluxed.fluxedcrystals.client.gui.sawmill.GUISawmill;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityCoalGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by Jared on 5/31/2016.
 */
public class TileEntityMachineSawmill extends TileEntityMachineBase implements IOpenableGUI {
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

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new GUISawmill(player.inventory, (TileEntityMachineSawmill) world.getTileEntity(pos));
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerSawmill(player.inventory, (TileEntityMachineSawmill) world.getTileEntity(pos));
    }
}
