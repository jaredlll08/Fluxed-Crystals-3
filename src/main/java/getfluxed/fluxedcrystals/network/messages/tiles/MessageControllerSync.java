package getfluxed.fluxedcrystals.network.messages.tiles;

import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageControllerSync implements IMessage, IMessageHandler<MessageControllerSync, IMessage> {

    private int x;
    private int y;
    private int z;
    private MultiBlock multiBlock;
    private String fluid;
    private int fluidAmount;
    private long current;
    private long maxCapacity;
    private Crystal crystalInfo;

    public MessageControllerSync() {

    }

    public MessageControllerSync(TileEntitySoilController tile) {
        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.multiBlock = tile.getMultiBlock();
        this.fluid = tile.tank.getFluid() != null ? FluidRegistry.getFluidName(tile.tank.getFluid()) : "Empty";
        this.fluidAmount = tile.tank.getFluidAmount();
        this.current = tile.container.getStoredPower();
        this.maxCapacity = tile.container.getCapacity();
        this.crystalInfo = tile.getCrystalInfo();

    }

    @Override
    public void fromBytes(ByteBuf buf) {

        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.fluid = ByteBufUtils.readUTF8String(buf);
        this.fluidAmount = buf.readInt();
        this.current = buf.readLong();
        this.maxCapacity = buf.readLong();
        this.crystalInfo = Crystal.readFromByteBuf(buf);
        this.multiBlock = MultiBlock.readFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        ByteBufUtils.writeUTF8String(buf, fluid);
        buf.writeInt(this.fluidAmount);
        buf.writeLong(current);
        buf.writeLong(maxCapacity);
        crystalInfo.writeToByteBuf(buf);
        MultiBlock.writeToByteBuf(buf, multiBlock);
    }


    @Override
    public IMessage onMessage(MessageControllerSync message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(MessageControllerSync message, MessageContext ctx) {
        if (FMLClientHandler.instance().getClient().theWorld != null) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));

            if (tileEntity instanceof TileEntitySoilController) {
                TileEntitySoilController tile = (TileEntitySoilController) tileEntity;
                tile.setMultiBlock(message.multiBlock);
                tile.container.setCapacity(message.maxCapacity);
                tile.container.givePower(message.current, false);
                if (tile.tank != null) {
                    tile.tank.setCapacity(message.multiBlock.getAirBlocks().size() * 16000);
                    if (!message.fluid.equals("Empty")) {
                        tile.tank.setFluid(FluidRegistry.getFluidStack(message.fluid, message.fluidAmount));
                    }
                }
                tile.setCrystalInfo(message.crystalInfo);
            }
        }
    }
}
