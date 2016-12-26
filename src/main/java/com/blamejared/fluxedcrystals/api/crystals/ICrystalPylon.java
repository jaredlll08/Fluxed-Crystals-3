package com.blamejared.fluxedcrystals.api.crystals;

import net.minecraft.util.math.BlockPos;

public interface ICrystalPylon {
	
	BlockPos getPos();
	
	ICrystalCore getCore();
	
	void setCore(ICrystalCore core);
}
