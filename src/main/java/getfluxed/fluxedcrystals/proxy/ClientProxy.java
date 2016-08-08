package getfluxed.fluxedcrystals.proxy;

import getfluxed.fluxedcrystals.client.greenhouse.ModelCube;
import getfluxed.fluxedcrystals.client.greenhouse.RenderController;
import getfluxed.fluxedcrystals.client.greenhouse.monitor.RenderPowerMonitor;
import getfluxed.fluxedcrystals.client.gui.GUIHandler;
import getfluxed.fluxedcrystals.events.ClientEventHandler;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.monitor.TileEntityPowerMonitor;
import getfluxed.fluxedcrystals.util.model.OBJColourLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Jared on 3/23/2016.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        super.registerRenderers();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerMonitor.class, new RenderPowerMonitor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoilController.class, new RenderController(new ModelCube()));
//        OBJLoader.INSTANCE.addDomain(Reference.modid);
        ModelLoaderRegistry.registerLoader(OBJColourLoader.INSTANCE);
        OBJColourLoader.INSTANCE.addDomain(Reference.modid);
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        new ClientEventHandler();
    }

    @Override
    public void registerGuis() {
        super.registerGuis();
        new GUIHandler();
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
