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


    @Override
    public void update() {
        if (getMultiBlock() != null && getMultiBlock().isActive()) {

            TileEntitySoilController master = (TileEntitySoilController) worldObj.getTileEntity(getMaster());
            if (!master.isGrowing() && master.getCrystalInfo().equals(CrystalInfo.NULL) && getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof ICrystalInfoProvider) {
                System.out.println("active");
                CrystalInfo info = ((ICrystalInfoProvider) getStackInSlot(0).getItem()).getCrystalInfo(getStackInSlot(0));
                master.setCrystalInfo(info);
                decrStackSize(0, 1);
                master.setCurrentGrowth(0);
                master.setGrowing(true);
            } else if (!master.getCrystalInfo().equals(CrystalInfo.NULL) && !master.getCrystalInfo().getName().equals(CrystalInfo.NULL.getName()) && !master.getCrystalInfo().getName().isEmpty()) {
                master.setCurrentGrowth(master.getCurrentGrowth() + 1);
                if (master.getCurrentGrowth() >= master.getCrystalInfo().getGrowthTime()) {
                    System.out.println(master.getCrystalInfo());
                    System.out.println(CrystalRegistry.getCrystal(master.getCrystalInfo().getName()));
                    System.out.println(CrystalRegistry.getCrystal(master.getCrystalInfo().getName()).getResourceOut().getItemStack());
                    addInventorySlotContents(1, CrystalRegistry.getCrystal(master.getCrystalInfo().getName()).getResourceOut().getItemStack());
                    master.setCurrentGrowth(0);
                    master.setGrowing(false);
                    master.setCrystalInfo(CrystalInfo.NULL);
                }
            }

        }
    }

    public ItemStack addInventorySlotContents(int i, ItemStack itemstack) {
        return getMasterTile() == null ? null : getMasterTile().addInventorySlotContents(i, itemstack);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return compound;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return getMasterTile() == null ? new int[]{} : getMasterTile().getSlotsForFace(side);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return getMasterTile() == null ? false : getMasterTile().canInsertItem(index, itemStackIn, direction);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return getMasterTile() == null ? false : getMasterTile().canExtractItem(index, stack, direction);
    }

    @Override
    public int getSizeInventory() {
        return getMasterTile() == null ? 0 : getMasterTile().getSizeInventory();
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return getMasterTile() == null ? null : getMasterTile().getStackInSlot(index);
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return getMasterTile() == null ? null : getMasterTile().decrStackSize(index, count);
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return getMasterTile() == null ? null : getMasterTile().removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        if (getMasterTile() != null) {
            getMasterTile().setInventorySlotContents(index, stack);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return getMasterTile() == null ? 0 : getMasterTile().getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return getMasterTile() == null ? false : getMasterTile().isUseableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (getMasterTile() != null) {
            getMasterTile().openInventory(player);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (getMasterTile() != null) {
            getMasterTile().closeInventory(player);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return getMasterTile() == null ? false : getMasterTile().isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return getMasterTile() == null ? 0 : getMasterTile().getField(id);
    }

    @Override
    public void setField(int id, int value) {
        if (getMasterTile() != null) {
            getMasterTile().setField(id, value);
        }
    }

    @Override
    public int getFieldCount() {
        return getMasterTile() == null ? 0 : getMasterTile().getFieldCount();
    }

    @Override
    public void clear() {
        if (getMasterTile() != null) {
            getMasterTile().clear();
        }
    }

    @Override
    public String getName() {
        return getMasterTile() == null ? null : getMasterTile().getName();
    }

    @Override
    public boolean hasCustomName() {
        return getMasterTile() == null ? false : getMasterTile().hasCustomName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return getMasterTile() == null ? null : getMasterTile().getDisplayName();
    }

    public TileEntitySoilController getMasterTile() {
        if (worldObj.getTileEntity(getMaster()) != null) {
            if (worldObj.getTileEntity(getMaster()) instanceof TileEntitySoilController) {
                return ((TileEntitySoilController) worldObj.getTileEntity(getMaster()));
            }
        }
        return null;
    }
}
