package com.equilibrium.entity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

public class LookAtTargetGoal extends Goal {
    private final MobEntity mob;
    private LivingEntity target;

    public LookAtTargetGoal(MobEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        this.target = this.mob.getTarget();
        return this.target != null && this.target.isAlive();
    }

    @Override
    public void tick() {
        if (this.target != null) {
            this.mob.getLookControl().lookAt(this.target, 30.0F, 30.0F);
        }
    }
}
