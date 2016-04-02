package getfluxed.fluxedcrystals.api.multiblock;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jared on 3/31/2016.
 */
public class MultiBlock{

    private BlockPos master;
    private List<BlockPos> bottomLayer;
    private List<BlockPos> topLayer;
    private List<BlockPos> airBlocks;
    private List<BlockPos> sides;
    private boolean active;

    public MultiBlock(BlockPos master, List<BlockPos> bottomLayer, List<BlockPos> topLayer, List<BlockPos> airBlocks, List<BlockPos> sides, boolean active){
        this.master = master;
        this.bottomLayer = bottomLayer;
        this.topLayer = topLayer;
        this.airBlocks = airBlocks;
        this.sides = sides;
        this.active = active;
    }

    public BlockPos getMaster(){
        return master;
    }

    public void setMaster(BlockPos master){
        this.master = master;
    }

    public List<BlockPos> getBottomLayer(){
        return bottomLayer;
    }

    public void setBottomLayer(List<BlockPos> bottomLayer){
        this.bottomLayer = bottomLayer;
    }

    public List<BlockPos> getTopLayer(){
        return topLayer;
    }

    public void setTopLayer(List<BlockPos> topLayer){
        this.topLayer = topLayer;
    }

    public List<BlockPos> getAirBlocks(){
        return airBlocks;
    }

    public void setAirBlocks(List<BlockPos> airBlocks){
        this.airBlocks = airBlocks;
    }

    public List<BlockPos> getSides(){
        return sides;
    }

    public List<BlockPos> getAllBlocks(){
        List<BlockPos> pos = new ArrayList<>();
        pos.addAll(getAirBlocks());
        pos.addAll(getBottomLayer());
        pos.addAll(getSides());
        pos.addAll(getTopLayer());
        return pos;
    }

    public void setSides(List<BlockPos> sides){
        this.sides = sides;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    @Override
    public String toString(){
        return "MultiBlock{" +
                "master=" + master +
                ", bottomLayer=" + bottomLayer +
                ", topLayer=" + topLayer +
                ", airBlocks=" + airBlocks +
                ", sides=" + sides +
                ", active=" + active +
                '}';
    }
}
