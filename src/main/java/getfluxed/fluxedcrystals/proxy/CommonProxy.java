package getfluxed.fluxedcrystals.proxy;

import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.client.gui.GUIHandler;
import getfluxed.fluxedcrystals.data.GeneratorData;
import getfluxed.fluxedcrystals.data.RecipeData;
import getfluxed.fluxedcrystals.events.CommonEventHandler;
import net.minecraft.entity.player.EntityPlayer;

import static getfluxed.fluxedcrystals.config.Config.registerJsons;

/**
 * Created by Jared on 3/23/2016.
 */
public class CommonProxy {

    public void registerRenderersPre(){

    }
    public void registerRenderers() {

    }

    public void registerEvents() {
        new CommonEventHandler();
    }

    public void registerGuis() {
        new GUIHandler();
    }

    public void registerOthers() {
        registerJsons();
        GeneratorData.init();
        RecipeData.init();
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public void updateColour(Crystal c) {
    }
}
