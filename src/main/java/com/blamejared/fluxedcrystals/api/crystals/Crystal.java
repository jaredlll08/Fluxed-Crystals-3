package com.blamejared.fluxedcrystals.api.crystals;

import net.minecraft.nbt.NBTTagCompound;

public class Crystal {
	
	private final String name;
	private final String oredictIn;
	private final String oredictOut;
	private int colour;
	private final float cost;
	
	public Crystal(String name, String oredictIn, String oredictOut, int colour, float cost) {
		this.name = name;
		this.oredictIn = oredictIn;
		this.oredictOut = oredictOut;
		this.colour = colour;
		this.cost = cost;
	}
	
	public String getName() {
		return name;
	}
	
	public String getOredictIn() {
		return oredictIn;
	}
	
	public String getOredictOut() {
		return oredictOut;
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
		tag.setString("oreDictIn", oredictIn);
		tag.setString("oreDictOut", oredictIn);
		tag.setInteger("colour", colour);
		tag.setFloat("cost", cost);
		return tag;
	}
	
	public static Crystal readFromNBT(NBTTagCompound tag) {
		return new Crystal(tag.getString("name"), tag.getString("oreDictIn"), tag.getString("oreDictOut"), tag.getInteger("colour"), tag.getInteger("cost"));
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Crystal{");
		sb.append("name='").append(name).append('\'');
		sb.append(", oredictIn='").append(oredictIn).append('\'');
		sb.append(", oredictOut='").append(oredictOut).append('\'');
		sb.append(", colour=").append(colour);
		sb.append(", cost=").append(cost);
		sb.append('}');
		return sb.toString();
	}
}
