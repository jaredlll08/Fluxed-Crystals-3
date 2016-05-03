package getfluxed.fluxedcrystals.network.messages.tiles.generator;

import getfluxed.fluxedcrystals.api.generators.generators.GeneratorBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGenerator implements IMessage, IMessageHandler<MessageGenerator, IMessage> {

	private int x;
	private int y;
	private int z;
	private int generationTimer;
	private int generationTimerDefault;
	private int energy;
	public MessageGenerator() {

	}

	public MessageGenerator(GeneratorBase tile) {

		this.x = tile.getPos().getX();
		this.y = tile.getPos().getY();
		this.z = tile.getPos().getZ();
		this.generationTimer = tile.generationTimer;
		this.generationTimerDefault = tile.generationTimerDefault;
		this.energy = tile.getEnergyStored();
	}	

	@Override
	public void fromBytes(ByteBuf buf) {

		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();

		this.generationTimer = buf.readInt();
		this.generationTimerDefault = buf.readInt();
		this.energy = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);

		buf.writeInt(generationTimer);
		buf.writeInt(generationTimerDefault);
		buf.writeInt(energy);
	}

	@Override
	public IMessage onMessage(MessageGenerator message, MessageContext ctx) {

		TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));

		if (tileEntity instanceof GeneratorBase) {

			((GeneratorBase) tileEntity).generationTimer = message.generationTimer;
			((GeneratorBase) tileEntity).generationTimerDefault = message.generationTimerDefault;
			((GeneratorBase) tileEntity).setEnergyStored(message.energy);
			
		}

		return null;

	}

}
