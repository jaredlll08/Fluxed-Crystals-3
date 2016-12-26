package com.blamejared.fluxedcrystals.proxies;

import com.blamejared.fluxedcrystals.blocks.FCBlocks;
import com.blamejared.fluxedcrystals.client.render.tile.*;
import com.blamejared.fluxedcrystals.events.ClientEventHandler;
import com.blamejared.fluxedcrystals.reference.Reference;
import com.blamejared.fluxedcrystals.tileentities.crystal.*;
import com.blamejared.fluxedcrystals.util.models.OBJColourLoader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends ServerProxy {
	
	
	@Override
	public void setupLoaders() {
		super.setupLoaders();
		ModelLoaderRegistry.registerLoader(OBJColourLoader.INSTANCE);
		OBJColourLoader.INSTANCE.addDomain(Reference.MODID);
	}
	
	@Override
	public void registerRenders() {
		super.registerRenders();
		ClientRegistry.registerTileEntity(TileEntityCrystal.class, "renderCrystal", new RenderTileEntityCrystal());
		ClientRegistry.registerTileEntity(TileEntityCrystalPylon.class, "renderCrystalPylon", new RenderTileEntityCrystalPylon());
	}
	
	public void setupBlockColours() {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if(worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityCrystal) {
				return ((TileEntityCrystal) worldIn.getTileEntity(pos)).colour;
			}
			return 0xFFFFFF;
		}, FCBlocks.CRYSTAL);
	}
	
	public void setupEvents() {
		super.setupEvents();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
}
