package getfluxed.fluxedcrystals.api.glassjar;

import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Jared.
 */
public class GlassJar {

    private BlockPos controller;
    private GlassJarSize size;
    private List<IBurner> burners;
    private int temperature;


    public GlassJar() {
    }

    public GlassJar(BlockPos controller, GlassJarSize size, List<IBurner> burners, int temperature) {
        this.controller = controller;
        this.size = size;
        this.burners = burners;
        this.temperature = temperature;
    }

    public BlockPos getController() {
        return controller;
    }

    public void setController(BlockPos controller) {
        this.controller = controller;
    }

    public GlassJarSize getSize() {
        return size;
    }

    public void setSize(GlassJarSize size) {
        this.size = size;
    }

    public List<IBurner> getBurners() {
        return burners;
    }

    public void setBurners(List<IBurner> burners) {
        this.burners = burners;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
