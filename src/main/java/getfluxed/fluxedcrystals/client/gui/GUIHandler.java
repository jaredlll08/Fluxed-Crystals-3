package getfluxed.fluxedcrystals.client.gui;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.api.client.gui.IOpenableGUI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
        BlockPos blockPos = new BlockPos(x, y, z);
        IOpenableGUI openableGUI = this.getOpenableGUI(ID, player, world, blockPos);
        return openableGUI != null ? openableGUI.getServerGuiElement(ID, player, world, blockPos) : null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        IOpenableGUI openableGUI = this.getOpenableGUI(ID, player, world, blockPos);
        return openableGUI != null ? openableGUI.getClientGuiElement(ID, player, world, blockPos) : null;
    }

    private IOpenableGUI getOpenableGUI(int id, EntityPlayer player, World world, BlockPos blockPos) {
        IOpenableGUI openableGUI = null;
        Entity entity = world.getEntityByID(id);
        if(entity instanceof IOpenableGUI) {
            openableGUI = (IOpenableGUI)entity;
        } else {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if(tileEntity instanceof IOpenableGUI) {
                openableGUI = (IOpenableGUI)tileEntity;
            } else {
                ItemStack heldItemMainhand =  player.getHeldItemMainhand();
                if(heldItemMainhand != null && heldItemMainhand.getItem() instanceof IOpenableGUI) {
                    openableGUI = (IOpenableGUI)heldItemMainhand.getItem();
                } else {
                    ItemStack heldItemOffhand = player.getHeldItemOffhand();
                    if(heldItemOffhand != null && heldItemOffhand.getItem() instanceof IOpenableGUI) {
                        openableGUI = (IOpenableGUI)heldItemOffhand.getItem();
                    }
                }
            }
        }

        return openableGUI;
    }

}
