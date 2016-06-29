package getfluxed.fluxedcrystals.proxy;

import getfluxed.fluxedcrystals.events.CommonEventHandler;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Jared on 3/23/2016.
 */
public class CommonProxy {
    public void registerRenderers() {

    }

    public void registerEvents() {
        new CommonEventHandler();
    }

    public void registerGuis() {

    }

    public EntityPlayer getClientPlayer() {
        return null;
    }
}
