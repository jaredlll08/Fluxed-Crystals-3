package com.blamejared.fluxedcrystals.api.crystals;

public class Crystal {
	
	private final String name;
	private final String oredict;
	private int colour;
	private final float rarity;
	
	public Crystal(String name, String oredict, int colour, float rarity) {
		this.name = name;
		this.oredict = oredict;
		this.colour = colour;
		this.rarity = rarity;
	}
	
	public String getName() {
		return name;
	}
	
	public String getOredict() {
		return oredict;
	}
	
	public int getColour() {
		return colour;
	}
	
	public float getRarity() {
		return rarity;
	}
	
	public void setColour(int colour) {
		this.colour = colour;
	}
}
