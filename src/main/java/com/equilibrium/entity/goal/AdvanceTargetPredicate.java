package com.equilibrium.entity.goal;

import java.util.function.Predicate;

import com.equilibrium.tags.ModBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AdvanceTargetPredicate extends TargetPredicate {
    public static final AdvanceTargetPredicate DEFAULT = createAttackable();
    private static final double MIN_DISTANCE = 2.0;
    private final boolean attackable;
    private double baseMaxDistance = -1.0;
    private boolean respectsVisibility = true;
    private boolean useDistanceScalingFactor = true;
    @Nullable
    private Predicate<LivingEntity> predicate;

    private AdvanceTargetPredicate(boolean attackable) {
        super(attackable);
        this.attackable = attackable;
    }

    public static AdvanceTargetPredicate createAttackable() {
        return new AdvanceTargetPredicate(true);
    }

    public static AdvanceTargetPredicate createNonAttackable() {
        return new AdvanceTargetPredicate(false);
    }

    public AdvanceTargetPredicate copy() {

        AdvanceTargetPredicate advanceTargetPredicate = this.attackable ? createAttackable() : createNonAttackable();
        advanceTargetPredicate.baseMaxDistance = this.baseMaxDistance;
        advanceTargetPredicate.respectsVisibility = this.respectsVisibility;
        advanceTargetPredicate.useDistanceScalingFactor = this.useDistanceScalingFactor;
        advanceTargetPredicate.predicate = this.predicate;
        return advanceTargetPredicate;
    }

    public AdvanceTargetPredicate setBaseMaxDistance(double baseMaxDistance) {
        this.baseMaxDistance = baseMaxDistance;
        return this;
    }

    public AdvanceTargetPredicate ignoreVisibility() {
        this.respectsVisibility = false;
        return this;
    }

    public AdvanceTargetPredicate ignoreDistanceScalingFactor() {
        this.useDistanceScalingFactor = false;
        return this;
    }

    public AdvanceTargetPredicate setPredicate(@Nullable Predicate<LivingEntity> predicate) {
        this.predicate = predicate;
        return this;
    }

    public boolean test(@Nullable LivingEntity baseEntity, LivingEntity targetEntity) {
        if (baseEntity == targetEntity) {
            return false;
        } else if (!targetEntity.isPartOfGame()) {
            return false;
        } else if (this.predicate != null && !this.predicate.test(targetEntity)) {
            return false;
        } else {
            if (baseEntity == null) {
                if (this.attackable && (!targetEntity.canTakeDamage() || targetEntity.getWorld().getDifficulty() == Difficulty.PEACEFUL)) {
                    return false;
                }
            } else {
                if (this.attackable && (!baseEntity.canTarget(targetEntity) || !baseEntity.canTarget(targetEntity.getType()) || baseEntity.isTeammate(targetEntity))) {
                    return false;
                }

                if (this.baseMaxDistance > 0.0) {
                    double d = this.useDistanceScalingFactor ? targetEntity.getAttackDistanceScalingFactor(baseEntity) : 1.0;
                    double e = Math.max(this.baseMaxDistance * d, 2.0);
                    double f = baseEntity.squaredDistanceTo(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
                    if (f > e * e) {
                        return false;
                    }
                }

                if (this.respectsVisibility && baseEntity instanceof MobEntity mobEntity && !canSeeThroughTransparentBlocks(mobEntity, targetEntity)) {
                    return false;
                }
            }

            return true;
        }
    }
    private boolean canSeeThroughTransparentBlocks(MobEntity mobEntity, LivingEntity targetEntity) {
        World world = mobEntity.getWorld();
        Vec3d startPos = new Vec3d(mobEntity.getX(), mobEntity.getEyeY(), mobEntity.getZ());
        Vec3d endPos = new Vec3d(targetEntity.getX(), targetEntity.getEyeY(), targetEntity.getZ());

        double followRange = mobEntity.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        double distance = startPos.distanceTo(endPos);

        // If the distance exceeds the follow range, return false
        if (distance > followRange) {
            return false;
        }






        BlockHitResult hitResult = world.raycast(new RaycastContext(startPos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mobEntity));

        BlockPos hitPos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(hitPos);

        // Check if the block is transparent
        return blockState.isTransparent(world,hitPos) || blockState.isAir()||blockState.isIn(ModBlockTags.TRANSPARENT_FOR_ZOMBIE);
    }
}
