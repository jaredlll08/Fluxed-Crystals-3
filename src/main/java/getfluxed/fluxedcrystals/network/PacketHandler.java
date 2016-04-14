package getfluxed.fluxedcrystals.network;


import getfluxed.fluxedcrystals.network.messages.tiles.MessageControllerSync;
import getfluxed.fluxedcrystals.reference.Reference;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.modid);
    public static int ID = 0;

    public static void preInit() {
        INSTANCE.registerMessage(MessageControllerSync.class, MessageControllerSync.class, ID++, Side.CLIENT);

    }

}
