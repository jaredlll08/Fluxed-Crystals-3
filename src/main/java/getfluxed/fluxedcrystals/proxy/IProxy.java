package getfluxed.fluxedcrystals.proxy;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Jared on 3/23/2016.
 */
public interface IProxy {

        void registerRenderers();

        void registerEvents();

        void registerGuis();

        EntityPlayer getClientPlayer();
}
