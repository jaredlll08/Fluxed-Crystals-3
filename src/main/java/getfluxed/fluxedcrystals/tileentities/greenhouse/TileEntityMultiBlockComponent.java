package getfluxed.fluxedcrystals.tileentities.greenhouse;

import getfluxed.fluxedcrystals.api.multiblock.IFrame;
import getfluxed.fluxedcrystals.api.multiblock.IGreenHouseComponent;
import getfluxed.fluxedcrystals.api.multiblock.MultiBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Jared on 3/19/2016.
 */
public class TileEntityMultiBlockComponent extends TileEntity implements IGreenHouseComponent, IFrame {
    private BlockPos masterPos;


    @Override
    public void onLoad() {
        super.onLoad();

    }

    @Override
    public boolean isMaster() {
        return false;
    }

    @Override
    public BlockPos getMaster() {
        return masterPos;
    }

    @Override
    public void setMaster(BlockPos pos) {
        masterPos = pos;
    }

    @Override
    public MultiBlock getMultiBlock() {
        if (getWorld() != null && getMaster() != null && !getMaster().equals(new BlockPos(0, 0, 0)) && getWorld().getTileEntity(getMaster()) != null) {
            return ((IGreenHouseComponent) getWorld().getTileEntity(getMaster())).getMultiBlock();
        }
        return null;
    }

    @Override
    public void breakBlock() {
        getMultiBlock().setActive(false);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (masterPos != null) {
            NBTTagCompound tag = new NBTTagCompound();
            writePosToNBT(tag, getMaster());
            compound.setTag("masterTag", tag);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        masterPos = readPosFromNBT(compound.getCompoundTag("masterTag"));
    }

    private BlockPos readPosFromNBT(NBTTagCompound tag) {
        return new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    }

    private void writePosToNBT(NBTTagCompound tag, BlockPos pos) {
        tag.setInteger("x", pos.getX());
        tag.setInteger("y", pos.getY());
        tag.setInteger("z", pos.getZ());

    }
}
