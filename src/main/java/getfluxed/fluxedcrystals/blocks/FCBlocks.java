package getfluxed.fluxedcrystals.blocks;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.blocks.greenhouse.BlockSoilController;
import getfluxed.fluxedcrystals.blocks.greenhouse.io.BlockFluidIO;
import getfluxed.fluxedcrystals.blocks.greenhouse.powerframes.BlockFrame;
import getfluxed.fluxedcrystals.blocks.greenhouse.soil.BlockSoil;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityFluidIO;
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
public class FCBlocks{

    public static Map<String, Block> renderMap = new HashMap<>();

    public static Block ghController = new BlockSoilController();
    public static Block ghFrame = new BlockFrame();
    public static Block ghFrameGlass = new BlockFrame();
    public static Block ghSoil = new BlockSoil();
    public static Block ghFluidIO = new BlockFluidIO();

    public static void preInit(){
        registerBlock(ghController, "ghSoilController", TileEntitySoilController.class);
        registerBlock(ghFrameGlass, "ghFrameGlass", TileEntityMultiBlockComponent.class);
        registerBlock(ghFrame, "ghFrame", TileEntityMultiBlockComponent.class);
        registerBlock(ghSoil, "ghSoil");
        registerBlock(ghFluidIO, "ghFluidIO", TileEntityFluidIO.class);
    }


    public static void init(){

        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        for(Map.Entry<String, Block> ent : renderMap.entrySet()){
            renderItem.getItemModelMesher().register(Item.getItemFromBlock(ent.getValue()), 0, new ModelResourceLocation(Reference.modid + ":" + ent.getKey(), "inventory"));
        }

    }


    public static void postInit(){
    }


    private static void registerBlock(Block block, String key){
        block.setUnlocalizedName(key);//.setCreativeTab(MMItems.tab);
        if(FluxedCrystals.isDevEnv)
            writeFile(block, key);
        renderMap.put(key, block);
        GameRegistry.registerBlock(block, key);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerBlock(Block block, String key, Class tile){
        registerBlock(block, key);
        if(FluxedCrystals.isDevEnv)
            writeFile(block, key);
        renderMap.put(key, block);
        GameRegistry.registerTileEntity(tile, key);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerBlock(Block block, String key, Class tile, CreativeTabs tab){
        block.setUnlocalizedName(key).setCreativeTab(tab);
        renderMap.put(key, block);
        if(FluxedCrystals.isDevEnv)
            writeFile(block, key);
        renderMap.put(key, block);
        GameRegistry.registerBlock(block, key);
        GameRegistry.registerTileEntity(tile, key);
    }

    public static void writeFile(Block item, String key){
        try{
            File baseBlockState = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            File baseBlockModel = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            File baseItem = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");

            if(System.getProperty("user.home").endsWith("Jared")){
                baseBlockState = new File(System.getProperty("user.home") + "/Documents/Github/Fluxed-Crystals-3/src/main/resources/assets/fluxedcrystals/blockstates/" + key + ".json");
                baseBlockModel = new File(System.getProperty("user.home") + "/Documents/Github/Fluxed-Crystals-3/src/main/resources/assets/fluxedcrystals/models/block/" + key + ".json");
                baseItem = new File(System.getProperty("user.home") + "/Documents/Github/Fluxed-Crystals-3/src/main/resources/assets/fluxedcrystals/models/item/" + key + ".json");
            }
            if(!baseBlockState.exists()){
                baseBlockState.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockState.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    if(line.contains("%modid%")){
                        line = line.replace("%modid%", Reference.modid);
                    }
                    if(line.contains("%key%")){
                        line = line.replace("%key%", key);
                    }
                    content.add(line);
                }
                scan.close();
                FileWriter write = new FileWriter(baseBlockState);
                for(String s : content){
                    write.write(s + "\n");
                }
                write.close();
            }
            if(!baseBlockModel.exists()){
                baseBlockModel.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockModel.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    if(line.contains("%modid%")){
                        line = line.replace("%modid%", Reference.modid);
                    }
                    if(line.contains("%key%")){
                        line = line.replace("%key%", key);
                    }
                    content.add(line);
                }
                scan.close();
                FileWriter write = new FileWriter(baseBlockModel);
                for(String s : content){
                    write.write(s + "\n");
                }
                write.close();
            }

            if(!baseItem.exists()){
                baseItem.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockItem.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    if(line.contains("%modid%")){
                        line = line.replace("%modid%", Reference.modid);
                    }
                    if(line.contains("%key%")){
                        line = line.replace("%key%", key);
                    }
                    content.add(line);
                }
                scan.close();
                FileWriter write = new FileWriter(baseItem);
                for(String s : content){
                    write.write(s + "\n");
                }
                write.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
