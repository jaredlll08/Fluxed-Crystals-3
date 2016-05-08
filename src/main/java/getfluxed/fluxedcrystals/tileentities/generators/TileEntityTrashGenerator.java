package getfluxed.fluxedcrystals.tileentities.generators;

import getfluxed.fluxedcrystals.api.generators.Registry;
import getfluxed.fluxedcrystals.api.generators.Registry.TrashGenerator;
import getfluxed.fluxedcrystals.api.generators.generators.GeneratorBase;
import getfluxed.fluxedcrystals.blocks.generators.BlockCoalGenerator;
import getfluxed.fluxedcrystals.blocks.generators.BlockTrashGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class TileEntityTrashGenerator extends GeneratorBase {

    public TileEntityTrashGenerator() {
        super(20000, 1);
    }


    @Override
    public void generateEnergy(World world, BlockPos pos, int generationTimer) {
        if (this.storage.getEnergyStored() < this.storage.getMaxEnergyStored()) {
            this.storage.receiveEnergy(10, false);
        }
    }

    @Override
    public void update() {
        super.update();
        if (worldObj.getBlockState(getPos()).getValue(BlockCoalGenerator.isActive) && !isGenerating() && getStackInSlot(0)==null)
            BlockTrashGenerator.setState(false, this.worldObj, this.pos);
        else if(!worldObj.getBlockState(getPos()).getValue(BlockCoalGenerator.isActive) && isGenerating())
            BlockTrashGenerator.setState(true, this.worldObj, this.pos);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int slotNumber, ItemStack stack) {
        return Registry.TrashGenerator.canTrash(stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }


    @Override
    public boolean canGenerateEnergy(ItemStack stack) {
        return TrashGenerator.canTrash(stack);
    }

    @Override
    public int getGenerationTime(ItemStack stack) {
        return 40;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
