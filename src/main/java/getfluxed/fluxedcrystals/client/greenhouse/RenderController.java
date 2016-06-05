package getfluxed.fluxedcrystals.client.greenhouse;

import getfluxed.fluxedcrystals.tileentities.greenhouse.TileEntitySoilController;
import getfluxed.fluxedcrystals.util.TextureUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Jared on 4/14/2016.
 */
public class RenderController extends TileEntitySpecialRenderer<TileEntitySoilController> {


    @Override
    public void renderTileEntityAt(TileEntitySoilController te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te != null) {
            if (te.isGrowing()) {
                BlockPos min = te.getMultiBlock().getAirBlocks().get(0).subtract(te.getPos());
                BlockPos max = te.getMultiBlock().getAirBlocks().get(te.getMultiBlock().getAirBlocks().size() - 1).subtract(te.getPos());
                double currentLiquid = te.tank.getFluidAmount();
                double maxLiquid = te.tank.getCapacity();
                double height = 5;
                TextureUtils.renderStackedFluidCuboid(new FluidStack(FluidRegistry.LAVA, 1000), x, y, z, min, min, max, min.down().getY(), min.down().getY() + height);
            }
            if (te.getMultiBlock().isActive() && te.tank.getFluid() != null) {
                //            GL11.glRotatef(180f, 1f, 0f, 0f);
                //            TextureUtils.renderStackedFluidCuboid(new FluidStack(FluidRegistry.WATER, 1000), x,y,z,te.getPos().offset(EnumFacing.UP), te.getMultiBlock().getAirBlocks().get(0).subtract(te.getPos()),te.getMultiBlock().getAirBlocks().get(te.getMultiBlock().getAirBlocks().size()-1).subtract(te.getPos()), y, y+16);
                float d = TextureUtils.FLUID_OFFSET;

                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();
                BlockPos min = te.getMultiBlock().getAirBlocks().get(0).subtract(te.getPos());
                BlockPos max = te.getMultiBlock().getAirBlocks().get(te.getMultiBlock().getAirBlocks().size() - 1).subtract(te.getPos());

                double currentLiquid = te.tank.getFluidAmount();
                double maxLiquid = te.tank.getCapacity();
                double height = ((currentLiquid / maxLiquid)) * (min.getY() * max.getY());

//                TextureUtils.renderFluidCuboid(te.tank.getFluid(), te.getPos(), x, y, z, 2, 3, 4, max.getY()  height - d, max.getY() - d);
//                System.out.println(te.getMultiBlock().getAirBlocks().get(0));
//                System.out.println(te.getPos());
                TextureUtils.renderStackedFluidCuboid(te.tank.getFluid(), x, y, z, min, min, max, min.down().getY(), min.down().getY() + height);


                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
        }
    }
}
