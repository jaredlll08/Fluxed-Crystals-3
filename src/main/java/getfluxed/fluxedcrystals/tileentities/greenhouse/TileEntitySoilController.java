package getfluxed.fluxedcrystals.tileentities.greenhouse;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.api.multiblock.IFrame;
import getfluxed.fluxedcrystals.api.multiblock.IGreenHouseComponent;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.blocks.greenhouse.BlockSoilController;
import getfluxed.fluxedcrystals.blocks.greenhouse.soil.BlockSoil;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageControllerSync;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jared on 3/19/2016.
 */
public class TileEntitySoilController extends TileEntity implements ITickable, IGreenHouseComponent{

    private MultiBlock multiBlock;

    private int tick;

    public TileEntitySoilController(){
        multiBlock = new MultiBlock(getPos());
    }


    @Override
    public void onLoad(){
        super.onLoad();
        boolean active = true;
        if(getMultiBlock().isActive()){
            for(BlockPos bp : getMultiBlock().getAirBlocks()){
                if(!worldObj.isAirBlock(bp)){
                    active = false;
                }else{
                    //                    worldObj.setBlockState(bp, Blocks.glass.getDefaultState());
                    IGreenHouseComponent tile = (IGreenHouseComponent) worldObj.getTileEntity(bp);
                    tile.setMaster(pos);
                }
            }
            for(BlockPos bp : getMultiBlock().getSides()){
                if(!worldObj.isAirBlock(bp)){
                    if(!(worldObj.getTileEntity(bp) instanceof TileEntityMultiBlockComponent)){
                        active = false;
                    }else{
                        //                        worldObj.setBlockState(bp, Blocks.stained_glass.getStateFromMeta(6));
                        IGreenHouseComponent tile = (IGreenHouseComponent) worldObj.getTileEntity(bp);
                        tile.setMaster(pos);
                    }
                }
            }
            for(BlockPos bp : getMultiBlock().getBottomLayer()){
                if(!bp.equals(multiBlock.getMaster()))
                    if(!worldObj.isAirBlock(bp)){
                        if(!(worldObj.getTileEntity(bp) instanceof TileEntityMultiBlockComponent)){

                            active = false;
                        }else{
                            //                            worldObj.setBlockState(bp, Blocks.dirt.getDefaultState());
                            IGreenHouseComponent tile = (IGreenHouseComponent) worldObj.getTileEntity(bp);
                            tile.setMaster(pos);
                        }
                    }
            }
            for(BlockPos bp : getMultiBlock().getTopLayer()){
                if(!worldObj.isAirBlock(bp)){
                    if(!(worldObj.getTileEntity(bp) instanceof TileEntityMultiBlockComponent)){
                        active = false;
                    }else{
                        IGreenHouseComponent tile = (IGreenHouseComponent) worldObj.getTileEntity(bp);
                        tile.setMaster(pos);
                        //                        worldObj.setBlockState(bp, Blocks.clay.getDefaultState());
                    }
                }
            }
            if(!worldObj.isRemote)
                PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));

