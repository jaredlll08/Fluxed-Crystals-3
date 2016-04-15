package getfluxed.fluxedcrystals.tileentities.greenhouse;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.api.multiblock.IFrame;
import getfluxed.fluxedcrystals.api.multiblock.IGreenHouseComponent;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.blocks.greenhouse.BlockSoilController;
import getfluxed.fluxedcrystals.blocks.greenhouse.soil.BlockSoil;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageControllerSync;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageGHLoad;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;

import java.util.LinkedList;

/**
 * Created by Jared on 3/19/2016.
 */
public class TileEntitySoilController extends TileEntity implements ITickable, IGreenHouseComponent {

    public FluidTank tank;
    //    @Override
//    public void onLoad() {
//        super.onLoad();
//        checkMultiblock();
//        if (getMultiBlock().isActive()) {
//            this.tank.setCapacity(multiBlock.getAirBlocks().size() * 16000);
//        }
//        if (!worldObj.isRemote)
//            PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
//
//    }
    boolean firstTicked = false;
    private MultiBlock multiBlock;
    private int tick;


//    @Override
//    public void onLoad(){
//        super.onLoad();
//        boolean active = true;
//        if(getMultiBlock().isActive()){
//            for(BlockPos bp : getMultiBlock().getAirBlocks()){
//
//                if(!worldObj.isAirBlock(bp)){
//                    active = false;
//                }else{
//                    //                    worldObj.setBlockState(bp, Blocks.glass.getDefaultState());
////                    TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
////                    tile.setMaster(pos);
//
////                    if(!worldObj.isRemote)
////                        PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoad(tile, getMaster()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
//
//                }
//            }
//            for(BlockPos bp : getMultiBlock().getSides()){
//                if(!worldObj.isAirBlock(bp)){
//                    if(!(worldObj.getTileEntity(bp) instanceof TileEntityMultiBlockComponent)){
//                        active = false;
//                    }else{
//                        //                        worldObj.setBlockState(bp, Blocks.stained_glass.getStateFromMeta(6));
//                        System.out.println(!worldObj.isRemote);
//                        TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
//                        tile.setMaster(pos);
//                        if(!worldObj.isRemote)
//                            PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoad(tile, getMaster()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
//
//                    }
//                }
//            }
//            for(BlockPos bp : getMultiBlock().getBottomLayer()){
//                if(!bp.equals(multiBlock.getMaster()))
//                    if(!worldObj.isAirBlock(bp)){
//                        if(!(worldObj.getTileEntity(bp) instanceof TileEntityMultiBlockComponent)){
//
//                            active = false;
//                        }else{
//                            //                            worldObj.setBlockState(bp, Blocks.dirt.getDefaultState());
//                            TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
//                            tile.setMaster(pos);
//                            if(!worldObj.isRemote)
//                                PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoad(tile, getMaster()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
//
//                        }
//                    }
//            }
//            for(BlockPos bp : getMultiBlock().getTopLayer()){
//                if(!worldObj.isAirBlock(bp)){
//                    if(!(worldObj.getTileEntity(bp) instanceof TileEntityMultiBlockComponent)){
//                        active = false;
//                    }else{
//                        TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
//                        tile.setMaster(pos);
//                        if(!worldObj.isRemote)
//                            PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoad(tile, getMaster()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
//
//                        //                        worldObj.setBlockState(bp, Blocks.clay.getDefaultState());
//                    }
//                }
//            }
//            if(tank.getCapacity() == 0)
//                this.tank.setCapacity(multiBlock.getAirBlocks().size() * 16000);
//            if(!worldObj.isRemote)
//                PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
//
//            getMultiBlock().setActive(active);
//
//        }
//    }


    public TileEntitySoilController() {
        multiBlock = new MultiBlock(getPos());
        tank = new FluidTank(0);
    }

    @Override
    public void update() {
        if (getWorld() != null && !firstTicked) {
            checkMultiblock();
            if (getMultiBlock().isActive()) {
                this.tank.setCapacity(multiBlock.getAirBlocks().size() * 16000);
            }
            if (!worldObj.isRemote)
                PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));

