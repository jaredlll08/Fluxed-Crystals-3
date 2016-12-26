package com.blamejared.fluxedcrystals.tileentities.misc;

import com.blamejared.fluxedcrystals.api.harvestable.IHarvestable;
import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class TileEntityHidden extends TileEntityBase implements IHarvestable {
	
	private Block oldBlock;
	
	public Block getOldBlock() {
		return oldBlock;
	}
	
	public void setOldBlock(Block oldBlock) {
		this.oldBlock = oldBlock;
	}
	
	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		data.setString("block", oldBlock.getRegistryName().toString());
		return super.writeToNBT(data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		oldBlock = Block.getBlockFromName(data.getString("block"));
	}
	
	@Override
	public boolean isHarvestable() {
		return false;
	}
}
