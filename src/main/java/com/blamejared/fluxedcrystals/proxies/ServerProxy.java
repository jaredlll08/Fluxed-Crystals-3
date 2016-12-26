package com.blamejared.fluxedcrystals.proxies;

import com.blamejared.fluxedcrystals.events.CommonEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy {
	
	public void setupLoaders(){
		
	}
	
	public void registerRenders(){
		
	}
	
	public void setupBlockColours(){
		
	}
	
	public void setupEvents(){
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
	}
}
