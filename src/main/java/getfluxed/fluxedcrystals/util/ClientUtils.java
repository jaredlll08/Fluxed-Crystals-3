package getfluxed.fluxedcrystals.util;

import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageEnergyUpdate;
import getfluxed.fluxedcrystals.tileentities.base.TileEnergyBase;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class ClientUtils {

	public static void updateEnergy(MessageEnergyUpdate message) {
		TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
		if (te instanceof TileEnergyBase) {
			((TileEnergyBase) te).setEnergyStored(message.stored);
		}
	}

}
