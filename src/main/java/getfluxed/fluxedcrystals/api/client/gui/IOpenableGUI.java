package getfluxed.fluxedcrystals.api.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by alex on 10/06/16.
 */
public interface IOpenableGUI {

    Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos);

    Object getServerGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos);
}
