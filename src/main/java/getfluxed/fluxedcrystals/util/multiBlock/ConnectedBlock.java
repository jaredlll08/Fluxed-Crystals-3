package getfluxed.fluxedcrystals.util.multiBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ConnectedBlock extends Block {

	public ConnectedBlock() {
		super(Material.AIR);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		return ((ConnectedTile) worldIn.getTileEntity(pos)).getBlock().block.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		((ConnectedTile) worldIn.getTileEntity(pos)).getBlock().block.onBlockHarvested(worldIn, pos, worldIn.getBlockState(pos), player);
	}

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		((ConnectedTile) worldIn.getTileEntity(pos)).getBlock().block.breakBlock(worldIn, pos, state);
	}

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(worldIn, pos))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

	public boolean canBlockStay(World world,BlockPos pos) {
		return ((ConnectedTile)world.getTileEntity(pos)).getMasterBlock().block==null;
	}
}
