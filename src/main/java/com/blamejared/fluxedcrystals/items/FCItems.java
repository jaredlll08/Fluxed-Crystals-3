package com.blamejared.fluxedcrystals.items;

import com.blamejared.fluxedcrystals.items.misc.ItemTuningFork;
import com.teamacronymcoders.base.modulesystem.*;
import com.teamacronymcoders.base.registry.ItemRegistry;
import com.teamacronymcoders.base.registry.config.ConfigRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.blamejared.fluxedcrystals.reference.Reference.MODID;

@Module(MODID)
public class FCItems extends ModuleBase {
	
	public static ItemTuningFork TUNING_FORK = new ItemTuningFork();
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}
	
	
	@Override
	public void registerItems(ConfigRegistry configRegistry, ItemRegistry itemRegistry) {
		super.registerItems(configRegistry, itemRegistry);
		itemRegistry.register(TUNING_FORK);
	}
	
	@Override
	public String getName() {
		return "Items";
	}
}
