package getfluxed.fluxedcrystals.api.crystals;

import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.api.registries.crystal.Resource;
import getfluxed.fluxedcrystals.config.Config;
import getfluxed.fluxedcrystals.util.JsonTools;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.awt.*;

import static getfluxed.fluxedcrystals.config.Config.isBlock;

/**
 * Created by Jared on 4/30/2016.
 */
public class Crystal {

    private String name;
    private Resource resourceIn;
    private Resource resourceOut;
    private int colour;
    private Color colourObj;
    private boolean dynamicColour;
    private int dustNeeded;
    private double growthTimePerBlock;
    private int crushedCrystalPerBlockMin;
    private int crushedCrystalPerBlockMax;

    public static Crystal NULL = new Crystal("null", "minecraft:bedrock", "minecraft:bedrock", "0xFFFFFF", false, 1, 1, 1, 1);

    public Crystal(String name, Resource resourceIn, Resource resourceOut, int colour, boolean dynamicColour, int dustNeeded, double growthTimePerBlock, int crushedCrystalPerBlockMin, int crushedCrystalPerBlockMax) {
        this.name = name;
        this.resourceIn = resourceIn;
        this.resourceOut = resourceOut;
        this.colour = colour;
        this.colourObj = new Color(colour);
        this.dynamicColour = dynamicColour;
        this.dustNeeded = dustNeeded;
        this.growthTimePerBlock = growthTimePerBlock;
        this.crushedCrystalPerBlockMin = crushedCrystalPerBlockMin;
        this.crushedCrystalPerBlockMax = crushedCrystalPerBlockMax;
    }

    public Crystal(String name, String resourceIn, String resourceOut, String colour, boolean dynamicColour, int dustNeeded, double growthTimePerBlock, int crushedCrystalPerBlockMin, int crushedCrystalPerBlockMax) {
        this.name = name;
        this.resourceIn = JsonTools.getResource(resourceIn);
        this.resourceOut = JsonTools.getResource(resourceOut);
        this.colour = Integer.decode(colour);
        this.colourObj = new Color(this.colour);
        this.dynamicColour = dynamicColour;
        this.dustNeeded = dustNeeded;
        this.growthTimePerBlock = growthTimePerBlock;
        this.crushedCrystalPerBlockMin = crushedCrystalPerBlockMin;
        this.crushedCrystalPerBlockMax = crushedCrystalPerBlockMax;
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


    public double getGrowthTimePerBlock() {
        return growthTimePerBlock;
    }

    public void setGrowthTimePerBlock(double growthTimePerBlock) {
        this.growthTimePerBlock = growthTimePerBlock;
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
        this.colourObj = new Color(colour);
    }

    public Color getColourObj() {
        return colourObj;
    }

    public void setDustNeeded(int dustNeeded) {
        this.dustNeeded = dustNeeded;
    }

    public int getCrushedCrystalPerBlockMax() {
        return crushedCrystalPerBlockMax;
    }

    public void setCrushedCrystalPerBlockMax(int crushedCrystalPerBlockMax) {
        this.crushedCrystalPerBlockMax = crushedCrystalPerBlockMax;
    }

    public int getCrushedCrystalPerBlockMin() {
        return crushedCrystalPerBlockMin;
    }

    public void setCrushedCrystalPerBlockMin(int crushedCrystalPerBlockMin) {
        this.crushedCrystalPerBlockMin = crushedCrystalPerBlockMin;
    }

    public void updateColour() {
        try {
            if (isDynamicColour()) {
                if (getResourceIn().isItemStack()) {
                    if (isBlock(getResourceIn().getItemStack())) {
                        String name = Block.REGISTRY.getNameForObject(Block.getBlockFromItem(getResourceIn().getItemStack().getItem())).toString();
                        IResource res = FMLClientHandler.instance().getClient().getResourceManager().getResource(new ResourceLocation(name.split(":")[0], "textures/blocks/" + name.split(":")[1] + ".png"));
                        if (res != null) {
                            setColour(Config.getColour(res.getInputStream()));
                        }

                    } else {
                        String name = Item.REGISTRY.getNameForObject(getResourceIn().getItemStack().getItem()).toString();
                        IResource res = FMLClientHandler.instance().getClient().getResourceManager().getResource(new ResourceLocation(name.split(":")[0], "textures/items/" + name.split(":")[1] + ".png"));
                        if (res != null) {
                            setColour(Config.getColour(res.getInputStream()));
                        }
                    }
                } else {
                    if (getResourceIn().isFluidStack()) {
                        String name = Block.REGISTRY.getNameForObject(getResourceIn().getFluidStack().getFluid().getBlock()).toString() + "_flow";
                        setColour(Config.getColour(FMLClientHandler.instance().getClient().getResourceManager().getResource(new ResourceLocation(name.split(":")[0], "textures/blocks/" + name.split(":")[1] + ".png")).getInputStream()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToByteBuf(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, getName());
    }

    public static Crystal readFromByteBuf(ByteBuf buf) {
        String name = ByteBufUtils.readUTF8String(buf);
        if (name.equals(NULL.getName())) {
            return NULL;
        }
        return CrystalRegistry.getCrystal(name) != null ? CrystalRegistry.getCrystal(name) : NULL;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setString("name", getName());
    }

    public static Crystal readFromNBT(NBTTagCompound tag) {

        String name = tag.getString("name");
        if (name.equals(NULL.getName())) {
            return NULL;
        }
        return CrystalRegistry.getCrystal(name) != null ? CrystalRegistry.getCrystal(name) : NULL;

    }

    public class CrystalType {
        private String name = TextFormatting.RED + "INVALID";
        private String resourceIn = "minecraft:bedrock";
        private String resourceOut = "minecraft:bedrock";
        private String colour = "0xFFFFFF";
        private boolean dynamicColour = true;
        private int dustNeeded = 32;
        private int crushedCrystalPerBlockMin = 2;
        private int crushedCrystalPerBlockMax = 6;
        private double growthTimePerBlock = 320;


        public Crystal register() {
            Crystal c = new Crystal(this.name, this.resourceIn, this.resourceOut, this.colour, this.dynamicColour, this.dustNeeded, this.growthTimePerBlock, this.crushedCrystalPerBlockMin, this.crushedCrystalPerBlockMax);
            c.updateColour();
            return c;
        }
    }
}
