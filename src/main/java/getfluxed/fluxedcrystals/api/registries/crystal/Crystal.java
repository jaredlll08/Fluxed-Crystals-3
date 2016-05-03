package getfluxed.fluxedcrystals.api.registries.crystal;

import getfluxed.fluxedcrystals.config.Config;
import getfluxed.fluxedcrystals.util.JsonTools;
import net.minecraft.block.Block;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

import static getfluxed.fluxedcrystals.config.Config.isBlock;

/**
 * Created by Jared on 4/30/2016.
 */
public class Crystal {

    private String name;
    private Resource resourceIn;
    private Resource resourceOut;
    private int colour;
    private boolean dynamicColour;
    private int dustNeeded;
    private int crushedCrystalPerBlock;
    private int crushedCrystalNeededPerBlock;

    public Crystal(String name, Resource resourceIn, Resource resourceOut, int colour, boolean dynamicColour, int dustNeeded, int crushedCrystalPerBlock, int crushedCrystalNeededPerBlock) {
        this.name = name;
        this.resourceIn = resourceIn;
        this.resourceOut = resourceOut;
        this.colour = colour;
        this.dynamicColour = dynamicColour;
        this.dustNeeded = dustNeeded;
        this.crushedCrystalPerBlock = crushedCrystalPerBlock;
        this.crushedCrystalNeededPerBlock = crushedCrystalNeededPerBlock;
    }

    public Crystal(String name, String resourceIn, String resourceOut, String colour, boolean dynamicColour, int dustNeeded, int crushedCrystalPerBlock, int crushedCrystalNeededPerBlock) {
        this.name = name;
        this.resourceIn = JsonTools.getResource(resourceIn);
        this.resourceOut = JsonTools.getResource(resourceOut);
        this.colour = Integer.decode(colour);
        this.dynamicColour = dynamicColour;
        this.dustNeeded = dustNeeded;
        this.crushedCrystalPerBlock = crushedCrystalPerBlock;
        this.crushedCrystalNeededPerBlock = crushedCrystalNeededPerBlock;
    }

    public String getName() {
        return name;
    }

    public Resource getResourceIn() {
        return resourceIn;
    }

    public Resource getResourceOut() {
        return resourceOut;
    }

    public int getColour() {
        return colour;
    }

    public boolean isDynamicColour() {
        return dynamicColour;
    }

    public void setDynamicColour(boolean dynamicColour) {
        this.dynamicColour = dynamicColour;
    }

    public int getDustNeeded() {
        return dustNeeded;
    }

    public int getCrushedCrystalPerBlock() {
        return crushedCrystalPerBlock;
    }

    public int getCrushedCrystalNeededPerBlock() {
        return crushedCrystalNeededPerBlock;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setResourceIn(Resource resourceIn) {
        this.resourceIn = resourceIn;
    }

    public void setResourceOut(Resource resourceOut) {
        this.resourceOut = resourceOut;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public void setDustNeeded(int dustNeeded) {
        this.dustNeeded = dustNeeded;
    }

    public void setCrushedCrystalPerBlock(int crushedCrystalPerBlock) {
        this.crushedCrystalPerBlock = crushedCrystalPerBlock;
    }

    public void setCrushedCrystalNeededPerBlock(int crushedCrystalNeededPerBlock) {
        this.crushedCrystalNeededPerBlock = crushedCrystalNeededPerBlock;
    }

    public void updateColour() {
        try {
            if (isDynamicColour()) {
                if (getResourceIn().isItemStack()) {
                    if (isBlock(getResourceIn().getItemStack())) {
                        String name = Block.blockRegistry.getNameForObject(Block.getBlockFromItem(getResourceIn().getItemStack().getItem())).toString();
                        IResource res = FMLClientHandler.instance().getClient().getResourceManager().getResource(new ResourceLocation(name.split(":")[0], "textures/blocks/" + name.split(":")[1] + ".png"));
                        if (res != null) {
                            setColour(Config.getColour(res.getInputStream()));
                        }

                    } else {
                        String name = Item.itemRegistry.getNameForObject(getResourceIn().getItemStack().getItem()).toString();
                        IResource res = FMLClientHandler.instance().getClient().getResourceManager().getResource(new ResourceLocation(name.split(":")[0], "textures/items/" + name.split(":")[1] + ".png"));
                        if (res != null) {
                            setColour(Config.getColour(res.getInputStream()));
                        }
                    }
                } else {
                    if (getResourceIn().isFluidStack()) {
                        String name = Block.blockRegistry.getNameForObject(getResourceIn().getFluidStack().getFluid().getBlock()).toString() + "_flow";
                        setColour(Config.getColour(FMLClientHandler.instance().getClient().getResourceManager().getResource(new ResourceLocation(name.split(":")[0], "textures/blocks/" + name.split(":")[1] + ".png")).getInputStream()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CrystalType {
        private String name = TextFormatting.RED + "INVALID";
        private String resourceIn = "minecraft:bedrock";
        private String resourceOut = "minecraft:bedrock";
        private String colour = "0xFFFFFF";
        private boolean dynamicColour = true;
        private int dustNeeded = 32;
        private int crushedCrystalPerBlock = 18;
        private int crushedCrystalNeededPerBlock = 16;


        public Crystal register() {
            Crystal c = new Crystal(this.name, this.resourceIn, this.resourceOut, this.colour, this.dynamicColour, this.dustNeeded, this.crushedCrystalPerBlock, this.crushedCrystalNeededPerBlock);
            c.updateColour();
            return c;
        }
    }
}
