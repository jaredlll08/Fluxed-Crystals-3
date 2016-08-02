package getfluxed.fluxedcrystals.network.messages.tiles;

import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGHLoadAll implements IMessage, IMessageHandler<MessageGHLoadAll, IMessage> {

    MultiBlock tiles;
    private BlockPos master;

    public MessageGHLoadAll() {

    }


    public MessageGHLoadAll(BlockPos master, MultiBlock tiles) {
        this.master = master;
        this.tiles = tiles;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.master = BlockPos.fromLong(buf.readLong());
        this.tiles = MultiBlock.readFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(master.toLong());
        this.tiles.writeToByteBuf(buf, tiles);
    }


    @Override
    public IMessage onMessage(MessageGHLoadAll message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(MessageGHLoadAll message, MessageContext ctx) {
        for (BlockPos bp : message.tiles.getBottomLayer()) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(bp);
            if (tileEntity instanceof TileEntityMultiBlockComponent) {
                TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) tileEntity;
                tile.setMaster(message.master);
            }
        }
        for (BlockPos bp : message.tiles.getTopLayer()) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(bp);
            if (tileEntity instanceof TileEntityMultiBlockComponent) {
                TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) tileEntity;
                tile.setMaster(message.master);
            }
        }
        for (BlockPos bp : message.tiles.getSides()) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(bp);
            if (tileEntity instanceof TileEntityMultiBlockComponent) {
                TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) tileEntity;
                tile.setMaster(message.master);
            }
        }
    }

}
