package getfluxed.fluxedcrystals;

import getfluxed.fluxedcrystals.blocks.FCBlocks;
import getfluxed.fluxedcrystals.config.Config;
import getfluxed.fluxedcrystals.items.FCItems;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.proxy.CommonProxy;
import getfluxed.fluxedcrystals.reference.Reference;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version, dependencies = Reference.dependencies)
public class FluxedCrystals {

    public static final Logger logger = LogManager.getLogger(Reference.modid);
    public static long totalTime = 0;
    public static boolean isDevEnv = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    @SidedProxy(clientSide = "getfluxed.fluxedcrystals.proxy.ClientProxy", serverSide = "getfluxed.fluxedcrystals.proxy.CommonProxy")
    public static CommonProxy proxy;


    @Mod.Instance
    public static FluxedCrystals instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger.log(Level.INFO, "Starting PreInit");
        long time = System.currentTimeMillis();
        Reference.configDirectory = new File(e.getSuggestedConfigurationFile().getParent(), "/" + Reference.modid + "/");
        Config.load();
        proxy.registerRenderersPre();
        FCBlocks.preInit();
        FCItems.preInit();

        PacketHandler.preInit();
        proxy.registerEvents();
        time = (System.currentTimeMillis() - time);
        totalTime += time;
        logger.log(Level.INFO, "Completed PreInit in: " + time + "ms");
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        logger.log(Level.INFO, "Starting Init");
        long time = System.currentTimeMillis();
        FCBlocks.init();
        FCItems.init();
        proxy.registerRenderers();
        proxy.registerGuis();
        time = (System.currentTimeMillis() - time);
        totalTime += time;
        logger.log(Level.INFO, "Completed Init in: " + time + "ms");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        logger.log(Level.INFO, "Starting PostInit");
        long time = System.currentTimeMillis();
        proxy.registerOthers();
        time = (System.currentTimeMillis() - time);
        totalTime += time;
        logger.log(Level.INFO, "Completed PostInit in: " + time + "ms");

    }


    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent e) {
        logger.log(Level.INFO, "Starting LoadComplete");
        long time = System.currentTimeMillis();
        time = (System.currentTimeMillis() - time);
        totalTime += time;
        logger.log(Level.INFO, "Completed LoadComplete in: " + time + "ms");
        logger.log(Level.INFO, "Fluxed-Crystals 3 loaded in: " + totalTime + "ms");
    }

}
