package getfluxed.fluxedcrystals.blocks.generators;

import getfluxed.fluxedcrystals.FluxedCrystals;
import getfluxed.fluxedcrystals.blocks.FCBlocks;
import getfluxed.fluxedcrystals.network.PacketHandler;
import getfluxed.fluxedcrystals.network.messages.tiles.generator.MessageGenerator;
import getfluxed.fluxedcrystals.tileentities.generators.TileEntityTrashGenerator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrashGenerator extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool isActive = PropertyBool.create("active");

	public BlockTrashGenerator() {
		super(Material.rock);
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		worldIn.setBlockState(pos, FCBlocks.trashGenerator.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(isActive, active), 3);
		worldIn.setBlockState(pos, FCBlocks.trashGenerator.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(isActive, active), 3);

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
			PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator((TileEntityTrashGenerator) tileentity), new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), (double) tileentity.getPos().getX(), (double) tileentity.getPos().getY(), (double) tileentity.getPos().getZ(), 128d));
		}
	}

    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, isActive});
    }

    /**
     * Possibly modify the given BlockState before rendering it on an Entity
     * (Minecarts, Endermen, ...)
     */
    @SideOnly(Side.CLIENT)
    public IBlockState getStateForEntityRender(IBlockState state) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(isActive, false);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        boolean active = false;
        if ((meta & 15 >> 2) == 1) {
            active = true;
        }
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(isActive, active);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | (state.getValue(FACING)).getHorizontalIndex();
        if (state.getValue(isActive)) {
            i = i | 1 << 2;
        } else {
            i = i | 0 << 2;
        }
        return i;
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to
     * allow for adjustments to the IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(isActive, false);
    }

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.openGui(FluxedCrystals.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityTrashGenerator();
	}
}
