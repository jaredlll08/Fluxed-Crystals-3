package getfluxed.fluxedcrystals.network.messages.tiles.greenhouse.io;

import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCrystalIO implements IMessage, IMessageHandler<MessageCrystalIO, IMessage> {

    private int x;
    private int y;
    private int z;
    private boolean growing;
    private double growthTime;
    private Crystal crystalInfo;

    public MessageCrystalIO() {

    }

    public MessageCrystalIO(TileEntitySoilController tile) {

        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.growing = tile.isGrowing();
        this.growthTime = tile.getCurrentGrowth();
        this.crystalInfo = tile.getCrystalInfo();
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();

        this.growing = buf.readBoolean();
        this.growthTime = buf.readDouble();
        this.crystalInfo = Crystal.readFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);

        buf.writeBoolean(this.growing);
        buf.writeDouble(this.growthTime);
        crystalInfo.writeToByteBuf(buf);
    }


    @Override
    public IMessage onMessage(MessageCrystalIO message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(MessageCrystalIO message, MessageContext ctx) {
        if (FMLClientHandler.instance().getClient().theWorld != null) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if (tileEntity instanceof TileEntitySoilController) {
                TileEntitySoilController tile = (TileEntitySoilController) tileEntity;
                tile.setGrowing(message.growing);
                tile.setCurrentGrowth(message.growthTime);
                tile.setCrystalInfo(message.crystalInfo);
            }
        }
    }
}
