package com.blamejared.fluxedcrystals.network.tiles;

import com.blamejared.fluxedcrystals.api.crystals.CrystalOre;
import com.blamejared.fluxedcrystals.tileentities.crystal.TileEntityCrystal;
import com.google.common.collect.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageCrystalSync implements IMessage, IMessageHandler<MessageCrystalSync, IMessage> {
	
	private BlockPos pos;
	private CrystalOre selectedType;
	private CrystalOre currentType;
	private float selectedCost;
	private float currentCost;
	private BiMap<BlockPos, CrystalOre> stateCache = HashBiMap.create();
	private boolean stateCacheDepleted;
	private boolean shouldPlaceCrystals;
	
	public MessageCrystalSync() {
		
	}
	
	public MessageCrystalSync(TileEntityCrystal tile) {
		this.pos = tile.getPos();
		this.selectedType = tile.getSelectedType();
		this.currentType = tile.getCurrentType();
		this.selectedCost = tile.getSelectedTypeCost();
		this.currentCost = tile.getCurrentCost();
		this.stateCache = tile.getStateCache();
		this.stateCacheDepleted = tile.isStateCacheDepleted();
		this.shouldPlaceCrystals = tile.isShouldPlaceCrystals();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.stateCacheDepleted = buf.readBoolean();
		NBTTagCompound selected = ByteBufUtils.readTag(buf);
		NBTTagCompound current = ByteBufUtils.readTag(buf);
		if(!selected.hasNoTags()) {
			this.selectedType = CrystalOre.readFromNBT(selected);
		}
		if(!current.hasNoTags()) {
			this.currentType = CrystalOre.readFromNBT(current);
		}
		this.selectedCost = buf.readFloat();
		this.currentCost = buf.readFloat();
		int size = buf.readInt();
		for(int i = 0; i < size; i++) {
			stateCache.put(BlockPos.fromLong(buf.readLong()), CrystalOre.readFromNBT(ByteBufUtils.readTag(buf)));
		}
		this.shouldPlaceCrystals = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeBoolean(this.stateCacheDepleted);
		ByteBufUtils.writeTag(buf, this.selectedType == null ? new NBTTagCompound() : this.selectedType.writeToNBT(new NBTTagCompound()));
		ByteBufUtils.writeTag(buf, this.currentType == null ? new NBTTagCompound() : this.currentType.writeToNBT(new NBTTagCompound()));
		buf.writeFloat(this.selectedCost);
		buf.writeFloat(this.currentCost);
		
		buf.writeInt(this.stateCache.size());
		stateCache.forEach((key, val) -> {
			buf.writeLong(key.toLong());
			ByteBufUtils.writeTag(buf, val.writeToNBT(new NBTTagCompound()));
		});
		
		buf.writeBoolean(this.shouldPlaceCrystals);
		
	}
	
	
	@Override
	public IMessage onMessage(MessageCrystalSync message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
		return null;
	}
	
	private void handle(MessageCrystalSync message, MessageContext ctx) {
		if(FMLClientHandler.instance().getClient().theWorld != null) {
			TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.pos);
			
			if(tileEntity instanceof TileEntityCrystal) {
				TileEntityCrystal tile = (TileEntityCrystal) tileEntity;
				tile.setCurrentCost(message.currentCost);
				tile.setCurrentType(message.currentType);
				tile.setSelectedType(message.selectedType);
				tile.setSelectedTypeCost(message.selectedCost);
				tile.setStateCache(message.stateCache);
				tile.setStateCacheDepleted(message.stateCacheDepleted);
				tile.setShouldPlaceCrystals(message.shouldPlaceCrystals);
			}
		}
	}
}
