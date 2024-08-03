package com.equilibrium.mixin.entitymixin.goal_mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.EnumSet;

@Mixin(LookAtEntityGoal.class)
public abstract class LookAtEntityGoalMixin {
    @Shadow
    private int lookTime;
    @Shadow
    @Final
    protected MobEntity mob;
    @Shadow
    @Final
    protected float chance = 1;

}
