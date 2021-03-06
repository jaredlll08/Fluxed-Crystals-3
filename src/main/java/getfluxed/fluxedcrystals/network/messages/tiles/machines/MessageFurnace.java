package getfluxed.fluxedcrystals.network.messages.tiles.machines;

import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineFurnace;
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
 * Created by Jared on 5/25/2016.
 */
public class MessageFurnace implements IMessage, IMessageHandler<MessageFurnace, IMessage> {

    private int x;
    private int y;
    private int z;
    private byte state;
    private int needCycleTime;
    private int itemCycleTime;
    private int deviceCycleTime;
    private long energy;

    public MessageFurnace() {

    }

    public MessageFurnace(TileEntityMachineFurnace furnace) {

        this.x = furnace.getPos().getX();
        this.y = furnace.getPos().getY();
        this.z = furnace.getPos().getZ();
        this.state = furnace.state;
        this.needCycleTime = furnace.needCycleTime;
        this.itemCycleTime = furnace.itemCycleTime;
        this.deviceCycleTime = furnace.deviceCycleTime;
        this.energy = furnace.container.getStoredPower();

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

        buf.writeLong(this.energy);
    }

    @Override
    public IMessage onMessage(MessageFurnace message, MessageContext ctx) {

        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));

        return null;
    }

    private void handle(MessageFurnace message, MessageContext ctx) {
        if (FMLClientHandler.instance().getClient().theWorld != null) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if (tileEntity instanceof TileEntityMachineFurnace) {
                ((TileEntityMachineFurnace) tileEntity).state = message.state;
                ((TileEntityMachineFurnace) tileEntity).needCycleTime = message.needCycleTime;
                ((TileEntityMachineFurnace) tileEntity).itemCycleTime = message.itemCycleTime;
                ((TileEntityMachineFurnace) tileEntity).deviceCycleTime = message.deviceCycleTime;
                ((TileEntityMachineFurnace) tileEntity).container = new BaseTeslaContainer(message.energy, 10000, 250, 20);


            }
        }
    }

}
