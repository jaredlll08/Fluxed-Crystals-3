package com.blamejared.fluxedcrystals;

import com.blamejared.fluxedcrystals.api.crystals.Crystal;
import com.blamejared.fluxedcrystals.api.registries.CrystalRegistry;
import com.blamejared.fluxedcrystals.network.tiles.*;
import com.blamejared.fluxedcrystals.proxies.ServerProxy;
import com.blamejared.fluxedcrystals.reference.Reference;
import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.util.*;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.util.*;

import static com.blamejared.fluxedcrystals.reference.Reference.*;

@Mod(modid = MODID, name = NAME, version = VERSION)
public class FluxedCrystals extends BaseModFoundation<FluxedCrystals> {
	
	@Mod.Instance(MODID)
	public static FluxedCrystals INSTANCE;
	
	@SidedProxy(clientSide = "com.blamejared.fluxedcrystals.proxies.ClientProxy", serverSide = "com.blamejared.fluxedcrystals.proxies.ServerProxy")
	public static ServerProxy PROXY;
	
	public FluxedCrystals() {
		super(MODID, NAME, VERSION, TAB);
	}
	
	@Mod.EventHandler
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		PROXY.setupLoaders();
		Reference.configDirectory = new File(event.getSuggestedConfigurationFile().getParent(), "/" + Reference.MODID + "/");
		getPacketHandler().registerPacket(MessageCrystalSync.class, MessageCrystalSync.class, Side.CLIENT);
		getPacketHandler().registerPacket(MessageCrystalClusterSync.class, MessageCrystalClusterSync.class, Side.CLIENT);
		getPacketHandler().registerPacket(MessageCrystalClusterSyncClient.class, MessageCrystalClusterSyncClient.class, Side.SERVER);
		
	}
	
	@Mod.EventHandler
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		PROXY.registerRenders();
		PROXY.setupBlockColours();
		PROXY.setupEvents();
		OreDictionary.registerOre("itemCoal", Items.COAL);
	}
	
	@Mod.EventHandler
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		
		List<String> ores = new ArrayList<>();
		for(String s : OreDictionary.getOreNames()) {
			if(s.toLowerCase().startsWith("ore")) {
				ores.add(s);
			}
		}
		registerCrystal("Coal", Blocks.COAL_ORE, Items.COAL, 0.1f);
		registerCrystal("Diamond", Blocks.DIAMOND_ORE, Items.DIAMOND, 10);
		registerCrystal("Emerald", Blocks.EMERALD_ORE, Items.EMERALD, 15f);
		registerCrystal("Gold", Blocks.GOLD_ORE, Items.GOLD_INGOT, 5f);
		registerCrystal("Iron", Blocks.IRON_ORE, Items.IRON_INGOT, 2f);
		registerCrystal("Lapis", Blocks.LAPIS_ORE, new ItemStack(Items.DYE, 1, 4), 0.5f);
		registerCrystal("Quartz", Blocks.QUARTZ_ORE, Items.QUARTZ, 1f);
		registerCrystal("Redstone", Blocks.REDSTONE_ORE, Items.REDSTONE, 0.8f);
		CrystalRegistry.dump(true);
	}
	
	public void registerCrystal(String name, Block blockin, Item itemout, float cost) {
		CrystalRegistry.register(new Crystal(name, new ItemStack(blockin), new ItemStack(itemout), ColourHelper.getColour(ResourceUtils.getResourceFromItem(new ItemStack(itemout)).getInputStream()), cost));
	}
	
	public void registerCrystal(String name, Block blockin, ItemStack itemout, float cost) {
		CrystalRegistry.register(new Crystal(name, new ItemStack(blockin), itemout, ColourHelper.getColour(ResourceUtils.getResourceFromItem(itemout).getInputStream()), cost));
	}
	
	@Override
	public FluxedCrystals getInstance() {
		return INSTANCE;
	}
}
