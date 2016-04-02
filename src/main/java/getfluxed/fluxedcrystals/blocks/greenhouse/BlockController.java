package getfluxed.fluxedcrystals.blocks.greenhouse;

import com.google.common.collect.Sets;
import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.blocks.base.FCBlock;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityController;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Jared on 3/19/2016.
 */
public class BlockController extends FCBlock implements ITileEntityProvider{

    private Set<Entity> spawnedEntities;

    public BlockController(){
        this.spawnedEntities = Sets.newHashSet();
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
        long time = System.currentTimeMillis();
        FluxedCrystals.logger.log(Level.INFO, "Block Clicked");
        TileEntityController tile = (TileEntityController) worldIn.getTileEntity(pos);

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
            while(worldIn.getBlockState(pos.offset(fac, ++count)).getBlock() instanceof BlockSoil){
            }
            count--;
            switch(fac){
                case NORTH:
                    if(northSize == 0){
                        northSize = count;
                    }
                    if(northSize > count){
                        northSize = count;
                    }
                    break;
                case SOUTH:
                    if(southSize == 0){
                        southSize = count;
                    }
                    if(southSize > count){
                        southSize = count;
                    }
                    break;
                case EAST:
                    if(eastSize == 0){
                        eastSize = count;
                    }
                    if(eastSize > count){
                        eastSize = count;
                    }
                    break;
                case WEST:
                    if(westSize == 0){
                        westSize = count;
                    }
                    if(westSize > count){
                        westSize = count;
                    }
                    break;
            }


        }
        northEast = northEast.offset(EnumFacing.EAST, eastSize + 1).offset(EnumFacing.NORTH, northSize + 1);
        southWest = southWest.offset(EnumFacing.SOUTH, southSize + 1).offset(EnumFacing.WEST, westSize + 1);

        boolean completeStructure = true;
        int ySize = 0;

        //Checks air inside
        for(BlockPos bp : BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1))){
            if(worldIn.getBlockState(bp).getBlock() instanceof BlockSoil || worldIn.getBlockState(bp).getBlock() instanceof BlockController){
                bottomLayer.add(bp);
            }else{
                completeStructure = false;
            }


            //Counts the height of the structure (excluding base platform)
            int y = 1;
            while((worldIn.isAirBlock(bp.offset(EnumFacing.UP, y)) || y == 0) && y < 256){
                y++;
            }
            if(y == 1 || y == 256 || y < ySize){
                completeStructure = false;
                y = 0;
            }
            ySize = y;

        }
        for(int y = 1; y < ySize; y++){
            if(!checkAirLayer(worldIn, northEast.offset(EnumFacing.UP, y), southWest.offset(EnumFacing.UP, y))){
                completeStructure = false;
            }
        }
        southWest = southWest.offset(EnumFacing.UP, ySize);
        BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1).offset(EnumFacing.DOWN, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1).offset(EnumFacing.UP, 1)).forEach(inner::add);
        AxisAlignedBB bound = new AxisAlignedBB(northEast.offset(EnumFacing.UP, ySize), southWest);
        for(BlockPos bp : BlockPos.getAllInBox(northEast.offset(EnumFacing.UP, ySize), southWest)){
            if(!inner.contains(bp)){
                topLayer.add(bp);
            }else{
                System.out.println(bp);
            }


        }

        //Gets all the airblocks
        BlockPos.getAllInBox(northEast.offset(EnumFacing.WEST, 1).offset(EnumFacing.SOUTH, 1).offset(EnumFacing.UP, 1), southWest.offset(EnumFacing.EAST, 1).offset(EnumFacing.NORTH, 1).offset(EnumFacing.DOWN, 1)).forEach(airPos::add);

        //Checks for frames around
        for(BlockPos bp : BlockPos.getAllInBox(northEast, southWest)){
            if(!inner.contains(bp)){
                sides.add(bp);
            }
        }

        if(completeStructure){
            MultiBlock multiBlock = new MultiBlock(pos, bottomLayer, topLayer, airPos, sides, true);
            tile.setMultiBlock(multiBlock);
            tile.onLoad();
            System.out.println("passed");
        }else{
            System.out.println("failed");
        }
        time = (System.currentTimeMillis() - time);
        AxisAlignedBB multiblock = new AxisAlignedBB(northEast, southWest);
        FluxedCrystals.logger.log(Level.INFO, String.format("Completed a %sx%sx%s structure in: %s ms", (int) multiblock.maxX - (int) multiblock.minX + 1, (int) multiblock.maxY - (int) multiblock.minY + 1, (int) multiblock.maxZ - (int) multiblock.minZ + 1, time));
        return true;
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
    public TileEntity createNewTileEntity(World worldIn, int meta){
        return new TileEntityController();
    }
}
