package getfluxed.fluxedcrystals.api.crystals;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by Jared on 6/4/2016.
 */
public class CrystalInfo {

    private String name;
    private AxisAlignedBB size;
    private double growthTime;
    private int minOut;
    private int maxOut;

    public CrystalInfo(String name, AxisAlignedBB size, double growthTime, int minOut, int maxOut) {
        this.name = name;
        this.size = size;
        this.growthTime = growthTime;
        this.minOut = minOut;
        this.maxOut = maxOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AxisAlignedBB getSize() {
        return size;
    }

    public void setSize(AxisAlignedBB size) {
        this.size = size;
    }

    public double getGrowthTime() {
        return growthTime;
    }

    public void setGrowthTime(double growthTime) {
        this.growthTime = growthTime;
    }

    public int getMinOut() {
        return minOut;
    }

    public void setMinOut(int minOut) {
        this.minOut = minOut;
    }

    public int getMaxOut() {
        return maxOut;
    }

    public void setMaxOut(int maxOut) {
        this.maxOut = maxOut;
    }

    public void writeToByteBuf(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, getName());
        buf.writeDouble(getSize().minX);
        buf.writeDouble(getSize().minY);
        buf.writeDouble(getSize().minZ);
        buf.writeDouble(getSize().maxX);
        buf.writeDouble(getSize().maxY);
        buf.writeDouble(getSize().maxZ);
        buf.writeDouble(getGrowthTime());
        buf.writeInt(getMinOut());
        buf.writeInt(getMaxOut());
    }

    public static CrystalInfo readFromByteBuf(ByteBuf buf) {
        String name = ByteBufUtils.readUTF8String(buf);
        AxisAlignedBB size = new AxisAlignedBB(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        double growth = buf.readDouble();
        int minOut = buf.readInt();
        int maxOut = buf.readInt();
        return new CrystalInfo(name, size, growth, minOut, maxOut);
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setString("name", getName());
        tag.setDouble("minX", getSize().minX);
        tag.setDouble("minY", getSize().minY);
        tag.setDouble("minZ", getSize().minZ);
        tag.setDouble("maxX", getSize().maxX);
        tag.setDouble("maxY", getSize().maxY);
        tag.setDouble("maxZ", getSize().maxZ);
        tag.setDouble("growthTime", getGrowthTime());
        tag.setInteger("minOut", getMinOut());
        tag.setInteger("maxOut", getMaxOut());
    }

    public static CrystalInfo readFromNBT(NBTTagCompound tag) {

        String name = tag.getString("name");
        AxisAlignedBB size = new AxisAlignedBB(tag.getDouble("minX"), tag.getDouble("minY"), tag.getDouble("minZ"), tag.getDouble("maxX"), tag.getDouble("maxY"), tag.getDouble("maxZ"));
        double growth = tag.getDouble("growthTime");
        int minOut = tag.getInteger("minOut");
        int maxOut = tag.getInteger("maxOut");
        return new CrystalInfo(name, size, growth, minOut, maxOut);
    }

    @Override
    public String toString() {
        return "CrystalInfo{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", growthTime=" + growthTime +
                ", minOut=" + minOut +
                ", maxOut=" + maxOut +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrystalInfo)) return false;

        CrystalInfo that = (CrystalInfo) o;

        if (Double.compare(that.getGrowthTime(), getGrowthTime()) != 0) return false;
        if (getMinOut() != that.getMinOut()) return false;
        if (getMaxOut() != that.getMaxOut()) return false;
        if (!getName().equals(that.getName())) return false;
        return getSize().equals(that.getSize());

    }



    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName().hashCode();
        result = 31 * result + getSize().hashCode();
        temp = Double.doubleToLongBits(getGrowthTime());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getMinOut();
        result = 31 * result + getMaxOut();
        return result;
    }

    public static CrystalInfo NULL = new CrystalInfo("__NULL__", new AxisAlignedBB(0, 0, 0, 0, 0, 0), 0, 0, 0);
}
