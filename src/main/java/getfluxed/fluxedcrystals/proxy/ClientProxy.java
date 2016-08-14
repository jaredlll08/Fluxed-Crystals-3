package getfluxed.fluxedcrystals.proxy;

import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.blocks.FCBlocks;
import getfluxed.fluxedcrystals.blocks.misc.BlockCrystalCube;
import getfluxed.fluxedcrystals.client.greenhouse.ModelCube;
import getfluxed.fluxedcrystals.client.greenhouse.RenderController;
import getfluxed.fluxedcrystals.client.greenhouse.monitor.RenderPowerMonitor;
import getfluxed.fluxedcrystals.config.Config;
import getfluxed.fluxedcrystals.events.ClientEventHandler;
import getfluxed.fluxedcrystals.items.FCItems;
import getfluxed.fluxedcrystals.items.crystal.ItemCrushedCrystal;
import getfluxed.fluxedcrystals.reference.Reference;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.greenhouse.monitor.TileEntityPowerMonitor;
import getfluxed.fluxedcrystals.tileentities.misc.TileEntityCrystalCube;
import getfluxed.fluxedcrystals.util.NBTHelper;
import getfluxed.fluxedcrystals.util.model.OBJColourLoader;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static getfluxed.fluxedcrystals.blocks.FCBlocks.crystalCube;
import static getfluxed.fluxedcrystals.config.Config.isBlock;
import static getfluxed.fluxedcrystals.items.FCItems.colourMap;
import static getfluxed.fluxedcrystals.items.FCItems.crystalCrushed;

/**
 * Created by Jared on 3/23/2016.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        super.registerRenderers();
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        for (Map.Entry<String, Item> ent : FCItems.renderMap.entrySet()) {
            if (!(ent.getValue() instanceof ItemCrushedCrystal))
                renderItem.getItemModelMesher().register(ent.getValue(), 0, new ModelResourceLocation(Reference.modid + ":" + ent.getKey(), "inventory"));
        }
        List<ResourceLocation> locs = new ArrayList<>();
        locs.add(new ResourceLocation(Reference.modid + ":" + "crystalCrushedShardRough"));
        locs.add(new ResourceLocation(Reference.modid + ":" + "crystalCrushedShardSmooth"));
        locs.add(new ResourceLocation(Reference.modid + ":" + "crystalCrushedChunkRough"));
        locs.add(new ResourceLocation(Reference.modid + ":" + "crystalCrushedChunkSmooth"));

        for (int i = 0; i < locs.size(); i++) {
            renderItem.getItemModelMesher().register(crystalCrushed, i, new ModelResourceLocation(locs.get(i), "inventory"));
        }

        ModelBakery.registerItemVariants(crystalCrushed, (ResourceLocation[]) locs.toArray(new ResourceLocation[locs.size()]));


        for (Map.Entry<Item, int[]> ent : colourMap.entrySet()) {
            //noinspection Convert2Lambda
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
                @Override
                public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                    Crystal c = CrystalRegistry.getCrystal(NBTHelper.getString(stack, "crystalName"));
                    if (c == null) {
                        return 0xFFFFFF;
                    }
                    for (int i : ent.getValue()) {
                        if (tintIndex == i)
                            if (c != null) {
                                return c.getColour();
                            }
                    }
                    return 0xFFFFFF;
                }

            }, ent.getKey());
        }

        for (Map.Entry<String, Block> ent : FCBlocks.renderMap.entrySet()) {
            renderItem.getItemModelMesher().register(Item.getItemFromBlock(ent.getValue()), 0, new ModelResourceLocation(Reference.modid + ":" + ent.getKey(), "inventory"));
        }
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
                if (state.getValue(BlockCrystalCube.onGround))
                    return ((TileEntityCrystalCube) worldIn.getTileEntity(pos)).getCrystal().getColour();
                return 0xFFFFFF;
            }
        }, new Block[]{crystalCube});

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerMonitor.class, new RenderPowerMonitor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoilController.class, new RenderController(new ModelCube()));
//        OBJLoader.INSTANCE.addDomain(Reference.modid);
        ModelLoaderRegistry.registerLoader(OBJColourLoader.INSTANCE);
        OBJColourLoader.INSTANCE.addDomain(Reference.modid);
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        new ClientEventHandler();
    }

    @Override
    public void registerGuis() {
        super.registerGuis();

    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public void updateColour(Crystal c) {
        try {
            if (c.isDynamicColour()) {
                if (c.getResourceIn().isItemStack()) {
                    if (isBlock(c.getResourceIn().getItemStack())) {
                        String name = Block.REGISTRY.getNameForObject(Block.getBlockFromItem(c.getResourceIn().getItemStack().getItem())).toString();
                        IResource res = FMLClientHandler.instance().getClient().getResourceManager().getResource(new ResourceLocation(name.split(":")[0], "textures/blocks/" + name.split(":")[1] + ".png"));
                        if (res != null) {
                            c.setColour(Config.getColour(res.getInputStream()));
                        }

                    } else {
                        String name = Item.REGISTRY.getNameForObject(c.getResourceIn().getItemStack().getItem()).toString();
                        IResource res = FMLClientHandler.instance().getClient().getResourceManager().getResource(new ResourceLocation(name.split(":")[0], "textures/items/" + name.split(":")[1] + ".png"));
                        if (res != null) {
                            c.setColour(Config.getColour(res.getInputStream()));
                        }
                    }
                } else {
                    if (c.getResourceIn().isFluidStack()) {
                        String name = Block.REGISTRY.getNameForObject(c.getResourceIn().getFluidStack().getFluid().getBlock()).toString() + "_flow";
                        c.setColour(Config.getColour(FMLClientHandler.instance().getClient().getResourceManager().getResource(new ResourceLocation(name.split(":")[0], "textures/blocks/" + name.split(":")[1] + ".png")).getInputStream()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
