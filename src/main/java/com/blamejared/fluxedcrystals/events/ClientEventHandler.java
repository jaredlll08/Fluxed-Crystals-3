package com.blamejared.fluxedcrystals.events;

import com.blamejared.fluxedcrystals.FluxedCrystals;
import com.blamejared.fluxedcrystals.api.crystals.Crystal;
import com.blamejared.fluxedcrystals.tileentities.crystal.TileEntityCrystalCluster;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class ClientEventHandler {
	
	public static Map<BlockPos, Crystal> crystalMap = new HashMap<>();
	
	@SubscribeEvent
	public void crystalSyncs(TickEvent.ClientTickEvent e) {
		World world = FluxedCrystals.PROXY.getWorld();
		if(!crystalMap.isEmpty()) {
			crystalMap.forEach((key, val) -> {
				if(world.getTileEntity(key) instanceof TileEntityCrystalCluster) {
					TileEntityCrystalCluster tile = (TileEntityCrystalCluster) world.getTileEntity(key);
					if(tile.getCrystalType() == null) {
						tile.setCrystalType(val);
					}
					world.notifyBlockUpdate(key, world.getBlockState(key), world.getBlockState(key), 8);
				}
			});
			crystalMap.clear();
		}
	}
	
}
