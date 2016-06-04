package getfluxed.fluxedcrystals.network.messages.tiles;

import getfluxed.fluxedcrystals.api.crystals.CrystalInfo;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
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
    private int current;
    private int maxCapacity;
    private boolean hasCrystalIO;
    private CrystalInfo crystalInfo;

    public MessageControllerSync() {

    }

    public MessageControllerSync(TileEntitySoilController tile) {

        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.multiBlock = tile.getMultiBlock();
        this.current = tile.getEnergyStorage().getMaxEnergyStored();
        this.maxCapacity = tile.getEnergyStorage().getMaxEnergyStored();
        this.hasCrystalIO = tile.getItems().length != 0;
        this.crystalInfo = tile.getCrystalInfo();
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();

        this.current = buf.readInt();
        this.maxCapacity = buf.readInt();
        this.hasCrystalIO = buf.readBoolean();
        this.crystalInfo = CrystalInfo.readFromByteBuf(buf);
        this.multiBlock = MultiBlock.readFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);


        buf.writeInt(current);
        buf.writeInt(maxCapacity);
        buf.writeBoolean(hasCrystalIO);
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
                tile.getEnergyStorage().setCapacity(message.maxCapacity);
                tile.getEnergyStorage().receiveEnergy(message.current, false);
                if (tile.tank != null)
                    tile.tank.setCapacity(message.multiBlock.getAirBlocks().size() * 16000);
                if (message.hasCrystalIO) {
                    if (tile.getItems().length != 2) {
                        tile.setItems(new ItemStack[2]);
                    }
                } else {
                    if (tile.getItems().length == 2) {
                        tile.setItems(new ItemStack[0]);
                    }
                }
                tile.setCrystalInfo(message.crystalInfo);
            }
        }
    }
}
