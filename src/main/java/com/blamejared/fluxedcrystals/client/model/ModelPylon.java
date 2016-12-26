package com.blamejared.fluxedcrystals.client.model;

import com.blamejared.fluxedcrystals.reference.Reference;
import com.blamejared.fluxedcrystals.util.models.*;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

public class ModelPylon {
	
	private IBakedModel cubeCentre;
	private IBakedModel cubeOuter;
	private IBakedModel rings;
	
	
	public ModelPylon() {
		
		try {
			OBJColourModel model = (OBJColourModel) OBJColourLoader.INSTANCE.loadModel(new ResourceLocation(Reference.MODID, "models/block/pylon.obj"));
			IModel pylon = ((OBJColourModel) model.retexture(ImmutableMap.of("#normal", Reference.MODID + ":model/pylon"))).process(ImmutableMap.of("flip-v", "true"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
