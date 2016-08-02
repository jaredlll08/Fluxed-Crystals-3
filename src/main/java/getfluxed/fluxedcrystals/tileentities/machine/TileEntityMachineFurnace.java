package getfluxed.fluxedcrystals.tileentities.machine;

import getfluxed.fluxedcrystals.api.client.gui.IOpenableGUI;
import getfluxed.fluxedcrystals.api.recipes.machines.RecipeMachineBase;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.client.gui.furnace.ContainerFurnace;
import getfluxed.fluxedcrystals.client.gui.furnace.GUIFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by Jared on 5/25/2016.
 */
public class TileEntityMachineFurnace extends TileEntityMachineBase implements IOpenableGUI {
    public TileEntityMachineFurnace() {
        super(32000, 2);
    }

    @Override
    public int getEnergyUsed() {
        return 250;
    }

    @Override
    public RecipeMachineBase getRecipe(String index) {
        return RecipeRegistry.getFurnaceRecipeByID(index);
    }

    @Override
    public HashMap<String, RecipeMachineBase> getRecipes() {
        return RecipeRegistry.getAllFurnaceRecipes();
    }

    @Override
    public boolean isValidInput(ItemStack stack) {
        return RecipeRegistry.isFurnaceInput(stack);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new GUIFurnace(player.inventory, (TileEntityMachineFurnace) world.getTileEntity(pos));
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerFurnace(player.inventory, (TileEntityMachineFurnace) world.getTileEntity(pos));
    }
}
