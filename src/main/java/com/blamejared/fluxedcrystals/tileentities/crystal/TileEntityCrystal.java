package com.blamejared.fluxedcrystals.tileentities.crystal;

import com.blamejared.fluxedcrystals.FluxedCrystals;
import com.blamejared.fluxedcrystals.api.crystals.*;
import com.blamejared.fluxedcrystals.api.harvestable.IHarvestable;
import com.blamejared.fluxedcrystals.api.registries.CrystalRegistry;
import com.blamejared.fluxedcrystals.blocks.FCBlocks;
import com.blamejared.fluxedcrystals.blocks.crystal.BlockCrystal;
import com.blamejared.fluxedcrystals.client.particle.ParticleBeam;
import com.blamejared.fluxedcrystals.client.sounds.FCSounds;
import com.blamejared.fluxedcrystals.network.tiles.*;
import com.teamacronymcoders.base.tileentities.TileEntityBase;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;
import java.util.*;

public class TileEntityCrystal extends TileEntityBase implements ITickable, ICrystalCore, IHarvestable {
	
	public float angle = 0;
	
	private int defaultColour = 0x0011DD;
	
	public List<ICrystalPylon> pylons = new ArrayList<>();
	
	private boolean activated = false;
	private int activatingTime = 100;
	
	private HashMap<BlockPos.MutableBlockPos, CrystalOre> stateCache = new HashMap<>();
	
	private CrystalOre selectedType = null;
	private CrystalOre currentType = null;
	private float selectedTypeCost = -1;
	private float currentCost = 0;
	
	private boolean stateCacheDepleted = false;
	
	public TileEntityCrystal() {
	}
	
