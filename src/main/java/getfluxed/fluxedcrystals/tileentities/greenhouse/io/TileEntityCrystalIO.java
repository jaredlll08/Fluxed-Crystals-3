package getfluxed.fluxedcrystals.tileentities.greenhouse.io;

import getfluxed.fluxedcrystals.api.client.gui.IOpenableGUI;
import getfluxed.fluxedcrystals.api.crystals.Crystal;
import getfluxed.fluxedcrystals.api.crystals.ICrystalInfoProvider;
import getfluxed.fluxedcrystals.api.registries.CrystalRegistry;
import getfluxed.fluxedcrystals.client.gui.crystalio.ContainerCrystalIO;
import getfluxed.fluxedcrystals.client.gui.crystalio.GUICrystalIO;
import getfluxed.fluxedcrystals.items.FCItems;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.greenhouse.io.MessageCrystalIO;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntityMultiBlockComponent;
import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.util.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.Random;

/**
 * Created by Jared on 6/4/2016.
 */
public class TileEntityCrystalIO extends TileEntityMultiBlockComponent implements ITickable, IOpenableGUI {

    public Random rand = new Random(2906);

    @Override
    public void update() {
        if (getMultiBlock() != null && getMultiBlock().isActive()) {
            boolean sendUpdate = false;
            TileEntitySoilController master = (TileEntitySoilController) worldObj.getTileEntity(getMaster());

            if (!master.isGrowing() && master.getCrystalInfo().equals(Crystal.NULL) && getMasterTile().itemStackHandler.getStackInSlot(0) != null && getMasterTile().itemStackHandler.getStackInSlot(0).getItem() instanceof ICrystalInfoProvider) {
                Crystal info = ((ICrystalInfoProvider) getMasterTile().itemStackHandler.getStackInSlot(0).getItem()).getCrystal(getMasterTile().itemStackHandler.getStackInSlot(0));
                master.setCrystalInfo(info);
                getMasterTile().itemStackHandler.extractItem(0,1,false);
                master.setCurrentGrowth(0);
                master.setGrowing(true);
                sendUpdate = true;
            } else if (!master.getCrystalInfo().equals(Crystal.NULL) && !master.getCrystalInfo().getName().equals(Crystal.NULL.getName())) {
                master.setCurrentGrowth(master.getCurrentGrowth() + 100);
                if (master.getCurrentGrowth() >= (master.getCrystalInfo().getGrowthTimePerBlock() * master.getMultiBlock().getAirBlocks().size())) {
                    ItemStack retStack = CrystalRegistry.getCrystal(master.getCrystalInfo().getName()).getResourceOut().getItemStack();
                    int retSize = rand.nextInt(master.getCrystalInfo().getCrushedCrystalPerBlockMax()) + getMasterTile().getCrystalInfo().getCrushedCrystalPerBlockMin() * (master.getMultiBlock().getAirBlocks().size());
                    int shardsRough = 0;
                    int shardsSmooth = 0;
                    int chunksRough = 0;
                    int chunksSmooth = 0;
                    System.out.println("ret: " + retSize);
                    while (retSize > 0) {
                        retSize--;
                        System.out.println(retSize);
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
                    //retSize = 153
//                    //chunksSmooth = 153 /9 (17), 17/9 (2), 2/9 (0)
//                    //chunksRough = (153 - chunksSmooth (0)) (153) /9 (17), 17 /9 (2)
//                    //shardsSmooth = (153 - chunksRough
//                    chunksSmooth = Math.round(retSize / 9 / 9 / 9);
//                    chunksRough = Math.round((retSize - (chunksSmooth*9*9*9)) / 9 / 9);
//                    shardsSmooth = Math.round((retSize - (chunksRough*9*9)) / 9);
//                    shardsRough = (retSize - (shardsSmooth*9));

//                    chunksRough -= chunksSmooth;
//                    shardsSmooth -= chunksRough;
//                    shardsRough -= shardsSmooth;


                    System.out.println("crushed: " + (shardsRough + ":" + shardsSmooth + ":" + chunksRough + ":" + chunksSmooth) + " retSize: " + retSize);
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
            if (sendUpdate) {
                PacketHandler.INSTANCE.sendToAllAround(new MessageCrystalIO(getMasterTile()), new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128D));
            }

        }
    }


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        return compound;
    }

    public TileEntitySoilController getMasterTile() {
        if (worldObj.getTileEntity(getMaster()) != null) {
            if (worldObj.getTileEntity(getMaster()) instanceof TileEntitySoilController) {
                return ((TileEntitySoilController) worldObj.getTileEntity(getMaster()));
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new GUICrystalIO(player.inventory, (TileEntityCrystalIO) world.getTileEntity(pos));
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerCrystalIO(player.inventory, (TileEntityCrystalIO) world.getTileEntity(pos));
    }
}
