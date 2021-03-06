package getfluxed.fluxedcrystals.client.gui;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.client.gui.coalGenerator.ContainerCoalGenerator;
import getfluxed.fluxedcrystals.client.gui.coalGenerator.GUICoalGenerator;
import getfluxed.fluxedcrystals.client.gui.crusher.ContainerCrusher;
import getfluxed.fluxedcrystals.client.gui.crusher.GUICrusher;
import getfluxed.fluxedcrystals.client.gui.crystalio.ContainerCrystalIO;
import getfluxed.fluxedcrystals.client.gui.crystalio.GUICrystalIO;
import getfluxed.fluxedcrystals.client.gui.furnace.ContainerFurnace;
import getfluxed.fluxedcrystals.client.gui.furnace.GUIFurnace;
import getfluxed.fluxedcrystals.client.gui.sawmill.ContainerSawmill;
import getfluxed.fluxedcrystals.client.gui.sawmill.GUISawmill;
import getfluxed.fluxedcrystals.client.gui.trashGenerator.ContainerTrashGenerator;
import getfluxed.fluxedcrystals.client.gui.trashGenerator.GuiTrashGenerator;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityCoalGenerator;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityTrashGenerator;
import getfluxed.fluxedcrystals.tileentities.greenhouse.io.TileEntityCrystalIO;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineCrusher;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineFurnace;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineSawmill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GUIHandler implements IGuiHandler {

    public GUIHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(FluxedCrystals.instance, this);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {

            case 0:
                if (te != null && te instanceof TileEntityCoalGenerator) {
                    return new ContainerCoalGenerator(player.inventory, (TileEntityCoalGenerator) te);
                }
                break;
            case 1:
                if (te != null && te instanceof TileEntityTrashGenerator) {
                    return new ContainerTrashGenerator(player.inventory, (TileEntityTrashGenerator) te);
                }
                break;

            case 2:
                if (te != null && te instanceof TileEntityMachineCrusher) {
                    return new ContainerCrusher(player.inventory, (TileEntityMachineCrusher) te);
                }
                break;
            case 3:
                if (te != null && te instanceof TileEntityMachineFurnace) {
                    return new ContainerFurnace(player.inventory, (TileEntityMachineFurnace) te);
                }
                break;
            case 4:
                if (te != null && te instanceof TileEntityMachineSawmill) {
                    return new ContainerSawmill(player.inventory, (TileEntityMachineSawmill) te);
                }
                break;
            case 5:
                if (te != null && te instanceof TileEntityCrystalIO) {
                    return new ContainerCrystalIO(player.inventory, (TileEntityCrystalIO) te);
                }
                break;

        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case 0:
                if (te != null && te instanceof TileEntityCoalGenerator) {
                    return new GUICoalGenerator(player.inventory, (TileEntityCoalGenerator) te);
                }
                break;
            case 1:
                if (te != null && te instanceof TileEntityTrashGenerator) {
                    return new GuiTrashGenerator(player.inventory, (TileEntityTrashGenerator) te);
                }
                break;
            case 2:
                if (te != null && te instanceof TileEntityMachineCrusher) {
                    return new GUICrusher(player.inventory, (TileEntityMachineCrusher) te);
                }
                break;
            case 3:
                if (te != null && te instanceof TileEntityMachineFurnace) {
                    return new GUIFurnace(player.inventory, (TileEntityMachineFurnace) te);
                }
                break;
            case 4:
                if (te != null && te instanceof TileEntityMachineSawmill) {
                    return new GUISawmill(player.inventory, (TileEntityMachineSawmill) te);
                }
                break;
            case 5:
                if (te != null && te instanceof TileEntityCrystalIO) {
                    return new GUICrystalIO(player.inventory, (TileEntityCrystalIO) te);
                }
                break;
        }
        return null;
    }

}
