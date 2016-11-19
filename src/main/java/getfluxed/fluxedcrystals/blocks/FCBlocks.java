package getfluxed.fluxedcrystals.blocks;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.blocks.generators.BlockCoalGenerator;
import getfluxed.fluxedcrystals.blocks.generators.BlockTrashGenerator;
import getfluxed.fluxedcrystals.blocks.glassjar.BlockBoilingWater;
import getfluxed.fluxedcrystals.blocks.glassjar.BlockBurner;
import getfluxed.fluxedcrystals.blocks.glassjar.BlockController;
import getfluxed.fluxedcrystals.blocks.glassjar.FluidBoilingWater;
import getfluxed.fluxedcrystals.blocks.greenhouse.BlockSoilController;
import getfluxed.fluxedcrystals.blocks.greenhouse.frame.BlockFrame;
import getfluxed.fluxedcrystals.blocks.greenhouse.frame.BlockFrameBattery;
import getfluxed.fluxedcrystals.blocks.greenhouse.frame.base.BlockBaseFrame;
import getfluxed.fluxedcrystals.blocks.greenhouse.io.BlockCrystalIO;
import getfluxed.fluxedcrystals.blocks.greenhouse.io.BlockFluidIO;
import getfluxed.fluxedcrystals.blocks.greenhouse.io.BlockPowerIO;
import getfluxed.fluxedcrystals.blocks.greenhouse.monitor.BlockPowerMonitor;
import getfluxed.fluxedcrystals.blocks.machines.BlockCrusher;
import getfluxed.fluxedcrystals.blocks.machines.BlockFluxfurnace;
import getfluxed.fluxedcrystals.blocks.machines.BlockSawmill;
import getfluxed.fluxedcrystals.blocks.misc.BlockCrystalCube;
import getfluxed.fluxedcrystals.blocks.misc.BlockPyrex;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityCoalGenerator;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityTrashGenerator;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityCrystalIO;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityFluidIO;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityPowerIO;
import getfluxed.fluxedcrystals.tileentities.greenhouse.monitor.TileEntityPowerMonitor;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineCrusher;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineFurnace;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineSawmill;
import getfluxed.fluxedcrystals.tileentities.misc.TileEntityCrystalCube;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static getfluxed.fluxedcrystals.reference.Reference.tab;

/**
 * Created by Jared on 3/17/2016.
 */
@SuppressWarnings("WeakerAccess")
public class FCBlocks {

    public static Map<String, Block> renderMap = new HashMap<>();

    public static Block ghController = new BlockSoilController();
    public static Block ghFrame = new BlockFrame();
    public static Block ghFrameGlass = new BlockFrame();
    public static Block ghBaseFrame = new BlockBaseFrame();
    public static Block ghFluidIO = new BlockFluidIO();
    public static Block ghPowerIO = new BlockPowerIO();
    public static Block ghCrystalIO = new BlockCrystalIO();
    public static Block ghBatteryBasic = new BlockFrameBattery(16000);
    public static Block ghBatteryAdvanced = new BlockFrameBattery(32000);
    public static Block ghPowerMonitor = new BlockPowerMonitor();

    public static Block basicCoalGenerator = new BlockCoalGenerator();
    public static Block trashGenerator = new BlockTrashGenerator();

    public static Block machineCrusher = new BlockCrusher();
    public static Block machineFurnace = new BlockFluxfurnace();
    public static Block machineSawmill = new BlockSawmill();

    public static Block crystalCube = new BlockCrystalCube();

    public static Block pyrex = new BlockPyrex();
    public static Block controller = new BlockController();
    public static Block burner = new BlockBurner();


    public static FluidBoilingWater fluidBoilingWater = new FluidBoilingWater();
    public static BlockBoilingWater blockBoilingWater;


    public static void preInit() {
        registerBlock(ghController, "ghSoilController", TileEntitySoilController.class);
        registerBlockMultiblock(ghFrameGlass, "ghFrameGlass");
        registerBlockMultiblock(ghFrame, "ghFrame");
        registerBlock(ghBaseFrame, "ghBaseFrame");
        registerBlock(ghFluidIO, "ghFluidIO", TileEntityFluidIO.class);
        registerBlock(ghPowerIO, "ghPowerIO", TileEntityPowerIO.class);
        registerBlock(ghCrystalIO, "ghCrystalIO", TileEntityCrystalIO.class);
        registerBlockMultiblock(ghBatteryBasic, "ghBatteryBasic");
        registerBlockMultiblock(ghBatteryAdvanced, "ghBatteryAdvanced");
        registerBlock(ghPowerMonitor, "ghPowerMonitor", TileEntityPowerMonitor.class);
        registerBlock(crystalCube, "crystalCube", TileEntityCrystalCube.class);

        registerBlock(pyrex, "pyrex");
        registerBlock(burner, "burner");
        registerBlock(controller, "controller");

        FluidRegistry.registerFluid(fluidBoilingWater);
        FluidRegistry.addBucketForFluid(fluidBoilingWater);
        blockBoilingWater = new BlockBoilingWater();
        registerBlock(blockBoilingWater, "boiling_water");


        registerGenerators();
        registerMachines();
    }

    public static void registerGenerators() {
        registerBlock(basicCoalGenerator, "coalGenBasic", TileEntityCoalGenerator.class);
        registerBlock(trashGenerator, "trashGenerator", TileEntityTrashGenerator.class);

    }

    public static void registerMachines() {
        registerBlock(machineCrusher, "machineCrusher", TileEntityMachineCrusher.class);
        registerBlock(machineFurnace, "machineFurnace", TileEntityMachineFurnace.class);
        registerBlock(machineSawmill, "machineSawmill", TileEntityMachineSawmill.class);


    }

    public static void init() {

    }

    public static void postInit() {
    }


    private static void registerBlock(Block block, String key) {
        registerBlock(block, key, key, null, tab);
    }

    private static void registerBlock(Block block, String key, String texture) {
        registerBlock(block, key, texture, null, tab);
    }

    private static void registerBlock(Block block, String key, String texture, Class tile) {
        registerBlock(block, key, texture, tile, tab);
    }

    private static void registerBlockMultiblock(Block block, String key) {
        registerBlock(block, key, key, TileEntityMultiBlockComponent.class, tab);
    }

    private static void registerBlock(Block block, String key, Class tile) {
        registerBlock(block, key, key, tile, tab);
    }

    private static void registerBlock(Block block, String key, Class tile, CreativeTabs tab) {
        registerBlock(block, key, key, tile, tab);
    }

    private static void registerBlock(Block block, String key, String texture, Class tile, CreativeTabs tab) {
        block.setUnlocalizedName(key).setCreativeTab(tab);
        if (FluxedCrystals.isDevEnv && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            writeFile(key, texture);
        renderMap.put(texture, block);
        GameRegistry.register(block, new ResourceLocation(Reference.MODID + ":" + key));
        GameRegistry.register(new ItemBlock(block), new ResourceLocation(Reference.MODID + ":" + key));
        if (tile != null) {
            GameRegistry.registerTileEntity(tile, key);
        }
    }

    public static void writeFile(String key, String texture) {
        try {
            File baseBlockState = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            File baseBlockModel = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            File baseItem = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            if (System.getProperty("user.home").endsWith("Jared")) {

                baseBlockState = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/blockstates/" + key + ".json");
                baseBlockModel = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/block/" + key + ".json");
                baseItem = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/item/" + key + ".json");
            }
            if (!baseBlockState.exists()) {
                baseBlockState.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockState.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
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
