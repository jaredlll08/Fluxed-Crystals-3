package getfluxed.fluxedcrystals.network.messages.tiles.generator;

import getfluxed.fluxedcrystals.api.generators.generators.FluidGeneratorBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGeneratorFluid implements IMessage, IMessageHandler<MessageGeneratorFluid, IMessage> {
    public String fluid;
    public int amount;
    public int x, y, z;

    public MessageGeneratorFluid() {
    }


    public MessageGeneratorFluid(FluidGeneratorBase tile) {
        this.fluid = "";
        if (tile.tank.getFluid() != null)
            this.fluid = FluidRegistry.getFluidName(tile.tank.getFluid().getFluid());
        this.amount = tile.tank.getFluidAmount();
        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
    }

    public MessageGeneratorFluid(String fluid, int amount, int x, int y, int z) {
        this.fluid = fluid;
        this.amount = amount;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.fluid = ByteBufUtils.readUTF8String(buf);
        this.amount = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, fluid);
        buf.writeInt(amount);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);

    }

    @Override
    public IMessage onMessage(MessageGeneratorFluid message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(MessageGeneratorFluid message, MessageContext ctx) {
        if (FMLClientHandler.instance().getClient().theWorld != null) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));

            if (tileEntity instanceof FluidGeneratorBase) {
                Fluid fluid;
                FluidStack stack = null;
                if (!message.fluid.isEmpty()) {
                    fluid = FluidRegistry.getFluid(message.fluid);
                    stack = new FluidStack(fluid, message.amount);
                }
                ((FluidGeneratorBase) tileEntity).tank.setFluid(stack);

            }
        }
    }


}
