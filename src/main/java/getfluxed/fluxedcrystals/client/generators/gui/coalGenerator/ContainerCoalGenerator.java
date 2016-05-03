package getfluxed.fluxedcrystals.client.generators.gui.coalGenerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import getfluxed.fluxedcrystals.client.generators.gui.slot.SlotTileDep;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityCoalGenerator;

public class ContainerCoalGenerator extends Container {
	public ContainerCoalGenerator(InventoryPlayer invPlayer, TileEntityCoalGenerator tile) {

		addSlotToContainer(new SlotTileDep(tile, 0, 80, 12));

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 142));
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, 84 + y * 18));
			}
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		return null;
	}

}
