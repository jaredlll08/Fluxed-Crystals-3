package com.blamejared.fluxedcrystals.api.registries;

import com.blamejared.fluxedcrystals.api.crystals.Crystal;
import com.blamejared.fluxedcrystals.reference.Reference;
import com.blamejared.fluxedcrystals.util.json.StackHelper;
import com.google.gson.*;
import com.google.gson.stream.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class CrystalRegistry {
	
	private static HashMap<String, Crystal> crystalMap = new HashMap<>();
	
	public static void register(Crystal crystal) {
		register(crystal.getName(), crystal);
	}
	
	public static void register(String name, Crystal crystal) {
		getCrystalMap().put(name, crystal);
	}
	
	public static HashMap<String, Crystal> getCrystalMap() {
		return crystalMap;
	}
	
	
	public static Crystal getCrystalFromName(String name) {
		return getCrystalMap().get(name);
	}
	
	public static Crystal getCrystalFromBlock(ItemStack block) {
		for(Map.Entry<String, Crystal> entry : crystalMap.entrySet()) {
			if(entry.getValue().getBlockIn().getItem() == block.getItem()) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public static void dump(boolean overide) {
		Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemStack.class, new TypeAdapter<ItemStack>() {
			
			@Override
			public void write(JsonWriter out, ItemStack value) throws IOException {
				out.value(StackHelper.getStringFromStack(value));
				
			}
			
			@Override
			public ItemStack read(JsonReader in) throws IOException {
				String next = in.nextString();
				System.out.println(next);
				return StackHelper.getStackFromString(next);
			}
		}).create();
		File jsonFile = new File(Reference.configDirectory, "crystal_data.json");
		boolean existed = true;
		if(!jsonFile.exists()) {
			try {
				jsonFile.createNewFile();
				existed = false;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		if(overide || !existed) {
			try {
				FileWriter writer = new FileWriter(jsonFile);
				List<Crystal> crystalList = new ArrayList<>(getCrystalMap().values());
				crystalList.sort(Comparator.comparing(Crystal::getName));
				writer.write(gson.toJson(crystalList));
				writer.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
