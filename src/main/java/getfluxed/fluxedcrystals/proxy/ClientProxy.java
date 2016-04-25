package getfluxed.fluxedcrystals.proxy;

import getfluxed.fluxedcrystals.client.greenhouse.RenderController;
import getfluxed.fluxedcrystals.client.greenhouse.monitor.RenderPowerMonitor;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.monitor.TileEntityPowerMonitor;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Jared on 3/23/2016.
 */
public class ClientProxy implements IProxy {
    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerMonitor.class, new RenderPowerMonitor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoilController.class, new RenderController());
    }
}
