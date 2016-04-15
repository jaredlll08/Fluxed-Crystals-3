package getfluxed.fluxedcrystals.network.messages.tiles;

import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageControllerSync implements IMessage, IMessageHandler<MessageControllerSync, IMessage> {

    private int x;
    private int y;
    private int z;
    private MultiBlock multiBlock;

    public MessageControllerSync() {

    }

    public MessageControllerSync(TileEntitySoilController tile) {

        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.multiBlock = tile.getMultiBlock();
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.multiBlock = MultiBlock.readFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);

        MultiBlock.writeToByteBuf(buf, multiBlock);
    }


    @Override
    public IMessage onMessage(MessageControllerSync message, MessageContext ctx) {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));

        if (tileEntity instanceof TileEntitySoilController) {
            TileEntitySoilController tile = (TileEntitySoilController) tileEntity;
            tile.setMultiBlock(message.multiBlock);
            if (tile.tank != null)
                tile.tank.setCapacity(message.multiBlock.getAirBlocks().size() * 16000);
        }

        return null;
    }
}
