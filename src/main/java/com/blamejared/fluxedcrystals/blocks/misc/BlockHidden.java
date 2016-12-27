package com.blamejared.fluxedcrystals.blocks.misc;

import com.blamejared.fluxedcrystals.FluxedCrystals;
import com.blamejared.fluxedcrystals.blocks.FCBlocks;
import com.blamejared.fluxedcrystals.blocks.crystal.BlockCrystal;
import com.blamejared.fluxedcrystals.tileentities.misc.TileEntityHidden;
import com.teamacronymcoders.base.blocks.BlockTEBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.fml.server.FMLServerHandler;

import javax.annotation.*;
import java.util.*;

public class BlockHidden extends BlockTEBase<TileEntityHidden> {
	
	public BlockHidden() {
		super(FCBlocks.MATERIAL_HIDDEN, "hidden");
		this.translucent = true;
	}
	
	public BlockPos getNearestCore(IBlockAccess world, BlockPos pos, List<BlockPos> list) {
		list.add(pos);
		for(EnumFacing facing : EnumFacing.values()) {
			if(!list.contains(pos.offset(facing)))
				if(world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockCrystal) {
					return pos.offset(facing);
				} else if(world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockHidden) {
					return ((BlockHidden) world.getBlockState(pos.offset(facing)).getBlock()).getNearestCore(world, pos.offset(facing), list);
				}
		}
		return BlockPos.ORIGIN;
	}
	
	public void revert(@Nonnull World world, @Nonnull BlockPos pos) {
		IBlockState newState = ((TileEntityHidden) world.getTileEntity(pos)).getOldBlock().getDefaultState();
		world.setBlockState(pos, newState, 3);
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		ItemStack stacks = new ItemStack(((TileEntityHidden)world.getTileEntity(pos)).getOldBlock());
		spawnAsEntity(world, pos, stacks);
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		
		super.breakBlock(world, pos, state);
		//		world.spawnEntityInWorld(new EntityItem(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, stack));
		
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {
		ItemStack stacks = new ItemStack(((TileEntityHidden)worldIn.getTileEntity(pos)).getOldBlock());
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		spawnAsEntity(worldIn, pos, stacks);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return false;
	}
	
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
		return new TileEntityHidden();
	}
	
	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityHidden.class;
	}
	
	/**
	 * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
	 * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
	 */
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 1.0F;
	}
	
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
	}
}
