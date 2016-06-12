package getfluxed.fluxedcrystals.events;

import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.api.crystals.Crystal;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Jared on 5/2/2016.
 */
public class ClientEventHandler {

    public ClientEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void stitch(TextureStitchEvent e) {
        for (Crystal c : CrystalRegistry.getCrystalMap().values()) {
            c.updateColour();
        }
    }

}
