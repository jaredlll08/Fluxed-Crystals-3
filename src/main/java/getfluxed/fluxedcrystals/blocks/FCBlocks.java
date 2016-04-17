package getfluxed.fluxedcrystals.blocks;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.blocks.greenhouse.BlockSoilController;
import getfluxed.fluxedcrystals.blocks.greenhouse.io.BlockFluidIO;
import getfluxed.fluxedcrystals.blocks.greenhouse.io.BlockPowerIO;
import getfluxed.fluxedcrystals.blocks.greenhouse.powerframes.BlockFrame;
import getfluxed.fluxedcrystals.blocks.greenhouse.powerframes.BlockFrameBattery;
import getfluxed.fluxedcrystals.blocks.greenhouse.soil.BlockSoil;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityFluidIO;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityPowerIO;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by Jared on 3/17/2016.
 */
public class FCBlocks {

    public static Map<String, Block> renderMap = new HashMap<>();

    public static Block ghController = new BlockSoilController();
    public static Block ghFrame = new BlockFrame();
    public static Block ghFrameGlass = new BlockFrame();
    public static Block ghSoil = new BlockSoil();
    public static Block ghFluidIO = new BlockFluidIO();
    public static Block ghPowerIO = new BlockPowerIO();


    public static Block ghBatteryBasic = new BlockFrameBattery(16000);

    public static void preInit() {
        registerBlock(ghController, "ghSoilController", TileEntitySoilController.class);
        registerBlock(ghFrameGlass, "ghFrameGlass", TileEntityMultiBlockComponent.class);
        registerBlock(ghFrame, "ghFrame", TileEntityMultiBlockComponent.class);
        registerBlock(ghSoil, "ghSoil");
        registerBlock(ghFluidIO, "ghFluidIO", TileEntityFluidIO.class);
        registerBlock(ghPowerIO, "ghPowerIO", TileEntityPowerIO.class);
        registerBlock(ghBatteryBasic, "ghBatteryBasic", TileEntityMultiBlockComponent.class);
    }


    public static void init() {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        for (Map.Entry<String, Block> ent : renderMap.entrySet()) {
            renderItem.getItemModelMesher().register(Item.getItemFromBlock(ent.getValue()), 0, new ModelResourceLocation(Reference.modid + ":" + ent.getKey(), "inventory"));
        }
    }


    public static void postInit() {
    }


    private static void registerBlock(Block block, String key) {
        registerBlock(block, key, key, null, FluxedCrystals.tab);
    }

    private static void registerBlock(Block block, String key, String texture) {
        registerBlock(block, key, texture, null, FluxedCrystals.tab);
    }

    private static void registerBlock(Block block, String key, String texture, Class tile) {
        registerBlock(block, key, texture, tile, FluxedCrystals.tab);
    }

    private static void registerBlock(Block block, String key, Class tile) {
        registerBlock(block, key, key, tile, FluxedCrystals.tab);
    }

    private static void registerBlock(Block block, String key, Class tile, CreativeTabs tab) {
        registerBlock(block, key, key, tile, tab);
    }


    private static void registerBlock(Block block, String key, String texture, Class tile, CreativeTabs tab) {
        block.setUnlocalizedName(key).setCreativeTab(tab);
        if (FluxedCrystals.isDevEnv)
            writeFile(key, texture);
        renderMap.put(texture, block);
        GameRegistry.registerBlock(block, key);
        if (tile != null)
            GameRegistry.registerTileEntity(tile, key);
    }

    public static void writeFile(String key, String texture) {
        try {
            File baseBlockState = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            File baseBlockModel = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            File baseItem = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");

            if (System.getProperty("user.home").endsWith("Jared")) {
                baseBlockState = new File(System.getProperty("user.home") + "/Documents/Github/Fluxed-Crystals-3/src/main/resources/assets/fluxedcrystals/blockstates/" + key + ".json");
                baseBlockModel = new File(System.getProperty("user.home") + "/Documents/Github/Fluxed-Crystals-3/src/main/resources/assets/fluxedcrystals/models/block/" + key + ".json");
                baseItem = new File(System.getProperty("user.home") + "/Documents/Github/Fluxed-Crystals-3/src/main/resources/assets/fluxedcrystals/models/item/" + key + ".json");
            }
            if (!baseBlockState.exists()) {
                baseBlockState.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockState.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
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
                FileWriter write = new FileWriter(baseBlockState);
                for (String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
            if (!baseBlockModel.exists()) {
                baseBlockModel.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockModel.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
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
                FileWriter write = new FileWriter(baseBlockModel);
                for (String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }

            if (!baseItem.exists()) {
                baseItem.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockItem.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
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
                FileWriter write = new FileWriter(baseItem);
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
