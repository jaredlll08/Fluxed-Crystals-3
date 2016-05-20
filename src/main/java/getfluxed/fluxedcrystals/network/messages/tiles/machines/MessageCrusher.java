package getfluxed.fluxedcrystals.network.messages.tiles.machines;

import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineCrusher;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Jared on 5/13/2016.
 */
public class MessageCrusher implements IMessage, IMessageHandler<MessageCrusher, IMessage> {

    private int x;
    private int y;
    private int z;
    private byte state;
    private int needCycleTime;
    private int itemCycleTime;
    private int deviceCycleTime;
    private int energy;

    public MessageCrusher() {

    }

    public MessageCrusher(TileEntityMachineCrusher tileEntityGemCutter) {

        this.x = tileEntityGemCutter.getPos().getX();
        this.y = tileEntityGemCutter.getPos().getY();
        this.z = tileEntityGemCutter.getPos().getZ();
        this.state = tileEntityGemCutter.state;
        this.needCycleTime = tileEntityGemCutter.needCycleTime;
        this.itemCycleTime = tileEntityGemCutter.itemCycleTime;
        this.deviceCycleTime = tileEntityGemCutter.deviceCycleTime;
        this.energy = tileEntityGemCutter.getEnergyStored();

    }

    @Override
    public void fromBytes(ByteBuf buf) {

        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();

        this.state = buf.readByte();

        this.needCycleTime = buf.readInt();
        this.itemCycleTime = buf.readInt();
        this.deviceCycleTime = buf.readInt();
        this.energy = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);

        buf.writeByte(this.state);

        buf.writeInt(this.needCycleTime);
        buf.writeInt(this.itemCycleTime);
        buf.writeInt(this.deviceCycleTime);

        buf.writeInt(this.energy);
    }

    @Override
    public IMessage onMessage(MessageCrusher message, MessageContext ctx) {

        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));

        if (tileEntity instanceof TileEntityMachineCrusher) {

            ((TileEntityMachineCrusher) tileEntity).state = message.state;
            ((TileEntityMachineCrusher) tileEntity).needCycleTime = message.needCycleTime;
            ((TileEntityMachineCrusher) tileEntity).itemCycleTime = message.itemCycleTime;
            ((TileEntityMachineCrusher) tileEntity).deviceCycleTime = message.deviceCycleTime;
            ((TileEntityMachineCrusher) tileEntity).setEnergyStored(message.energy);


        }

        return null;

    }

}
