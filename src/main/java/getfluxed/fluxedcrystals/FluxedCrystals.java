package getfluxed.fluxedcrystals;

import getfluxed.fluxedcrystals.blocks.FCBlocks;
import getfluxed.fluxedcrystals.config.Config;
import getfluxed.fluxedcrystals.items.FCItems;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.proxy.IProxy;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.util.CreativeTabFC;
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

import static getfluxed.fluxedcrystals.config.Config.registerJsons;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version, dependencies = Reference.dependencies)
public class FluxedCrystals {


    public static final Logger logger = LogManager.getLogger(Reference.modid);
    public static long totalTime = 0;
    public static boolean isDevEnv = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    @SidedProxy(clientSide = "getfluxed.fluxedcrystals.proxy.ClientProxy", serverSide = "getfluxed.fluxedcrystals.proxy.ServerProxy")
    public static IProxy proxy;


    public static CreativeTabFC tab = new CreativeTabFC();

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger.log(Level.INFO, "Starting PreInit");
        long time = System.currentTimeMillis();
        Reference.configDirectory = new File(e.getSuggestedConfigurationFile(), "/" + Reference.modid + "/");
        Config.load();
        FCBlocks.preInit();
        FCItems.preInit();
        PacketHandler.preInit();
        proxy.registerRenderers();
        time = (System.currentTimeMillis() - time);
        totalTime += time;
        logger.log(Level.INFO, "Completed PreInit in: " + time + "ms");
//        CrystalRegistry.register(new Crystal("Test", new Resource(new ItemStack(Items.apple)), new Resource(new ItemStack(Items.arrow)), 0xFF0000, 32, 18, 16));
//        CrystalRegistry.register(new Crystal("Another", new Resource(new ItemStack(Items.apple)), new Resource(new ItemStack(Items.arrow)), 0x00FF00, 32, 18, 16));
//        CrystalRegistry.register(new Crystal("Boop", new Resource(new ItemStack(Items.apple)), new Resource(new ItemStack(Items.arrow)), 0x0000FF, 32, 18, 16));
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        logger.log(Level.INFO, "Starting Init");
        long time = System.currentTimeMillis();

        FCBlocks.init();
        FCItems.init();
        time = (System.currentTimeMillis() - time);
        totalTime += time;
        logger.log(Level.INFO, "Completed Init in: " + time + "ms");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        logger.log(Level.INFO, "Starting PostInit");
        long time = System.currentTimeMillis();
        time = (System.currentTimeMillis() - time);
        totalTime += time;
        logger.log(Level.INFO, "Completed PostInit in: " + time + "ms");

    }


    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent e) {
        logger.log(Level.INFO, "Starting LoadComplete");
        long time = System.currentTimeMillis();
        registerJsons();
        time = (System.currentTimeMillis() - time);
        totalTime += time;
        logger.log(Level.INFO, "Completed LoadComplete in: " + time + "ms");
        logger.log(Level.INFO, "Fluxed-Crystals 3 loaded in: " + totalTime + "ms");
    }

}
