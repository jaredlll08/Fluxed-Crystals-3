package getfluxed.fluxedcrystals.tileentities.glassjar;

import getfluxed.fluxedcrystals.api.glassjar.GlassJar;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * Created by Jared.
 */
public class TileEntityController extends TileEntity implements ITickable {

    private GlassJar glassJar;

    @Override
    public void update() {

    }

    public GlassJar getGlassJar() {
        return glassJar;
    }

    public void setGlassJar(GlassJar glassJar) {
        this.glassJar = glassJar;
    }
}
