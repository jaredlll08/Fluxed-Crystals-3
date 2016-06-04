package getfluxed.fluxedcrystals.config;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.api.registries.crystal.Crystal;
import getfluxed.fluxedcrystals.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Created by Jared on 3/23/2016.
 */
public class Config {


    public static void load() {
        Configuration configuration = new Configuration(new File(Reference.configDirectory, String.format("%s.cfg", Reference.modid)));
        configuration.load();
        configuration.save();
    }

    public static void registerJsons() {
        CrystalRegistry.setEditing(true);
        CrystalRegistry.getCrystalMap().clear();
        File jsons = new File(Reference.configDirectory, "/jsons/");
        if (!jsons.exists()) {
            jsons.mkdirs();
        }
        File seed = new File(jsons, "seedData.json");
        if (!seed.exists()) {
            try {
                FileUtils.copyURLToFile(FluxedCrystals.class.getResource("/assets/" + Reference.modid + "/jsons/seedData.json"), seed);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JSONParser<Crystal.CrystalType> readerCrystal = new JSONParser<Crystal.CrystalType>(seed, Crystal.CrystalType.class);
        registerCrystals(readerCrystal.getElements("data"));
        CrystalRegistry.setEditing(false);
        try {
            for (Map.Entry<String, Crystal> c : CrystalRegistry.getCrystalMap().entrySet()) {
                try {

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static boolean isBlock(ItemStack stack) {
        ResourceLocation name = Block.REGISTRY.getNameForObject(Block.getBlockFromItem(stack.getItem()));
        return name != null && !name.toString().equals("minecraft:air") && Block.REGISTRY.containsKey(name);
    }

    public static void registerCrystals(Collection<? extends Crystal.CrystalType> types) {
        for (Crystal.CrystalType type : types) {
            CrystalRegistry.register(type.register());
        }
    }

    public static int getColour(InputStream stream) throws Exception {
        ImageInputStream is = ImageIO.createImageInputStream(stream);
        Iterator iter = ImageIO.getImageReaders(is);

        if (!iter.hasNext()) {
            System.out.println("Cannot load the specified stream " + stream);
        }
        ImageReader imageReader = (ImageReader) iter.next();
        imageReader.setInput(is);


        Image img = imageReader.read(0).getScaledInstance(1, 1, Image.SCALE_AREA_AVERAGING);
        BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = image.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        int height = image.getHeight();
        int width = image.getWidth();

        Map m = new HashMap();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                Integer counter = (Integer) m.get(rgb);
                if (counter == null)
                    counter = 0;
                counter++;
                m.put(rgb, counter);
            }
        }
        String colourHex = getMostCommonColour(m);
        return Integer.decode("0x" + colourHex);
    }


    public static String getMostCommonColour(Map map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, (o1, o2) -> ((Comparable) ((Map.Entry) (o1)).getValue())
                .compareTo(((Map.Entry) (o2)).getValue()));
        if (list.size() == 0) {
            return "FFFFFF";
        }
        Map.Entry me = (Map.Entry) list.get(list.size() - 1);
        int[] rgb = getRGBArr((Integer) me.getKey());
        return Integer.toHexString(rgb[0]) + Integer.toHexString(rgb[1]) + Integer.toHexString(rgb[2]);
    }

    public static int[] getRGBArr(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red, green, blue};

    }


}
