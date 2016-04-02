package getfluxed.fluxedcrystals.tileentities.greenhouse;

import getfluxed.fluxedcrystals.api.multiblock.IFrame;
import getfluxed.fluxedcrystals.api.multiblock.IGreenHouseComponent;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.blocks.greenhouse.BlockSoil;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Jared on 3/19/2016.
 */
public class TileEntityController extends TileEntity implements ITickable, IGreenHouseComponent{

    private MultiBlock multiBlock;


    @Override
    public void onLoad(){
        super.onLoad();
        boolean active = true;
        if(getMultiBlock() != null){
            for(BlockPos bp : getMultiBlock().getAirBlocks()){
                if(!worldObj.isAirBlock(bp)){
                    System.out.println("air false" + bp);

                    active = false;
                }else{
                    worldObj.setBlockState(bp, Blocks.glass.getDefaultState());
                }
            }
            for(BlockPos bp : getMultiBlock().getSides()){
                if(!worldObj.isAirBlock(bp)){
                    if(!(worldObj.getTileEntity(bp) instanceof IFrame)){
                        System.out.println("sides false " + bp);

                        active = false;
                    }else{
                        worldObj.setBlockState(bp, Blocks.stained_glass.getStateFromMeta(6));
                    }
                }
            }
            for(BlockPos bp : getMultiBlock().getBottomLayer()){
                if(!bp.equals(multiBlock.getMaster()))
                    if(!worldObj.isAirBlock(bp)){
                        if(!(worldObj.getBlockState(bp).getBlock() instanceof BlockSoil)){
                            System.out.println("bottom false " + bp);

                            active = false;
                        }else{
                            worldObj.setBlockState(bp, Blocks.dirt.getDefaultState());
                        }
                    }
            }

            for(BlockPos bp : getMultiBlock().getTopLayer()){
                if(!worldObj.isAirBlock(bp)){
                    if(!(worldObj.getBlockState(bp).getBlock() instanceof BlockSoil)){
                        System.out.println("top false " + bp);
                        active = false;
                    }else{
                        worldObj.setBlockState(bp, Blocks.clay.getDefaultState());
                    }
                }
            }
            getMultiBlock().setActive(active);
        }
    }

    @Override
    public void update(){

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
}
