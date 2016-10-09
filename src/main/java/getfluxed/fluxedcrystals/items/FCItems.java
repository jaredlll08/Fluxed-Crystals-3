package getfluxed.fluxedcrystals.items;

import getfluxed.fluxedcrystals.items.crystal.ItemCrushedCrystal;
import getfluxed.fluxedcrystals.items.crystal.ItemCrystalDust;
import getfluxed.fluxedcrystals.items.crystal.ItemCrystalSolution;
import getfluxed.fluxedcrystals.items.crystal.ItemCrystalSolutionShell;
import getfluxed.fluxedcrystals.reference.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static getfluxed.fluxedcrystals.FluxedCrystals.isDevEnv;
import static getfluxed.fluxedcrystals.reference.Reference.tab;

/**
 * Created by Jared on 3/17/2016.
 */
public class FCItems {

    public static Map<String, Item> renderMap = new HashMap<String, Item>();
    public static Map<Item, int[]> colourMap = new HashMap<>();


    public static Item crystalDust = new ItemCrystalDust();
    public static Item crystalSolution = new ItemCrystalSolution();
    public static Item crystalSolutionShell = new ItemCrystalSolutionShell();
    public static Item crystalCrushed = new ItemCrushedCrystal();


    public static void preInit() {
        registerItem(crystalSolutionShell, "crystalSolutionShell", "crystalSolutionShell");
        registerItemColour(crystalDust, "crystalDust", "crystalDust", new int[]{0});
        registerItemColour(crystalSolution, "crystalSolution", "crystalSolution", new int[]{1});
        registerItemColour(crystalCrushed, "crystalCrushedShardRough", "crystalCrushedShardRough", new String[]{"crystalCrushedShardRough", "crystalCrushedShardSmooth", "crystalCrushedChunkRough", "crystalCrushedChunkSmooth" }, new int[]{0});


    }

    public static void init() {

    }

    public static void registerItem(Item item, String name, String key) {
        if (isDevEnv && FMLCommonHandler.instance().getEffectiveSide()== Side.CLIENT)
            writeFile(key, key);
        item.setUnlocalizedName(key).setCreativeTab(tab);
        renderMap.put(key, item);

        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }

    public static void registerItemColour(Item item, String name, String key, int[] layers) {
        if (isDevEnv && FMLCommonHandler.instance().getEffectiveSide()== Side.CLIENT)
            writeFile(key, key);
        item.setUnlocalizedName(key).setCreativeTab(tab);
        renderMap.put(key, item);
        colourMap.put(item, layers);
        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }

    public static void registerItemColour(Item item, String name, String key, String textures[], int[] layers) {
        if (isDevEnv && FMLCommonHandler.instance().getEffectiveSide()== Side.CLIENT) {
            for (String tex : textures) {
                writeFile(key, tex);
            }
        }

        item.setUnlocalizedName(key).setCreativeTab(tab);
        renderMap.put(key, item);
        colourMap.put(item, layers);
        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }

    public static void registerItemMeta(Item item, String name, String key) {
        if (isDevEnv && FMLCommonHandler.instance().getEffectiveSide()== Side.CLIENT)
            writeFile(key, key);
        item.setCreativeTab(tab);
        renderMap.put(key, item);

        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }

    public static void registerItem(Item item, String name, String key, String texture) {
        if (isDevEnv && FMLCommonHandler.instance().getEffectiveSide()== Side.CLIENT)
            writeFile(key, texture);
        item.setUnlocalizedName(key).setCreativeTab(tab);

        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }

    public static void writeFile(String key, String texture) {
        try {
            File f = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            if (System.getProperty("user.home").endsWith("Jared")) {
                f = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/item/" + key + ".json");
            }
            if (!f.exists()) {
                f.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseItem.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<>();
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if (line.contains("%modid%")) {
                        line = line.replace("%modid%", Reference.MODID);
                    }
                    if (line.contains("%key%")) {
                        line = line.replace("%key%", key);
                    }
                    if (line.contains("%texture%")) {
                        line = line.replace("%texture%", texture);
                    }
                    content.add(line);
                }
                scan.close();
                FileWriter write = new FileWriter(f);
                for (String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
