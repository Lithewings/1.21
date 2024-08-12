package com.equilibrium.mixin.entitymixin.goal_mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetMixin <T extends LivingEntity> extends TrackTargetGoal {
    public ActiveTargetMixin(MobEntity mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    @Override
    protected double getFollowRange() {
        return this.mob.getWorld().getRegistryKey()== World.OVERWORLD ? this.mob.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE): 0.25* (this.mob.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE));
    }
}
