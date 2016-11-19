package getfluxed.fluxedcrystals.api.glassjar;

/**
 * Created by Jared.
 */
public enum GlassJarSize {
    SIZE5X5(5, 5, 5), SIZE7X7(7, 7, 7), SIZE9X9(9, 9, 9), SIZE11X11(11, 11, 11);

    private final int xSize;
    private final int ySize;
    private final int zSize;


    GlassJarSize(int xSize, int ySize, int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public int getzSize() {
        return zSize;
    }
}
