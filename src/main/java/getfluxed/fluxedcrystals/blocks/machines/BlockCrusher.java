package getfluxed.fluxedcrystals.blocks.machines;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.machines.MessageCrusher;
import getfluxed.fluxedcrystals.tileentities.machine.TileEntityMachineCrusher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by Jared on 5/3/2016.
 */
public class BlockCrusher extends BlockMachine {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMachineCrusher();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(FluxedCrystals.instance, 2, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    public void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(isActive, active), 3);
        worldIn.setBlockState(pos, getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(isActive, active), 3);

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
            PacketHandler.INSTANCE.sendToAllAround(new MessageCrusher((TileEntityMachineCrusher) tileentity), new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), (double) tileentity.getPos().getX(), (double) tileentity.getPos().getY(), (double) tileentity.getPos().getZ(), 128d));
        }
    }
}
