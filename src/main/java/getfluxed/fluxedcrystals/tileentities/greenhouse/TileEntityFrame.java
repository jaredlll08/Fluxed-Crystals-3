package getfluxed.fluxedcrystals.tileentities.greenhouse;

import getfluxed.fluxedcrystals.api.multiblock.IFrame;
import getfluxed.fluxedcrystals.api.multiblock.IGreenHouseComponent;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.api.nbt.TileEntityNBT;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Jared on 3/19/2016.
 */
public class TileEntityFrame extends TileEntityNBT implements IGreenHouseComponent, IFrame{
    private BlockPos masterPos;

    @Override
    public boolean isMaster(){
        return false;
    }

    @Override
    public BlockPos getMaster(){
        return masterPos;
    }

    @Override
    public MultiBlock getMultiBlock(){
        if(getMaster() != null){
            return ((IGreenHouseComponent) worldObj.getTileEntity(getMaster())).getMultiBlock();
        }
        return null;
    }

    @Override
    public void setMaster(BlockPos pos){
        if(!isMaster()){
            getMultiBlock().setMaster(pos);
        }
    }

    @Override
    public void breakBlock(){
        getMultiBlock().setActive(false);
    }


}
