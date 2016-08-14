package getfluxed.fluxedcrystals.tileentities.generators;

import getfluxed.fluxedcrystals.api.generators.Registry;
import getfluxed.fluxedcrystals.api.generators.generators.GeneratorBase;
import getfluxed.fluxedcrystals.blocks.generators.BlockCoalGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCoalGenerator extends GeneratorBase  {

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

}
