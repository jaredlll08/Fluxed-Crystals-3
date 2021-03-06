package getfluxed.fluxedcrystals.tileentities.greenhouse.io;

import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.api.crystals.ICrystalInfoProvider;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.blocks.FCBlocks;
import getfluxed.fluxedcrystals.blocks.misc.BlockCrystalCube;
import getfluxed.fluxedcrystals.items.FCItems;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.tileentities.misc.TileEntityCrystalCube;
import getfluxed.fluxedcrystals.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Created by Jared on 6/4/2016.
 */
public class TileEntityCrystalIO extends TileEntityMultiBlockComponent implements ITickable {

    public Random rand = new Random(2906);

    @Override
    public void update() {
        if (getMultiBlock() != null && getMultiBlock().isActive()) {
            boolean sendUpdate = false;
            TileEntitySoilController master = (TileEntitySoilController) worldObj.getTileEntity(getMaster());

            if (!master.isGrowing() && master.getCrystalInfo().equals(Crystal.NULL) && getMasterTile().itemStackHandler.getStackInSlot(0) != null && getMasterTile().itemStackHandler.getStackInSlot(0).getItem() instanceof ICrystalInfoProvider) {
                Crystal info = ((ICrystalInfoProvider) getMasterTile().itemStackHandler.getStackInSlot(0).getItem()).getCrystal(getMasterTile().itemStackHandler.getStackInSlot(0));
                master.setCrystalInfo(info);
                getMasterTile().itemStackHandler.extractItem(0, 1, false);
                master.setCurrentGrowth(0);
                master.setGrowing(true);
                sendUpdate = true;
            } else if (!master.getCrystalInfo().equals(Crystal.NULL) && !master.getCrystalInfo().getName().equals(Crystal.NULL.getName())) {
                master.setCurrentGrowth(master.getCurrentGrowth() + 100);
                int stage = (int) ((master.getCurrentGrowth() / (master.getCrystalInfo().getGrowthTimePerBlock() * master.getMultiBlock().getAirBlocks().size())) * 10);
                switch (stage) {
                    case 0:
                        for (BlockPos bp : master.getMultiBlock().getBottomLayer()) {
                            bp = bp.up();
                            worldObj.setBlockState(bp, FCBlocks.crystalCube.getDefaultState().withProperty(BlockCrystalCube.onGround, true), 3);
                            ((TileEntityCrystalCube) worldObj.getTileEntity(bp)).setCrystal(master.getCrystalInfo());
                        }
                        break;
                }
                if (master.getCurrentGrowth() >= (master.getCrystalInfo().getGrowthTimePerBlock() * master.getMultiBlock().getAirBlocks().size())) {
                    ItemStack retStack = CrystalRegistry.getCrystal(master.getCrystalInfo().getName()).getResourceOut().getItemStack();
                    int retSize = rand.nextInt(master.getCrystalInfo().getCrushedCrystalPerBlockMax()) + getMasterTile().getCrystalInfo().getCrushedCrystalPerBlockMin() * (master.getMultiBlock().getAirBlocks().size());
                    int shardsRough = 0;
                    int shardsSmooth = 0;
                    int chunksRough = 0;
                    int chunksSmooth = 0;
                    while (retSize > 0) {
                        retSize--;
                        shardsRough++;
                        if (shardsRough >= 9) {
                            if (shardsSmooth < 64) {
                                shardsRough = 0;
                                shardsSmooth++;
                            }
                        }
                        if (shardsSmooth >= 9) {
                            if (chunksRough < 64) {
                                shardsSmooth = 0;
                                chunksRough++;
                            }
                        }
                        if (chunksRough >= 9) {
                            if (chunksSmooth < 64) {
                                chunksRough = 0;
                                chunksSmooth++;
                            }
                        }
                    }
                    ItemStack roughShards = new ItemStack(FCItems.crystalCrushed, shardsRough, 0);
                    NBTHelper.setString(roughShards, "crystalName", master.getCrystalInfo().getName());
                    ItemStack smoothShards = new ItemStack(FCItems.crystalCrushed, shardsSmooth, 1);
                    NBTHelper.setString(smoothShards, "crystalName", master.getCrystalInfo().getName());
                    ItemStack roughChunks = new ItemStack(FCItems.crystalCrushed, chunksRough, 2);
                    NBTHelper.setString(roughChunks, "crystalName", master.getCrystalInfo().getName());
                    ItemStack smoothChunks = new ItemStack(FCItems.crystalCrushed, chunksSmooth, 3);
                    NBTHelper.setString(smoothChunks, "crystalName", master.getCrystalInfo().getName());
                    if (roughShards.stackSize > 0)
                        getMasterTile().itemStackHandler.insertItem(1, roughShards, false);
                    if (smoothShards.stackSize > 0)
                        getMasterTile().itemStackHandler.insertItem(2, smoothShards, false);
                    if (roughChunks.stackSize > 0)
                        getMasterTile().itemStackHandler.insertItem(3, roughChunks, false);
                    if (smoothChunks.stackSize > 0)
                        getMasterTile().itemStackHandler.insertItem(4, smoothChunks, false);
                    master.setCurrentGrowth(0);
                    master.setGrowing(false);
                    master.setCrystalInfo(Crystal.NULL);
                }
                sendUpdate = true;
            }
            if (master.isGrowing() && master.getCrystalInfo().equals(Crystal.NULL)) {
                master.setGrowing(false);
                master.setCurrentGrowth(0);
                sendUpdate = true;
            }
            if (!worldObj.isRemote && sendUpdate) {
                getMasterTile().markDirty();
            }

        }
    }


    public TileEntitySoilController getMasterTile() {
        if (worldObj.getTileEntity(getMaster()) != null) {
            if (worldObj.getTileEntity(getMaster()) instanceof TileEntitySoilController) {
                return ((TileEntitySoilController) worldObj.getTileEntity(getMaster()));
            }
        }
        return null;
    }

}
