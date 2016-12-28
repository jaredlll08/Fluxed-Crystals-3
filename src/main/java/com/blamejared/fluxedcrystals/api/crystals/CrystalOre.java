package com.blamejared.fluxedcrystals.api.crystals;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class CrystalOre {
	
	private ItemStack stack;
	private BlockPos pos;
	private int colour;
	
	public static final CrystalOre NULL = new CrystalOre(new ItemStack(Blocks.AIR), BlockPos.ORIGIN, 0xFFFFFF);
	
	public CrystalOre() {
		this(new ItemStack(Blocks.AIR), BlockPos.ORIGIN, 0xFFFFFF);
	}
	
	
	public CrystalOre(ItemStack stack, BlockPos pos, int colour) {
		this.stack = stack;
		this.pos = pos;
		this.colour = colour;
	}
	
	public CrystalOre(int colour) {
		this(new ItemStack(Blocks.AIR), BlockPos.ORIGIN, colour);
	}
	
	
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagCompound item = new NBTTagCompound();
		stack.writeToNBT(item);
		tag.setTag("item", item);
		tag.setLong("pos", pos.toLong());
		tag.setInteger("colour", colour);
		return tag;
	}
	
	public static CrystalOre readFromNBT(NBTTagCompound tag) {
		return new CrystalOre(ItemStack.loadItemStackFromNBT(tag.getCompoundTag("item")), BlockPos.fromLong(tag.getLong("pos")), tag.getInteger("colour"));
	}
	
	
	public ItemStack getStack() {
		return stack;
	}
	
	public void setStack(ItemStack stack) {
		this.stack = stack;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public void setPos(BlockPos pos) {
		this.pos = pos;
	}
	
	public int getColour() {
		return colour;
	}
	
	public void setColour(int colour) {
		this.colour = colour;
	}
}
