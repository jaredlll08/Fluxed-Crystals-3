package com.blamejared.fluxedcrystals.client.sounds;

import com.blamejared.fluxedcrystals.reference.Reference;
import com.teamacronymcoders.base.modulesystem.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

@Module(Reference.MODID)
public class FCSounds extends ModuleBase {
	
	
	public static SoundEvent CRYSTAL_HUM;
	
	private static SoundEvent getRegisteredSoundEvent(String name) {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation(name));
	}
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		String[] sounds = new String[]{Reference.MODID + ":crystal_hum"};
		for(String s : sounds) {
			ResourceLocation loc = new ResourceLocation(s);
			GameRegistry.register(new SoundEvent(loc), loc);
		}
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		CRYSTAL_HUM = getRegisteredSoundEvent(Reference.MODID + ":crystal_hum");
	}
	
	@Nullable
	@Override
	public String getClientProxyPath() {
		return "com.blamejared.fluxedcrystals.client.sounds.proxy.SoundsClientProxy";
	}
	
	@Override
	public String getName() {
		return "Sounds";
	}
}
