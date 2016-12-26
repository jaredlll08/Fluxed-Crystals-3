package com.blamejared.fluxedcrystals;

import com.blamejared.fluxedcrystals.proxies.ServerProxy;
import com.teamacronymcoders.base.BaseModFoundation;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

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
	}
	
	@Override
	public FluxedCrystals getInstance() {
		return INSTANCE;
	}
}
