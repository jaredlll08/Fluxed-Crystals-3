package getfluxed.fluxedcrystals.util.multiBlock;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class BlockCoord {

	public Block block;
	public int x;
	public int y;
	public int z;

	public BlockCoord(Block block, int x, int y, int z) {
		this.block = block;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockCoord(TileEntity tile) {
		if (tile != null) {
			this.block = tile.getWorld().getBlockState(tile.getPos()).getBlock();
			this.x = tile.getPos().getX();
			this.y = tile.getPos().getY();
			this.z = tile.getPos().getZ();
		}
	}
	

	public void readFromNBT(NBTTagCompound tag) {
		this.x = tag.getInteger("posX");
		this.y = tag.getInteger("posY");
		this.z = tag.getInteger("posZ");

	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("posX", x);
		tag.setInteger("posY", y);
		tag.setInteger("posZ", z);

	}
}
