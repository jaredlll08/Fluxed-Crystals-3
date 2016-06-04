package getfluxed.fluxedcrystals.tileentities.greenhouse;

import cofh.api.energy.EnergyStorage;
import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.api.crystals.CrystalInfo;
import getfluxed.fluxedcrystals.api.multiblock.IFrame;
import getfluxed.fluxedcrystals.api.multiblock.IGreenHouseComponent;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.blocks.greenhouse.BlockSoilController;
import getfluxed.fluxedcrystals.blocks.greenhouse.frame.BlockFrameBattery;
import getfluxed.fluxedcrystals.blocks.greenhouse.frame.base.BlockBaseFrame;
import getfluxed.fluxedcrystals.blocks.greenhouse.io.BlockCrystalIO;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageControllerSync;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageGHLoad;
import getfluxed.fluxedcrystals.tileentities.base.TileEnergyBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.LinkedList;

/**
 * Created by Jared on 3/19/2016.
 */
public class TileEntitySoilController extends TileEnergyBase implements ITickable, IGreenHouseComponent, ISidedInventory {

    public FluidTank tank;
    boolean firstTicked = false;
    private MultiBlock multiBlock;
    private int tick;
    private ItemStack[] items;
    private CrystalInfo crystalInfo;

    public TileEntitySoilController() {
        super(0, 250);
        getEnergyStorage().setMaxTransfer(250);
        multiBlock = new MultiBlock(getPos());
        tank = new FluidTank(0);
        items = new ItemStack[0];
        crystalInfo = CrystalInfo.NULL;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 8192D;
    }

    @Override
    public EnumSet<EnumFacing> getValidOutputs() {
        return EnumSet.allOf(EnumFacing.class);
    }

    @Override
    public EnumSet<EnumFacing> getValidInputs() {
        return EnumSet.allOf(EnumFacing.class);
    }

