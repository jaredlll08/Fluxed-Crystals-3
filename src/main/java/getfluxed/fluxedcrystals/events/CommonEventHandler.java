package getfluxed.fluxedcrystals.events;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Jared on 5/30/2016.
 */
public class CommonEventHandler {
    public CommonEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void toolTip(ItemTooltipEvent e) {
        Block block = Block.getBlockFromItem(e.getItemStack().getItem());
        if (block instanceof ITileEntityProvider) {
            TileEntity tile = ((ITileEntityProvider) block).createNewTileEntity(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld(), e.getItemStack().getItemDamage());
            if (tile != null && tile instanceof ITickable) {
                e.getToolTip().add(TextFormatting.RED + "This block contains a ticking Tile Entity" + TextFormatting.RESET);
                e.getToolTip().add(TextFormatting.RED + "and may be unsafe to build with" + TextFormatting.RESET);
            }
        }
    }

}
