package getfluxed.fluxedcrystals.network;


import getfluxed.fluxedcrystals.network.messages.tiles.MessageControllerSync;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageFluid;
import getfluxed.fluxedcrystals.network.messages.tiles.MessageGHLoad;
import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageEnergyUpdate;
import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageGenerator;
import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageGeneratorFluid;
import getfluxed.fluxedcrystals.network.messages.tiles.machines.MessageCrusher;
import getfluxed.fluxedcrystals.network.messages.tiles.machines.MessageFurnace;
import getfluxed.fluxedcrystals.network.messages.tiles.machines.MessageMachineBase;
import getfluxed.fluxedcrystals.network.messages.tiles.machines.MessageSawmill;
import getfluxed.fluxedcrystals.reference.Reference;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.modid);
    public static int ID = 0;

    public static void preInit() {
        INSTANCE.registerMessage(MessageControllerSync.class, MessageControllerSync.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageFluid.class, MessageFluid.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageGHLoad.class, MessageGHLoad.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageGeneratorFluid.class, MessageGeneratorFluid.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageGenerator.class, MessageGenerator.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageEnergyUpdate.class, MessageEnergyUpdate.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageCrusher.class, MessageCrusher.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageFurnace.class, MessageFurnace.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageSawmill.class, MessageSawmill.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageMachineBase.class, MessageMachineBase.class, ID++, Side.CLIENT);



    }

}
