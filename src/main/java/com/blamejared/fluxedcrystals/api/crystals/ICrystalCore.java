package com.blamejared.fluxedcrystals.api.crystals;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ICrystalCore {
	
	int getColour();
	
	List<ICrystalPylon> getPylons();
	
	BlockPos getPos();
	
}