            getMultiBlock().setActive(active);
        }
    }

    @Override
    public void update(){
        if(tick % 40 == 0){
            if(!getMultiBlock().isActive()){
                checkMultiblock();
            }
        }

        if(getMultiBlock() != null && getMultiBlock().getAirBlocks() != null)
            for(BlockPos bp : getMultiBlock().getAirBlocks()){
                if(worldObj.isRemote){
                    EntitySmallFireball ball = new EntitySmallFireball(getWorld(), bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5, 0, 0, 0);
                    worldObj.spawnEntityInWorld(ball);
                    ball.setDead();
                }
            }
        tick = (tick + 1);
    }

    public void setMultiBlock(MultiBlock multiBlock){
        this.multiBlock = multiBlock;
    }

    @Override
    public boolean isMaster(){
        return multiBlock.getMaster().equals(getPos());
    }

    @Override
    public BlockPos getMaster(){
        return multiBlock.getMaster();
    }


    @Override
    public MultiBlock getMultiBlock(){
        return multiBlock;
    }

    @Override
    public void setMaster(BlockPos pos){
        if(!isMaster()){
            multiBlock.setMaster(pos);
        }
    }

    @Override
    public void breakBlock(){
        getMultiBlock().setActive(false);
    }

    public boolean checkMultiblock(){
        long time = System.currentTimeMillis();

        List<BlockPos> airPos = new ArrayList<>();
        List<BlockPos> bottomLayer = new ArrayList<>();
        List<BlockPos> topLayer = new ArrayList<>();
        List<BlockPos> sides = new ArrayList<>();
        List<BlockPos> inner = new ArrayList<>();


        int northSize = 0;
        int southSize = 0;
        int eastSize = 0;
        int westSize = 0;

        BlockPos southWest = pos;
        BlockPos northEast = pos;
        for(EnumFacing fac : EnumFacing.HORIZONTALS){
            int count = 0;
            while(getWorld().getBlockState(pos.offset(fac, ++count)).getBlock() instanceof BlockSoil){
            }
            count--;

            switch(fac){
                case NORTH:
                    if(northSize == 0 || northSize > count){
                        northSize = count;
                    }
                    break;
                case SOUTH:
                    if(southSize == 0 || southSize > count){
                        southSize = count;
                    }
                    break;
                case EAST:
                    if(eastSize == 0 || eastSize > count){
                        eastSize = count;
                    }
                    break;
                case WEST:
                    if(westSize == 0 || westSize > count){
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
        for(BlockPos bp : BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1))){
            if(getWorld().getBlockState(bp).getBlock() instanceof BlockSoil || getWorld().getBlockState(bp).getBlock() instanceof BlockSoilController){
                bottomLayer.add(bp);
            }else{
                completeStructure = false;
            }


            //Counts the height of the structure (excluding base platform)
            int y = 1;
            while((getWorld().isAirBlock(bp.offset(EnumFacing.UP, y)) || y == 0) && y < 256){
                y++;
            }
            if(y == 1 || y == 256 || y < ySize){
                completeStructure = false;
                y = 0;
            }
            ySize = y;

        }
        for(int y = 1; y < ySize; y++){
            if(!checkAirLayer(getWorld(), northEast.offset(EnumFacing.UP, y), southWest.offset(EnumFacing.UP, y))){
                completeStructure = false;
            }
        }
        southWest = southWest.offset(EnumFacing.UP, ySize);
        BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1).offset(EnumFacing.DOWN, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1).offset(EnumFacing.UP, 1)).forEach(inner::add);
        for(BlockPos bp : bottomLayer){
            topLayer.add(bp.offset(EnumFacing.UP, ySize));
        }

        //Gets all the airblocks
        BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1).offset(EnumFacing.UP, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1).offset(EnumFacing.DOWN, 1)).forEach(airPos::add);

        //Checks for frames around
        for(BlockPos bp : BlockPos.getAllInBox(northEast, southWest)){
            if(!inner.contains(bp)){
                sides.add(bp);
            }
        }

        //TODO check all arrays here
        for(BlockPos bp : sides){
            if(!(worldObj.getTileEntity(bp) instanceof IFrame)){
                completeStructure = false;
            }else{
                TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) worldObj.getTileEntity(bp);
                tile.setMaster(pos);
            }
        }
        for(BlockPos bp : bottomLayer){
            if(!(worldObj.getBlockState(bp).getBlock() instanceof BlockSoil) && !(worldObj.getBlockState(bp).getBlock() instanceof BlockSoilController)){
                completeStructure = false;
            }else{
                IGreenHouseComponent tile = (IGreenHouseComponent) worldObj.getTileEntity(bp);
                tile.setMaster(pos);
            }
        }
        for(BlockPos bp : topLayer){
            if(!(worldObj.getTileEntity(bp) instanceof IFrame)){
                completeStructure = false;
            }else{
                IGreenHouseComponent tile = (IGreenHouseComponent) worldObj.getTileEntity(bp);
                tile.setMaster(pos);
            }
        }
        for(BlockPos bp : airPos){
            if(!worldObj.isAirBlock(bp)){
                completeStructure = false;
            }else{
            }
        }


        if(completeStructure){
            MultiBlock multiBlock = new MultiBlock(pos, bottomLayer, topLayer, airPos, sides, true);
            setMultiBlock(multiBlock);
        }else{
        }
        if(!worldObj.isRemote)
            PacketHandler.INSTANCE.sendToAllAround(new MessageControllerSync(this), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
        time = (System.currentTimeMillis() - time);
        AxisAlignedBB multiblock = new AxisAlignedBB(northEast, southWest);
        FluxedCrystals.logger.log(Level.INFO, String.format("Completed a %sx%sx%s structure in: %s ms", (int) multiblock.maxX - (int) multiblock.minX + 1, (int) multiblock.maxY - (int) multiblock.minY + 1, (int) multiblock.maxZ - (int) multiblock.minZ + 1, time));

        return false;
    }

    public boolean checkAirLayer(World world, BlockPos northEast, BlockPos southWest){
        boolean passed = true;
        for(BlockPos bp : BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1))){
            if(!world.isAirBlock(bp)){
                passed = false;
            }
        }
        return passed;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        setMultiBlock(MultiBlock.readFromNBT(compound.getCompoundTag("multiblock")));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound){
        super.writeToNBT(compound);
        NBTTagCompound multi = new NBTTagCompound();
        MultiBlock.writeToNBT(multi, getMultiBlock());
        compound.setTag("multiblock", multi);
    }
}
