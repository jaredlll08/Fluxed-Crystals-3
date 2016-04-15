package getfluxed.fluxedcrystals.client.greenhouse;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.util.TextureUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Jared on 4/14/2016.
 */
public class RenderController extends TileEntitySpecialRenderer<TileEntitySoilController> {


    /**
     * calculate the rendering heights for all the liquids
     *
     * @param capacity Max capacity of smeltery, to calculate how much height one liquid takes up
     * @param height   Maximum height, basically represents how much height full capacity is
     * @param min      Minimum amount of height for a fluid. A fluid can never have less than this value height returned
     * @return Array with heights corresponding to input-list liquids
     */
    public static int calcLiquidHeights(FluidStack fluid, int capacity, int height) {
        float h = (float) fluid.amount / (float) capacity;
        return (int) Math.ceil(h * (float) height);
    }

    @Override
    public void renderTileEntityAt(TileEntitySoilController te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te != null)
            if (te.getMultiBlock().isActive() && te.tank.getFluid() != null) {
                //            GL11.glRotatef(180f, 1f, 0f, 0f);
                //            TextureUtils.renderStackedFluidCuboid(new FluidStack(FluidRegistry.WATER, 1000), x,y,z,te.getPos().offset(EnumFacing.UP), te.getMultiBlock().getAirBlocks().get(0).subtract(te.getPos()),te.getMultiBlock().getAirBlocks().get(te.getMultiBlock().getAirBlocks().size()-1).subtract(te.getPos()), y, y+16);
                float d = TextureUtils.FLUID_OFFSET;


                BlockPos min = te.getMultiBlock().getAirBlocks().get(0).subtract(te.getPos());
                BlockPos max = te.getMultiBlock().getAirBlocks().get(te.getMultiBlock().getAirBlocks().size() - 1).subtract(te.getPos());

                double currentLiquid = te.tank.getFluidAmount();
                double maxLiquid = te.tank.getCapacity();
                double height = ((currentLiquid / maxLiquid));
                System.out.println(height);

//                TextureUtils.renderFluidCuboid(te.tank.getFluid(), te.getPos(), x, y, z, 2, 3, 4, max.getY()  height - d, max.getY() - d);
                TextureUtils.renderStackedFluidCuboid(te.tank.getFluid(), x, y, z, min, min, max, min.down().getY(), max.down().getY() + height);

            }
    }
}
