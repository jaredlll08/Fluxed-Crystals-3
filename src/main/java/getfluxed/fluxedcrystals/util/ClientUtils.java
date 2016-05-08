package getfluxed.fluxedcrystals.util;

import cofh.api.energy.IEnergyHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageEnergyUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class ClientUtils {

	public static void updateEnergy(MessageEnergyUpdate message) {
        if(Minecraft.getMinecraft().theWorld !=null) {
            TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if (te instanceof IEnergyHandler) {
                ((IEnergyHandler) te).receiveEnergy(null, message.stored, false);
            }
        }
	}

}
