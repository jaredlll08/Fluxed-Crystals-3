package com.blamejared.fluxedcrystals.blocks;

import com.blamejared.fluxedcrystals.api.materials.*;
import com.blamejared.fluxedcrystals.blocks.crystal.*;
import com.blamejared.fluxedcrystals.blocks.misc.*;
import com.teamacronymcoders.base.modulesystem.*;
import com.teamacronymcoders.base.registry.BlockRegistry;
import com.teamacronymcoders.base.registry.config.ConfigRegistry;
import net.minecraft.block.material.Material;

import static com.blamejared.fluxedcrystals.reference.Reference.MODID;

@Module(MODID)
public class FCBlocks extends ModuleBase {
	
	public static Material MATERIAL_CRYSTAL = new MaterialCrystal();
	public static Material MATERIAL_HIDDEN = new MaterialHidden();
	
	public static BlockQuartzGlass QUARTZ_GLASS = new BlockQuartzGlass();
	public static BlockCrystal CRYSTAL = new BlockCrystal();
	public static BlockCrystalCore CRYSTAL_CORE = new BlockCrystalCore();
	public static BlockCrystalPylon CRYSTAL_PYLON = new BlockCrystalPylon();
	public static BlockCrystalCluster CRYSTAL_CLUSTER = new BlockCrystalCluster();
	
	public static BlockHidden HIDDEN = new BlockHidden();
	
	@Override
	public void registerBlocks(ConfigRegistry configRegistry, BlockRegistry blockRegistry) {
		super.registerBlocks(configRegistry, blockRegistry);
		blockRegistry.register(QUARTZ_GLASS);
		blockRegistry.register(CRYSTAL);
		blockRegistry.register(CRYSTAL_CORE);
		blockRegistry.register(CRYSTAL_PYLON);
		blockRegistry.register(CRYSTAL_CLUSTER);
		blockRegistry.register(HIDDEN);
	}
	
	@Override
	public String getName() {
		return "Blocks";
	}
}
