package getfluxed.fluxedcrystals.tileentities.generators;

import getfluxed.fluxedcrystals.api.client.gui.IOpenableGUI;
import getfluxed.fluxedcrystals.api.generators.Registry;
import getfluxed.fluxedcrystals.api.generators.generators.GeneratorBase;
import getfluxed.fluxedcrystals.blocks.generators.BlockCoalGenerator;
import getfluxed.fluxedcrystals.client.gui.coalGenerator.ContainerCoalGenerator;
import getfluxed.fluxedcrystals.client.gui.coalGenerator.GUICoalGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCoalGenerator extends GeneratorBase implements IOpenableGUI {

    public TileEntityCoalGenerator() {
        super(50000, 1);
    }

    @Override
    public void generateEnergy(World world, BlockPos pos, int timer) {
        this.container.givePower(40, false);
    }

    @Override
    public void update() {
        super.update();
        boolean active = true;
        if (!isGenerating()) {
            active = false;
        }
        if (itemStackHandler.getStackInSlot(0) == null && !isGenerating()) {
            active = false;
        }
        if (this.container.getStoredPower() == this.container.getCapacity()) {
            active = false;
        }

        if (worldObj.getBlockState(getPos()).getValue(BlockCoalGenerator.isActive) != active) {
            BlockCoalGenerator.setState(active, this.worldObj, this.pos);
        }
    }

    public boolean canGenerateEnergy(ItemStack stack) {
        return this.container.getStoredPower() < this.container.getCapacity() && Registry.BasicCoalGenerator.containsItemStack(stack);
    }


    @Override
    public int getGenerationTime(ItemStack stack) {
        return Registry.BasicCoalGenerator.getBurnTime(stack);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new GUICoalGenerator(player.inventory, (TileEntityCoalGenerator) world.getTileEntity(pos));
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerCoalGenerator(player.inventory, (TileEntityCoalGenerator) world.getTileEntity(pos));
    }
}
