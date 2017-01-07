package com.blamejared.fluxedcrystals.util.json;

import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.text.*;

public class StackHelper {
	
	public static ItemStack getStackFromString(String str) {
		if(str == null || str.equals(""))
			return null;
		return getStackFromArray(str.trim().split(" "));
	}
	
	public static String getStringFromStack(ItemStack stack) {
		String str = stack.getItem().getRegistryName().toString();
		str = str + " " + stack.getItemDamage();
		
		if(stack.hasTagCompound()) {
			str = str + " " + stack.getTagCompound().toString();
		}
		
		str = str + " *" + stack.stackSize;
		
		return str;
	}
	
	private static NBTTagCompound getTag(String[] str, int pos) {
		String s = formatNBT(str, pos).getUnformattedText();
		try {
			NBTBase nbtbase = JsonToNBT.getTagFromJson(s);
			if(!(nbtbase instanceof NBTTagCompound))
				return null;
			return (NBTTagCompound) nbtbase;
		} catch(Exception nbtexception) {
			return null;
		}
	}
	
	public static boolean isAmount(String str) {
		return str.startsWith("*");
	}
	
	private static ItemStack getStackFromArray(String[] str) {
		Item item = getItemByText(str[0]);
		int meta = 0;
		int amount = 1;
		ItemStack stack = new ItemStack(item, 1, meta);
		NBTTagCompound tag = null;
		if(str.length > 1) {
			if(isAmount(str[1]))
				amount = parseAmount(str[1]);
			else
				meta = parseMeta(str[1]);
		}
		
		if(str.length > 2) {
			tag = getTag(str, 2);
			if(tag == null)
				amount = parseAmount(str[2]);
		}
		
		if(str.length > 3) {
			amount = parseAmount(str[3]);
		}
		
		stack.setItemDamage(meta);
		
		if(tag != null) {
			stack.setTagCompound(tag);
		}
		
		if(amount >= 1) {
			stack.stackSize = amount;
		}
		
		return stack;
	}
	
	private static Item getItemByText(String str) {
		Item item = (Item) Item.getByNameOrId(str);
		if(item == null) {
			try {
				Item item1 = Item.getItemById(Integer.parseInt(str));
				item = item1;
			} catch(NumberFormatException numberformatexception) {
				;
			}
		}
		
		return item;
	}
	
	private static ITextComponent formatNBT(String[] str, int start) {
		TextComponentString chatcomponenttext = new TextComponentString("");
		
		for(int j = start; j < str.length; ++j) {
			if(j > start) {
				chatcomponenttext.appendText(" ");
			}
			
			Object object = new TextComponentString(str[j]);
			chatcomponenttext.appendSibling((ITextComponent) object);
		}
		
		return chatcomponenttext;
	}
	
	private static int parseMeta(String str) {
		try {
			return Integer.parseInt(str);
		} catch(NumberFormatException numberformatexception) {
			return 0;
		}
	}
	
	private static int parseAmount(String str) {
		try {
			return Integer.parseInt(str.substring(1, str.length()));
		} catch(NumberFormatException numberformatexception) {
			return 0;
		}
	}
}
