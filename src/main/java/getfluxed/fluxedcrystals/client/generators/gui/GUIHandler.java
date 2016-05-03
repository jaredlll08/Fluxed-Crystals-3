package getfluxed.fluxedcrystals.client.generators.gui;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.client.generators.gui.coalGenerator.ContainerCoalGenerator;
import getfluxed.fluxedcrystals.client.generators.gui.coalGenerator.GUICoalGenerator;
import getfluxed.fluxedcrystals.client.generators.gui.trashGenerator.ContainerTrashGenerator;
import getfluxed.fluxedcrystals.client.generators.gui.trashGenerator.GuiTrashGenerator;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityCoalGenerator;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityTrashGenerator;
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
		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
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

		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
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
		}
		return null;
	}

}
