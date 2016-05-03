package getfluxed.fluxedcrystals.blocks.generators;

import getfluxed.fluxedcrystals.tileentities.generators.TileEntityLavaGenerator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

public class BlockLavaGenerator extends BlockContainer {

    public BlockLavaGenerator() {
        super(Material.rock);
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityLavaGenerator tile = (TileEntityLavaGenerator) worldIn.getTileEntity(pos);
        if (heldItem != null && tile instanceof IFluidHandler) {
            if (FluidUtil.interactWithTank(heldItem, playerIn, tile, side.getOpposite()))
                return true;
        }

        if (heldItem == null)
            return false;

        if (tile.getFluid() != null)
            playerIn.addChatComponentMessage(new TextComponentString(tile.getFluid().getLocalizedName() + ":" + tile.getFluidAmount() + ""));
        return FluidContainerRegistry.isFilledContainer(heldItem) || heldItem.getItem() instanceof IFluidContainerItem;
    }
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityLavaGenerator();
    }

}