	@Override
	public void update() {
		if(worldObj.getTotalWorldTime() % 33 == 0)
			worldObj.playSound(null, pos, FCSounds.CRYSTAL_HUM, SoundCategory.BLOCKS, 0.6F, 1F);
		if(worldObj.isRemote) {
			if(angle < 360) {
//				angle += 2;
			} else {
//				angle = 0;
			}
			if(!pylons.isEmpty()) {
				pylons.forEach(pylon -> {
					if(getWorld().getTileEntity(pylon.getPos()) != null) {
						Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleBeam(getWorld(), getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, pylon.getPos().getX() + 0.5, pylon.getPos().getY() + 0.5 + ((TileEntityCrystalPylon) getWorld().getTileEntity(pylon.getPos())).floatAm, pylon.getPos().getZ() + 0.5, 0, 0.4f, 1f, 2));
					}
				});
			}
		}
		if(!activated && activatingTime-- < 0) {
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
			
			
			boolean dirty = false;
			if(stateCache.isEmpty() && !stateCacheDepleted) {
				if(!worldObj.isRemote) {
					if(selectedType != null && currentCost >= selectedTypeCost) {
						BlockPos clusterPos = getRandomCrystalPos(getPos(), 5);
						while(worldObj.getBlockState(clusterPos).getBlock() == FCBlocks.CRYSTAL_CLUSTER && (currentCost -= selectedTypeCost) > 0) {
							
							if(((TileEntityCrystalCluster) worldObj.getTileEntity(clusterPos)).getCrystalType() == null || ((TileEntityCrystalCluster) worldObj.getTileEntity(clusterPos)).getCrystalType().getName().isEmpty()) {
								((TileEntityCrystalCluster) worldObj.getTileEntity(clusterPos)).setCrystalType(CrystalRegistry.getCrystalFromName(selectedType.getName()));
								
								worldObj.getTileEntity(clusterPos).markDirty();
								FluxedCrystals.INSTANCE.getPacketHandler().sendToAllAround(new MessageCrystalClusterSync(((TileEntityCrystalCluster) worldObj.getTileEntity(clusterPos))), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
								
							}
							clusterPos = getRandomCrystalPos(getPos(), 5);
							
						}
						currentCost = 0;
						selectedTypeCost = -2;
						selectedType = null;
						currentType = null;
						stateCache.clear();
						dirty = true;
						stateCacheDepleted = true;
					}
					if(!stateCacheDepleted) {
						BlockPos.getAllInBox(pos.offset(EnumFacing.DOWN, pos.getY() - 2).west(16).north(16), pos.east(16).south(16)).forEach(pos -> {
							if(!worldObj.isAirBlock(pos) && !(worldObj.getBlockState(pos).getBlock() instanceof BlockLiquid) || (worldObj.getTileEntity(pos) != null && worldObj.getTileEntity(pos) instanceof IHarvestable && (((IHarvestable) worldObj.getTileEntity(pos)).isHarvestable()))) {
								ItemStack stack = new ItemStack(worldObj.getBlockState(pos).getBlock());
								Crystal crystal = CrystalRegistry.getCrystalFromBlock(stack);
								if(crystal != null) {
									stateCache.put(new BlockPos.MutableBlockPos(pos), new CrystalOre(crystal.getName(), worldObj.getBlockState(pos), pos));
								}
							}
						});
						dirty = true;
					}
					
				}
			} else if(!stateCache.isEmpty() && !worldObj.isRemote) {
				if(selectedType == null) {
					List<CrystalOre> positions = new ArrayList<>(stateCache.values());
					
					CrystalOre current = null;
					while(current == null) {
						current = positions.get(worldObj.rand.nextInt(positions.size()));
						if(!worldObj.getBlockState(current.getPos()).equals(current.getState())) {
							stateCache.remove(pos);
							current = null;
						}
						if(current != null) {
							selectedType = current;
							selectedTypeCost = CrystalRegistry.getCrystalFromName(current.getName()).getCost();
						}
					}
					dirty = true;
				}
				
				if(worldObj.getTotalWorldTime() % 1 == 0) {
					List<BlockPos> keys = new ArrayList<>(stateCache.keySet());
					List<CrystalOre> values = new ArrayList<>(stateCache.values());
					int chosenPos = worldObj.rand.nextInt(values.size());
					currentType = values.get(chosenPos);
					if(currentType != null) {
						if(worldObj.getBlockState(currentType.getPos()).equals(currentType.getState())) {
							worldObj.setBlockToAir(currentType.getPos());
							currentCost += CrystalRegistry.getCrystalFromName(currentType.getName()).getCost();
							//							dirty = true;
							stateCache.remove(keys.get(chosenPos), values.get(chosenPos));
							System.out.println(stateCache.size() + ":" +angle++);
						}
					}
				}
			}
			
			
			if(!worldObj.isRemote && dirty) {
				FluxedCrystals.INSTANCE.getPacketHandler().sendToAllAround(new MessageCrystalSync(this), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
				markDirty();
			}
		}
		
	}
	
	public BlockPos getRandomCrystalPos(BlockPos base, int range) {
		List<BlockPos> clusters = new ArrayList<>();
		BlockPos.getAllInBox(base.down(range).north(range).west(range), base.up(range).south(range).east(range)).forEach(pos -> {
			if(worldObj.getBlockState(pos).getBlock() == FCBlocks.CRYSTAL_CLUSTER) {
				clusters.add(pos);
			}
		});
		return clusters.size() > 0 ? clusters.get(worldObj.rand.nextInt(clusters.size())) : base;
		//		while(pos == BlockPos.ORIGIN) {
		//			if(triedCount++ > 30) {
		//				return getRandomCrystalPos(base.down(), range);
		//			}
		//			BlockPos newPos = new BlockPos(base.getX() + worldObj.rand.nextInt(range * 2) - range, base.getY() + worldObj.rand.nextInt(range * 2) - range, base.getZ() + worldObj.rand.nextInt(range * 2) - range);
		//			if((worldObj.getBlockState(newPos).getBlock() == FCBlocks.CRYSTAL_CLUSTER)) {
		//				pos = newPos;
		//			}
		//		}
		//		return pos;
	}
	
	
	@Override
	public void markDirty() {
		super.markDirty();
	}
	
	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		data.setBoolean("activated", activated);
		data.setInteger("activatingTime", activatingTime);
		if(selectedType != null)
			data.setTag("selectedType", selectedType.writeToNBT(new NBTTagCompound()));
		if(selectedType != null)
			data.setTag("currentType", currentType.writeToNBT(new NBTTagCompound()));
		data.setFloat("selectedTypeCost", selectedTypeCost);
		data.setFloat("currentCost", currentCost);
		data.setBoolean("stateCacheDepleted", stateCacheDepleted);
		return super.writeToNBT(data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		this.activated = data.getBoolean("activated");
		this.activatingTime = data.getInteger("activatingTime");
		if(!data.getCompoundTag("selectedType").hasNoTags())
			this.selectedType = CrystalOre.readFromNBT(data.getCompoundTag("selectedType"));
		if(!data.getCompoundTag("currentType").hasNoTags())
			this.currentType = CrystalOre.readFromNBT(data.getCompoundTag("currentType"));
		this.selectedTypeCost = data.getFloat("selectedTypeCost");
		this.currentCost = data.getFloat("currentCost");
		this.stateCacheDepleted = data.getBoolean("stateCacheDepleted");
	}
	
	@Override
	protected void readFromUpdatePacket(NBTTagCompound data) {
		super.readFromUpdatePacket(data);
		readFromNBT(data);
	}
	
	@Override
	protected NBTTagCompound writeToUpdatePacket(NBTTagCompound data) {
		writeToNBT(data);
		return super.writeToUpdatePacket(data);
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return 8192D;
	}
	
	public void setPylons(List<ICrystalPylon> pylons) {
		this.pylons = pylons;
	}
	
	public HashMap<BlockPos.MutableBlockPos, CrystalOre> getStateCache() {
		return stateCache;
	}
	
	public void setStateCache(HashMap<BlockPos.MutableBlockPos, CrystalOre> stateCache) {
		this.stateCache = stateCache;
	}
	
	@Override
	public int getColour() {
		if(selectedType != null) {
			return CrystalRegistry.getCrystalFromName(selectedType.getName()).getColour();
		}
		return defaultColour;
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
	
	
	public CrystalOre getSelectedType() {
		return selectedType;
	}
	
	public void setSelectedType(CrystalOre selectedType) {
		this.selectedType = selectedType;
	}
	
	public CrystalOre getCurrentType() {
		return currentType;
	}
	
	public void setCurrentType(CrystalOre currentType) {
		this.currentType = currentType;
	}
	
	public float getSelectedTypeCost() {
		return selectedTypeCost;
	}
	
	public void setSelectedTypeCost(float selectedTypeCost) {
		this.selectedTypeCost = selectedTypeCost;
	}
	
	public float getCurrentCost() {
		return currentCost;
	}
	
	public boolean isStateCacheDepleted() {
		return stateCacheDepleted;
	}
	
	public void setStateCacheDepleted(boolean stateCacheDepleted) {
		this.stateCacheDepleted = stateCacheDepleted;
	}
	
	public void setCurrentCost(float currentCost) {
		this.currentCost = currentCost;
	}
}
