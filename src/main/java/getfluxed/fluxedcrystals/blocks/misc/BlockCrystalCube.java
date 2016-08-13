package getfluxed.fluxedcrystals.blocks.misc;

import getfluxed.fluxedcrystals.tileentities.misc.TileEntityCrystalCube;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by Jared on 7/15/2016.
 */
public class BlockCrystalCube extends Block implements ITileEntityProvider{
    public static final PropertyBool onGround = PropertyBool.create("ground");

    public BlockCrystalCube() {
        super(Material.GLASS);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{onGround});
    }
    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }
    /**
     * Possibly modify the given BlockState before rendering it on an Entity
     * (Minecarts, Endermen, ...)
     */
    @SideOnly(Side.CLIENT)
    public IBlockState getStateForEntityRender(IBlockState state) {
        return this.getDefaultState().withProperty(onGround, false);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        boolean active = false;
        if ((meta & 15 >> 2) == 1) {
            active = true;
        }
        return this.getDefaultState().withProperty(onGround, active);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(onGround)) {
            i = i | 1 << 2;
        } else {
            i = i | 0 << 2;
        }
        return i;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        return true;
    }

    public void setState(boolean active, World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, getDefaultState().withProperty(onGround, active), 3);
        worldIn.setBlockState(pos, getDefaultState().withProperty(onGround, active), 3);
//        if (tileentity != null) {
//            tileentity.validate();
//            worldIn.setTileEntity(pos, tileentity);
//            PacketHandler.INSTANCE.sendToAllAround(new MessageMachineBase((TileEntityMachineBase) tileentity), new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), (double) tileentity.getPos().getX(), (double) tileentity.getPos().getY(), (double) tileentity.getPos().getZ(), 128d));
//        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCrystalCube();
    }
}
