package com.blamejared.fluxedcrystals.tileentities.crystal;

import com.blamejared.fluxedcrystals.api.harvestable.IHarvestable;
import com.teamacronymcoders.base.tileentities.TileEntityBase;

public class TileEntityCrystalCluster extends TileEntityBase implements IHarvestable {
	
	@Override
	public boolean isHarvestable() {
		return false;
	}
}
