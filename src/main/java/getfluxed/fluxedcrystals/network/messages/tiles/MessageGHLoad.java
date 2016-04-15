package getfluxed.fluxedcrystals.network.messages.tiles;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGHLoad implements IMessage, IMessageHandler<MessageGHLoad, IMessage> {

    private int x;
    private int y;
    private int z;
    private BlockPos master;

    public MessageGHLoad() {

    }

    public MessageGHLoad(TileEntityMultiBlockComponent tile) {

        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.master = tile.getMaster();
    }

    public MessageGHLoad(TileEntityMultiBlockComponent tile, BlockPos master) {

        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.master = master;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.master = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(master != null ? master.getX() : 0);
        buf.writeInt(master != null ? master.getY() : 0);
        buf.writeInt(master != null ? master.getZ() : 0);

    }


    @Override
    public IMessage onMessage(MessageGHLoad message, MessageContext ctx) {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
        if (tileEntity instanceof TileEntityMultiBlockComponent) {

            TileEntityMultiBlockComponent tile = (TileEntityMultiBlockComponent) tileEntity;
            tile.setMaster(message.master);

        }

        return null;
    }

}
