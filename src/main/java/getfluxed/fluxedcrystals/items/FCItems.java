package getfluxed.fluxedcrystals.items;

import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.api.registries.crystal.Crystal;
import getfluxed.fluxedcrystals.items.crystal.ItemCrystalDust;
import getfluxed.fluxedcrystals.items.crystal.ItemCrystalSolution;
import getfluxed.fluxedcrystals.items.crystal.ItemCrystalSolutionShell;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.util.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static getfluxed.fluxedcrystals.FluxedCrystals.isDevEnv;
import static getfluxed.fluxedcrystals.FluxedCrystals.tab;

/**
 * Created by Jared on 3/17/2016.
 */
public class FCItems {

    public static Map<String, Item> renderMap = new HashMap<String, Item>();
    public static Map<Item, int[]> colourMap = new HashMap<>();


    public static Item crystalDust = new ItemCrystalDust();
    public static Item crystalSolution = new ItemCrystalSolution();
    public static Item crystalSolutionShell = new ItemCrystalSolutionShell();



    public static void preInit() {
        registerItem(crystalSolutionShell, "crystalSolutionShell", "crystalSolutionShell");
        registerItemColour(crystalDust, "crystalDust", "crystalDust", new int[]{0});
        registerItemColour(crystalSolution, "crystalSolution", "crystalSolution", new int[]{1});


    }

    public static void init() {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        for (Map.Entry<String, Item> ent : renderMap.entrySet()) {
            renderItem.getItemModelMesher().register(ent.getValue(), 0, new ModelResourceLocation(Reference.modid + ":" + ent.getKey(), "inventory"));
        }
        for (Map.Entry<Item, int[]> ent : colourMap.entrySet()) {
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
                Crystal c = CrystalRegistry.getCrystal(NBTHelper.getString(stack, "crystalName"));
                if(c ==null){
                    System.out.println(NBTHelper.getString(stack, "crystalName"));
                    return 0xFFFFFF;
                }
                for(int i : ent.getValue()) {
                    if (tintIndex == i)
                        if (c != null) {
                            return c.getColour();
                        }
                }
                return 0xFFFFFF;
            }, ent.getKey());
        }
    }

    public static void registerItem(Item item, String name, String key) {
        if (isDevEnv)
            writeFile(key, key);
        item.setUnlocalizedName(key).setCreativeTab(tab);
        renderMap.put(key, item);

        GameRegistry.register(item, new ResourceLocation(Reference.modid + ":" + key));
    }

    public static void registerItemColour(Item item, String name, String key, int[] layers) {
        if (isDevEnv)
            writeFile(key, key);
        item.setUnlocalizedName(key).setCreativeTab(tab);
        renderMap.put(key, item);
        colourMap.put(item, layers);
        GameRegistry.register(item, new ResourceLocation(Reference.modid + ":" + key));
    }

    public static void registerItemMeta(Item item, String name, String key) {
        if (isDevEnv)
            writeFile(key, key);
        item.setCreativeTab(tab);
        renderMap.put(key, item);

        GameRegistry.register(item, new ResourceLocation(Reference.modid + ":" + key));
    }

    public static void registerItem(Item item, String name, String key, String texture) {
        if (isDevEnv)
            writeFile(key, texture);
        item.setUnlocalizedName(key).setCreativeTab(tab);

        GameRegistry.register(item, new ResourceLocation(Reference.modid + ":" + key));
    }

    public static void writeFile(String key, String texture) {
        try {
            File f = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            if (System.getProperty("user.home").endsWith("Jared")) {
                f = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.modid + "/models/item/" + key + ".json");
            }
            if (!f.exists()) {
                f.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseItem.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<>();
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if (line.contains("%modid%")) {
                        line = line.replace("%modid%", Reference.modid);
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
