package com.blamejared.fluxedcrystals.blocks.crystal;

import com.blamejared.fluxedcrystals.blocks.FCBlocks;
import com.blamejared.fluxedcrystals.tileentities.crystal.TileEntityCrystalPylon;
import com.teamacronymcoders.base.blocks.BlockTEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nonnull;

public class BlockCrystalPylon extends BlockTEBase<TileEntityCrystalPylon> {
	
	public BlockCrystalPylon() {
		super(FCBlocks.MATERIAL_CRYSTAL, "pylon");
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		super.neighborChanged(state, worldIn, pos, blockIn);
		if(worldIn.getBlockState(pos.down()).getBlock() != Blocks.OBSIDIAN || worldIn.getBlockState(pos.down().down()).getBlock() != Blocks.OBSIDIAN) {
			worldIn.setBlockState(pos, FCBlocks.CRYSTAL_CORE.getDefaultState());
		}
			
	}
	
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
		return new TileEntityCrystalPylon();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityCrystalPylon.class;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isVisuallyOpaque() {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	
	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}
}
