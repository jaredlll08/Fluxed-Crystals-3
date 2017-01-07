package com.blamejared.fluxedcrystals.blocks.misc;

import com.blamejared.fluxedcrystals.blocks.FCBlocks;
import com.blamejared.fluxedcrystals.blocks.crystal.BlockCrystalCore;
import com.blamejared.fluxedcrystals.items.misc.ItemTuningFork;
import com.teamacronymcoders.base.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nullable;
import java.util.*;

public class BlockQuartzGlass extends BlockBase {
	
	public BlockQuartzGlass() {
		super(Material.GLASS, "quartz_glass");
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(playerIn.getHeldItem(hand) != null && playerIn.getHeldItem(hand).getItem() instanceof ItemTuningFork) {
			BlockPos core = getNearestCore(worldIn, pos, new ArrayList<>());
			if(!core.equals(BlockPos.ORIGIN)) {
				((BlockCrystalCore) worldIn.getBlockState(core).getBlock()).activateCrystal(worldIn, playerIn, core);
			}else{
				worldIn.setBlockState(pos, FCBlocks.CRYSTAL_CLUSTER.getDefaultState());
			}
			return true;
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
	
	
	public BlockPos getNearestCore(World world, BlockPos pos, List<BlockPos> list) {
		list.add(pos);
		for(EnumFacing facing : EnumFacing.values()) {
			if(!list.contains(pos.offset(facing)))
				if(world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockCrystalCore) {
					return pos.offset(facing);
				} else if(world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockQuartzGlass) {
					return ((BlockQuartzGlass) world.getBlockState(pos.offset(facing)).getBlock()).getNearestCore(world, pos.offset(facing), list);
				}
		}
		return BlockPos.ORIGIN;
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();
		
		if(blockState != iblockstate)
			return true;
		
		if(block == this) {
			return false;
		}
		return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
}
