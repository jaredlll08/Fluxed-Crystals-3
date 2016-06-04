package getfluxed.fluxedcrystals.events;

import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Jared on 5/30/2016.
 */
public class CommonEventHandler {
    public CommonEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

}
