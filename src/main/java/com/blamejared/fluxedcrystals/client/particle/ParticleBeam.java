package com.blamejared.fluxedcrystals.client.particle;

import com.blamejared.fluxedcrystals.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("unused")
public class ParticleBeam extends Particle {
	
	private double offset = 0.0D;
	private double targX = 0.0D;
	private double targY = 0.0D;
	private double targZ = 0.0D;
	private double prevTickX = 0.0D;
	
	private double prevTickY = 0.0D;
	private double prevTickZ = 0.0D;
	private float prevSize = 0.0F;
	private float length = 0.0F;
	private float rotYaw = 0.0F;
	private float rotPitch = 0.0F;
	private float prevYaw = 0.0F;
	private float prevPitch = 0.0F;
	private float endMod = 1.0F;
	private boolean reverse = false;
	private int rotationspeed = 3;
	
	public ParticleBeam(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float red, float green, float blue, int age) {
		super(par1World, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
		setSize(0.02F, 0.02F);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.targX = targetX;
		this.targY = targetY;
		this.targZ = targetZ;
		this.prevYaw = this.rotYaw;
		this.prevPitch = this.rotPitch;
		this.particleMaxAge = age;
		rotationspeed = 5;
		Entity renderentity = FMLClientHandler.instance().getClient().getRenderViewEntity();
		int visibleDistance = 50;
		if(!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
			visibleDistance = 25;
		}
		if(renderentity.getDistance(this.posX, this.posY, this.posZ) > visibleDistance) {
			this.particleMaxAge = 0;
		}
	}
	
	public void onUpdate() {
//		this.prevPosX = this.posX;
//		this.prevPosY = (this.posY + this.offset);
//		this.prevPosZ = this.posZ;
		
//		this.prevTickX = this.targX;
//		this.prevTickY = this.targY;
//		this.prevTickZ = this.targZ;
		
//		this.prevYaw = this.rotYaw;
//		this.prevPitch = this.rotPitch;
		
		float distX = (float) (this.posX - this.targX);
		float distY = (float) (this.posY - this.targY);
		float distZ = (float) (this.posZ - this.targZ);
		double var7 = MathHelper.sqrt_double(distX * distX + distZ * distZ);
		this.rotYaw = ((float) (Math.atan2(distX, distZ) * 180.0D / Math.PI));
		this.rotPitch = ((float) (Math.atan2(distY, var7) * 180.0D / Math.PI));
		this.length = MathHelper.sqrt_float(distX * distX + distY * distY + distZ * distZ);
		this.prevYaw = this.rotYaw;
		this.prevPitch = this.rotPitch;
		if(++this.particleAge >= this.particleMaxAge) {
			setExpired();
		}
	}
	
	@Override
	public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		Tessellator.getInstance().draw();
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		float var9 = 1.0F;
		float slide = Minecraft.getMinecraft().thePlayer.ticksExisted;
		float rotation = worldObj.getTotalWorldTime() / 180;
		float rot = (float) (this.worldObj.provider.getWorldTime() % (360 / this.rotationspeed) * this.rotationspeed) + this.rotationspeed * partialTicks;
		float size = 1F;
		size = this.prevSize + (Math.min(10 / 4.0F, 1.0F) - this.prevSize) * partialTicks;
		float op = 0.4f;
		//		if((this.particleMaxAge - this.particleAge <= 4)) {
		//			op = partialTicks;
		//		}
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID + ":textures/particles/beam.png"));
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glDisable(GL11.GL_CULL_FACE);
		
		float var11 = slide + partialTicks;
		if(this.reverse) {
			var11 *= -1.0F;
		}
		float var12 = var11 * 0.4F - MathHelper.floor_float(-var11 * 0.1F);
		
		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(771, 770);
		
		float xx = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
		float yy = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
		float zz = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
		GL11.glTranslated(xx, yy, zz);
		
		float rotYaw = this.prevYaw + (this.rotYaw - this.prevYaw) * partialTicks;
		float rotPitch = this.prevPitch + (this.rotPitch - this.prevPitch)* 360;
		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(180.0F + rotYaw, 0.0F, 0.0F, -1.0F);
		GL11.glRotatef(rotPitch, 1.0F, 0.0F, 0.0F);
		
		double var44 = -0.08D ;
		double var17 = 0.08D ;
		double var44b = -0.08D  * this.endMod;
		double var17b = 0.08D  * this.endMod;
		
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		
		for(int t = 0; t < 6; t++) {
			GlStateManager.enableNormalize();
			worldRendererIn.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			double var29 = this.length  * var9;
			double var31 = 0.0D;
			double var33 = 1.0D;
			double var35 = -1.0F;
			double var37 = this.length  * var9;
			GL11.glRotatef(30, 0.0F, 1.0F, 0.0F);
			worldRendererIn.pos(var44b, var29, 0).tex(var33, var37).color(this.particleRed, this.particleGreen, this.particleBlue, op).endVertex();
			worldRendererIn.pos(var44, 0, 0).tex(var33, var35).color(this.particleRed, this.particleGreen, this.particleBlue, op).endVertex();
			worldRendererIn.pos(var17, 0, 0).tex(var31, var35).color(this.particleRed, this.particleGreen, this.particleBlue, op).endVertex();
			worldRendererIn.pos(var17b, var29, 0).tex(var31, var37).color(this.particleRed, this.particleGreen, this.particleBlue, op).endVertex();
			Tessellator.getInstance().draw();
		}
	
		GL11.glPopAttrib();
		GL11.glPopMatrix();
		
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(3042);
		GL11.glEnable(2884);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/particle/particles.png"));
		worldRendererIn.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		this.prevSize = size;
	}
	
	public void setRGB(float r, float g, float b) {
		this.particleRed = r;
		this.particleGreen = g;
		this.particleBlue = b;
	}
	
	public void setEndMod(float endMod) {
		this.endMod = endMod;
	}
	
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	
	public void setRotationspeed(int rotationspeed) {
		this.rotationspeed = rotationspeed;
	}
}
