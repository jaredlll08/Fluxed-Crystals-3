package getfluxed.fluxedcrystals.tileentities.greenhouse.io;

import getfluxed.fluxedcrystals.api.client.gui.IOpenableGUI;
import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.api.crystals.ICrystalInfoProvider;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.client.gui.crystalio.ContainerCrystalIO;
import getfluxed.fluxedcrystals.client.gui.crystalio.GUICrystalIO;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.greenhouse.io.MessageCrystalIO;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

/**
 * Created by Jared on 6/4/2016.
 */
public class TileEntityCrystalIO extends TileEntityMultiBlockComponent implements ITickable, ISidedInventory, IOpenableGUI {


    @Override
    public void update() {
        if (getMultiBlock() != null && getMultiBlock().isActive()) {
            boolean sendUpdate = false;
            TileEntitySoilController master = (TileEntitySoilController) worldObj.getTileEntity(getMaster());

            if (!master.isGrowing() && master.getCrystalInfo().equals(Crystal.NULL) && getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof ICrystalInfoProvider) {
                Crystal info = ((ICrystalInfoProvider) getStackInSlot(0).getItem()).getCrystal(getStackInSlot(0));
                master.setCrystalInfo(info);
                decrStackSize(0, 1);
                master.setCurrentGrowth(0);
                master.setGrowing(true);
                sendUpdate = true;
            } else if (!master.getCrystalInfo().equals(Crystal.NULL) && !master.getCrystalInfo().getName().equals(Crystal.NULL.getName())) {
                master.setCurrentGrowth(master.getCurrentGrowth() + 1);
                System.out.println(((master.getCrystalInfo().getGrowthTimePerBlock() * master.getMultiBlock().getAirBlocks().size())));
                if (master.getCurrentGrowth() >= (master.getCrystalInfo().getGrowthTimePerBlock()*master.getMultiBlock().getAirBlocks().size())) {
                    addInventorySlotContents(1, CrystalRegistry.getCrystal(master.getCrystalInfo().getName()).getResourceOut().getItemStack());
                    master.setCurrentGrowth(0);
                    master.setGrowing(false);
                    master.setCrystalInfo(Crystal.NULL);
                }
                sendUpdate = true;
            }
            if(master.isGrowing() && master.getCrystalInfo().equals(Crystal.NULL)){
                master.setGrowing(false);
                master.setCurrentGrowth(0);
                sendUpdate = true;
            }
            if (sendUpdate) {
                PacketHandler.INSTANCE.sendToAllAround(new MessageCrystalIO(getMasterTile()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
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

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new GUICrystalIO(player.inventory, (TileEntityCrystalIO) world.getTileEntity(pos));
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerCrystalIO(player.inventory, (TileEntityCrystalIO) world.getTileEntity(pos));
    }
}
