package getfluxed.fluxedcrystals.tileentities.greenhouse;

import com.google.common.base.Stopwatch;
import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.api.multiblock.IFrame;
import getfluxed.fluxedcrystals.api.multiblock.IGreenHouseComponent;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.api.nbt.EnumConverter;
import getfluxed.fluxedcrystals.api.nbt.NBT;
import getfluxed.fluxedcrystals.api.nbt.TileEntityNBT;
import getfluxed.fluxedcrystals.blocks.greenhouse.BlockSoilController;
import getfluxed.fluxedcrystals.blocks.greenhouse.frame.BlockFrameBattery;
import getfluxed.fluxedcrystals.blocks.greenhouse.frame.base.BlockBaseFrame;
import getfluxed.fluxedcrystals.blocks.greenhouse.io.BlockCrystalIO;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageControllerSync;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageGHLoadAll;
import net.darkhax.tesla.api.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jared on 3/19/2016.
 */
public class TileEntitySoilController extends TileEntityNBT implements ITickable, IGreenHouseComponent {

    public ItemStackHandler itemStackHandler = new ItemStackHandler(5);

    @NBT(EnumConverter.FLUIDTANK)
    public FluidTank tank;
    private MultiBlock multiBlock;
    private Crystal crystalInfo;
    private boolean growing;
    private boolean firstTicked;
    private int tick = 0;
    private double currentGrowth = 0;

    public BaseTeslaContainer container;

    public TileEntitySoilController() {
        container = new BaseTeslaContainer(0, 250, 250);
        multiBlock = new MultiBlock(getPos());
        tank = new FluidTank(Integer.MAX_VALUE);
        crystalInfo = Crystal.NULL;
        growing = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        setMultiBlock(MultiBlock.readFromNBT(compound.getCompoundTag("multiblock")));
        itemStackHandler.deserializeNBT(compound.getCompoundTag("items"));
        setCrystalInfo(Crystal.readFromNBT(compound.getCompoundTag("crystalTag")));
        setGrowing(compound.getBoolean("growing"));
        setCurrentGrowth(compound.getDouble("currentGrowth"));
        container.deserializeNBT(compound.getCompoundTag("tesla"));
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound multi = new NBTTagCompound();
        MultiBlock.writeToNBT(multi, getMultiBlock());
        compound.setTag("multiblock", multi);
        compound.setTag("items", itemStackHandler.serializeNBT());
        NBTTagCompound crystalTag = new NBTTagCompound();
        getCrystalInfo().writeToNBT(crystalTag);
        compound.setTag("crystalTag", crystalTag);
        compound.setBoolean("growing", isGrowing());
        compound.setDouble("currentGrowth", getCurrentGrowth());
        compound.setTag("tesla", container.serializeNBT());
        return compound;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 8192D;
    }

//    @Override
//    public EnumSet<EnumFacing> getValidOutputs() {
//        return EnumSet.allOf(EnumFacing.class);
//    }
//
//    @Override
//    public EnumSet<EnumFacing> getValidInputs() {
//        return EnumSet.allOf(EnumFacing.class);
//    }

    @Override
    public void update() {
        if (getWorld() != null && !firstTicked) {
            long check = checkMultiblock();
            if (check > -1) {
                this.container.setCapacity(check);
            }
            if (!worldObj.isRemote) {
                markDirty();
            }
            firstTicked = true;
        }
        //TODO update client when a side is broken
        if (tick % 80 == 0) {
            if (!getMultiBlock().isActive()) {
                long check = checkMultiblock();
                if (check > -1) {
                    if (multiBlock.getMaster().equals(new BlockPos(0, 0, 0))) {
                        multiBlock.setMaster(getPos());
                    }
                    this.container.setCapacity(check);
                    if (!worldObj.isRemote) {
                        markDirty();
                    }
                } else {
                    if (!worldObj.isRemote) {
                        markDirty();
                    }
                }
            }
        }

        tick++;

    }


    @Override
    public void onLoad() {
        super.onLoad();

    }

