package getfluxed.fluxedcrystals.tileentities.generators;

import getfluxed.fluxedcrystals.api.generators.generators.FluidGeneratorBase;
import getfluxed.fluxedcrystals.blocks.generators.BlockCoalGenerator;
import getfluxed.fluxedcrystals.blocks.generators.BlockLavaGenerator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityLavaGenerator extends FluidGeneratorBase {
	private static int maxEnergy = 100000;
	private static int capacity = 8000;

	

	public TileEntityLavaGenerator() {
		super(capacity, maxEnergy);
	}



	@Override
	public void generateEnergy(World world, BlockPos pos, int timer) {

			if (this.storage.getEnergyStored() < this.storage.getMaxEnergyStored()) {
				this.storage.receiveEnergy(40, false);
			}
	}

	@Override
	public void update() {
		super.update();
		if (worldObj.getBlockState(getPos()).getValue(BlockCoalGenerator.isActive) && !isGenerating() && tank.getFluid()==null)
			BlockLavaGenerator.setState(false, this.worldObj, this.pos);
		else if(!worldObj.getBlockState(getPos()).getValue(BlockCoalGenerator.isActive) && isGenerating())
            BlockLavaGenerator.setState(true, this.worldObj, this.pos);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
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
