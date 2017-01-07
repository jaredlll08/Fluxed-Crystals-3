package com.blamejared.fluxedcrystals.tileentities.crystal;

import com.blamejared.fluxedcrystals.FluxedCrystals;
import com.blamejared.fluxedcrystals.api.crystals.Crystal;
import com.blamejared.fluxedcrystals.api.harvestable.IHarvestable;
import com.blamejared.fluxedcrystals.network.tiles.MessageCrystalClusterSync;
import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.*;

public class TileEntityCrystalCluster extends TileEntityBase implements IHarvestable, ITickable {
	
	private Crystal crystalType;
	private int amount;
	
	public boolean updated = false;
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		if(!data.getCompoundTag("crystal").hasNoTags())
			this.crystalType = Crystal.readFromNBT(data.getCompoundTag("crystal"));
		this.amount = data.getInteger("amount");
		this.updated = data.getBoolean("updated");
		
	}
	
	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		if(crystalType != null)
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
	
	
	@Nonnull
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(super.getUpdateTag());
	}
	
	@Override
	public void handleUpdateTag(@Nonnull NBTTagCompound data) {
		super.handleUpdateTag(data);
		readFromNBT(data);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public void markDirty() {
//		FluxedCrystals.INSTANCE.getPacketHandler().sendToAllAround(new MessageCrystalClusterSync(this), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
		super.markDirty();
	}
	
	@Override
	public boolean isHarvestable() {
		return false;
	}
	
	@Override
	public void update() {
	}
	
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		System.out.println(packet.getNbtCompound());
		readFromNBT(packet.getNbtCompound());
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound data = new NBTTagCompound();
		this.writeToNBT(data);
		System.out.println(data);
		return new SPacketUpdateTileEntity(this.getPos(), 0, data);
	}
}
