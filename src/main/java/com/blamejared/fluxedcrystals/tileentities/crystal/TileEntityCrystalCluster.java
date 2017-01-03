package com.blamejared.fluxedcrystals.tileentities.crystal;

import com.blamejared.fluxedcrystals.api.crystals.Crystal;
import com.blamejared.fluxedcrystals.api.harvestable.IHarvestable;
import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class TileEntityCrystalCluster extends TileEntityBase implements IHarvestable {
	
	private Crystal crystalType;
	private int amount;
	
	public boolean updated = false;
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		this.crystalType = Crystal.readFromNBT(data.getCompoundTag("crystal"));
		this.amount = data.getInteger("amount");
		this.updated = data.getBoolean("updated");
		
	}
	
	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		data.setTag("crystal", crystalType.writeToNBT(new NBTTagCompound()));
		data.setInteger("amount", amount);
		data.setBoolean("updated", updated);
		return super.writeToNBT(data);
	}
	
	public Crystal getCrystalType() {
		return crystalType;
	}
	
	public void setCrystalType(Crystal crystalType) {
		this.crystalType = crystalType;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Override
	protected void readFromUpdatePacket(NBTTagCompound data) {
		super.readFromUpdatePacket(data);
		readFromNBT(data);
	}
	
	@Override
	protected NBTTagCompound writeToUpdatePacket(NBTTagCompound data) {
		writeToNBT(data);
		return super.writeToUpdatePacket(data);
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		//		FluxedCrystals.INSTANCE.getPacketHandler().sendToAllAround(new MessageCrystalClusterSync(this), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
	}
	
	@Override
	public boolean isHarvestable() {
		return false;
	}
	
}
