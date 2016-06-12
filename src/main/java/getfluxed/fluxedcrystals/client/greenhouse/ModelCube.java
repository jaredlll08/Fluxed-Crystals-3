package getfluxed.fluxedcrystals.client.greenhouse;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * NewProject - Undefined
 * Created using Tabula 5.0.0
 */
public class ModelCube extends ModelBase {
    public ModelRenderer cube;

    public ModelCube() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.cube = new ModelRenderer(this, 0, 0);
        this.cube.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.cube.addBox(-4.0F, 12.0F, -4.0F, 8, 8, 8, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.cube.render(f5);
    }

    public void render(float f5) {
        this.cube.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
