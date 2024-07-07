package com.equilibrium.world.dimension;

import com.equilibrium.block.ModBlocks;
import com.equilibrium.block.UnderworldPortalBlock;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import net.minecraft.world.dimension.PortalManager;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class UnderworldPortal {
    private static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;
    private static final AbstractBlock.ContextPredicate IS_VALID_FRAME_BLOCK = (state, world, pos) -> state.isOf(Blocks.OBSIDIAN);
    private static final float FALLBACK_THRESHOLD = 4.0F;
    private static final double HEIGHT_STRETCH = 1.0;
    private final WorldAccess world;
    private final Direction.Axis axis;
    private final Direction negativeDir;
    private int foundPortalBlocks;
    @Nullable
    private BlockPos lowerCorner;
    private int height;
    private final int width;

    public static Optional<UnderworldPortal> getNewPortal(WorldAccess world, BlockPos pos, Direction.Axis axis) {
        return getOrEmpty(world, pos, areaHelper -> areaHelper.isValid() && areaHelper.foundPortalBlocks == 0, axis);
    }

    public static Optional<UnderworldPortal> getOrEmpty(WorldAccess world, BlockPos pos, Predicate<UnderworldPortal> validator, Direction.Axis axis) {
        Optional<UnderworldPortal> optional = Optional.of(new UnderworldPortal(world, pos, axis)).filter(validator);
        if (optional.isPresent()) {
            return optional;
        } else {

            Direction.Axis axis2 = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(new UnderworldPortal(world, pos, axis2)).filter(validator);
        }
    }

    public UnderworldPortal(WorldAccess world, BlockPos pos, Direction.Axis axis) {
        this.world = world;
        this.axis = axis;
        this.negativeDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.lowerCorner = this.getLowerCorner(pos);
        if (this.lowerCorner == null) {
            this.lowerCorner = pos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.getWidth();
            if (this.width > 0) {
                this.height = this.getHeight();
            }
        }
    }

    @Nullable
    private BlockPos getLowerCorner(BlockPos pos) {
        int i = Math.max(this.world.getBottomY(), pos.getY() - 21);

        while (pos.getY() > i && validStateInsidePortal(this.world.getBlockState(pos.down()))) {
            pos = pos.down();
        }

        Direction direction = this.negativeDir.getOpposite();
        int j = this.getWidth(pos, direction) - 1;
        return j < 0 ? null : pos.offset(direction, j);
    }

    private int getWidth() {
        int i = this.getWidth(this.lowerCorner, this.negativeDir);
        return i >= 2 && i <= 21 ? i : 0;
    }

    private int getWidth(BlockPos pos, Direction direction) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int i = 0; i <= 21; i++) {
            mutable.set(pos).move(direction, i);
            BlockState blockState = this.world.getBlockState(mutable);
            if (!validStateInsidePortal(blockState)) {
                if (IS_VALID_FRAME_BLOCK.test(blockState, this.world, mutable)) {
                    return i;
                }
                break;
            }

            BlockState blockState2 = this.world.getBlockState(mutable.move(Direction.DOWN));
            if (!IS_VALID_FRAME_BLOCK.test(blockState2, this.world, mutable)) {
                break;
            }
        }

        return 0;
    }

    private int getHeight() {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int i = this.getPotentialHeight(mutable);
        return i >= 3 && i <= 21 && this.isHorizontalFrameValid(mutable, i) ? i : 0;
    }

    private boolean isHorizontalFrameValid(BlockPos.Mutable pos, int height) {
        for (int i = 0; i < this.width; i++) {
            BlockPos.Mutable mutable = pos.set(this.lowerCorner).move(Direction.UP, height).move(this.negativeDir, i);
            if (!IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(mutable), this.world, mutable)) {
                return false;
            }
        }

        return true;
    }

    private int getPotentialHeight(BlockPos.Mutable pos) {
        for (int i = 0; i < 21; i++) {
            pos.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, -1);
            if (!IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(pos), this.world, pos)) {
                return i;
            }

            pos.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, this.width);
            if (!IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(pos), this.world, pos)) {
                return i;
            }

            for (int j = 0; j < this.width; j++) {
                pos.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, j);
                BlockState blockState = this.world.getBlockState(pos);
                if (!validStateInsidePortal(blockState)) {
                    return i;
                }

                if (blockState.isOf(ModBlocks.UNDERWORLD_PORTAL)) {
                    this.foundPortalBlocks++;
                }
            }
        }

        return 21;
    }

    private static boolean validStateInsidePortal(BlockState state) {
        return state.isAir() || state.isIn(BlockTags.FIRE) || state.isOf(ModBlocks.UNDERWORLD_PORTAL);
    }

    public boolean isValid() {
        return this.lowerCorner != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void createPortal() {
        /*
        规则:当传送门距离世界底部距离小于等于5时,传送门才可以被点亮为自定义的传送门方块
        1、在主世界,建立在距离底部小于等于5格距离的传送门,将传送到地下世界
        2、在地下世界,建立在距离底部小于等于5格距离的传送门,将传送到下界
        3、在地下世界,建立在距离底部大于5格距离的传送门,将传送到主世界
        4、在下界,任意位置的传送门,都将传送到地下世界
         */
        //记录当前注册的世界
        RegistryKey<World> registryKey = ((ServerWorld) world).getRegistryKey();

        //是否距离底部小于5格距离
        boolean locatedInBottom = getHeightFromBottom() <= 5;

        //是否是主世界
        boolean overworld = (getWorldDimension((ServerWorld) world) == World.OVERWORLD);
        //是否是地下世界
        boolean underworld = (getWorldDimension((ServerWorld) world) == RegistryKey.of(RegistryKeys.WORLD, Identifier.of("miteequilibrium", "underworld")));

        //是否为下界
        boolean netherworld = (getWorldDimension((ServerWorld) world) == World.NETHER);


        if (locatedInBottom && overworld) {
            //在主世界,建立在距离底部小于等于5格距离的传送门,将传送到地下世界
            setUnderworldPortalBlock();
            System.out.println("execute 1 ");
        }else if ((!locatedInBottom) && underworld) {
            //在地下世界,建立在距离底部大于5格距离的传送门,将传送到主世界
            setUnderworldPortalBlock();
            System.out.println("execute 2 ");
        }
        else{
            return;

        }

    }


    public boolean wasAlreadyValid() {
        return this.isValid() && this.foundPortalBlocks == this.width * this.height;
    }

    public static Vec3d entityPosInPortal(BlockLocating.Rectangle portalRect, Direction.Axis portalAxis, Vec3d entityPos, EntityDimensions entityDimensions) {
        double d = (double)portalRect.width - (double)entityDimensions.width();
        double e = (double)portalRect.height - (double)entityDimensions.height();
        BlockPos blockPos = portalRect.lowerLeft;
        double g;
        if (d > 0.0) {
            double f = (double)blockPos.getComponentAlongAxis(portalAxis) + (double)entityDimensions.width() / 2.0;
            g = MathHelper.clamp(MathHelper.getLerpProgress(entityPos.getComponentAlongAxis(portalAxis) - f, 0.0, d), 0.0, 1.0);
        } else {
            g = 0.5;
        }

        double f;
        if (e > 0.0) {
            Direction.Axis axis = Direction.Axis.Y;
            f = MathHelper.clamp(MathHelper.getLerpProgress(entityPos.getComponentAlongAxis(axis) - (double)blockPos.getComponentAlongAxis(axis), 0.0, e), 0.0, 1.0);
        } else {
            f = 0.0;
        }

        Direction.Axis axis = portalAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        double h = entityPos.getComponentAlongAxis(axis) - ((double)blockPos.getComponentAlongAxis(axis) + 0.5);
        return new Vec3d(g, f, h);
    }

    public static Vec3d findOpenPosition(Vec3d fallback, ServerWorld world, Entity entity, EntityDimensions dimensions) {
        if (!(dimensions.width() > 4.0F) && !(dimensions.height() > 4.0F)) {
            double d = (double)dimensions.height() / 2.0;
            Vec3d vec3d = fallback.add(0.0, d, 0.0);
            VoxelShape voxelShape = VoxelShapes.cuboid(Box.of(vec3d, (double)dimensions.width(), 0.0, (double)dimensions.width()).stretch(0.0, 1.0, 0.0).expand(1.0E-6));
            Optional<Vec3d> optional = world.findClosestCollision(
                    entity, voxelShape, vec3d, (double)dimensions.width(), (double)dimensions.height(), (double)dimensions.width()
            );
            Optional<Vec3d> optional2 = optional.map(pos -> pos.subtract(0.0, d, 0.0));
            return (Vec3d)optional2.orElse(fallback);
        } else {
            return fallback;
        }
    }

    private int getHeightFromBottom(){
        if (this.lowerCorner != null) {
            return Math.abs(world.getBottomY()-this.lowerCorner.getY());
        }
        else{
            System.out.println("It might the portal doesn't exist");
            return 384;
        }
    }
    public RegistryKey<World> getWorldDimension(ServerWorld world) {
        return world.getRegistryKey();
        //RegistryKey<World> registryKey = world.getRegistryKey() == World.NETHER ? World.OVERWORLD : World.NETHER;
    }

    private void setUnderworldPortalBlock() {
        BlockState blockState = ModBlocks.UNDERWORLD_PORTAL.getDefaultState().with(UnderworldPortalBlock.AXIS,this.axis);
        BlockPos.iterate(this.lowerCorner, this.lowerCorner.offset(Direction.UP, this.height - 1).offset(this.negativeDir, this.width - 1))
                .forEach(pos -> this.world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS | Block.FORCE_STATE));
    }




}
