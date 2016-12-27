package com.blamejared.fluxedcrystals.proxies;

import com.blamejared.fluxedcrystals.events.CommonEventHandler;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.server.FMLServerHandler;

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
	
	public World getWorld(){
		return FMLServerHandler.instance().getServer().getEntityWorld();
	}
}
