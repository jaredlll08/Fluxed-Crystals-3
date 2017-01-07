package com.blamejared.fluxedcrystals.network.tiles;

import com.blamejared.fluxedcrystals.tileentities.crystal.TileEntityCrystalCluster;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.server.FMLServerHandler;

public class MessageCrystalClusterSyncClient implements IMessage, IMessageHandler<MessageCrystalClusterSyncClient, IMessage> {
	
	private BlockPos pos;
	private int dimension;
	
	public MessageCrystalClusterSyncClient() {
		
	}
	
	public MessageCrystalClusterSyncClient(TileEntityCrystalCluster tile) {
		this.pos = tile.getPos();
		this.dimension = tile.getWorld().provider.getDimension();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.dimension = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeInt(this.dimension);
	}
	
	
	@Override
	public IMessage onMessage(MessageCrystalClusterSyncClient message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
//		if(FMLServerHandler.instance().getServer() != null)
//			FMLServerHandler.instance().getServer().addScheduledTask(() -> handle(message, ctx));
		return null;
	}
	
	private void handle(MessageCrystalClusterSyncClient message, MessageContext ctx) {
		if(FMLServerHandler.instance().getServer().worldServers[message.dimension] != null) {
			
			TileEntity tileEntity = FMLServerHandler.instance().getServer().worldServers[message.dimension].getTileEntity(message.pos);
			if(tileEntity instanceof TileEntityCrystalCluster) {
				TileEntityCrystalCluster tile = (TileEntityCrystalCluster) tileEntity;
				tile.updated = true;
			}
		}
	}
}
