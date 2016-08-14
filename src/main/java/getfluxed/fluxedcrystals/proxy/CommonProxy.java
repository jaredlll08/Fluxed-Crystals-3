package getfluxed.fluxedcrystals.proxy;

import getfluxed.fluxedcrystals.api.recipes.machines.RecipeSawmill;
import getfluxed.fluxedcrystals.api.registries.RecipeRegistry;
import getfluxed.fluxedcrystals.data.GeneratorData;
import getfluxed.fluxedcrystals.data.RecipeData;
import getfluxed.fluxedcrystals.events.CommonEventHandler;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

import static getfluxed.fluxedcrystals.config.Config.registerJsons;

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


    public void registerOthers(){
        registerJsons();
        GeneratorData.init();
        RecipeData.init();
        System.out.println("side");
        for (Map.Entry<String, RecipeSawmill> entry : RecipeRegistry.getAllSawmillRecipes().entrySet()) {
            System.out.println(entry);
        }
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }
}
