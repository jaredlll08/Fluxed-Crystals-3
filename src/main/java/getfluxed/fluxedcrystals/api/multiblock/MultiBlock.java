package getfluxed.fluxedcrystals.api.multiblock;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jared on 3/31/2016.
 */
public class MultiBlock {

    private BlockPos master;
    private LinkedList<BlockPos> bottomLayer;
    private LinkedList<BlockPos> topLayer;
    private LinkedList<BlockPos> airBlocks;
    private LinkedList<BlockPos> sides;
    private boolean active;

    public MultiBlock(BlockPos master) {
        this.master = master;
        this.bottomLayer = null;
        this.topLayer = null;
        this.airBlocks = null;
        this.sides = null;
        this.active = false;
    }

    public MultiBlock(BlockPos master, LinkedList<BlockPos> bottomLayer, LinkedList<BlockPos> topLayer, LinkedList<BlockPos> airBlocks, LinkedList<BlockPos> sides, boolean active) {
        this.master = master;
        this.bottomLayer = bottomLayer;
        this.topLayer = topLayer;
        this.airBlocks = airBlocks;
        this.sides = sides;
        this.active = active;
    }

    private static BlockPos readPosFromBB(ByteBuf buf) {
        return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    private static void writePosToBB(ByteBuf buf, BlockPos pos) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    private static BlockPos readPosFromNBT(NBTTagCompound tag) {
        return new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    }

    private static void writePosToNBT(NBTTagCompound tag, BlockPos pos) {
        tag.setInteger("x", pos.getX());
        tag.setInteger("y", pos.getY());
        tag.setInteger("z", pos.getZ());

    }

    public static MultiBlock readFromNBT(NBTTagCompound tag) {
        BlockPos master = readPosFromNBT(tag.getCompoundTag("master"));
        LinkedList<BlockPos> bottomLayer = new LinkedList<>();
        for (int i = 0; i < tag.getTagList("bottomLayer", Constants.NBT.TAG_COMPOUND).tagCount(); i++) {
            bottomLayer.add(readPosFromNBT(tag.getTagList("bottomLayer", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i)));
        }
        LinkedList<BlockPos> topLayer = new LinkedList<>();
        for (int i = 0; i < tag.getTagList("topLayer", Constants.NBT.TAG_COMPOUND).tagCount(); i++) {
            bottomLayer.add(readPosFromNBT(tag.getTagList("topLayer", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i)));
        }
        LinkedList<BlockPos> airBlocks = new LinkedList<>();
        for (int i = 0; i < tag.getTagList("airBlocks", Constants.NBT.TAG_COMPOUND).tagCount(); i++) {
            airBlocks.add(readPosFromNBT(tag.getTagList("airBlocks", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i)));
        }

        LinkedList<BlockPos> sides = new LinkedList<>();
        for (int i = 0; i < tag.getTagList("sides", Constants.NBT.TAG_COMPOUND).tagCount(); i++) {
            sides.add(readPosFromNBT(tag.getTagList("sides", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i)));
        }
        boolean active = tag.getBoolean("active");
        return new MultiBlock(master, bottomLayer, topLayer, airBlocks, sides, active);
    }

    public static void writeToNBT(NBTTagCompound tag, MultiBlock multiBlock) {
        writePosToNBT(tag, multiBlock.getMaster());
        if (multiBlock.isActive()) {
            tag.setBoolean("written", true);
            NBTTagList bottomLayer = new NBTTagList();
            for (BlockPos bp : multiBlock.getBottomLayer()) {
                NBTTagCompound bpTag = new NBTTagCompound();
                writePosToNBT(bpTag, bp);
                bottomLayer.appendTag(bpTag);
            }
            tag.setTag("bottomLayer", bottomLayer);
            NBTTagList topLayer = new NBTTagList();
            for (BlockPos bp : multiBlock.getTopLayer()) {
                NBTTagCompound bpTag = new NBTTagCompound();
                writePosToNBT(bpTag, bp);
                topLayer.appendTag(bpTag);
            }
            tag.setTag("topLayer", topLayer);
            NBTTagList airBlocks = new NBTTagList();
            for (BlockPos bp : multiBlock.getAirBlocks()) {
                NBTTagCompound bpTag = new NBTTagCompound();
                writePosToNBT(bpTag, bp);
                airBlocks.appendTag(bpTag);
            }
            tag.setTag("airBlocks", airBlocks);
            NBTTagList sides = new NBTTagList();
            for (BlockPos bp : multiBlock.getSides()) {
                NBTTagCompound bpTag = new NBTTagCompound();
                writePosToNBT(bpTag, bp);
                sides.appendTag(bpTag);
            }
            tag.setTag("sides", sides);
            tag.setBoolean("active", multiBlock.isActive());
        } else {
            tag.setBoolean("written", false);
            tag.setTag("bottomLayer", new NBTTagList());
            tag.setTag("topLayer", new NBTTagList());
            tag.setTag("airBlocks", new NBTTagList());
            tag.setTag("sides", new NBTTagList());
            tag.setBoolean("active", multiBlock.isActive());
        }
    }

    public static MultiBlock readFromByteBuf(ByteBuf buf) {
        BlockPos master = readPosFromBB(buf);
        LinkedList<BlockPos> bottomLayer = new LinkedList<>();
        LinkedList<BlockPos> topLayer = new LinkedList<>();
        LinkedList<BlockPos> sides = new LinkedList<>();
        LinkedList<BlockPos> airBlocks = new LinkedList<>();
        boolean active = false;
        if (buf.readBoolean()) {

            int botSize = buf.readInt();
            for (int i = 0; i < botSize; i++) {
                bottomLayer.add(readPosFromBB(buf));
            }

            int topSize = buf.readInt();
            for (int i = 0; i < topSize; i++) {
                topLayer.add(readPosFromBB(buf));
            }

            int airSize = buf.readInt();
            for (int i = 0; i < airSize; i++) {
                airBlocks.add(readPosFromBB(buf));
            }


            int sidSize = buf.readInt();
            for (int i = 0; i < sidSize; i++) {
                sides.add(readPosFromBB(buf));
            }
            active = buf.readBoolean();
        }
        return new MultiBlock(master, bottomLayer, topLayer, airBlocks, sides, active);
    }


    public static void writeToByteBuf(ByteBuf buf, MultiBlock multiBlock) {
        writePosToBB(buf, multiBlock.getMaster());
        if (multiBlock.isActive()) {
            buf.writeInt(multiBlock.getBottomLayer().size());
            for (BlockPos bp : multiBlock.getBottomLayer()) {
                writePosToBB(buf, bp);
            }
            buf.writeInt(multiBlock.getTopLayer().size());
            for (BlockPos bp : multiBlock.getTopLayer()) {
                writePosToBB(buf, bp);
            }
            buf.writeInt(multiBlock.getAirBlocks().size());
            for (BlockPos bp : multiBlock.getAirBlocks()) {
                writePosToBB(buf, bp);
            }
            buf.writeInt(multiBlock.getSides().size());
            for (BlockPos bp : multiBlock.getSides()) {
                writePosToBB(buf, bp);
            }
            buf.writeBoolean(multiBlock.isActive());
        } else {
            buf.writeInt(0);
            buf.writeInt(0);
            buf.writeInt(0);
            buf.writeInt(0);
            buf.writeBoolean(multiBlock.isActive());
        }
    }

    public BlockPos getMaster() {
        return master;
    }

    public void setMaster(BlockPos master) {
        this.master = master;
    }

    public List<BlockPos> getBottomLayer() {
        return bottomLayer;
    }

    public void setBottomLayer(LinkedList<BlockPos> bottomLayer) {
        this.bottomLayer = bottomLayer;
    }

    public List<BlockPos> getTopLayer() {
        return topLayer;
    }

    public void setTopLayer(LinkedList<BlockPos> topLayer) {
        this.topLayer = topLayer;
    }

    public List<BlockPos> getAirBlocks() {
        return airBlocks;
    }

    public void setAirBlocks(LinkedList<BlockPos> airBlocks) {
        this.airBlocks = airBlocks;
    }

    public List<BlockPos> getSides() {
        return sides;
    }

    public void setSides(LinkedList<BlockPos> sides) {
        this.sides = sides;
    }

    public List<BlockPos> getAllBlocks() {
        List<BlockPos> pos = new ArrayList<>();
        pos.addAll(getAirBlocks());
        pos.addAll(getBottomLayer());
        pos.addAll(getSides());
        pos.addAll(getTopLayer());
        return pos;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "MultiBlock{" +
                "master=" + master +
                ", bottomLayer=" + bottomLayer +
                ", topLayer=" + topLayer +
                ", airBlocks=" + airBlocks +
                ", sides=" + sides +
                ", active=" + active +
                '}';
    }

    public boolean containsModule(World world, IGreenHouseModule module) {
        for (BlockPos bp : getSides()) {
            if (world.getTileEntity(bp) instanceof IGreenHouseModule) {
                if (((IGreenHouseModule) world.getTileEntity(bp)).getName().equals(module.getName())) {
                    return true;
                }
            }
        }
        for (BlockPos bp : getTopLayer()) {
            if (world.getTileEntity(bp) instanceof IGreenHouseModule) {
                if (((IGreenHouseModule) world.getTileEntity(bp)).getName().equals(module.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

}
