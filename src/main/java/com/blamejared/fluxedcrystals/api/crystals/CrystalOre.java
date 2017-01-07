package com.blamejared.fluxedcrystals.api.crystals;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class CrystalOre {
	
	private String name;
	private IBlockState state;
	private BlockPos pos;
	
	public static final CrystalOre NULL = new CrystalOre("null", Blocks.AIR.getDefaultState(), BlockPos.ORIGIN);
	
	public CrystalOre() {
		this("null", Blocks.AIR.getDefaultState(), BlockPos.ORIGIN);
	}
	
	
	public CrystalOre(String name, IBlockState state, BlockPos pos) {
		this.name = name;
		this.state = state;
		this.pos = pos;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("name", name);
		tag.setString("block", state.getBlock().getRegistryName().toString());
		tag.setInteger("meta", state.getBlock().getMetaFromState(state));
		tag.setLong("pos", pos.toLong());
		return tag;
	}
	
	public static CrystalOre readFromNBT(NBTTagCompound tag) {
		return new CrystalOre(tag.getString("name"), Block.getBlockFromName(tag.getString("block")).getDefaultState().getBlock().getStateFromMeta(tag.getInteger("meta")), BlockPos.fromLong(tag.getLong("pos")));
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public IBlockState getState() {
		return state;
	}
	
	public void setState(IBlockState state) {
		this.state = state;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public void setPos(BlockPos pos) {
		this.pos = pos;
	}
	
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CrystalOre{");
		sb.append("name='").append(name).append('\'');
		sb.append(", state=").append(state);
		sb.append(", pos=").append(pos);
		sb.append('}');
		return sb.toString();
	}
}
