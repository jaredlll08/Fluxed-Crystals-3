package getfluxed.fluxedcrystals.config;

import getfluxed.fluxedcrystals.reference.Reference;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * Created by Jared on 3/23/2016.
 */
public class Config {



    public static void load(){
        Configuration configuration = new Configuration(Reference.configDirectory);
        configuration.load();

        configuration.save();
    }

}