            firstTicked = true;

        }
        if (tick % 40 == 0) {
            if (!getMultiBlock().isActive()) {
                checkMultiblock();
                if (getMultiBlock().isActive()) {
                    this.tank.setCapacity(multiBlock.getAirBlocks().size() * 16000);
                }
                if (!worldObj.isRemote)
                    PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));

            }
        }

        if (getMultiBlock() != null && getMultiBlock().getAirBlocks() != null)
            for (BlockPos bp : getMultiBlock().getAirBlocks()) {
                if (worldObj.isRemote) {
                    EntitySmallFireball ball = new EntitySmallFireball(getWorld(), bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5, 0, 0, 0);
                    //                    worldObj.spawnEntityInWorld(ball);
                    ball.setDead();
                }
            }
        tick = (tick + 1);
        //        System.out.println(tank.getCapacity());
    }

    @Override
    public boolean isMaster() {
        return multiBlock.getMaster().equals(getPos());
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
        return multiBlock;
    }

    public void setMultiBlock(MultiBlock multiBlock) {
        this.multiBlock = multiBlock;
    }

    @Override
    public void breakBlock() {
        getMultiBlock().setActive(false);
    }

    public boolean checkMultiblock() {
        long time = System.currentTimeMillis();

        LinkedList<BlockPos> airPos = new LinkedList<>();
        LinkedList<BlockPos> bottomLayer = new LinkedList<>();
        LinkedList<BlockPos> topLayer = new LinkedList<>();
        LinkedList<BlockPos> sides = new LinkedList<>();
        LinkedList<BlockPos> inner = new LinkedList<>();


        int northSize = 0;
        int southSize = 0;
        int eastSize = 0;
        int westSize = 0;

        BlockPos southWest = pos;
        BlockPos northEast = pos;
        for (EnumFacing fac : EnumFacing.HORIZONTALS) {
            int count = 0;
            while (getWorld().getBlockState(pos.offset(fac, ++count)).getBlock() instanceof BlockSoil) {
            }
            count--;

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
        northEast = northEast.offset(EnumFacing.EAST, eastSize + 1).offset(EnumFacing.NORTH, northSize + 1);
        southWest = southWest.offset(EnumFacing.SOUTH, southSize + 1).offset(EnumFacing.WEST, westSize + 1);

        boolean completeStructure = true;
        int ySize = 0;

        //Checks gets the bottom layer
        for (BlockPos bp : BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1))) {
            if (getWorld().getBlockState(bp).getBlock() instanceof BlockSoil || getWorld().getBlockState(bp).getBlock() instanceof BlockSoilController) {
                bottomLayer.add(bp);
            } else {
                return false;
            }


            //Counts the height of the structure (excluding base platform)
            int y = 1;
            while ((getWorld().isAirBlock(bp.offset(EnumFacing.UP, y)) || y == 0) && y < 256) {
                y++;
            }
            if (y == 1 || y == 256 || y < ySize) {
                return false;
            }
            ySize = y;

        }
        for (int y = 1; y < ySize; y++) {
            if (!checkAirLayer(getWorld(), northEast.offset(EnumFacing.UP, y), southWest.offset(EnumFacing.UP, y))) {
                return false;
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

        //TODO check all arrays here
        for (BlockPos bp : sides) {
            if (!(worldObj.getTileEntity(bp) instanceof IFrame)) {
                return false;
            } else {
                TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
                tile.setMaster(pos);
                if (!worldObj.isRemote)
                    PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoad(tile, getMaster()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), 128D));

            }
        }
        for (BlockPos bp : bottomLayer) {
            if (!(worldObj.getBlockState(bp).getBlock() instanceof BlockSoil) && !(worldObj.getBlockState(bp).getBlock() instanceof BlockSoilController)) {
                return false;
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
                return false;
            } else {
                TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
                tile.setMaster(pos);
                if (!worldObj.isRemote)
                    PacketHandler.INSTANCE.sendToAllAround(new MessageGHLoad(tile, getMaster()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));

            }
        }
        for (BlockPos bp : airPos) {
            if (!worldObj.isAirBlock(bp)) {
                return false;
            } else {
            }
        }


        if (completeStructure) {
            MultiBlock multiBlock = new MultiBlock(pos, bottomLayer, topLayer, airPos, sides, true);
            setMultiBlock(multiBlock);
            this.tank.setCapacity(multiBlock.getAirBlocks().size() * 16000);
        } else {
        }
        //        if(!worldObj.isRemote)
        //            PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
        time = (System.currentTimeMillis() - time);
        AxisAlignedBB multiblock = new AxisAlignedBB(northEast, southWest);
        FluxedCrystals.logger.log(Level.INFO, String.format("Completed a %sx%sx%s structure in: %s ms", (int) multiblock.maxX - (int) multiblock.minX + 1, (int) multiblock.maxY - (int) multiblock.minY + 1, (int) multiblock.maxZ - (int) multiblock.minZ + 1, time));

        return false;
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
        System.out.println(compound.getCompoundTag("multiblock"));
        setMultiBlock(MultiBlock.readFromNBT(compound.getCompoundTag("multiblock")));
        NBTTagCompound tankTag = compound.getCompoundTag("tank");
        this.tank.readFromNBT(tankTag);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound multi = new NBTTagCompound();
        MultiBlock.writeToNBT(multi, getMultiBlock());
        compound.setTag("multiblock", multi);
        System.out.println(compound);
        NBTTagCompound tankTag = new NBTTagCompound();
        tank.writeToNBT(tankTag);
        compound.setTag("tank", tankTag);
    }

    // we send all our info to the client on load
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());

    }

}