    @Override
    public void update() {
        if (getWorld() != null && !firstTicked) {

            this.setMaxStorage(checkMultiblock());
            if (getMultiBlock().isActive()) {
                this.tank.setCapacity(multiBlock.getAirBlocks().size() * 16000);
            }
            if (!worldObj.isRemote) {
                PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
            }
            firstTicked = true;
        }

        if (tick % 40 == 0) {
            if (!getMultiBlock().isActive()) {
                if (multiBlock.getMaster().equals(new BlockPos(0, 0, 0))) {
                    multiBlock.setMaster(getPos());
                }

                if (getMultiBlock().isActive()) {
                    this.tank.setCapacity(multiBlock.getAirBlocks().size() * 16000);
                }
                this.setMaxStorage(checkMultiblock());
                if (!worldObj.isRemote)
                    PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));

            }
        }

        tick = (tick + 1);

    }

    @Override
    public boolean isMaster() {
        return multiBlock.getMaster().equals(getPos()) || multiBlock.getMaster().equals(new BlockPos(0, 0, 0));
    }

    @Override
    public BlockPos getMaster() {
        return multiBlock.getMaster();
    }

    @Override
    public void setMaster(BlockPos pos) {
        if (!isMaster()) {
            multiBlock.setMaster(pos);
        }
    }

    @Override
    public MultiBlock getMultiBlock() {

        return multiBlock != null ? multiBlock : new MultiBlock(getPos());
    }

    public void setMultiBlock(MultiBlock multiBlock) {
        this.multiBlock = multiBlock;
    }

    @Override
    public void breakBlock() {
        getMultiBlock().setActive(false);
    }

    public int checkMultiblock() {
        if (isMaster()) {
            long time = System.currentTimeMillis();

            int northSize = 0;
            int southSize = 0;
            int eastSize = 0;
            int westSize = 0;


            for (EnumFacing fac : EnumFacing.HORIZONTALS) {
                int count = 0;
                while (getWorld().getBlockState(pos.offset(fac, ++count)).getBlock() instanceof BlockBaseFrame) {
                }
                count--;
                if (count == 0) {
                    return 0;
                }
                switch (fac) {
                    case NORTH:
                        if (northSize == 0 || northSize > count) {
                            northSize = count;
                        }
                        break;
                    case SOUTH:
                        if (southSize == 0 || southSize > count) {
                            southSize = count;
                        }
                        break;
                    case EAST:
                        if (eastSize == 0 || eastSize > count) {
                            eastSize = count;
                        }
                        break;
                    case WEST:
                        if (westSize == 0 || westSize > count) {
                            westSize = count;
                        }
                        break;
                }


            }
            LinkedList<BlockPos> airPos = new LinkedList<>();
            LinkedList<BlockPos> bottomLayer = new LinkedList<>();
            LinkedList<BlockPos> topLayer = new LinkedList<>();
            LinkedList<BlockPos> sides = new LinkedList<>();
            LinkedList<BlockPos> inner = new LinkedList<>();

            BlockPos southWest = pos.offset(EnumFacing.SOUTH, southSize + 1).offset(EnumFacing.WEST, westSize + 1);
            BlockPos northEast = pos.offset(EnumFacing.EAST, eastSize + 1).offset(EnumFacing.NORTH, northSize + 1);

            boolean completeStructure = true;
            int ySize = 0;

            //Checks gets the bottom layer
            for (BlockPos bp : BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1))) {
                if (getWorld().getBlockState(bp).getBlock() instanceof BlockBaseFrame || bp.equals(getPos())) {
                    bottomLayer.add(bp);
                } else {
                    return 0;
                }


                //Counts the height of the structure (excluding base platform)
                int y = 1;
                while ((getWorld().isAirBlock(bp.offset(EnumFacing.UP, y)) || y == 0) && y < 256) {
                    y++;
                }
                if (y == 1 || y == 256 || y < ySize) {
                    return 0;
                }
                ySize = y;

            }
            for (int y = 1; y < ySize; y++) {
                if (!checkAirLayer(getWorld(), northEast.offset(EnumFacing.UP, y), southWest.offset(EnumFacing.UP, y))) {
                    return 0;
                }

            }
            southWest = southWest.offset(EnumFacing.UP, ySize);
            BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1).offset(EnumFacing.DOWN, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1).offset(EnumFacing.UP, 1)).forEach(inner::add);
            for (BlockPos bp : bottomLayer) {
                topLayer.add(bp.offset(EnumFacing.UP, ySize));
            }

            //Gets all the airblocks
            BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1).offset(EnumFacing.UP, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1).offset(EnumFacing.DOWN, 1)).forEach(airPos::add);

            //Checks for frames around
            for (BlockPos bp : BlockPos.getAllInBox(northEast, southWest)) {
                if (!inner.contains(bp)) {
                    sides.add(bp);
                }
            }
            int energyCapacity = 0;
            boolean hasCrystalIO = false;
            for (BlockPos bp : sides) {
                if (!(worldObj.getTileEntity(bp) instanceof IFrame)) {
                    return 0;
                } else {
                    TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
                    tile.setMaster(pos);
                    if (worldObj.getBlockState(bp).getBlock() instanceof BlockFrameBattery) {
                        energyCapacity += ((BlockFrameBattery) worldObj.getBlockState(bp).getBlock()).getCapacity();
                    } else if (worldObj.getBlockState(bp).getBlock() instanceof BlockCrystalIO) {
                        hasCrystalIO = true;
                    }
                    if (!worldObj.isRemote)
                        PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoad(tile, getMaster()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), 128D));

                }
            }
            for (BlockPos bp : bottomLayer) {
                if (!(worldObj.getBlockState(bp).getBlock() instanceof BlockBaseFrame) && !bp.equals(getPos())) {
                    return 0;
                } else {
                    if (!(worldObj.getBlockState(bp).getBlock() instanceof BlockSoilController)) {
                        TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
                        tile.setMaster(pos);
                        if (!worldObj.isRemote)
                            PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoad(tile, getMaster()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
                    }
                }
            }
            for (BlockPos bp : topLayer) {
                if (!(worldObj.getTileEntity(bp) instanceof IFrame)) {
                    return 0;
                } else {
                    TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
                    tile.setMaster(pos);
                    if (worldObj.getBlockState(bp).getBlock() instanceof BlockFrameBattery) {
                        energyCapacity += ((BlockFrameBattery) worldObj.getBlockState(bp).getBlock()).getCapacity();
                    } else if (worldObj.getBlockState(bp).getBlock() instanceof BlockCrystalIO) {
                        hasCrystalIO = true;
                    }
                    if (!worldObj.isRemote)
                        PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoad(tile, getMaster()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));

                }
            }
            for (BlockPos bp : airPos) {
                if (!worldObj.isAirBlock(bp)) {
                    return 0;
                } else {
                }
            }


            if (completeStructure) {
                MultiBlock multiBlock = new MultiBlock(pos, bottomLayer, topLayer, airPos, sides, true);
                setMultiBlock(multiBlock);
                this.tank.setCapacity(multiBlock.getAirBlocks().size() * 16000);
                setMaxStorage(energyCapacity);
                if (hasCrystalIO) {
                    items = new ItemStack[2];
                }
                this.setMaster(getPos());
                time = (System.currentTimeMillis() - time);
                AxisAlignedBB multiblock = new AxisAlignedBB(northEast, southWest);
                FluxedCrystals.logger.log(Level.INFO, String.format("Completed a %sx%sx%s structure in: %s ms", (int) multiblock.maxX - (int) multiblock.minX + 1, (int) multiblock.maxY - (int) multiblock.minY + 1, (int) multiblock.maxZ - (int) multiblock.minZ + 1, time));
                return energyCapacity;
            }
        }
        return 0;
    }

    public boolean checkAirLayer(World world, BlockPos northEast, BlockPos southWest) {
        boolean passed = true;
        for (BlockPos bp : BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1))) {
            if (!world.isAirBlock(bp)) {
                passed = false;
            }
        }
        return passed;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        setMultiBlock(MultiBlock.readFromNBT(compound.getCompoundTag("multiblock")));
        NBTTagCompound tankTag = compound.getCompoundTag("tank");
        this.tank.readFromNBT(tankTag);
        readInventoryFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound multi = new NBTTagCompound();
        MultiBlock.writeToNBT(multi, getMultiBlock());
        compound.setTag("multiblock", multi);
        NBTTagCompound tankTag = new NBTTagCompound();
        tank.writeToNBT(tankTag);
        compound.setTag("tank", tankTag);
        writeInventoryToNBT(compound);
        return compound;
    }

    public void readInventoryFromNBT(NBTTagCompound tags) {
        NBTTagList nbttaglist = tags.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int iter = 0; iter < nbttaglist.tagCount(); iter++) {
            NBTTagCompound tagList = (NBTTagCompound) nbttaglist.getCompoundTagAt(iter);
            byte slotID = tagList.getByte("Slot");
            if (slotID >= 0 && slotID < items.length) {
                items[slotID] = ItemStack.loadItemStackFromNBT(tagList);
            }
        }
    }

    public void writeInventoryToNBT(NBTTagCompound tags) {
        NBTTagList nbttaglist = new NBTTagList();
        for (int iter = 0; iter < items.length; iter++) {
            if (items[iter] != null) {
                NBTTagCompound tagList = new NBTTagCompound();
                tagList.setByte("Slot", (byte) iter);
                items[iter].writeToNBT(tagList);
                nbttaglist.appendTag(tagList);
            }
        }

        tags.setTag("Items", nbttaglist);
    }

    public EnergyStorage getEnergyStorage() {
        return storage;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }


    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(items, index);
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
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
        int itemLength = items.length;
        items = new ItemStack[itemLength];
    }

    @Override
    public String getName() {
        return "Greenhouse";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }


    @Override
    public ItemStack decrStackSize(int i, int count) {
        ItemStack itemstack = getStackInSlot(i);

        if (itemstack != null) {
            if (itemstack.stackSize <= count) {
                setInventorySlotContents(i, null);
            } else {
                itemstack = itemstack.splitStack(count);

            }
        }

        return itemstack;
    }

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1) {

        return items[par1];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        items[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public ItemStack addInventorySlotContents(int i, ItemStack itemstack) {
        if (items[i] != null) {

            if (items[i].isItemEqual(itemstack)) {
                items[i].stackSize += itemstack.stackSize;
            }
            if (items[i].stackSize > getInventoryStackLimit()) {
                items[i].stackSize = getInventoryStackLimit();
            }
        } else {
            setInventorySlotContents(i, itemstack);
        }
        return null;
    }


    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(getPos().getX() + 0.5f, getPos().getY() + 0.5f, getPos().getZ() + 0.5f) <= 64;
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

    public ItemStack[] getItems() {
        return items;
    }


    public void setItems(ItemStack[] items) {
        this.items = items;
    }

    public CrystalInfo getCrystalInfo() {
        return crystalInfo;
    }

    public void setCrystalInfo(CrystalInfo crystalInfo) {
        this.crystalInfo = crystalInfo;
    }
}
