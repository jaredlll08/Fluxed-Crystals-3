package getfluxed.fluxedcrystals.blocks.greenhouse.powerframes;

/**
 * Created by Jared on 4/17/2016.
 */
public class BlockFrameBattery extends BlockFrame {
    private int capacity;

    public BlockFrameBattery(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

}
