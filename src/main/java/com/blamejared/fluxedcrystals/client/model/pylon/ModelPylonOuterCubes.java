package com.blamejared.fluxedcrystals.client.model.pylon;

import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;

public class ModelPylonOuterCubes extends ModelBase {
	
	private ModelRenderer Elem15;//Cube
	private ModelRenderer Elem14;//Cube
	private ModelRenderer Elem13;//Cube
	private ModelRenderer Elem12;//Cube
	private ModelRenderer Elem11;//Cube
	private ModelRenderer Elem10;//Cube
	private ModelRenderer Elem7;//Cube
	private ModelRenderer Elem6;//Cube
	private ModelRenderer Elem5;//Cube
	
	public ModelPylonOuterCubes() {
		textureWidth = 64;
		textureHeight = 32;
		
		//Cube
		Elem15 = new ModelRenderer(this, 0, 0);
		Elem15.addBox(0F, 0F, 0F, 2, 2, 2);
		Elem15.setRotationPoint(2F, 12F, 3F);
		Elem15.setTextureSize(64, 32);
		Elem15.setTextureOffset(16,0);
		setRotation(Elem15, -0F, -0F, -0F);
		//Elem15.rotateAngleX = -0F;
		//Elem15.rotateAngleY = -0F;
		//Elem15.rotateAngleZ = -0F;
		//Cube
		Elem14 = new ModelRenderer(this, 0, 0);
		Elem14.addBox(0F, 0F, 0F, 2, 2, 2);
		Elem14.setRotationPoint(-4.5F, 19F, 2.5F);
		Elem14.setTextureSize(64, 32);
		Elem14.setTextureOffset(14,0);
		setRotation(Elem14, -0F, -0F, -0F);
		//Elem14.rotateAngleX = -0F;
		//Elem14.rotateAngleY = -0F;
		//Elem14.rotateAngleZ = -0F;
		//Cube
		Elem13 = new ModelRenderer(this, 0, 0);
		Elem13.addBox(0F, 0F, 0F, 1, 1, 1);
		Elem13.setRotationPoint(3F, 18F, 2.5F);
		Elem13.setTextureSize(64, 32);
		Elem13.setTextureOffset(24,0);
		setRotation(Elem13, -0F, -0F, -0F);
		//Elem13.rotateAngleX = -0F;
		//Elem13.rotateAngleY = -0F;
		//Elem13.rotateAngleZ = -0F;
		//Cube
		Elem12 = new ModelRenderer(this, 0, 0);
		Elem12.addBox(0F, 0F, 0F, 2, 2, 2);
		Elem12.setRotationPoint(2.5F, 11.5F, -5F);
		Elem12.setTextureSize(64, 32);
		Elem12.setTextureOffset(0,0);
		setRotation(Elem12, -0F, -0F, -0F);
		//Elem12.rotateAngleX = -0F;
		//Elem12.rotateAngleY = -0F;
		//Elem12.rotateAngleZ = -0F;
		//Cube
		Elem11 = new ModelRenderer(this, 0, 0);
		Elem11.addBox(0F, 0F, 0F, 1, 1, 1);
		Elem11.setRotationPoint(-3.5F, 12.5F, 3F);
		Elem11.setTextureSize(64, 32);
		setRotation(Elem11, -0F, -0F, -0F);
		//Elem11.rotateAngleX = -0F;
		//Elem11.rotateAngleY = -0F;
		//Elem11.rotateAngleZ = -0F;
		//Cube
		Elem10 = new ModelRenderer(this, 0, 0);
		Elem10.addBox(0F, 0F, 0F, 2, 2, 2);
		Elem10.setRotationPoint(4F, 19F, -4F);
		Elem10.setTextureSize(64, 32);
		Elem10.setTextureOffset(32,0);
		setRotation(Elem10, -0F, -0F, -0F);
		//Elem10.rotateAngleX = -0F;
		//Elem10.rotateAngleY = -0F;
		//Elem10.rotateAngleZ = -0F;
		//Cube
		Elem7 = new ModelRenderer(this, 0, 0);
		Elem7.addBox(0F, 0F, 0F, 2, 2, 2);
		Elem7.setRotationPoint(-5F, 15.5F, -4.5F);
		Elem7.setTextureSize(64, 32);
		Elem7.setTextureOffset(40,0);
		setRotation(Elem7, -0F, -0F, -0F);
		//Elem7.rotateAngleX = -0F;
		//Elem7.rotateAngleY = -0F;
		//Elem7.rotateAngleZ = -0F;
		//Cube
		Elem6 = new ModelRenderer(this, 0, 0);
		Elem6.addBox(0F, 0F, 0F, 2, 2, 2);
		Elem6.setRotationPoint(-1F, 20F, -1F);
		Elem6.setTextureSize(64, 32);
		Elem6.setTextureOffset(48,0);
		setRotation(Elem6, -0F, -0F, -0F);
		//Elem6.rotateAngleX = -0F;
		//Elem6.rotateAngleY = -0F;
		//Elem6.rotateAngleZ = -0F;
		//Cube
		Elem5 = new ModelRenderer(this, 0, 0);
		Elem5.addBox(0F, 0F, 0F, 2, 2, 2);
		Elem5.setRotationPoint(-1F, 10F, -1F);
		Elem5.setTextureSize(64, 32);
		Elem5.setTextureOffset(0,0);
		setRotation(Elem5, -0F, -0F, -0F);
		//Elem5.rotateAngleX = -0F;
		//Elem5.rotateAngleY = -0F;
		//Elem5.rotateAngleZ = -0F;
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Elem15.render(f5);//Cube
		Elem14.render(f5);//Cube
		Elem13.render(f5);//Cube
		Elem12.render(f5);//Cube
		Elem11.render(f5);//Cube
		Elem10.render(f5);//Cube
		Elem7.render(f5);//Cube
		Elem6.render(f5);//Cube
		Elem5.render(f5);//Cube
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void render(float f5) {
		render(null, 0, 0, 0, 0, 0, f5);
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
	
}
