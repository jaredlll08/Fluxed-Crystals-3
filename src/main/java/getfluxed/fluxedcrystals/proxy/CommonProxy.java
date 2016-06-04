package getfluxed.fluxedcrystals.proxy;

import getfluxed.fluxedcrystals.events.CommonEventHandler;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Jared on 3/23/2016.
 */
public class CommonProxy implements IProxy {
    @Override
    public void registerRenderers() {

    }

    @Override
    public void registerEvents() {
        new CommonEventHandler();
    }

    @Override
    public void registerGuis() {

    }

    @Override
    public EntityPlayer getClientPlayer() {
        return null;
    }
}
