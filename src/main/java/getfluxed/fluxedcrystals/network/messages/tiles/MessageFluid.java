package getfluxed.fluxedcrystals.network.messages.tiles;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
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

public class MessageFluid implements IMessage, IMessageHandler<MessageFluid, IMessage> {
    public String fluid;
    public int amount;
    public BlockPos pos;

    public MessageFluid() {
    }

    public MessageFluid(TileEntitySoilController tile) {
        this.fluid = "";
        if (tile.tank.getFluid() != null)
            this.fluid = tile.tank.getFluid().getFluid().getName();
        this.amount = tile.tank.getFluidAmount();
        this.pos = tile.getPos();
    }

    public MessageFluid(String fluid, int amount, BlockPos pos) {
        this.fluid = fluid;
        this.amount = amount;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.fluid = ByteBufUtils.readUTF8String(buf);
        this.amount = buf.readInt();

        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, fluid);
        buf.writeInt(amount);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());

    }

    @Override
    public IMessage onMessage(MessageFluid message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(MessageFluid message, MessageContext ctx) {
        if (FMLClientHandler.instance().getClient().theWorld != null) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.pos);

            if (tileEntity instanceof TileEntitySoilController) {
                Fluid fluid;
                FluidStack stack = null;
                if (!message.fluid.isEmpty()) {
                    fluid = FluidRegistry.getFluid(message.fluid);
                    stack = new FluidStack(fluid, message.amount);
                }
                ((TileEntitySoilController) tileEntity).tank.setFluid(stack);

            }
        }
    }


}
