package com.equilibrium.entity.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Function;

public class AdvanceEscapeDangerGoal extends Goal {


    public static final int RANGE_Y = 1;
    protected final PathAwareEntity mob;
    protected final double speed;
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected boolean active;
    private final Function<PathAwareEntity, TagKey<DamageType>> entityToDangerousDamageTypes;

    public AdvanceEscapeDangerGoal(PathAwareEntity mob, double speed) {
        this(mob, speed, DamageTypeTags.PANIC_CAUSES);
    }

    public AdvanceEscapeDangerGoal(PathAwareEntity mob, double speed, TagKey<DamageType> dangerousDamageTypes) {
        this(mob, speed, entity -> dangerousDamageTypes);
    }

    public AdvanceEscapeDangerGoal(PathAwareEntity mob, double speed, Function<PathAwareEntity, TagKey<DamageType>> entityToDangerousDamageTypes) {
        this.mob = mob;
        this.speed = speed;
        this.entityToDangerousDamageTypes = entityToDangerousDamageTypes;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.isInDanger()) {
            return false;
        } else {
            if (this.mob.isOnFire()) {
                BlockPos blockPos = this.locateClosestWater(this.mob.getWorld(), this.mob, 16);
                if (blockPos != null) {
                    this.targetX = (double) blockPos.getX();
                    this.targetY = (double) blockPos.getY();
                    this.targetZ = (double) blockPos.getZ();
                    return true;
                }
            }

            return this.findTarget();
        }
    }

    protected boolean isInDanger() {
        return this.mob.getRecentDamageSource() != null
                && this.mob.getRecentDamageSource().isIn((TagKey<DamageType>) this.entityToDangerousDamageTypes.apply(this.mob));
    }

    protected boolean findTarget() {
        Vec3d vec3d = NoPenaltyTargeting.find(this.mob, 64, 16);
        if (vec3d == null) {
            return false;
        } else {
            this.targetX = vec3d.x;
            this.targetY = vec3d.y;
            this.targetZ = vec3d.z;
            return true;
        }
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed);
        this.active = true;
    }

    @Override
    public void stop() {
        this.active = false;
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle();
    }

    @Nullable
    protected BlockPos locateClosestWater(BlockView world, Entity entity, int rangeX) {
        BlockPos blockPos = entity.getBlockPos();
        return !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty()
                ? null
                : (BlockPos) BlockPos.findClosest(entity.getBlockPos(), rangeX, 1, pos -> world.getFluidState(pos).isIn(FluidTags.WATER)).orElse(null);
    }
}