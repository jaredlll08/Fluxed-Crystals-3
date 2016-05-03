package getfluxed.fluxedcrystals.tileentities.generators;

import getfluxed.fluxedcrystals.api.generators.generators.FluidGeneratorBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityLavaGenerator extends FluidGeneratorBase {
	private static int maxEnergy = 100000;
	private static int capacity = 8000;
	private static int capacityTank = 16000;

	

	public TileEntityLavaGenerator() {
		super(capacityTank, capacity, maxEnergy);
	}



	@Override
	public void generateEnergy(World world, BlockPos pos, int timer) {

			if (this.storage.getEnergyStored() < this.storage.getMaxEnergyStored()) {
				this.storage.receiveEnergy(40, false);
			}
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		System.out.println("Filled!");
		return super.fill(resource, doFill);

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		writeFluidToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readFluidFromNBT(nbt);
	}
}
