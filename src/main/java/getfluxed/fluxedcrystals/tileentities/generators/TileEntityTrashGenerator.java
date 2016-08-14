package getfluxed.fluxedcrystals.tileentities.generators;

import getfluxed.fluxedcrystals.api.generators.Registry.TrashGenerator;
import getfluxed.fluxedcrystals.api.generators.generators.GeneratorBase;
import getfluxed.fluxedcrystals.blocks.generators.BlockTrashGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTrashGenerator extends GeneratorBase {

    public TileEntityTrashGenerator() {
        super(20000, 1);
    }

    @Override
    public void generateEnergy(World world, BlockPos pos, int generationTimer) {
        this.container.givePower(10, false);

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

        if (worldObj.getBlockState(getPos()).getValue(BlockTrashGenerator.isActive) != active) {
            BlockTrashGenerator.setState(active, this.worldObj, this.pos);
        }

    }



    @Override
    public boolean canGenerateEnergy(ItemStack stack) {
        return TrashGenerator.canTrash(stack);
    }

    @Override
    public int getGenerationTime(ItemStack stack) {
        return 40;
    }

}
