package com.blamejared.fluxedcrystals;

import com.blamejared.fluxedcrystals.api.crystals.Crystal;
import com.blamejared.fluxedcrystals.api.registries.CrystalRegistry;
import com.blamejared.fluxedcrystals.proxies.ServerProxy;
import com.blamejared.fluxedcrystals.reference.Reference;
import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.util.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.util.*;

import static com.blamejared.fluxedcrystals.reference.Reference.*;
import static net.minecraftforge.oredict.OreDictionary.getOres;

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
	}
	
	@Mod.EventHandler
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		PROXY.registerRenders();
		PROXY.setupBlockColours();
		PROXY.setupEvents();
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
		Map<String, Float> rarityMap = new HashMap<>();
		rarityMap.put("Coal", 0.1f);
		rarityMap.put("Diamond", 10f);
		rarityMap.put("Emerald", 15f);
		rarityMap.put("Gold", 5f);
		rarityMap.put("Iron", 2f);
		rarityMap.put("Lapis", 0.5f);
		rarityMap.put("Quartz", 1f);
		rarityMap.put("Redstone", 0.8f);
		ores.forEach(ore -> {
			CrystalRegistry.register(ore.split("ore")[1], new Crystal(ore.split("ore")[1], ore, ColourHelper.getColour(ResourceUtils.getResourceFromItem(getOres(ore).get(0)).getInputStream()), rarityMap.getOrDefault(ore.split("ore")[1], 1.5f)));
		});
		CrystalRegistry.dump(true);
	}
	
	@Override
	public FluxedCrystals getInstance() {
		return INSTANCE;
	}
}
