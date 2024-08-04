package com.equilibrium.entity.goal;

import com.equilibrium.MITEequilibrium;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class BreakBlockGoal extends Goal {
    private static final int MIN_MAX_PROGRESS = 240;
    private final Predicate<Difficulty> difficultySufficientPredicate;
    private final MobEntity mob;

    private int maxProgress;


    protected boolean shouldStop;

    private float offsetX, offsetZ;

    protected BlockPos breakPos = BlockPos.ORIGIN;
    protected BlockState breakState = Blocks.AIR.getDefaultState();
    protected int breakProgress = -1, prevBreakStage = -1;

    @Override
    public void start() {
        this.shouldStop = false;
        this.offsetX = (float) ((double) this.breakPos.getX() + 0.5 - this.mob.getX());
        this.offsetZ = (float) ((double) this.breakPos.getZ() + 0.5 - this.mob.getZ());
        this.breakProgress = 0;
    }



    @Override
    public void stop() {
        super.stop();
        this.mob.getWorld().setBlockBreakingInfo(this.mob.getId(), this.breakPos, -1);
    }






    public BreakBlockGoal(MobEntity mob, int maxProgress, Predicate<Difficulty> difficultySufficientPredicate) {
        this.mob = mob;
        this.maxProgress = maxProgress;
        this.difficultySufficientPredicate = difficultySufficientPredicate;

    }
    float harvestBonus = 1;

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }


    protected int getMaxProgress() {
        return(int)(800*harvestBonus);
    }

    public static final double[][] FIND_NEAREST_BLOCKS = {{0, -1, 0}, {0, 1, 0}, {0, 2, 0}, {-1, 0, 0}, {-1, 1, 0}, {1, 0, 0}, {1, 1, 0}, {0, 0, 1}, {0, 1, 1}, {0, 0, -1}, {0, 1, -1}};

    public static BlockPos getPosFacing(Entity entity, boolean isBackward) {
        if (entity == null) {
            MITEequilibrium.LOGGER.error("EntityHelper/getPosFacing;entity==null");
            return BlockPos.ORIGIN;
        }
        var entityPos = entity.getBlockPos();
        var facing = entity.getHorizontalFacing();
        return switch (isBackward ? facing.getOpposite() : facing) {
            case EAST -> entityPos.east();
            case SOUTH -> entityPos.south();
            case WEST -> entityPos.west();
            default -> entityPos.north();
        };
    }

    public boolean canBreakBlock(@NotNull BlockState state) {
        return !state.isAir();
    }


    @Override
    public boolean canStart() {
        //Choose a block to break
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            for (double[] findPos : FIND_NEAREST_BLOCKS) {
                //Should not dig upward when not above target
                if (findPos[1] == -1 && this.mob.getY() <= target.getY()) continue;
                //Should not dig downward when not under target
                if (findPos[1] == 2 && this.mob.getY() >= target.getY()) continue;
                //Choose a pos to break
                BlockPos pendingBreakPos = new BlockPos((int) (this.mob.getX() + findPos[0]), (int) (this.mob.getY() + findPos[1]), (int) (this.mob.getZ() + findPos[2]));
                //Should not break the block behind itself
                BlockPos backPos = getPosFacing(this.mob, true);
                if (pendingBreakPos.getX() == backPos.getX() && pendingBreakPos.getZ() == backPos.getZ()) continue;
                BlockState pendingBreakState = this.mob.getWorld().getBlockState(pendingBreakPos);

                //Determine whether to start
                if (canBreakBlock(pendingBreakState) && this.mob.getNavigation().isIdle()) {
                    this.breakPos = pendingBreakPos;
                    this.breakState = pendingBreakState;
                    if(this.mob.getMainHandStack().isSuitableFor(breakState))
                        harvestBonus=0.5f;
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean shouldContinue() {
        if (this.mob.getTarget() == null) {
            return false;
        }
        boolean b1 = !this.shouldStop;
        boolean b2 = this.breakProgress <= this.getMaxProgress();
        boolean b3 = canBreakBlock(this.breakState);
        boolean b4 = this.breakPos.isWithinDistance(this.mob.getPos(), 2);
        boolean b5 = this.mob.getDamageTracker().getTimeSinceLastAttack() > 20;
        return (!this.shouldStop) && (this.breakProgress <= this.getMaxProgress() )&& (canBreakBlock(this.breakState)) &&( this.breakPos.isWithinDistance(this.mob.getPos(), 2));
    }




    public static final Predicate<BlockState> IS_GRAVITY_AFFECTED = state -> state != null && (state.isOf(Blocks.GRAVEL));


    public static void checkBlockGravity(World world, BlockPos pos) {
        try {
            if (!(world instanceof ServerWorld)) return;
            for (BlockPos bp : new BlockPos[]{pos, pos.up(), pos.down(), pos.east(), pos.west(), pos.south(), pos.north()}) {
                //Check the pos and its immediate pos
                BlockState state = world.getBlockState(bp);
                if (IS_GRAVITY_AFFECTED.test(state)) {
                    if (FallingBlock.canFallThrough(world.getBlockState(bp.down())) || bp.getY() < world.getBottomY()) {
                        if (IS_GRAVITY_AFFECTED.test(state)) FallingBlockEntity.spawnFromBlock(world, bp, state);
                        //Recurse for further neighbor tick
                        for (BlockPos bpn : new BlockPos[]{bp.up(), bp.down(), bp.east(), bp.west(), bp.south(), bp.north()})
                            checkBlockGravity(world, bpn);
                    }
                }
            }
        } catch (StackOverflowError error) {
            MITEequilibrium.LOGGER.error("WorldHelper/checkBlockGravity(): StackOverflowError");
        }
    }






    //maxProgress = 800
    @Override
    public void tick() {
        ++this.breakProgress;
        //更新方块状态,比如在挖掘一半时被提前破坏了
        BlockState pendingBreakState = this.mob.getWorld().getBlockState(this.breakPos);
        if(pendingBreakState.isOf(Blocks.AIR))
            this.shouldStop = true;
        if (this.offsetX * (float) ((double) this.breakPos.getX() + 0.5 - this.mob.getX()) + this.offsetZ * (float) ((double) this.breakPos.getZ() + 0.5 - this.mob.getZ()) < 0.0f)
            this.shouldStop = true;


        float breakStage = ((float) this.breakProgress / getMaxProgress() )*8;
        if ((int)breakStage != this.prevBreakStage) {
            this.mob.swingHand(this.mob.getActiveHand());
            this.mob.getWorld().setBlockBreakingInfo(this.mob.getId(), this.breakPos, (int)breakStage);
            this.mob.getWorld().syncWorldEvent(WorldEvents.BLOCK_BROKEN, this.breakPos, Block.getRawIdFromState(this.breakState));
            this.prevBreakStage = (int)breakStage;
        }
        if (this.breakProgress >= this.getMaxProgress()) {
            this.mob.getWorld().breakBlock(this.breakPos, true, this.mob);
            checkBlockGravity(this.mob.getWorld(), this.breakPos);
        }
    }




}
