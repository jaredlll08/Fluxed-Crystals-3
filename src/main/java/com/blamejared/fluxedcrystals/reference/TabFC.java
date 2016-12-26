package com.blamejared.fluxedcrystals.reference;

import com.blamejared.fluxedcrystals.items.FCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabFC extends CreativeTabs {
	
	public TabFC() {
		super("fluxedcrystals");
	}
	
	@Override
	public Item getTabIconItem() {
		return FCItems.TUNING_FORK;
	}
}
