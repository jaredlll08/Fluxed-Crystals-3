package com.blamejared.fluxedcrystals.api.crystals;

public class CrystalOre {
	
	private String name;
	private int colour;
	private int amount;
	
	public CrystalOre(String name, int colour, int amount) {
		this.name = name;
		this.colour = colour;
		this.amount = amount;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getColour() {
		return colour;
	}
	
	public void setColour(int colour) {
		this.colour = colour;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
