package com.blamejared.fluxedcrystals.api.crystals;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Crystal {
	
	private final String name;
	private final ItemStack blockIn;
	private final ItemStack itemOut;
	private int colour;
	private final float cost;
	
	public Crystal(String name, ItemStack blockIn, ItemStack itemOut, int colour, float cost) {
		this.name = name;
		this.blockIn = blockIn;
		this.itemOut = itemOut;
		this.colour = colour;
		this.cost = cost;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getBlockIn() {
		return blockIn;
	}
	
	public ItemStack getItemOut() {
		return itemOut;
	}
	
	public int getColour() {
		return colour;
	}
	
	public void setColour(int colour) {
		this.colour = colour;
	}
	
	public float getCost() {
		return cost;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("name", name);
		System.out.println(toString());
		tag.setTag("blockIn", blockIn.writeToNBT(new NBTTagCompound()));
		tag.setTag("itemOut", itemOut.writeToNBT(new NBTTagCompound()));
		tag.setInteger("colour", colour);
		tag.setFloat("cost", cost);
		return tag;
	}
	
	public static Crystal readFromNBT(NBTTagCompound tag) {
		return new Crystal(tag.getString("name"), ItemStack.loadItemStackFromNBT(tag.getCompoundTag("blockIn")), ItemStack.loadItemStackFromNBT(tag.getCompoundTag("itemOut")), tag.getInteger("colour"), tag.getInteger("cost"));
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Crystal{");
		sb.append("name='").append(name).append('\'');
		sb.append(", blockIn=").append(blockIn);
		sb.append(", itemOut=").append(itemOut);
		sb.append(", colour=").append(colour);
		sb.append(", cost=").append(cost);
		sb.append('}');
		return sb.toString();
	}
}
