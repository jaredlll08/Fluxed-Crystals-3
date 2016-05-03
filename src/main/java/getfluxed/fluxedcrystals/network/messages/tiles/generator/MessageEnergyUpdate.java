package getfluxed.fluxedcrystals.network.messages.tiles.generator;

import getfluxed.fluxedcrystals.util.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageEnergyUpdate implements IMessage, IMessageHandler<MessageEnergyUpdate, IMessage> {
	public int x, y, z;
	public int stored;
	public int mana;

	public MessageEnergyUpdate() {
	}

	public MessageEnergyUpdate(int x, int y, int z, int stored) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.stored = stored;
	}

	public MessageEnergyUpdate(int x, int y, int z, int stored, int mana) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.stored = stored;
		this.mana = mana;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.stored = buf.readInt();
		this.mana = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(stored);
		buf.writeInt(mana);
	}

	@Override
	public IMessage onMessage(MessageEnergyUpdate message, MessageContext ctx) {
		ClientUtils.updateEnergy(message);
		return null;
	}
}
