package com.blamejared.fluxedcrystals.blocks.crystal;

import com.blamejared.fluxedcrystals.blocks.FCBlocks;
import com.blamejared.fluxedcrystals.tileentities.crystal.TileEntityCrystalCluster;
import com.teamacronymcoders.base.blocks.BlockTEBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nonnull;

public class BlockCrystalCluster extends BlockTEBase<TileEntityCrystalCluster> {
	
	public BlockCrystalCluster() {
		super(FCBlocks.MATERIAL_CRYSTAL, "crystal_cluster");
	}
	
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
		return new TileEntityCrystalCluster();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityCrystalCluster.class;
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
