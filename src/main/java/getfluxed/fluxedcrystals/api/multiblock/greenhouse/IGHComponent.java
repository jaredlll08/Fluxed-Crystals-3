package getfluxed.fluxedcrystals.api.multiblock.greenhouse;

import getfluxed.fluxedcrystals.api.multiblock.EnumPartType;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jared on 7/25/2016.
 */
public interface IGHComponent {

    boolean isMaster();

    IGHMaster getMaster();

    default boolean hasMaster() {
        return getMaster() != null;
    }

    boolean isValid(EnumPartType type);

    default List<BlockPos> getAir(World world, List<BlockPos> bottomLayer, BlockPos top) {
        List<BlockPos> retPos = new LinkedList<>();
        for (BlockPos bp : bottomLayer) {
            for (int i = 1; i <= top.down().getY() - bp.getY(); i++) {
                if (world.isAirBlock(bp.up(1))) {
                    retPos.add(bp.up(i));
                } else {
                    return null;
                }
            }
        }
        return retPos;
    }

    default List<BlockPos> getSides(World world, BlockPos master, BlockPos bottom, BlockPos top, EnumFacing facing) {
        List<BlockPos> retPos = new LinkedList<>();
        //the other side
        BlockPos base = bottom.offset(facing);
        while (world.getTileEntity(base) != null && world.getTileEntity(base) instanceof IGHComponent) {
            if (((IGHComponent) world.getTileEntity(base)).isValid(EnumPartType.BASE)) {
                base = base.offset(facing);
            } else if (((IGHComponent) world.getTileEntity(base)).isValid(EnumPartType.BASE)) {
                base = base.offset(facing);
                break;
            } else {
                break;
            }
        }
        facing.rotateYCCW();

//        world.setBlockState(bottom, Blocks.BOOKSHELF.getDefaultState());
//        world.setBlockState(top.add(1, 0, 1), Blocks.BOOKSHELF.getDefaultState());

        return retPos;
    }

    default List<BlockPos> getSides(World world, BlockPos master, List<BlockPos> air, BlockPos bottom, BlockPos top) {
        List<BlockPos> retPos = new LinkedList<>();
        for (BlockPos bp : BlockPos.getAllInBox(bottom, top)) {
            if (!air.contains(bp)) {
                if (world.getTileEntity(bp) instanceof IGHComponent) {
                    if (((IGHComponent) world.getTileEntity(bp)).isValid(EnumPartType.SIDE)) {
                        retPos.add(bp);
                    }
                } else {
                    return null;
                }
            }
        }
        world.setBlockState(bottom, Blocks.BOOKSHELF.getDefaultState());
        world.setBlockState(top.add(1, 0, 1), Blocks.BOOKSHELF.getDefaultState());

        return retPos;
    }

    default List<BlockPos> getBaseConnected(World world, BlockPos pos, List<BlockPos> checked) {
        if (checked == null) {
            return null;
        }
        if (checked.contains(pos)) {
            return checked;
        } else {
            checked.add(pos);
        }
        for (EnumFacing fac : EnumFacing.HORIZONTALS) {
            TileEntity tile = world.getTileEntity(pos.offset(fac));
            if (tile != null && tile instanceof IGHComponent) {
                IGHComponent comp = (IGHComponent) tile;
                if (comp.isValid(EnumPartType.BASE)) {
                    comp.getBaseConnected(world, pos.offset(fac), checked);
                }
            }
        }
        return checked;
    }

    default List<BlockPos> getTopLayer(World world, List<BlockPos> bottomLayer, BlockPos top) {
        List<BlockPos> layer = new LinkedList<>();
        for (BlockPos bp : bottomLayer) {
            TileEntity tile = world.getTileEntity(bp.up(top.getY() - bp.getY()));
            if (tile != null && tile instanceof IGHComponent) {
                IGHComponent comp = (IGHComponent) tile;
                if (comp.isValid(EnumPartType.TOP)) {
                    layer.add(bp.up(top.getY() - bp.getY()));
                } else {
                    layer = null;
                    return null;
                }
            }
        }
        return layer;
    }

    /**
     * returns the bottommost inner block (starting with the block that called it)
     *
     * @param pos
     * @return
     */
    default BlockPos getBottom(World world, BlockPos master, BlockPos pos, EnumFacing facing) {
        TileEntity tile = world.getTileEntity(pos);
        TileEntity facingTile = world.getTileEntity(pos.offset(facing));
        if (tile != null && tile instanceof IGHComponent) {
            if (facingTile != null) {
                if (facingTile instanceof IGHComponent && ((IGHComponent) facingTile).isValid(EnumPartType.BASE)) {
                    return pos;
                } else {
                    return master;
                }
            } else if (!world.isAirBlock(pos.offset(facing))) {
                return master;
            } else {
                return getBottom(world, master, pos.down(), facing);
            }
        }
        return master;
    }

    /**
     * returns the topmost top block (starting with the block that called it)
     *
     * @param pos
     * @return
     */
    default BlockPos getTop(World world, BlockPos master, BlockPos pos, EnumFacing facing) {
        TileEntity tile = world.getTileEntity(pos);
        TileEntity facingTile = world.getTileEntity(pos.offset(facing));
        if (tile != null && tile instanceof IGHComponent) {
            if (facingTile != null) {
                if (facingTile instanceof IGHComponent && ((IGHComponent) facingTile).isValid(EnumPartType.TOP)) {
                    return pos;
                } else {
                    return master;
                }
            } else if (!world.isAirBlock(pos.offset(facing))) {
                return master;
            } else {
                return getTop(world, master, pos.up(), facing);
            }
        }
        return master;
    }

}
