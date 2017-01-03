package com.blamejared.fluxedcrystals.network.tiles;

import com.blamejared.fluxedcrystals.api.crystals.Crystal;
import com.blamejared.fluxedcrystals.tileentities.crystal.TileEntityCrystalCluster;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageCrystalClusterSync implements IMessage, IMessageHandler<MessageCrystalClusterSync, IMessage> {
	
	private BlockPos pos;
	private Crystal crystal;
	private int amount;
	
	public MessageCrystalClusterSync() {
		
	}
	
	public MessageCrystalClusterSync(TileEntityCrystalCluster tile) {
		this.pos = tile.getPos();
		this.crystal = tile.getCrystalType();
		this.amount = tile.getAmount();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		NBTTagCompound crystal = ByteBufUtils.readTag(buf);
		if(!crystal.hasNoTags()) {
			this.crystal = Crystal.readFromNBT(crystal);
		}
		this.amount = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		ByteBufUtils.writeTag(buf, this.crystal == null ? new NBTTagCompound() : this.crystal.writeToNBT(new NBTTagCompound()));
		buf.writeInt(amount);
	}
	
	
	@Override
	public IMessage onMessage(MessageCrystalClusterSync message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
		return null;
	}
	
	private void handle(MessageCrystalClusterSync message, MessageContext ctx) {
		if(FMLClientHandler.instance().getClient().theWorld != null) {
			
			TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.pos);
			if(tileEntity instanceof TileEntityCrystalCluster) {
				TileEntityCrystalCluster tile = (TileEntityCrystalCluster) tileEntity;
				tile.setCrystalType(message.crystal);
				tile.setAmount(message.amount);
				FMLClientHandler.instance().getClient().theWorld.markBlockRangeForRenderUpdate(message.pos, message.pos);
			}
		}
	}
}