    @Override
    public void validate() {
        super.validate();
//        if (!getMultiBlock().isActive()) {
//            System.out.println("called");
////TODO this causes a crash because it loops, find another place!
//            int check = checkMultiblock();
//            if (check > -1) {
//                setMaxStorage(check);
//                if (getMultiBlock().isActive()) {
//                    tank.setCapacity(getMultiBlock().getAirBlocks().size() * 16000);
//                }
//                if (!getWorld().isRemote) {
//                    PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
//                }
//            }
//        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
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

    public long checkMultiblock() {
        if (isMaster()) {
            Stopwatch watch = Stopwatch.createStarted();
            Stopwatch total = Stopwatch.createStarted();

            int northSize = 0;
            int southSize = 0;
            int eastSize = 0;
            int westSize = 0;

            for (EnumFacing fac : EnumFacing.HORIZONTALS) {
                int count = 0;
                sides:
                while (getWorld().getBlockState(pos.offset(fac, ++count)).getBlock() instanceof BlockBaseFrame) {
                }

                count--;
                if (count == 0) {
                    return -1;
                }

                switch (fac) {
                    default:
                        break;
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
            watch.reset().start();
            LinkedList<BlockPos> airPos = new LinkedList<>();
            LinkedList<BlockPos> bottomLayer = new LinkedList<>();
            LinkedList<BlockPos> topLayer = new LinkedList<>();
            LinkedList<BlockPos> sides = new LinkedList<>();
            LinkedList<BlockPos> inner = new LinkedList<>();

            BlockPos southWest = pos.offset(EnumFacing.SOUTH, southSize + 1).offset(EnumFacing.WEST, westSize + 1);
            BlockPos northEast = pos.offset(EnumFacing.EAST, eastSize + 1).offset(EnumFacing.NORTH, northSize + 1);
            watch.reset().start();

            boolean completeStructure = true;
            int ySize = 0;

            //Checks gets the bottom layer
            for (BlockPos bp : BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1))) {
                if (getWorld().getBlockState(bp).getBlock() instanceof BlockBaseFrame || bp.equals(getPos())) {
                    bottomLayer.add(bp);
                } else {
                    return -1;
                }


                //Counts the height of the structure (excluding base platform)
                int y = 1;
                air:
                while ((getWorld().isAirBlock(bp.offset(EnumFacing.UP, y)) || y == 0) && y < 256) {
                    y++;
                }
                watch.reset().start();

                if (y == 1 || y == 256 || y < ySize) {
                    return -1;
                }
                ySize = y;
            }
            watch.reset().start();

            air:
            for (int y = 1; y < ySize; y++) {
                if (!checkAirLayer(getWorld(), northEast.offset(EnumFacing.UP, y), southWest.offset(EnumFacing.UP, y))) {
                    return -1;
                }
            }
            watch.reset().start();
            southWest = southWest.offset(EnumFacing.UP, ySize);
            BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1).offset(EnumFacing.DOWN, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1).offset(EnumFacing.UP, 1)).forEach(inner::add);

            for (BlockPos bp : bottomLayer) {
                topLayer.add(bp.offset(EnumFacing.UP, ySize));
            }
            watch.reset().start();
            //Gets all the airblocks
            BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1).offset(EnumFacing.UP, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1).offset(EnumFacing.DOWN, 1)).forEach(airPos::add);

            //Checks for frames around
            for (BlockPos bp : BlockPos.getAllInBox(northEast, southWest)) {
                if (!inner.contains(bp)) {
                    sides.add(bp);
                }
            }
            watch.reset().start();
            int energyCapacity = 0;
            boolean hasCrystalIO = false;
            for (BlockPos bp : sides) {
                if (!(worldObj.getTileEntity(bp) instanceof IFrame)) {
                    return -1;
                } else {
                    TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
                    tile.setMaster(pos);
                    if (worldObj.getBlockState(bp).getBlock() instanceof BlockFrameBattery) {
                        energyCapacity += ((BlockFrameBattery) worldObj.getBlockState(bp).getBlock()).getCapacity();
                    } else if (worldObj.getBlockState(bp).getBlock() instanceof BlockCrystalIO) {
                        hasCrystalIO = true;
                    }

                }
            }

            watch.reset().start();
            for (BlockPos bp : bottomLayer) {
                if (!(worldObj.getBlockState(bp).getBlock() instanceof BlockBaseFrame) && !bp.equals(getPos())) {
                    return -1;
                } else {
                    if (!(worldObj.getBlockState(bp).getBlock() instanceof BlockSoilController)) {
                        TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
                        tile.setMaster(pos);
                    }
                }
            }

            watch.reset().start();
            for (BlockPos bp : topLayer) {
                if (!(worldObj.getTileEntity(bp) instanceof IFrame)) {
                    return -1;
                } else {
                    TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
                    tile.setMaster(pos);
                    if (worldObj.getBlockState(bp).getBlock() instanceof BlockFrameBattery) {
                        energyCapacity += ((BlockFrameBattery) worldObj.getBlockState(bp).getBlock()).getCapacity();
                    } else if (worldObj.getBlockState(bp).getBlock() instanceof BlockCrystalIO) {
                        hasCrystalIO = true;
                    }

                }
            }

            watch.reset().start();
            for (BlockPos bp : airPos) {
                if (!worldObj.isAirBlock(bp)) {
                    return -1;
                } else {
                }
            }
            watch.reset().start();
            if (completeStructure) {
                System.out.println("complete");
                MultiBlock multiBlock = new MultiBlock(pos, bottomLayer, topLayer, airPos, sides, true);
                setMultiBlock(multiBlock);
                this.tank.setCapacity(multiBlock.getAirBlocks().size() * 16000);
//                setMaxStorage(energyCapacity);
                this.container.setCapacity(energyCapacity);
                this.setMaster(getPos());
                if (!worldObj.isRemote) {
                    PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoadAll(getMaster(), multiBlock), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
                }
                long time = total.elapsed(TimeUnit.MILLISECONDS);
                AxisAlignedBB multiblock = new AxisAlignedBB(northEast, southWest);
                FluxedCrystals.logger.log(Level.INFO, String.format("Completed a %sx%sx%s structure in: %s ms", (int) multiblock.maxX - (int) multiblock.minX + 1, (int) multiblock.maxY - (int) multiblock.minY + 1, (int) multiblock.maxZ - (int) multiblock.minZ + 1, time));
                return energyCapacity;
            }
            System.out.println("Failed: " + watch.elapsed(TimeUnit.MILLISECONDS));
            watch.reset().start();
        }
        return -1;
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

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }


    public Crystal getCrystalInfo() {
        return crystalInfo;
    }

    public void setCrystalInfo(Crystal crystal) {
        this.crystalInfo = crystal;
    }

    public double getCurrentGrowth() {
        return currentGrowth;
    }

    public void setCurrentGrowth(double currentGrowth) {
        this.currentGrowth = currentGrowth;
    }

    public boolean isGrowing() {
        return growing;
    }

    public void setGrowing(boolean growing) {
        this.growing = growing;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemStackHandler;
        } else if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return (T) this.container;
        return super.getCapability(capability, facing);
    }
}
