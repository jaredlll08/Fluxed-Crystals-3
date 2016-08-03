package getfluxed.fluxedcrystals.network.messages.tiles.machines;

import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineBase;
import io.netty.buffer.ByteBuf;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Jared on 5/31/2016.
 */
public class MessageMachineBase implements IMessage, IMessageHandler<MessageMachineBase, IMessage> {

    private int x;
    private int y;
    private int z;
    private byte state;
    private int needCycleTime;
    private int itemCycleTime;
    private int deviceCycleTime;
    private long energy;

    public MessageMachineBase() {

    }

    public MessageMachineBase(TileEntityMachineBase tile) {

        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.state = tile.state;
        this.needCycleTime = tile.needCycleTime;
        this.itemCycleTime = tile.itemCycleTime;
        this.deviceCycleTime = tile.deviceCycleTime;
        this.energy = tile.container.getStoredPower();

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
        this.energy = buf.readLong();

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
        buf.writeLong(this.energy);
    }

    @Override
    public IMessage onMessage(MessageMachineBase message, MessageContext ctx) {

        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));

        return null;
    }

    private void handle(MessageMachineBase message, MessageContext ctx) {
        if (FMLClientHandler.instance().getClient().theWorld != null) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if (tileEntity instanceof TileEntityMachineBase) {
                ((TileEntityMachineBase) tileEntity).state = message.state;
                ((TileEntityMachineBase) tileEntity).needCycleTime = message.needCycleTime;
                ((TileEntityMachineBase) tileEntity).itemCycleTime = message.itemCycleTime;
                ((TileEntityMachineBase) tileEntity).deviceCycleTime = message.deviceCycleTime;
                long cap = ((TileEntityMachineBase) tileEntity).container.getCapacity();
                long input = ((TileEntityMachineBase) tileEntity).container.getInputRate();
                long output = ((TileEntityMachineBase) tileEntity).container.getOutputRate();
                ((TileEntityMachineBase) tileEntity).container = new BaseTeslaContainer(message.energy, cap, input, output);
            }
        }
    }

}
