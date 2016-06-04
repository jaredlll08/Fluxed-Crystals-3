package getfluxed.fluxedcrystals.tileentities.greenhouse.io;

import getfluxed.fluxedcrystals.api.crystals.CrystalInfo;
import getfluxed.fluxedcrystals.api.crystals.ICrystalInfoProvider;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by Jared on 6/4/2016.
 */
public class TileEntityCrystalIO extends TileEntityMultiBlockComponent implements ITickable, ISidedInventory {
    private boolean growing;
    private double currentGrowth = 0;

    @Override
    public void update() {
        if (getMultiBlock() != null && getMultiBlock().isActive()) {
            TileEntitySoilController master = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            if (!growing && getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof ICrystalInfoProvider) {
                CrystalInfo info = ((ICrystalInfoProvider) getStackInSlot(0).getItem()).getCrystalInfo(getStackInSlot(0));
                master.setCrystalInfo(info);
                decrStackSize(0, 1);
                growing = true;
            } else if (!master.getCrystalInfo().equals(CrystalInfo.NULL)) {
                if (currentGrowth++ >= master.getCrystalInfo().getGrowthTime()) {
                    addInventorySlotContents(1, CrystalRegistry.getCrystal(master.getCrystalInfo().getName()).getResourceOut().getItemStack());
                    currentGrowth = 0;
                    growing = false;
                    master.setCrystalInfo(CrystalInfo.NULL);
                } else {
                    currentGrowth++;
                }
            }

        }
    }

    public ItemStack addInventorySlotContents(int i, ItemStack itemstack) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).addInventorySlotContents(i, itemstack);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.growing = compound.getBoolean("growing");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("growing", growing);
        return compound;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).getSlotsForFace(side);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).canInsertItem(index, itemStackIn, direction);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).canExtractItem(index, stack, direction);
    }

    @Override
    public int getSizeInventory() {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).getSizeInventory();
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).getStackInSlot(index);
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).decrStackSize(index, count);
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).isUseableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).getField(id);
    }

    @Override
    public void setField(int id, int value) {
        ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).getFieldCount();
    }

    @Override
    public void clear() {
        ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).clear();
    }

    @Override
    public String getName() {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).getName();
    }

    @Override
    public boolean hasCustomName() {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).hasCustomName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return ((TileEntitySoilController) worldObj.getTileEntity(getMaster())).getDisplayName();
    }
}
