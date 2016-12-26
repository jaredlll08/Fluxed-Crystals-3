package com.blamejared.fluxedcrystals.blocks.crystal;

import com.blamejared.fluxedcrystals.blocks.FCBlocks;
import com.blamejared.fluxedcrystals.items.misc.ItemTuningFork;
import com.blamejared.fluxedcrystals.tileentities.misc.TileEntityHidden;
import com.teamacronymcoders.base.blocks.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.*;

public class BlockCrystalCore extends BlockBase {
	
	public BlockCrystalCore() {
		super(Material.IRON, "crystal_core");
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(playerIn.getHeldItem(hand) != null && playerIn.getHeldItem(hand).getItem() instanceof ItemTuningFork) {
			if(worldIn.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN && worldIn.getBlockState(pos.down().down()).getBlock() == Blocks.OBSIDIAN) {
				worldIn.setBlockState(pos, FCBlocks.CRYSTAL_PYLON.getDefaultState());
			}
			return true;
		}
		return false;
	}
	
	public boolean activateCrystal(World world, EntityPlayer player, BlockPos pos) {
		List<BlockPos> glassList = StreamSupport.stream(BlockPos.getAllInBox(pos.down(3).south().east(), pos.up(2).west().north()).spliterator(), false).filter(nPos -> !pos.equals(nPos) && world.getBlockState(nPos).getBlock() == FCBlocks.QUARTZ_GLASS).collect(Collectors.toList());
		boolean active = glassList.size() == 53;
		if(active) {
			glassList.forEach(i->{
				world.setBlockState(i, FCBlocks.HIDDEN.getDefaultState());
				TileEntityHidden tile = (TileEntityHidden) world.getTileEntity(i);
				tile.setOldBlock(FCBlocks.QUARTZ_GLASS);
			});
			world.setBlockState(pos, FCBlocks.CRYSTAL.getDefaultState());
		
		}
		
		return active;
	}
}
