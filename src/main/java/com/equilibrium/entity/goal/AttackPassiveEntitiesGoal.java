package com.equilibrium.entity.goal;

import java.util.EnumSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

/**
 * A target goal that finds a passive entity as the target.
 */
public class AttackPassiveEntitiesGoal<T extends LivingEntity> extends TrackTargetGoal {
    private final Class<T> targetClass;
    @Nullable
    private LivingEntity targetEntity;
    private final TargetPredicate targetPredicate;

    public AttackPassiveEntitiesGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility, boolean checkCanNavigate) {
        super(mob, checkVisibility, checkCanNavigate);
        this.targetClass = targetClass;
        this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange());
        this.setControls(EnumSet.of(Goal.Control.TARGET));
    }
    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, 4.0, distance);
    }
    @Override
    public boolean canStart() {
        this.findClosestTarget();
        return this.targetEntity != null;
    }

    private void findClosestTarget() {
        this.targetEntity = this.mob.getWorld().getClosestEntity(
                this.mob.getWorld().getEntitiesByClass(this.targetClass, this.getSearchBox(this.getFollowRange()), (entity) -> entity instanceof PassiveEntity),
                this.targetPredicate,
                this.mob,
                this.mob.getX(),
                this.mob.getEyeY(),
                this.mob.getZ()
        );
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }
}
