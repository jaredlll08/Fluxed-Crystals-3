package com.blamejared.fluxedcrystals.blocks.crystal;

import com.blamejared.fluxedcrystals.blocks.FCBlocks;
import com.blamejared.fluxedcrystals.blocks.misc.BlockHidden;
import com.blamejared.fluxedcrystals.tileentities.crystal.TileEntityCrystal;
import com.teamacronymcoders.base.blocks.BlockTEBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.*;

public class BlockCrystal extends BlockTEBase<TileEntityCrystal> {
	
	public BlockCrystal() {
		super(FCBlocks.MATERIAL_CRYSTAL, "crystal");
	}
	
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
		return new TileEntityCrystal();
	}
	
	public boolean checkValidity(IBlockAccess world, BlockPos pos) {
		List<BlockPos> glassList = StreamSupport.stream(BlockPos.getAllInBox(pos.down(3).south().east(), pos.up(2).west().north()).spliterator(), false).filter(nPos -> !pos.equals(nPos) && world.getBlockState(nPos).getBlock() == FCBlocks.HIDDEN).collect(Collectors.toList());
		return glassList.size() == 53;
	}
	
	public void invalidate(World world, BlockPos pos){
		StreamSupport.stream(BlockPos.getAllInBox(pos.down(3).south().east(), pos.up(2).west().north()).spliterator(), false).filter(nPos -> !pos.equals(nPos) && world.getBlockState(nPos).getBlock() == FCBlocks.HIDDEN).forEach(i->{
			BlockHidden hid = (BlockHidden) world.getBlockState(i).getBlock();
			hid.revert(world, i);
		});
		world.setBlockState(pos, FCBlocks.CRYSTAL_CORE.getDefaultState());
		
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityCrystal.class;
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
