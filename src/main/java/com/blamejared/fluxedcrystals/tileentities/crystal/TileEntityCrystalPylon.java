package com.blamejared.fluxedcrystals.tileentities.crystal;

import com.blamejared.fluxedcrystals.api.crystals.*;
import com.blamejared.fluxedcrystals.api.harvestable.IHarvestable;
import com.blamejared.fluxedcrystals.blocks.FCBlocks;
import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityCrystalPylon extends TileEntityBase implements ITickable, ICrystalPylon, IHarvestable {
	
	public float angle = 0;
	public float floatAm = 0;
	public boolean floatingUp = true;
	
	public ICrystalCore core;
	
	@Override
	public void update() {
		angle += worldObj.rand.nextInt(2) + 1;
		if(floatingUp) {
			if(floatAm <= 0.4) {
				floatAm = (float) Math.sin((angle + Minecraft.getMinecraft().getRenderPartialTicks()) / 10) * 0.1f + 0.1f;
			} else {
				floatingUp = false;
			}
		} else {
			if(floatAm >= -0.2) {
				floatAm = (float) Math.sin((angle + Minecraft.getMinecraft().getRenderPartialTicks()) / 10) * 0.1f + 0.1f;
			} else {
				floatingUp = true;
			}
		}
		
		if(worldObj.getTotalWorldTime() % 20 == 0) {
			if(worldObj.getBlockState(getPos().down()).getBlock() != Blocks.OBSIDIAN || worldObj.getBlockState(getPos().down().down()).getBlock() != Blocks.OBSIDIAN) {
				worldObj.setBlockState(getPos(), FCBlocks.CRYSTAL_CORE.getDefaultState());
			}
		}
	}
	
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return 8192D;
	}
	
	@Override
	public BlockPos getPos() {
		return super.getPos();
	}
	
	@Override
	public ICrystalCore getCore() {
		return core;
	}
	
	@Override
	public void setCore(ICrystalCore core) {
		this.core = core;
		
	}
	
	@Override
	public boolean isHarvestable() {
		return false;
	}
}
