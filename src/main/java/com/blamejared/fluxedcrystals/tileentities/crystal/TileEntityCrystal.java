package com.blamejared.fluxedcrystals.tileentities.crystal;

import com.blamejared.fluxedcrystals.api.crystals.*;
import com.blamejared.fluxedcrystals.api.harvestable.IHarvestable;
import com.blamejared.fluxedcrystals.blocks.crystal.BlockCrystal;
import com.blamejared.fluxedcrystals.client.particle.ParticleBeam;
import com.blamejared.fluxedcrystals.client.sounds.FCSounds;
import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class TileEntityCrystal extends TileEntityBase implements ITickable, ICrystalCore, IHarvestable {
	
	private boolean ran = false;
	public float angle = 0;
	
	public int colour = 0x0011DD;
	
	public List<ICrystalPylon> pylons = new ArrayList<>();
	
	private boolean activated = false;
	private int activatingTime = 100;
	
	public TileEntityCrystal() {
	}
	
	@Override
	public void update() {
		if(worldObj.getTotalWorldTime() % 33 == 0)
			worldObj.playSound(null, pos, FCSounds.CRYSTAL_HUM, SoundCategory.BLOCKS, 0.6F, 1F);
		if(angle < 360) {
			angle += 2;
		} else {
			angle = 0;
		}
		if(activatingTime-- < 0) {
			activatingTime = 0;
			activated = true;
		}
		if(worldObj.getTotalWorldTime() % 20 == 0)
			if(!((BlockCrystal) worldObj.getBlockState(pos).getBlock()).checkValidity(worldObj, pos)) {
				((BlockCrystal) worldObj.getBlockState(pos).getBlock()).invalidate(worldObj, pos);
			}
		if(activated) {
			if(worldObj.getTotalWorldTime() % 40 == 0) {
				if(pylons.isEmpty()) {
					BlockPos.getAllInBox(getPos().north(5).west(5).down(5), getPos().south(5).east(5).up(5)).forEach(pos -> {
						if(!worldObj.isAirBlock(pos)) {
							if(worldObj.getTileEntity(pos) instanceof ICrystalPylon) {
								ICrystalPylon pylon = (ICrystalPylon) worldObj.getTileEntity(pos);
								pylon.setCore(this);
								pylons.add(pylon);
							}
						}
					});
				} else {
					List<ICrystalPylon> removed = new ArrayList<>();
					pylons.forEach(i -> {
						if(worldObj.getTileEntity(i.getPos()) == null) {
							removed.add(i);
						} else if(!(worldObj.getTileEntity(i.getPos()) instanceof TileEntityCrystalPylon)) {
							removed.add(i);
						}
					});
					pylons.removeAll(removed);
					BlockPos.getAllInBox(getPos().north(5).west(5).down(5), getPos().south(5).east(5).up(5)).forEach(pos -> {
						if(!worldObj.isAirBlock(pos)) {
							if(worldObj.getTileEntity(pos) instanceof TileEntityCrystalPylon) {
								if(!pylons.contains(worldObj.getTileEntity(pos))) {
									ICrystalPylon pylon = (ICrystalPylon) worldObj.getTileEntity(pos);
									pylon.setCore(this);
									pylons.add(pylon);
								}
							}
						}
					});
				}
			}
			if(worldObj.isRemote) {
				if(!pylons.isEmpty()) {
					pylons.forEach(pylon -> {
						if(getWorld().getTileEntity(pylon.getPos()) != null) {
							Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleBeam(getWorld(), getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, pylon.getPos().getX() + 0.5, pylon.getPos().getY() + 0.5 + ((TileEntityCrystalPylon) getWorld().getTileEntity(pylon.getPos())).floatAm, pylon.getPos().getZ() + 0.5, 0, 0.6f, 0.8f, 2));
						}
					});
				}
			}
			if(!ran) {
				Map<String, Integer> commonMap = new HashMap<>();
				BlockPos.getAllInBox(pos.offset(EnumFacing.DOWN, pos.getY() - 2).west(16).north(16), pos.east(16).south(16)).forEach(i -> {
					if(worldObj.getBlockState(i) != null && worldObj.getBlockState(i).getBlock() != null && !worldObj.isAirBlock(i) && new ItemStack(worldObj.getBlockState(i).getBlock()) != null && new ItemStack(worldObj.getBlockState(i).getBlock()).getItem() != null || (worldObj.getTileEntity(i) != null && worldObj.getTileEntity(i) instanceof IHarvestable && (((IHarvestable) worldObj.getTileEntity(i)).isHarvestable()))) {
						ItemStack stack = new ItemStack(worldObj.getBlockState(i).getBlock());
						int[] ids = OreDictionary.getOreIDs(stack);
						boolean found = false;
						for(int i1 : ids) {
							if(OreDictionary.getOreName(i1).startsWith("ore")) {
								found = true;
								if(commonMap.containsKey(stack.getUnlocalizedName()))
									commonMap.replace(stack.getUnlocalizedName(), commonMap.getOrDefault(stack.getUnlocalizedName(), 0) + 1);
								else {
									commonMap.put(stack.getUnlocalizedName(), commonMap.getOrDefault(stack.getUnlocalizedName(), 0) + 1);
								}
								//							if(!worldObj.isRemote)
								//								worldObj.spawnEntityInWorld(new EntityItem(worldObj, getPos().getX(), getPos().getY(), getPos().getZ(), stack));
								//								worldObj.setBlockToAir(i);
								break;
							}
							
						}
						if(!found) {
//							worldObj.setBlockState(i, Blocks.BARRIER.getDefaultState());
						}
						
					}
				});
				final String[] mostCommon = {null};
				final int[] mostCommonInt = {-1};
				commonMap.forEach((key, val) -> {
					if(val > mostCommonInt[0]) {
						mostCommonInt[0] = val;
						mostCommon[0] = key;
					}
				});
				System.out.println(mostCommon[0]);
				ran = true;
			}
		}
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return 8192D;
	}
	
	@Override
	public int getColour() {
		return 0;
	}
	
	@Override
	public List<ICrystalPylon> getPylons() {
		return pylons;
	}
	
	public boolean isActivated() {
		return activated;
	}
	
	public int getActivatingTime() {
		return activatingTime;
	}
	
	@Override
	public boolean isHarvestable() {
		return false;
	}
}
